package org.example.fitness.validation;

import org.example.fitness.exception.ValidationException;
import org.example.fitness.model.Client;

public class ClientValidator implements Validator<Client> {

    @Override
    public void validate(Client client) throws ValidationException {
        if (client == null) {
            throw new ValidationException("Клиент не может быть null.");
        }
        if (client.getName() == null || client.getName().isBlank()) {
            throw new ValidationException("Имя клиента обязательно.");
        }
    }
}
