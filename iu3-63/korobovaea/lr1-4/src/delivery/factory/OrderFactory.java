package delivery.factory;

import delivery.model.DeliveryZone;
import delivery.model.Order;

public abstract class OrderFactory {

    public abstract Order createOrder(int id, String clientName, DeliveryZone zone);
}