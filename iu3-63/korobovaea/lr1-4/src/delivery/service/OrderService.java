package delivery.service;

import delivery.client.ReferenceClient;
import delivery.exception.DeliveryException;
import delivery.exception.DuplicateOrderException;
import delivery.exception.NoCourierAvailableException;
import delivery.exception.OrderNotFoundException;
import delivery.grpc.CalculateTimeResponse;
import delivery.grpc.ValidateZoneResponse;
import delivery.model.Courier;
import delivery.model.Order;
import delivery.model.OrderStatus;
import delivery.observer.CustomerNotifier;
import delivery.observer.OrderLogger;
import delivery.observer.OrderObserver;
import delivery.repository.InMemoryOrderRepository;
import delivery.repository.OrderRepository;

import delivery.util.TraceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderService {

    private static final String REFERENCE_UNAVAILABLE =
            "Справочный сервис (Service B) недоступен. Проверьте, что ReferenceServer запущен на порту 9090, и повторите попытку.";

    private final OrderRepository orderRepository;
    private final CourierService courierService;
    private final ReferenceClient referenceClient;

    private final List<OrderObserver> observers = new ArrayList<>();

    public OrderService(ReferenceClient referenceClient) {

        this.orderRepository = new InMemoryOrderRepository();
        this.courierService = new CourierService();
        this.referenceClient = referenceClient;

        courierService.addCourier(new Courier(1, "Alex"));
        courierService.addCourier(new Courier(2, "John"));
        courierService.addCourier(new Courier(3, "Mike"));

        addObserver(new CustomerNotifier());
        addObserver(new OrderLogger());
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(Order order, OrderStatus status) {
        for (OrderObserver observer : observers) {
            observer.update(order, status);
        }
    }

    public void createOrder(Order order)
            throws DeliveryException {

        if (orderRepository.findById(order.getId()) != null) {
            throw new DuplicateOrderException(order.getId());
        }

        String zoneName = order.getZone().name();
        Optional<ValidateZoneResponse> validation = referenceClient.validateZone(zoneName, traceId());
        if (validation.isEmpty()) {
            throw new DeliveryException(REFERENCE_UNAVAILABLE);
        }
        if (!validation.get().getValid()) {
            throw new DeliveryException(validation.get().getMessage());
        }

        Optional<CalculateTimeResponse> time = referenceClient.calculateDeliveryTime(zoneName, traceId());
        if (time.isEmpty()) {
            throw new DeliveryException(REFERENCE_UNAVAILABLE);
        }

        order.setEstimatedDeliveryMinutes(time.get().getMinutes());
        orderRepository.save(order);
    }

    private static String traceId() {
        return Optional.ofNullable(TraceContext.get()).orElse("no-trace");
    }

    public void assignCourier(int orderId)
            throws OrderNotFoundException, NoCourierAvailableException {

        Order order = orderRepository.findById(orderId);

        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        Courier courier = courierService.findAvailableCourier();

        if (courier == null) {
            throw new NoCourierAvailableException();
        }

        order.assignCourier(courier);
        courier.setAvailable(false);

        notifyObservers(order, OrderStatus.ASSIGNED);
    }

    public void updateStatus(int orderId, OrderStatus status)
            throws OrderNotFoundException {

        Order order = orderRepository.findById(orderId);

        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        order.setStatus(status);

        notifyObservers(order, status);

        if (status == OrderStatus.DELIVERED && order.getCourier() != null) {
            order.getCourier().setAvailable(true);
        }
    }
}
