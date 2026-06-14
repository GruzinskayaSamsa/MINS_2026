package org.example.fitness.repository.implementation;

import org.example.fitness.model.Subscription;
import org.example.fitness.repository.IdGenerator;
import org.example.fitness.repository.IntSubscriptionRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SubscriptionRepository implements IntSubscriptionRepository {
    private final Map<Integer, Subscription> stor = new ConcurrentHashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public Subscription save(Subscription sub) {
        if (sub.getId() == 0) {
            sub.setId(idGenerator.next());
        }
        stor.put(sub.getId(), copy(sub));
        return sub;
    }

    @Override
    public Subscription findById(int id) {
        return copy(stor.get(id));
    }

    @Override
    public List<Subscription> findAll() {
        return stor.values().stream().map(SubscriptionRepository::copy).collect(Collectors.toList());
    }

    @Override
    public List<Subscription> findByClientId(int clientId) {
        return stor.values().stream().filter(s -> s.getClientId() == clientId).map(SubscriptionRepository::copy).collect(Collectors.toList());
    }

    @Override
    public void deleteById(int id) {
        stor.remove(id);
    }

    private static Subscription copy(Subscription s) {
        if (s == null) return null;
        Subscription c = new Subscription(s.getId(), s.getClientId(), s.getPlan(), s.getStartDate(), s.getEndDate());
        c.setVisitsUsed(s.getVisitsUsed());
        c.setActive(s.isActive());
        return c;
    }
}

