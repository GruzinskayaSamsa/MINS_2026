package org.example.fitness.validation;

import org.example.fitness.exception.ValidationException;
import org.example.fitness.model.Training;

public class TrainingValidator implements Validator<Training> {
    @Override
    public void validate(Training t) throws ValidationException {
        if (t == null) throw new ValidationException("Тренировка не может быть null.");
        if (t.getName() == null || t.getName().isBlank()) {
            throw new ValidationException("Название тренировки обязательно.");
        }
        if (t.getDuration() == null || t.getDuration().isNegative() || t.getDuration().isZero()) {
            throw new ValidationException("Длительность тренировки должна быть > 0.");
        }
        if (t.getMaxParticipants() <= 0) {
            throw new ValidationException("Максимум участников должен быть > 0.");
        }
    }
}

