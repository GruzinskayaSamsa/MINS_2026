package org.example.reference.form;

/**
 * Общая проверка полей формы «новый тип тренировки» (Reference и режим local).
 */
public final class TrainingFormInputParser {

    private TrainingFormInputParser() {
    }

    public record Result(boolean valid, String message, int parsedDurationMinutes, int parsedMaxParticipants) {
    }

    public static Result parse(String name, String descIgnored, String durationStr, String maxStr) {
        if (name == null || name.isBlank()) {
            return new Result(false, "Название тренировки обязательно.", 0, 0);
        }
        String dtrim = durationStr == null ? "" : durationStr.trim();
        String mtrim = maxStr == null ? "" : maxStr.trim();
        int minutes;
        try {
            minutes = Integer.parseInt(dtrim);
        } catch (NumberFormatException e) {
            return new Result(false,
                    "Ожидалось целое число для длительности (минут), получено: '" + dtrim + "'.", 0, 0);
        }
        int max;
        try {
            max = Integer.parseInt(mtrim);
        } catch (NumberFormatException e) {
            return new Result(false,
                    "Ожидалось целое число для максимума участников, получено: '" + mtrim + "'.", 0, 0);
        }
        if (minutes <= 0) {
            return new Result(false, "Длительность тренировки должна быть > 0.", 0, 0);
        }
        if (max <= 0) {
            return new Result(false, "Максимум участников должен быть > 0.", 0, 0);
        }
        return new Result(true, "", minutes, max);
    }
}
