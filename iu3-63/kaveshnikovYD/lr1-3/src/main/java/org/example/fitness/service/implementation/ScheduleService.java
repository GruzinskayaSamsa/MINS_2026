package org.example.fitness.service.implementation;

import org.example.fitness.model.ScheduleEntry;
import org.example.fitness.repository.IntScheduleRepository;
import org.example.fitness.repository.IntTrainingRepository;
import org.example.fitness.service.IntScheduleService;
import org.example.fitness.validation.Validator;

import java.time.LocalDate;
import java.util.List;


public class ScheduleService implements IntScheduleService {

    private final IntScheduleRepository scheduleRepository;
    private final IntTrainingRepository trainingRepository;
    private final Validator<ScheduleEntry> validator;

    public ScheduleService(IntScheduleRepository scheduleRepository,
                           IntTrainingRepository trainingRepository,
                           Validator<ScheduleEntry> validator) {
        this.scheduleRepository = scheduleRepository;
        this.trainingRepository = trainingRepository;
        this.validator = validator;
    }

    @Override
    public ScheduleEntry addEntry(ScheduleEntry entry) {
        validator.validate(entry);
        if (trainingRepository.findById(entry.getTrainingId()) == null) {
            throw new org.example.fitness.exception.ValidationException(
                    "Тренировка с id=" + entry.getTrainingId() + " не найдена.");
        }
        return scheduleRepository.save(entry);
    }

    @Override
    public ScheduleEntry getById(int id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public List<ScheduleEntry> getByDate(LocalDate date) {
        return scheduleRepository.findByDate(date);
    }

    @Override
    public List<ScheduleEntry> getAll() {
        return scheduleRepository.findAll();
    }
}
