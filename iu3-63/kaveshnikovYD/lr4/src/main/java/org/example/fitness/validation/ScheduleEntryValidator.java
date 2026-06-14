package org.example.fitness.validation;

import org.example.fitness.exception.ValidationException;
import org.example.fitness.model.ScheduleEntry;

public class ScheduleEntryValidator implements Validator<ScheduleEntry> {
    @Override
    public void validate(ScheduleEntry entry) throws ValidationException {
        if (entry == null) throw new ValidationException("Запись расписания не может быть null.");
        if (entry.getTrainingId() <= 0) throw new ValidationException("Некорректный id тренировки.");
        if (entry.getDate() == null) throw new ValidationException("Дата тренировки обязательна.");
        if (entry.getStartTime() == null) throw new ValidationException("Время тренировки обязательно.");
        if (entry.getTrainerName() == null || entry.getTrainerName().isBlank()) {
            throw new ValidationException("Имя тренера обязательно.");
        }
    }
}

