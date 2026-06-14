package org.example.fitness.subscriptions.usage;

import org.example.fitness.exception.SubscriptionExpiredException;
import org.example.fitness.exception.VisitLimitExceededException;
import org.example.fitness.model.Subscription;

import java.time.LocalDate;

//Списание для абонементов, ограниченных количеством посещений.

public class VisitBasedUsageStrategy implements UsageStrategy {
    @Override
    public void useOne(Subscription sub, LocalDate date) {
        if (sub == null) {
            throw new IllegalArgumentException("План абонемента не может быть null");
        }
        if (!sub.isVisitBased()) {
            throw new IllegalArgumentException("Ожидается абонемент с ограничением по посещениям");
        }
        if (sub.isExpired(date)) {
            throw new SubscriptionExpiredException(sub.getId(), sub.getEndDate());
        }
        if (!sub.hasVisitsLeft()) {
            throw new VisitLimitExceededException(sub.getId(), sub.getVisitsUsed(), sub.getVisitsTotal());
        }
        sub.setVisitsUsed(sub.getVisitsUsed() + 1);
    }
}

