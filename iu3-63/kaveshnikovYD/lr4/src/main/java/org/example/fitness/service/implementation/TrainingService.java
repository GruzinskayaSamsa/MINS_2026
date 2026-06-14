package org.example.fitness.service.implementation;

import org.example.fitness.exception.ValidationException;
import org.example.fitness.model.Training;
import org.example.fitness.reference.ParsedTrainingFormValues;
import org.example.fitness.reference.TrainingReferenceGateway;
import org.example.fitness.repository.IntTrainingRepository;
import org.example.fitness.service.IntTrainingService;
import org.example.fitness.validation.Validator;

import java.time.Duration;
import java.util.List;

public class TrainingService implements IntTrainingService {

    private final IntTrainingRepository trainingRepository;
    private final TrainingReferenceGateway trainingReferenceGateway;
    private final Validator<Training> validator;

    public TrainingService(IntTrainingRepository trainingRepository,
                           TrainingReferenceGateway trainingReferenceGateway,
                           Validator<Training> validator) {
        this.trainingRepository = trainingRepository;
        this.trainingReferenceGateway = trainingReferenceGateway;
        this.validator = validator;
    }

    @Override
    public Training createFromConsoleForm(String name, String desc, String durationStr, String maxStr) {
        ParsedTrainingFormValues v = trainingReferenceGateway.parseAndValidateTrainingForm(name, desc, durationStr, maxStr);
        Training training = new Training(0, name, desc, Duration.ofMinutes(v.durationMinutes()), v.maxParticipants());
        validator.validate(training);
        Training saved = trainingRepository.save(training);
        trainingReferenceGateway.registerTraining(saved.getId(), saved.getName());
        return saved;
    }

    @Override
    public Training create(Training training) {
        validator.validate(training);
        trainingReferenceGateway.validateTrainingDraftAgainstReference(training);
        Training saved = trainingRepository.save(training);
        trainingReferenceGateway.registerTraining(saved.getId(), saved.getName());
        return saved;
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
