package org.example.fitness.observer;

import org.example.fitness.clock.ClockSim;
import org.example.fitness.model.ScheduleEntry;
import org.example.fitness.notification.NotificationBoard;
import org.example.fitness.service.IntScheduleService;
import org.example.fitness.service.IntTrainingService;

import java.util.List;

public class ScheduleTodayNotifier implements NotificationContributor {
    private final IntScheduleService scheduleService;
    private final ClockSim clockSim;
    private final IntTrainingService trainingService;
    private final NotificationBoard notificationBoard;

    public ScheduleTodayNotifier(IntScheduleService scheduleService, ClockSim clockSim, IntTrainingService trainingService, NotificationBoard notificationBoard) {
        this.scheduleService = scheduleService;
        this.clockSim = clockSim;
        this.trainingService = trainingService;
        this.notificationBoard = notificationBoard;
    }

    @Override
    public void contribute() {
        List<ScheduleEntry> list = scheduleService.getByDate(clockSim.getToday().toLocalDate());
        if (list.isEmpty()) {
            return;
        }
        boolean fl = false;
        notificationBoard.add("Сегодня в расписании:");
        for (ScheduleEntry e : list) {
            String line = "  id=" + e.getId() + " | тренировка=" + trainingService.getNameById(e.getTrainingId()) + " | " + e.getDate() + " " + e.getStartTime() + " | " + e.getTrainerName();
            notificationBoard.add(line);
            fl = true;
        }
        if (!fl) {
            notificationBoard.add("Cегодня тренировок нет");
        }
    }
}
