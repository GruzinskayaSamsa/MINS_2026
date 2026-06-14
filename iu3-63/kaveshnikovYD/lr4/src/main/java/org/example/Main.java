package org.example;

import org.example.fitness.clock.ClockSim;
import org.example.fitness.console.FitnessConsole;
import org.example.fitness.jsonclient.IntJsonCLientOut;
import org.example.fitness.jsonclient.JsonClientConsole;
import org.example.fitness.jsonclient.JsonCLientOut;
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
import org.example.fitness.reference.ClientReferenceGateway;
import org.example.fitness.reference.GrpcClientReferenceGateway;
import org.example.fitness.reference.GrpcTrainingReferenceGateway;
import org.example.fitness.reference.LocalClientReferenceGateway;
import org.example.fitness.reference.LocalTrainingReferenceGateway;
import org.example.fitness.reference.TrainingReferenceGateway;
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
        String mode = args.length > 0 ? args[0].trim().toLowerCase() : "grpc";
        boolean grpcMode = !"local".equals(mode);

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

        ClientReferenceGateway clientReferenceGateway;
        TrainingReferenceGateway trainingReferenceGateway;
        AutoCloseable grpcClientGateway = null;
        AutoCloseable grpcTrainingGateway = null;
        if (grpcMode) {
            grpcClientGateway = new GrpcClientReferenceGateway("localhost", 6565);
            grpcTrainingGateway = new GrpcTrainingReferenceGateway("localhost", 6565);
            clientReferenceGateway = (ClientReferenceGateway) grpcClientGateway;
            trainingReferenceGateway = (TrainingReferenceGateway) grpcTrainingGateway;
            System.out.println("Основной сервис (консоль). Справочник по gRPC: localhost:6565.");
        } else {
            clientReferenceGateway = new LocalClientReferenceGateway(clientRepo);
            trainingReferenceGateway = new LocalTrainingReferenceGateway(trainingRepo);
            System.out.println("Режим справочника: локально (без сети).");
        }

        IntClientService clientService = new ClientService(clientRepo, clientReferenceGateway, clientValidator);
        IntSubscriptionService subscriptionService = new SubscriptionService(
                subRepo, clientReferenceGateway, subValidator, usageStrategyFactory, clockSim);
        IntTrainingService trainingService = new TrainingService(trainingRepo, trainingReferenceGateway, trainingValidator);
        IntScheduleService scheduleService = new ScheduleService(scheduleRepo, trainingReferenceGateway, scheduleValidator);
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
        IntJsonCLientOut jsonClientDumpHack = new JsonCLientOut(clientService, subscriptionService);
        JsonClientConsole jsonClientConsole = new JsonClientConsole(jsonClientDumpHack);

        FitnessConsole console = new FitnessConsole(subscriptionStateDate, subscriptionStateSub, pricePreviewService, jsonClientConsole,
                clientService, subscriptionService, trainingService,
                scheduleService, attendanceService, clockSim, notificationBoard, notificationContributors);

        try {
            console.run();
        } finally {
            closeQuietly(grpcClientGateway);
            closeQuietly(grpcTrainingGateway);
        }
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }
}
