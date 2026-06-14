package org.example.fitness.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Одна запись в расписании: тренировка в конкретное время.
 */
public class ScheduleEntry {

    private int id;
    private int trainingId;
    private LocalDate date;
    private LocalTime startTime;
    private String trainerName;
    

    public ScheduleEntry(int id, int trainingId, LocalDate date, LocalTime startTime, String trainerName) {
        this.id = id;
        this.trainingId = trainingId;
        this.date = date;
        this.startTime = startTime;
        this.trainerName = trainerName;
    }

    public LocalDateTime getStartDateTime() {
        return LocalDateTime.of(date, startTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleEntry that = (ScheduleEntry) o;
        return id == that.id;
    }
}
