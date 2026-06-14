package org.example.fitness.subscriptions.usage;

import org.example.fitness.model.Subscription;

import java.time.LocalDate;

// Стратегия списания 1 посещения/использования для конкретного типа абонемента.

public interface UsageStrategy {
    void useOne(Subscription sub, LocalDate date);
}

