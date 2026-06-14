package org.example.fitness.service.implementation;

import org.example.fitness.clock.ClockSim;
import org.example.fitness.exception.ClientNotFoundException;
import org.example.fitness.exception.SubscriptionNotFoundException;
import org.example.fitness.exception.ValidationException;
import org.example.fitness.model.Subscription;
import org.example.fitness.reference.ClientReferenceGateway;
import org.example.fitness.repository.IntSubscriptionRepository;
import org.example.fitness.service.IntSubscriptionService;
import org.example.fitness.subscriptions.usage.UsageStrategyFactory;
import org.example.fitness.validation.Validator;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис абонементов. Одна ответственность (S). Зависит от абстракций (D).
 */
public class SubscriptionService implements IntSubscriptionService {

    private final ClockSim clockSim;
    private final IntSubscriptionRepository subscriptionRepository;
    private final ClientReferenceGateway clientReferenceGateway;
    private final Validator<Subscription> validator;
    private final UsageStrategyFactory usageStrategyFactory;

    public SubscriptionService(IntSubscriptionRepository subscriptionRepository,
                               ClientReferenceGateway clientReferenceGateway,
                               Validator<Subscription> validator,
                               UsageStrategyFactory usageStrategyFactory,
                               ClockSim clockSim) {
        this.subscriptionRepository = subscriptionRepository;
        this.clientReferenceGateway = clientReferenceGateway;
        this.validator = validator;
        this.usageStrategyFactory = usageStrategyFactory;
        this.clockSim = clockSim;
    }

    @Override
    public Subscription create(Subscription subscription) {
        if (!clientReferenceGateway.clientExists(subscription.getClientId())) {
            throw new ClientNotFoundException(subscription.getClientId());
        }
        LocalDate today = clockSim.getToday().toLocalDate();
        List<Subscription> existing = subscriptionRepository.findByClientId(subscription.getClientId());
        boolean hasActiveWithVisitsLeft = existing.stream()
                .anyMatch(s -> !s.isExpired(today) && s.hasVisitsLeft());
        if (hasActiveWithVisitsLeft) {
            throw new ValidationException(
                    "У клиента уже есть действующий абонемент с доступными посещениями. " +
                    "Новый можно оформить только после окончания посещений.");
        }
        validator.validate(subscription);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getById(int id) {
        Subscription subscription = subscriptionRepository.findById(id);
        if (subscription == null) {
            throw new SubscriptionNotFoundException(id);
        }
        return subscription;
    }

    @Override
    public int getTotalVisitsById(int subscriptionId) {
        return getById(subscriptionId).getVisitsTotal();
    }

    @Override
    public List<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public List<Subscription> getByClientId(int clientId) {
        if (!clientReferenceGateway.clientExists(clientId)) {
            throw new ClientNotFoundException(clientId);
        }
        return subscriptionRepository.findByClientId(clientId);
    }

    @Override
    public void delete(int id) {
        if (subscriptionRepository.findById(id) == null) {
            throw new SubscriptionNotFoundException(id);
        }
        subscriptionRepository.deleteById(id);
    }

    @Override
    public Subscription useOneVisitBySubscriptionId(int subscriptionId, LocalDate date) {
        Subscription sub = getById(subscriptionId);
        var strategy = usageStrategyFactory.getStrategy(sub);
        strategy.useOne(sub, date);
        return subscriptionRepository.save(sub);
    }

    @Override
    public Subscription save(Subscription subscription) {
        if (subscription == null) {
            throw new ValidationException("Абонемент не может быть null.");
        }
        return subscriptionRepository.save(subscription);
    }
}
