package org.example.fitness.state;

import org.example.fitness.clock.ClockSim;
import org.example.fitness.model.Subscription;
import org.example.fitness.service.IntSubscriptionService;

import java.util.List;

public class SubscriptionStateSub implements StateInt {
    private final IntSubscriptionService subscriptionService;
    private final ClockSim clockSim;
    public SubscriptionStateSub(IntSubscriptionService subscriptionService, ClockSim clockSim) {
        this.subscriptionService = subscriptionService;
        this.clockSim = clockSim;
    }

    @Override
    public void contribute() {

        List<Subscription> subscriptions = subscriptionService.getAll();
        for (Subscription subscription : subscriptions) {
            if (subscription.isActive() && subscription.isVisitBased() && !subscription.hasVisitsLeft()) {
                subscription.setActive(false);
                subscriptionService.save(subscription);
            }
        }
    }
}
