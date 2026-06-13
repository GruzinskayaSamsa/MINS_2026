package delivery.observer;

import delivery.model.Order;
import delivery.model.OrderStatus;

public interface OrderObserver {

    void update(Order order, OrderStatus status);
}