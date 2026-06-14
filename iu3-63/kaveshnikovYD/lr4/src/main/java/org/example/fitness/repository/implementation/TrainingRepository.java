package org.example.fitness.repository.implementation;

import org.example.fitness.model.Training;
import org.example.fitness.repository.IdGenerator;
import org.example.fitness.repository.IntTrainingRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TrainingRepository implements IntTrainingRepository {
    private final Map<Integer, Training> stor = new ConcurrentHashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public Training save(Training training) {
        if (training.getId() == 0) {
            training.setId(idGenerator.next());
        }
        stor.put(training.getId(), copy(training));
        return training;
    }

    @Override
    public Training findById(int id) {
        return copy(stor.get(id));
    }

    @Override
    public List<Training> findAll() {
        return stor.values().stream().map(TrainingRepository::copy).collect(Collectors.toList());
    }

    @Override
    public void deleteById(int id) {
        stor.remove(id);
    }

    @Override
    public String findNameById(int id) {
        return stor.get(id).getName();
    }

    private static Training copy(Training training) {
        if (training == null) return null;
        return new Training(
                training.getId(),
                training.getName(),
                training.getDescription(),
                training.getDuration(),
                training.getMaxParticipants()
        );
    }
}

