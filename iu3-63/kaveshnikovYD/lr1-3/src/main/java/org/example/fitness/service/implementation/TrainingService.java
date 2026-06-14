package org.example.fitness.service.implementation;

import org.example.fitness.exception.ValidationException;
import org.example.fitness.model.Training;
import org.example.fitness.repository.IntTrainingRepository;
import org.example.fitness.service.IntTrainingService;
import org.example.fitness.validation.Validator;

import java.util.List;

public class TrainingService implements IntTrainingService {

    private final IntTrainingRepository trainingRepository;
    private final Validator<Training> validator;

    public TrainingService(IntTrainingRepository trainingRepository, Validator<Training> validator) {
        this.trainingRepository = trainingRepository;
        this.validator = validator;
    }

    @Override
    public Training create(Training training) {
        validator.validate(training);
        return trainingRepository.save(training);
    }

    @Override
    public Training getById(int id) {
        return trainingRepository.findById(id);
    }

    @Override
    public List<Training> getAll() {
        return trainingRepository.findAll();
    }

    @Override
    public String getNameById(int id) {
        return trainingRepository.findNameById(id);
    }

    @Override
    public void delete(int id) {
        if (trainingRepository.findById(id) == null) {
            throw new ValidationException("Тренировка с id=" + id + " не найдена.");
        }
        trainingRepository.deleteById(id);
    }
}
