package org.example.fitness.repository;

import org.example.fitness.model.ScheduleEntry;

import java.time.LocalDate;
import java.util.List;

public interface IntScheduleRepository {
    ScheduleEntry save(ScheduleEntry entry);
    ScheduleEntry findById(int id);
    List<ScheduleEntry> findAll();
    List<ScheduleEntry> findByDate(LocalDate date);
    void deleteById(int id);
}

