package org.example.fitness.exception;


public class FitnessException extends RuntimeException {

    public FitnessException(String message) {
        super(message);
    }

    public FitnessException(String message, Throwable cause) {
        super(message, cause);
    }
}
