package org.example.fitness.observer;

import org.example.fitness.clock.ClockSim;
import org.example.fitness.model.Subscription;
import org.example.fitness.notification.NotificationBoard;
import org.example.fitness.service.IntSubscriptionService;
import org.example.fitness.subscriptions.usage.TimeBasedUsageStrategy;
import org.example.fitness.subscriptions.usage.UsageStrategy;
import org.example.fitness.subscriptions.usage.UsageStrategyFactory;

import java.util.List;

public class SubscriptionTodayNotifier implements NotificationContributor {

    private final IntSubscriptionService subscriptionService;
    private final UsageStrategyFactory usageStrategyFactory;
    private final NotificationBoard notificationBoard;
    private final ClockSim clockSim;

    public SubscriptionTodayNotifier(IntSubscriptionService subscriptionService,
                                     UsageStrategyFactory usageStrategyFactory,
                                     NotificationBoard notificationBoard,
                                     ClockSim clockSim) {
        this.subscriptionService = subscriptionService;
        this.usageStrategyFactory = usageStrategyFactory;
        this.notificationBoard = notificationBoard;
        this.clockSim = clockSim;
    }

    @Override
    public void contribute() {
        List<Subscription> list = subscriptionService.getAll();
        if (list.isEmpty()) {
            return;
        }
        boolean fl = false;
        notificationBoard.add("Абонементы:");
        for (Subscription e : list) {
            UsageStrategy strategy = usageStrategyFactory.getStrategy(e);
            if (strategy instanceof TimeBasedUsageStrategy) {
                if (e.isExpired(clockSim.getToday().toLocalDate())) {
                    notificationBoard.add(formatLine(e));
                    fl = true;
                }
            }
        }
        if (!fl) {
            notificationBoard.add("Истекающих абонементов сегодня нет");
        }
    }

    private static String formatLine(Subscription e) {
        return "  id=" + e.getId()
                + " | " + e.getType()
                + " | " + e.getStartDate() + " — " + e.getEndDate()
                + " | посещений " + e.formatVisits();
    }
}
