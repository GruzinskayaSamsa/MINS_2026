package org.example.fitness.repository;

import org.example.fitness.model.Attendance;

import java.util.List;

public interface IntAttendanceRepository {
    Attendance save(Attendance attendance);
    Attendance findById(int id);
    List<Attendance> findAll();
    List<Attendance> findByClientId(int clientId);
    List<Attendance> findBySubscriptionId(int subscriptionId);
    void deleteById(int id);
}

