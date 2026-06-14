package org.example.fitness.service.implementation;

import org.example.fitness.clock.ClockSim;
import org.example.fitness.model.Attendance;
import org.example.fitness.repository.IntAttendanceRepository;
import org.example.fitness.service.IntAttendanceService;
import org.example.fitness.service.IntSubscriptionService;
import org.example.fitness.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;


public class AttendanceService implements IntAttendanceService {

    private final IntAttendanceRepository attendanceRepository;
    private final IntSubscriptionService subscriptionService;
    private final Validator<Attendance> validator;
    private final ClockSim clockSim;

    public AttendanceService(IntAttendanceRepository attendanceRepository,
                             IntSubscriptionService subscriptionService,
                             Validator<Attendance> validator,
                             ClockSim clockSim) {
        this.attendanceRepository = attendanceRepository;
        this.subscriptionService = subscriptionService;
        this.validator = validator;
        this.clockSim = clockSim;
    }

    @Override
    public Attendance checkInBySubscription(int subscriptionId) {
        LocalDateTime now = clockSim.getToday();
        var sub = subscriptionService.useOneVisitBySubscriptionId(subscriptionId, now.toLocalDate());
        Attendance att = new Attendance(0, sub.getClientId(), subscriptionId, now);
        validator.validate(att);
        return attendanceRepository.save(att);
    }

    @Override
    public List<Attendance> getByClient(int clientId) {
        return attendanceRepository.findByClientId(clientId);
    }
}
