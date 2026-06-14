package org.example.fitness.subscriptions.usage;

import org.example.fitness.exception.SubscriptionExpiredException;
import org.example.fitness.model.Subscription;

import java.time.LocalDate;

//Списание для абонементов, ограниченных сроком (без ограничения по количеству посещений).

public class TimeBasedUsageStrategy implements UsageStrategy {
    @Override
    public void useOne(Subscription sub, LocalDate date) {
        if (sub == null) {
            throw new IllegalArgumentException("Подписка не может быть null");
        }
        if (sub.isVisitBased()) {
            throw new IllegalArgumentException("Ожидается абонемент с ограничением по сроку");
        }
        if (sub.isExpired(date)) {
            throw new SubscriptionExpiredException(sub.getId(), sub.getEndDate());
        }
        sub.setVisitsUsed(sub.getVisitsUsed() + 1);
    }
}

