package delivery.repository;

import delivery.model.Order;
import java.util.List;

public interface OrderRepository {

    void save(Order order);

    Order findById(int id);

    List<Order> findAll();
}