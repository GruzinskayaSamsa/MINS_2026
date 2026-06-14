package org.example.fitness.exception;

public class SubscriptionNotFoundException extends FitnessException {

    public SubscriptionNotFoundException(String message) {
        super(message);
    }

    public SubscriptionNotFoundException(int subscriptionId) {
        super("Абонемент с id=" + subscriptionId + " не найден.");
    }
}
