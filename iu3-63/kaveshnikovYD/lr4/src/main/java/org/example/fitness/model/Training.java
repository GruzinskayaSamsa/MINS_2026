package org.example.fitness.model;

import java.time.Duration;


public class Training {

    private int id;
    private String name;
    private String description;
    private Duration duration;
    private int maxParticipants;

    public Training() {
    }

    public Training(int id, String name, String description, Duration duration, int maxParticipants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.maxParticipants = maxParticipants;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return id == training.id;
    }
}
