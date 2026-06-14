package org.example.fitness.console;

import org.example.fitness.model.Client;
import org.example.fitness.model.ScheduleEntry;
import org.example.fitness.model.Subscription;
import org.example.fitness.model.SubscriptionPlan;
import org.example.fitness.model.Training;
import org.example.fitness.service.IntClientService;
import org.example.fitness.service.IntScheduleService;
import org.example.fitness.service.IntSubscriptionService;
import org.example.fitness.service.IntTrainingService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Заполнение системы демо-данными (для удобства проверки в консоли).
 * Ничего не удаляет; добавляет данные только если соответствующие списки пустые.
 */
public class DemoDataSeeder {

    private final IntClientService clientService;
    private final IntSubscriptionService subscriptionService;
    private final IntTrainingService trainingService;
    private final IntScheduleService scheduleService;

    public DemoDataSeeder(IntClientService clientService,
                          IntSubscriptionService subscriptionService,
                          IntTrainingService trainingService,
                          IntScheduleService scheduleService) {
        this.clientService = clientService;
        this.subscriptionService = subscriptionService;
        this.trainingService = trainingService;
        this.scheduleService = scheduleService;
    }

    public void seedIfEmpty() {

        if (clientService.getAll().isEmpty()) {
            clientService.register(new Client(0, "Анапа", "8005553535", "anapa@gmail.com"));
            clientService.register(new Client(0, "Златоуст", "79167570044", "Златоуст@gmail.com"));
            clientService.register(new Client(0, "Добрыня", "+7-999-333-33-33", "Добрыня@mail.ru"));

        }

        if (trainingService.getAll().isEmpty()) {
            trainingService.create(new Training(0, "Йога", "утренняя", Duration.ofMinutes(60), 15));
            trainingService.create(new Training(0, "Силовая", "грудь качатьь", Duration.ofMinutes(55), 12));
            trainingService.create(new Training(0, "Стретчинг", "растяжка", Duration.ofMinutes(45), 18));

        }

        List<Training> trainings = trainingService.getAll();
        if (scheduleService.getAll().isEmpty() && !trainings.isEmpty()) {
            Training t1 = trainings.get(0);
            Training t2 = trainings.size() > 1 ? trainings.get(1) : trainings.get(0);

            LocalDate today = LocalDate.now();
            scheduleService.addEntry(new ScheduleEntry(0, t1.getId(), today, LocalTime.of(9, 0), "Любава"));
            scheduleService.addEntry(new ScheduleEntry(0, t2.getId(), today, LocalTime.of(18, 30), "Святорусский"));
            scheduleService.addEntry(new ScheduleEntry(0, t1.getId(), today.plusDays(1), LocalTime.of(10, 0), "Любава"));

        }

        List<Client> clients = clientService.getAll();
        if (subscriptionService.getAll().isEmpty() && !clients.isEmpty()) {
            LocalDate today = LocalDate.now();
            Client c1 = clients.get(0);
            Client c2 = clients.size() > 1 ? clients.get(1) : clients.get(0);

            Subscription s1 = subscriptionService.create(new Subscription(
                    0, c1.getId(), SubscriptionPlan.TIME_1_MONTH, today, today.plusDays(1)));
            Subscription s2 = subscriptionService.create(new Subscription(
                    0, c2.getId(), SubscriptionPlan.VISITS_1, today, today.plusMonths(12)));

        }

    }
}

