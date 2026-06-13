package delivery;

import delivery.bad.QuickOrderCalculator;
import delivery.client.ReferenceClient;
import delivery.exception.DeliveryException;
import delivery.factory.DefaultOrderFactory;
import delivery.factory.ExpressOrderFactory;
import delivery.factory.OrderFactory;
import delivery.grpc.GetZoneBasePriceResponse;
import delivery.model.DeliveryZone;
import delivery.model.Order;
import delivery.model.OrderStatus;
import delivery.service.OrderService;
import delivery.util.TraceContext;

import java.util.Scanner;
import java.util.UUID;

/**
 * Service A (Core): консольное приложение с бизнес-логикой заказов.
 * Взаимодействие со справочником — только по gRPC к {@link delivery.reference.ReferenceServer}.
 */
public class Main {

    private static final String REFERENCE_DOWN =
            "Справочный сервис (Service B) недоступен. Запустите ReferenceServer (порт 9090) и повторите попытку.";

    public static void main(String[] args) throws InterruptedException {

        try (ReferenceClient referenceClient = new ReferenceClient("localhost", 9090)) {

            OrderService orderService = new OrderService(referenceClient);
            Scanner scanner = new Scanner(System.in);

            while (true) {

                System.out.println("\n===== FOOD DELIVERY (Service A — Core) =====");
                System.out.println("1 - Создать заказ");
                System.out.println("2 - Назначить курьера");
                System.out.println("3 - Показать все заказы");
                System.out.println("5 - Изменить статус заказа");
                System.out.println("6 - Быстрый расчет стоимости (база из Service B)");
                System.out.println("0 - Выход");

                int choice = readIntInRange(scanner, 0, 6);

                String traceId = UUID.randomUUID().toString();
                TraceContext.set(traceId);
                System.out.println("[Core] Новый запрос traceId=" + traceId);

                try {

                    switch (choice) {

                        case 1:
                            createOrder(scanner, orderService);
                            break;

                        case 2:
                            assignCourier(scanner, orderService);
                            break;

                        case 3:
                            showOrders(orderService);
                            break;

                        case 5:
                            changeStatus(scanner, orderService);
                            break;

                        case 6:
                            quickCalculate(scanner, referenceClient, traceId);
                            break;

                        case 0:
                            return;

                        default:
                            break;
                    }

                } catch (DeliveryException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                } finally {
                    TraceContext.clear();
                }
            }
        }
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Введите число:");
                scanner.next();
            }
        }
    }

    private static int readIntInRange(Scanner scanner, int min, int max) {
        while (true) {
            int value = readInt(scanner);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Введите число от " + min + " до " + max);
        }
    }

    private static void createOrder(Scanner scanner, OrderService orderService)
            throws DeliveryException {

        System.out.println("ID:");
        int id = readInt(scanner);
        scanner.nextLine();

        System.out.println("Имя:");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Клиент";
        }

        System.out.println("Зона:");
        for (DeliveryZone zone : DeliveryZone.values()) {
            System.out.println(zone.ordinal() + 1 + " - " + zone);
        }

        int zoneChoice = readIntInRange(scanner, 1, DeliveryZone.values().length);
        DeliveryZone zone = DeliveryZone.values()[zoneChoice - 1];

        System.out.println("Тип:");
        System.out.println("1 - Обычный");
        System.out.println("2 - Экспресс");

        int type = readIntInRange(scanner, 1, 2);

        OrderFactory factory;
        if (type == 2) {
            factory = new ExpressOrderFactory();
        } else {
            factory = new DefaultOrderFactory();
        }

        Order order = factory.createOrder(id, name, zone);

        orderService.createOrder(order);

        System.out.println("Создан. Оценка времени доставки (из справочника): "
                + order.getEstimatedDeliveryMinutes() + " мин");
    }

    private static void assignCourier(Scanner scanner, OrderService orderService)
            throws DeliveryException {

        System.out.println("ID заказа:");
        int id = readInt(scanner);

        orderService.assignCourier(id);

        System.out.println("Курьер назначен");
    }

    private static void showOrders(OrderService orderService) {

        for (Order order : orderService.getOrderRepository().findAll()) {
            System.out.println(order);
        }
    }

    private static void changeStatus(Scanner scanner, OrderService orderService)
            throws DeliveryException {

        System.out.println("ID:");
        int id = readInt(scanner);

        for (OrderStatus status : OrderStatus.values()) {
            System.out.println(status.ordinal() + " - " + status);
        }

        int choice = readIntInRange(scanner, 0, OrderStatus.values().length - 1);

        orderService.updateStatus(id, OrderStatus.values()[choice]);

        System.out.println("Статус обновлен");
    }

    private static void quickCalculate(Scanner scanner, ReferenceClient referenceClient, String traceId) {

        System.out.println("Выберите зону:");
        for (DeliveryZone z : DeliveryZone.values()) {
            System.out.println(z.ordinal() + 1 + " - " + z);
        }

        int zone = readIntInRange(scanner, 1, DeliveryZone.values().length);
        DeliveryZone dz = DeliveryZone.values()[zone - 1];
        String zoneName = dz.name();

        System.out.println("Количество товаров:");
        int items = readInt(scanner);

        System.out.println("Экспресс? (1 да / 0 нет):");
        int expressInput = readIntInRange(scanner, 0, 1);

        boolean express = expressInput == 1;

        java.util.Optional<GetZoneBasePriceResponse> priceResp = referenceClient.getZoneBasePrice(zoneName, traceId);
        if (priceResp.isEmpty()) {
            System.out.println(REFERENCE_DOWN);
            return;
        }
        GetZoneBasePriceResponse resp = priceResp.get();
        if (!resp.getFound()) {
            System.out.println("Справочник не содержит цену для зоны " + zoneName);
            return;
        }

        double price = QuickOrderCalculator.calculateWithBase(resp.getBasePrice(), items, express);

        System.out.println("[Core] Базовая цена из Service B: " + resp.getBasePrice());
        System.out.println("Примерная стоимость: " + price);
        // Локальный техдолг: вторая оценка из God Class с захардкоженной таблицей (см. QuickOrderCalculator).
        System.out.println("[сравнение / антипаттерн] Та же формула, но база только из кода (без B): "
                + QuickOrderCalculator.calculate(zone, items, express));
    }
}
