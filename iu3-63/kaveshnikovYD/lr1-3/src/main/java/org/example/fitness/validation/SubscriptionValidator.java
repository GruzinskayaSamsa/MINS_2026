package org.example.fitness.validation;

import org.example.fitness.exception.ValidationException;
import org.example.fitness.model.Subscription;

public class SubscriptionValidator implements Validator<Subscription> {

    @Override
    public void validate(Subscription sub) throws ValidationException {
        if (sub == null) {
            throw new ValidationException("Абонемент не может быть null.");
        }
        if (sub.getClientId() <= 0) {
            throw new ValidationException("Некорректный id клиента.");
        }
        if (sub.getPlan() == null) {
            throw new ValidationException("План абонемента обязателен.");
        }
        if (sub.getEndDate() != null && sub.getStartDate() != null
                && sub.getEndDate().isBefore(sub.getStartDate())) {
            throw new ValidationException("Дата окончания не может быть раньше даты начала.");
        }
        if (sub.getVisitsUsed() < 0) {
            throw new ValidationException("Количество использованных посещений не может быть отрицательным.");
        }
    }
}
