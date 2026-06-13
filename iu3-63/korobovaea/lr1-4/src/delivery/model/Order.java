package delivery.model;

import delivery.strategy.DeliveryTimeCalculator;

public class Order {

    private int id;
    private String clientName;
    private DeliveryZone zone;
    private OrderStatus status;
    private Courier courier;

    private DeliveryTimeCalculator calculator;

    /** Эталонное время доставки из Service B (после успешной валидации). */
    private Integer estimatedDeliveryMinutes;

    public Order(int id, String clientName, DeliveryZone zone) {
        this.id = id;
        this.clientName = clientName;
        this.zone = zone;
        this.status = OrderStatus.CREATED;
    }

    public int getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public DeliveryZone getZone() {
        return zone;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Courier getCourier() {
        return courier;
    }

    public Integer getEstimatedDeliveryMinutes() {
        return estimatedDeliveryMinutes;
    }

    public void setEstimatedDeliveryMinutes(Integer estimatedDeliveryMinutes) {
        this.estimatedDeliveryMinutes = estimatedDeliveryMinutes;
    }

    public void assignCourier(Courier courier) {
        this.courier = courier;
        this.status = OrderStatus.ASSIGNED;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setCalculator(DeliveryTimeCalculator calculator) {
        this.calculator = calculator;
    }

    public DeliveryTimeCalculator getCalculator() {
        return calculator;
    }

    @Override
    public String toString() {
        String courierName = courier != null ? courier.getName() : "нет";

        String time = estimatedDeliveryMinutes != null
                ? estimatedDeliveryMinutes + " мин"
                : "—";

        return "Order{id=" + id +
                ", client='" + clientName + '\'' +
                ", zone=" + zone +
                ", status=" + status +
                ", courier=" + courierName +
                ", time=" + time +
                '}';
    }
}
