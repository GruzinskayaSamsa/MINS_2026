package org.example.fitness.state;

import org.example.fitness.clock.ClockSim;
import org.example.fitness.model.Subscription;
import org.example.fitness.service.IntSubscriptionService;

import java.time.LocalDate;
import java.util.List;

public class SubscriptionStateDate implements StateInt {
    private final IntSubscriptionService subscriptionService;
    private final ClockSim clockSim;

    public SubscriptionStateDate(IntSubscriptionService subscriptionService, ClockSim clockSim) {
        this.subscriptionService = subscriptionService;
        this.clockSim = clockSim;
    }

    @Override
    public void contribute() {
        LocalDate today = clockSim.getToday().toLocalDate();
        List<Subscription> subscriptions = subscriptionService.getAll();
        for (Subscription subscription : subscriptions) {
            if (subscription.isActive() && subscription.isExpired(today)) {
                subscription.setActive(false);
                subscriptionService.save(subscription);
            }
        }
    }
}
