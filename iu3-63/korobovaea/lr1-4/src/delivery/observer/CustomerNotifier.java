package delivery.observer;

import delivery.model.Order;
import delivery.model.OrderStatus;
import delivery.util.TraceContext;

public class CustomerNotifier implements OrderObserver {

    @Override
    public void update(Order order, OrderStatus status) {

        String trace = TraceContext.get();
        String tracePart = trace != null ? " traceId=" + trace : "";

        System.out.println(
                "[Core]" + tracePart + " [УВЕДОМЛЕНИЕ КЛИЕНТУ] Заказ " + order.getId() +
                        " теперь в статусе: " + status
        );
    }
}
