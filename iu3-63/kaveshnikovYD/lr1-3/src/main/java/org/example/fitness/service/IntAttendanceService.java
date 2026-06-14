package org.example.fitness.service;

import org.example.fitness.model.Attendance;

import java.util.List;

public interface IntAttendanceService {

    Attendance checkInBySubscription(int subscriptionId);

    List<Attendance> getByClient(int clientId);
}
