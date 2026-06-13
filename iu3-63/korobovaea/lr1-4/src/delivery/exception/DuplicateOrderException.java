package delivery.exception;

public class DuplicateOrderException extends DeliveryException {

  public DuplicateOrderException(int id) {
    super("Order with id " + id + " already exists");
  }
}