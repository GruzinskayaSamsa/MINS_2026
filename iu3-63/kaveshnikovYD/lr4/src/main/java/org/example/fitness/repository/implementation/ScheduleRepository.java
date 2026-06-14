package org.example.fitness.repository.implementation;

import org.example.fitness.model.ScheduleEntry;
import org.example.fitness.repository.IdGenerator;
import org.example.fitness.repository.IntScheduleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ScheduleRepository implements IntScheduleRepository {
    private final Map<Integer, ScheduleEntry> stor = new ConcurrentHashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public ScheduleEntry save(ScheduleEntry entry) {
        if (entry.getId() == 0) {
            entry.setId(idGenerator.next());
        }
        stor.put(entry.getId(), copy(entry));
        return entry;
    }

    @Override
    public ScheduleEntry findById(int id) {
        return copy(stor.get(id));
    }

    @Override
    public List<ScheduleEntry> findAll() {
        return stor.values().stream().map(ScheduleRepository::copy).collect(Collectors.toList());
    }

    @Override
    public List<ScheduleEntry> findByDate(LocalDate date) {
        return stor.values().stream()
                .filter(e -> e.getDate().equals(date))
                .map(ScheduleRepository::copy)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(int id) {
        stor.remove(id);
    }

    private static ScheduleEntry copy(ScheduleEntry entry) {
        if (entry == null) return null;
        return new ScheduleEntry(
                entry.getId(),
                entry.getTrainingId(),
                entry.getDate(),
                entry.getStartTime(),
                entry.getTrainerName()
        );
    }
}

