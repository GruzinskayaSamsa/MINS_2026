package delivery.factory;

import delivery.model.DeliveryZone;
import delivery.model.Order;
import delivery.strategy.ExpressDeliveryCalculator;

public class ExpressOrderFactory extends OrderFactory {

    @Override
    public Order createOrder(int id, String clientName, DeliveryZone zone) {

        Order order = new Order(id, clientName, zone);

        //  экспресс стратегия
        order.setCalculator(new ExpressDeliveryCalculator());

        return order;
    }
}