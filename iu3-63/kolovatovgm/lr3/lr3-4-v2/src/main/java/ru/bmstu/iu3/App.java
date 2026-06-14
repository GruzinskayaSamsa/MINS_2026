package ru.bmstu.iu3;

import ru.bmstu.iu3.order.Order;
import ru.bmstu.iu3.payment.CardPayment;
import ru.bmstu.iu3.payment.CashPayment;
import ru.bmstu.iu3.payment.Payment;
import ru.bmstu.iu3.service.BillPresenter;
import ru.bmstu.iu3.service.CafeService;
import ru.bmstu.iu3.service.ChefConsultationService;
import ru.bmstu.iu3.service.MenuService;
import ru.bmstu.iu3.service.OrderService;
import ru.bmstu.iu3.service.PaymentService;
import ru.bmstu.iu3.service.Reader;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        Reader reader = new Reader();
        try {
            Order order = new Order();
            List<Payment> paiments = new ArrayList<>();
            paiments.add(new CardPayment());
            paiments.add(new CashPayment());
            MenuService menuService = new MenuService();
            OrderService orderService = new OrderService(order, menuService.getMenu(), reader, new BillPresenter());
            PaymentService paimentService = new PaymentService(paiments, reader);
            ChefConsultationService chefConsultationService =
                    new ChefConsultationService(menuService.getMenu(), paimentService, reader);
            CafeService cafeService = new CafeService(menuService, orderService, paimentService, chefConsultationService, reader);
            cafeService.run();
        } catch (Exception e) {
            System.out.println("Произошла непредвиденная ошибка. Обратитесь к разработчику.");
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }
}

