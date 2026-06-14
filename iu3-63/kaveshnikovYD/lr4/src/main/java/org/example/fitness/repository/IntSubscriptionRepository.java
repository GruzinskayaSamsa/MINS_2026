package org.example.fitness.repository;

import org.example.fitness.model.Subscription;

import java.util.List;

public interface IntSubscriptionRepository {
    Subscription save(Subscription subscription);
    Subscription findById(int id);
    List<Subscription> findAll();
    List<Subscription> findByClientId(int clientId);
    void deleteById(int id);
}

