package org.example.fitness.validation;

import org.example.fitness.exception.ValidationException;
import org.example.fitness.model.Attendance;

public class AttendanceValidator implements Validator<Attendance> {
    @Override
    public void validate(Attendance a) throws ValidationException {
        if (a == null) throw new ValidationException("Посещение не может быть null.");
        if (a.getClientId() <= 0) throw new ValidationException("Некорректный id клиента.");
        if (a.getSubscriptionId() <= 0) throw new ValidationException("Некорректный id абонемента.");
        if (a.getCheckedInAt() == null) throw new ValidationException("Время отметки посещения обязательно.");
    }
}

