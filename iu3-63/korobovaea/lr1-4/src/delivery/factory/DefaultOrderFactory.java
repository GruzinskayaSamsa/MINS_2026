package delivery.factory;

import delivery.model.DeliveryZone;
import delivery.model.Order;
import delivery.strategy.DefaultDeliveryTimeCalculator;

public class DefaultOrderFactory extends OrderFactory {

    @Override
    public Order createOrder(int id, String clientName, DeliveryZone zone) {

        Order order = new Order(id, clientName, zone);

        //  обычная стратегия
        order.setCalculator(new DefaultDeliveryTimeCalculator());

        return order;
    }
}