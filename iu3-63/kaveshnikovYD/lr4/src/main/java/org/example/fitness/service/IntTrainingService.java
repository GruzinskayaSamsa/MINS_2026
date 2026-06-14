package org.example.fitness.service;

import org.example.fitness.model.Training;

import java.util.List;

public interface IntTrainingService {

    /**
     * Добавление типа тренировки из консоли: сначала проверка формы на справочнике (gRPC) / локально теми же правилами.
     */
    Training createFromConsoleForm(String name, String desc, String durationStr, String maxStr);

    Training create(Training training);

    Training getById(int id);

    List<Training> getAll();

    String getNameById(int id);

    void delete(int id);
}
