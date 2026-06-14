package org.example.fitness.exception;

import java.time.LocalDate;

public class SubscriptionExpiredException extends FitnessException {

    public SubscriptionExpiredException(String message) {
        super(message);
    }

    public SubscriptionExpiredException(int subscriptionId, LocalDate expiredOn) {
        super("Абонемент id=" + subscriptionId + " истёк " + expiredOn + ".");
    }
}
