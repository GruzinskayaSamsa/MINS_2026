package org.example.fitness.service;

import org.example.fitness.model.ScheduleEntry;

import java.time.LocalDate;
import java.util.List;

public interface IntScheduleService {

    ScheduleEntry addEntry(ScheduleEntry entry);

    ScheduleEntry getById(int id);

    List<ScheduleEntry> getByDate(LocalDate date);

    List<ScheduleEntry> getAll();
}
