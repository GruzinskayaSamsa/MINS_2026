package delivery.observer;

import delivery.model.Order;
import delivery.model.OrderStatus;
import delivery.util.TraceContext;

public class OrderLogger implements OrderObserver {

    @Override
    public void update(Order order, OrderStatus status) {

        String trace = TraceContext.get();
        String tracePart = trace != null ? " traceId=" + trace : "";

        System.out.println(
                "[Core]" + tracePart + " Заказ " + order.getId() +
                        " изменил статус на: " + status
        );
    }
}
