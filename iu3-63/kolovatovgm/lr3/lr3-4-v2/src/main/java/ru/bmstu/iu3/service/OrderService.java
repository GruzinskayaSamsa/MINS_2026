package ru.bmstu.iu3.service;

import ru.bmstu.iu3.order.*;
import ru.bmstu.iu3.menu.*;

public class OrderService {
    private final OrderManager order;
    private final MenuRepository menu;
    private final OrderSelectionFlow selectionFlow;
    private final BillPresenter billPresenter;

    public OrderService(OrderManager order, MenuRepository menu, InputReader reader, BillPresenter billPresenter) {
        this.order = order;
        this.menu = menu;
        this.selectionFlow = new OrderSelectionFlow(reader);
        this.billPresenter = billPresenter;
    }

    public void makeOrder() {
        selectionFlow.run(order, menu);
    }

    public int getBill() {
        return order.getPrice();
    }

    public void showBill() {
        billPresenter.present(order);
    }

    public void clearOrder() {
        order.clear();
    }
}
