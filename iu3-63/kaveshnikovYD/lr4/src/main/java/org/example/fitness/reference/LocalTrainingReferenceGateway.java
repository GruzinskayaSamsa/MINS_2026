package org.example.fitness.reference;

import org.example.fitness.exception.ValidationException;
import org.example.fitness.repository.IntTrainingRepository;
import org.example.reference.form.TrainingFormInputParser;

public class LocalTrainingReferenceGateway implements TrainingReferenceGateway {
    private final IntTrainingRepository trainingRepository;

    public LocalTrainingReferenceGateway(IntTrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public boolean trainingExists(int trainingId) {
        return trainingRepository.findById(trainingId) != null;
    }

    @Override
    public ParsedTrainingFormValues parseAndValidateTrainingForm(String name, String desc, String durationStr, String maxStr) {
        TrainingFormInputParser.Result r = TrainingFormInputParser.parse(name, desc, durationStr, maxStr);
        if (!r.valid()) {
            throw new ValidationException(r.message());
        }
        return new ParsedTrainingFormValues(r.parsedDurationMinutes(), r.parsedMaxParticipants());
    }
}
