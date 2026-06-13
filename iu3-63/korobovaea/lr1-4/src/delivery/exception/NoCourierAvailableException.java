package delivery.exception;

public class NoCourierAvailableException extends DeliveryException {

    public NoCourierAvailableException() {
        super("No courier available");
    }
}