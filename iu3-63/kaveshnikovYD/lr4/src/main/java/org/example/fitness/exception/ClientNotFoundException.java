package org.example.fitness.exception;

public class ClientNotFoundException extends FitnessException {

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(int clientId) {
        super("Клиент с id=" + clientId + " не найден.");
    }
}
