package org.example.fitness.subscriptions.usage;

import org.example.fitness.model.Subscription;
import org.example.fitness.model.SubscriptionPlan;

//Фабрика выбирает стратегию списания по типу плана абонемента.

public class UsageStrategyFactory {

    private final UsageStrategy visitBasedStrategy;
    private final UsageStrategy timeBasedStrategy;

    public UsageStrategyFactory() {
        this.visitBasedStrategy = new VisitBasedUsageStrategy();
        this.timeBasedStrategy = new TimeBasedUsageStrategy();
    }

    public UsageStrategy getStrategy(Subscription plan) {
        if (plan == null) {
            throw new IllegalArgumentException("План абонемента не может быть null");
        }
        return plan.isVisitBased() ? visitBasedStrategy : timeBasedStrategy;
    }
}
