package org.example.fitness.repository.implementation;

import org.example.fitness.model.Attendance;
import org.example.fitness.repository.IdGenerator;
import org.example.fitness.repository.IntAttendanceRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AttendanceRepository implements IntAttendanceRepository {
    private final Map<Integer, Attendance> stor = new ConcurrentHashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public Attendance save(Attendance attendance) {
        if (attendance.getId() == 0) {
            attendance.setId(idGenerator.next());
        }
        stor.put(attendance.getId(), copy(attendance));
        return attendance;
    }

    @Override
    public Attendance findById(int id) {
        return copy(stor.get(id));
    }

    @Override
    public List<Attendance> findAll() {
        return stor.values().stream().map(AttendanceRepository::copy).collect(Collectors.toList());
    }

    @Override
    public List<Attendance> findByClientId(int clientId) {
        return stor.values().stream()
                .filter(a -> a.getClientId() == clientId)
                .map(AttendanceRepository::copy)
                .collect(Collectors.toList());
    }

    @Override
    public List<Attendance> findBySubscriptionId(int subscriptionId) {
        return stor.values().stream()
                .filter(a -> a.getSubscriptionId() == subscriptionId)
                .map(AttendanceRepository::copy)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(int id) {
        stor.remove(id);
    }

    private static Attendance copy(Attendance attendance) {
        if (attendance == null) return null;
        return new Attendance(
                attendance.getId(),
                attendance.getClientId(),
                attendance.getSubscriptionId(),
                attendance.getCheckedInAt()
        );
    }
}

