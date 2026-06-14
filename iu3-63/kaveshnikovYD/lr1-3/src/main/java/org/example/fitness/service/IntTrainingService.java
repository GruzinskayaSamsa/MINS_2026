package org.example.fitness.service;

import org.example.fitness.model.Training;

import java.util.List;

public interface IntTrainingService {

    Training create(Training training);

    Training getById(int id);

    List<Training> getAll();

    String getNameById(int id);

    void delete(int id);
}
