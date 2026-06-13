package delivery.exception;

public class OrderNotFoundException extends DeliveryException {

    public OrderNotFoundException(int id) {
        super("Order with id " + id + " not found");
    }
}