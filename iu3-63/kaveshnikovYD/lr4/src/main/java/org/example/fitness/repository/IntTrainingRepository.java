package org.example.fitness.repository;

import org.example.fitness.model.Training;

import java.util.List;

public interface IntTrainingRepository {
    Training save(Training training);
    Training findById(int id);
    List<Training> findAll();
    String findNameById(int id);
    void deleteById(int id);
}

