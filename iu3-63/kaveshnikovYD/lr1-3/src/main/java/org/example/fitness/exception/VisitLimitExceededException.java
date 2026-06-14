package org.example.fitness.exception;

/**
 * Превышен лимит посещений по абонементу (для абонементов с ограничением по посещениям).
 */
public class VisitLimitExceededException extends FitnessException {

    public VisitLimitExceededException(String message) {
        super(message);
    }

    public VisitLimitExceededException(int subscriptionId, int used, int total) {
        super("Лимит посещений по абонементу id=" + subscriptionId + " исчерпан: " + used + "/" + total + ".");
    }
}

