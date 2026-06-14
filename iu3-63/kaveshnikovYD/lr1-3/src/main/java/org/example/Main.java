package org.example;

import org.example.fitness.clock.ClockSim;
import org.example.fitness.console.FitnessConsole;
import org.example.fitness.jsonclient.JsonClientConsole;
import org.example.fitness.jsonclient.JsonClientDumpHack;
import org.example.fitness.model.Attendance;
import org.example.fitness.model.Client;
import org.example.fitness.model.ScheduleEntry;
import org.example.fitness.model.Subscription;
import org.example.fitness.model.Training;
import org.example.fitness.notification.NotificationBoard;
import org.example.fitness.observer.NotificationContributor;
import org.example.fitness.observer.ScheduleTodayNotifier;
import org.example.fitness.observer.SubscriptionTodayNotifier;
import org.example.fitness.pricing.PricePreviewService;
import org.example.fitness.pricing.base.QuickSubscriptionPriceCalculator;
import org.example.fitness.pricing.discount.ChaoticDiscountEngine;
import org.example.fitness.repository.IntAttendanceRepository;
import org.example.fitness.repository.IntClientRepository;
import org.example.fitness.repository.IntScheduleRepository;
import org.example.fitness.repository.IntSubscriptionRepository;
import org.example.fitness.repository.IntTrainingRepository;
import org.example.fitness.service.IntAttendanceService;
import org.example.fitness.service.IntClientService;
import org.example.fitness.service.IntScheduleService;
import org.example.fitness.service.IntSubscriptionService;
import org.example.fitness.service.IntTrainingService;
import org.example.fitness.service.implementation.AttendanceService;
import org.example.fitness.service.implementation.ClientService;
import org.example.fitness.service.implementation.ScheduleService;
import org.example.fitness.service.implementation.SubscriptionService;
import org.example.fitness.service.implementation.TrainingService;
import org.example.fitness.state.SubscriptionStateDate;
import org.example.fitness.state.SubscriptionStateSub;
import org.example.fitness.subscriptions.usage.UsageStrategyFactory;
import org.example.fitness.validation.*;

import java.util.List;


public class Main {

    public static void main(String[] args) {
        IntClientRepository clientRepo = new org.example.fitness.repository.implementation.ClientRepository();
        IntSubscriptionRepository subRepo = new org.example.fitness.repository.implementation.SubscriptionRepository();
        IntTrainingRepository trainingRepo = new org.example.fitness.repository.implementation.TrainingRepository();
        IntScheduleRepository scheduleRepo = new org.example.fitness.repository.implementation.ScheduleRepository();
        IntAttendanceRepository attendanceRepo = new org.example.fitness.repository.implementation.AttendanceRepository();

        Validator<Client> clientValidator = new ClientValidator();
        Validator<Subscription> subValidator = new SubscriptionValidator();
        Validator<Training> trainingValidator = new TrainingValidator();
        Validator<ScheduleEntry> scheduleValidator = new ScheduleEntryValidator();
        Validator<Attendance> attendanceValidator = new AttendanceValidator();
        UsageStrategyFactory usageStrategyFactory = new UsageStrategyFactory();
        ClockSim clockSim = new ClockSim();
        NotificationBoard notificationBoard = new NotificationBoard();
        IntClientService clientService = new ClientService(clientRepo, clientValidator);
        IntSubscriptionService subscriptionService = new SubscriptionService(subRepo, clientRepo, subValidator, usageStrategyFactory, clockSim);
        IntTrainingService trainingService = new TrainingService(trainingRepo, trainingValidator);
        IntScheduleService scheduleService = new ScheduleService(scheduleRepo, trainingRepo, scheduleValidator);
        IntAttendanceService attendanceService = new AttendanceService(attendanceRepo, subscriptionService, attendanceValidator, clockSim);
        ScheduleTodayNotifier scheduleTodayNotifier = new ScheduleTodayNotifier(
                scheduleService, clockSim, trainingService, notificationBoard);
        SubscriptionTodayNotifier subscriptionTodayNotifier = new SubscriptionTodayNotifier(
                subscriptionService, usageStrategyFactory, notificationBoard,clockSim);

        List<NotificationContributor> notificationContributors = List.of(
                scheduleTodayNotifier,
                subscriptionTodayNotifier);

        SubscriptionStateDate subscriptionStateDate = new SubscriptionStateDate(subscriptionService, clockSim);
        SubscriptionStateSub subscriptionStateSub = new SubscriptionStateSub(subscriptionService,  clockSim);
        PricePreviewService pricePreviewService = new PricePreviewService(
                new QuickSubscriptionPriceCalculator(),
                new ChaoticDiscountEngine()
        );
        JsonClientDumpHack jsonClientDumpHack = new JsonClientDumpHack(clientService, subscriptionService);
        JsonClientConsole jsonClientConsole = new JsonClientConsole(jsonClientDumpHack);

        FitnessConsole console = new FitnessConsole(subscriptionStateDate, subscriptionStateSub, pricePreviewService, jsonClientConsole,
                clientService, subscriptionService, trainingService,
                scheduleService, attendanceService, clockSim, notificationBoard, notificationContributors);

        console.run();
    }
}
