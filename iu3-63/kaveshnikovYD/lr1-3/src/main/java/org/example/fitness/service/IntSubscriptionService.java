package org.example.fitness.service;

import org.example.fitness.model.Subscription;

import java.time.LocalDate;
import java.util.List;

public interface IntSubscriptionService {

    Subscription create(Subscription subscription);

    Subscription getById(int id);

    int getTotalVisitsById(int subscriptionId);

    List<Subscription> getAll();

    List<Subscription> getByClientId(int clientId);

    void delete(int id);

    Subscription useOneVisitBySubscriptionId(int subscriptionId, LocalDate date);

    Subscription save(Subscription subscription);
}
