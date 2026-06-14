package org.example.fitness.console;

import org.example.fitness.clock.ClockSim;
import org.example.fitness.exception.FitnessException;
import org.example.fitness.jsonclient.JsonClientConsole;
import org.example.fitness.notification.NotificationBoard;
import org.example.fitness.observer.NotificationContributor;
import org.example.fitness.exception.ValidationException;
import org.example.fitness.model.Attendance;
import org.example.fitness.model.Client;
import org.example.fitness.model.ScheduleEntry;
import org.example.fitness.model.Subscription;
import org.example.fitness.model.SubscriptionPlan;
import org.example.fitness.model.Training;
import org.example.fitness.service.IntAttendanceService;
import org.example.fitness.service.IntClientService;
import org.example.fitness.service.IntScheduleService;
import org.example.fitness.service.IntSubscriptionService;
import org.example.fitness.service.IntTrainingService;
import org.example.fitness.pricing.PricePreviewResult;
import org.example.fitness.pricing.PricePreviewService;
import org.example.fitness.pricing.QuickPriceInput;
import org.example.fitness.state.SubscriptionStateDate;
import org.example.fitness.state.SubscriptionStateSub;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


public class FitnessConsole {

    private final Scanner scanner = new Scanner(System.in);
    private final IntClientService clientService;
    private final IntSubscriptionService subscriptionService;
    private final IntTrainingService trainingService;
    private final IntScheduleService scheduleService;
    private final IntAttendanceService attendanceService;
    private final DemoDataSeeder demoDataSeeder;
    private final ClockSim clockSim;
    private final NotificationBoard notificationBoard;
    private final List<NotificationContributor> notificationContributors;
    private final SubscriptionStateDate subscriptionStateDate;
    private final SubscriptionStateSub subscriptionStateSub;
    private final PricePreviewService pricePreviewService;
    private final JsonClientConsole jsonClientConsole;

    public FitnessConsole(SubscriptionStateDate subscriptionStateDate,
                          SubscriptionStateSub subscriptionStateSub,
                          PricePreviewService pricePreviewService,
                          JsonClientConsole jsonClientConsole,
                          IntClientService clientService,
                          IntSubscriptionService subscriptionService,
                          IntTrainingService trainingService,
                          IntScheduleService scheduleService,
                          IntAttendanceService attendanceService,
                          ClockSim clockSim,
                          NotificationBoard notificationBoard,
                          List<NotificationContributor> notificationContributors) {
        this.clientService = clientService;
        this.subscriptionService = subscriptionService;
        this.trainingService = trainingService;
        this.scheduleService = scheduleService;
        this.attendanceService = attendanceService;
        this.clockSim = clockSim;
        this.notificationBoard = notificationBoard;
        this.notificationContributors = notificationContributors;
        this.subscriptionStateDate = subscriptionStateDate;
        this.subscriptionStateSub = subscriptionStateSub;
        this.pricePreviewService = pricePreviewService;
        this.jsonClientConsole = jsonClientConsole;
        this.demoDataSeeder = new DemoDataSeeder(
                clientService, subscriptionService, trainingService, scheduleService);
    }

    public void run() {
        System.out.println("=== Информационная система фитнес-клуба ===\n");
        while (true) {
            for (NotificationContributor contributor : notificationContributors) {
                contributor.contribute();
            }
            notificationBoard.printToConsole();
            subscriptionStateDate.contribute();
            subscriptionStateSub.contribute();
            printMenu();
            String choice = readLine("Выбор: ").trim();
            if (choice.isEmpty()) continue;
            if ("99".equals(choice)) {
                System.out.println("Выход.");
                break;
            }
            try {
                handleCommand(choice);
            } catch (FitnessException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("Дата: " + clockSim.getToday().toLocalDate());
        System.out.println("--- Демо ---");
        System.out.println("  0 — Загрузить демо-данные");
        System.out.println("--- Клиенты ---");
        System.out.println("  1 — Добавить клиента");
        System.out.println("  2 — Список клиентов");
        System.out.println("  3 — Удалить клиента");
        System.out.println("--- Типы тренировок ---");
        System.out.println("  4 — Добавить тип тренировки");
        System.out.println("  5 — Список типов тренировок");
        System.out.println("  6 — Удалить тренировку");
        System.out.println("--- Абонементы ---");
        System.out.println("  7 — Добавить абонемент");
        System.out.println("  8 — Список всех абонементов");
        System.out.println("  9 — Абонементы клиента (по id клиента)");
        System.out.println(" 10 — Удалить абонемент");
        System.out.println("--- Расписание ---");
        System.out.println(" 11 — Добавить занятие в расписание");
        System.out.println(" 12 — Расписание на дату");
        System.out.println(" 13 — Всё расписание");
        System.out.println("--- Посещения ---");
        System.out.println(" 14 — Отметить посещение (по id абонемента)");
        System.out.println(" 15 — Посещения клиента");
        System.out.println(" 16 — Быстрый расчет стоимости абонемента");
        System.out.println(" 17 — JSON-вывод клиентов (hack)");
        System.out.println(" 98 — Следующий день");
        System.out.println(" 99 — Выход");
    }

    private void handleCommand(String choice) {
        switch (choice) {
            case "0" -> seedDemoData();
            case "1" -> addClient();
            case "2" -> listClients();
            case "3" -> deleteClient();
            case "4" -> addTraining();
            case "5" -> listTrainings();
            case "6" -> deleteTraining();
            case "7" -> addSubscription();
            case "8" -> listAllSubscriptions();
            case "9" -> listSubscriptionsByClient();
            case "10" -> deleteSubscription();
            case "11" -> addScheduleEntry();
            case "12" -> listScheduleByDate();
            case "13" -> listAllSchedule();
            case "14" -> checkIn();
            case "15" -> listAttendancesByClient();
            case "16" -> previewSubscriptionPrice();
            case "17" -> dumpClientsJsonHack();
            case "98" -> clockSim.advanceDay();
            default -> System.out.println("Неизвестная команда.");
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readIntId(String prompt) {
        String value = readLine(prompt).trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ValidationException("Ожидалось целое число, получено: '" + value + "'.");
        }
    }

    private int readInt(String prompt) {
        String value = readLine(prompt).trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ValidationException("Ожидалось целое число, получено: '" + value + "'.");
        }
    }

    private LocalDate readDate(String prompt) {
        System.out.println(prompt + " (формат ГГГГ-ММ-ДД, например 2026-03-15)");
        String value = readLine("Дата: ").trim();
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Неверный формат даты: '" + value + "'. Используйте ГГГГ-ММ-ДД.");
        }
    }

    private LocalTime readTime(String prompt) {
        System.out.println(prompt + " (формат ЧЧ:ММ, например 09:00)");
        String value = readLine("Время: ").trim();
        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Неверный формат времени: '" + value + "'. Используйте ЧЧ:ММ.");
        }
    }

    private void addClient() {
        String name = readLine("Имя: ").trim();
        String phone = readLine("Телефон: ").trim();
        String email = readLine("Email: ").trim();
        Client c = clientService.register(new Client(0, name, phone, email));
        System.out.println("Клиент добавлен, id=" + c.getId());
    }

    private void listClients() {
        List<Client> list = clientService.getAll();
        if (list.isEmpty()) {
            System.out.println("Клиентов нет.");
            return;
        }
        for (Client c : list) {
            System.out.println("  id=" + c.getId() + " | " + c.getName() + " | " + c.getPhone() + " | " + c.getEmail());
        }
    }

    private void addTraining() {
        String name = readLine("Название тренировки: ").trim();
        String desc = readLine("Описание: ").trim();
        int minutes = readInt("Длительность (минут): ");
        int max = readInt("Макс. участников: ");
        Training t = trainingService.create(new Training(0, name, desc, Duration.ofMinutes(minutes), max));
        System.out.println("Тип тренировки добавлен, id=" + t.getId());
    }

    private void listTrainings() {
        List<Training> list = trainingService.getAll();
        if (list.isEmpty()) {
            System.out.println("Типов тренировок нет.");
            return;
        }
        for (Training t : list) {
            System.out.println("  id=" + t.getId() + " | " + t.getName() + " | " + t.getDuration().toMinutes() + " мин | макс " + t.getMaxParticipants());
        }
    }

    private void addSubscription() {
        int clientId = readIntId("id клиента: ");
        SubscriptionPlan plan = readPlan();
        LocalDate start = readDate("Дата начала");
        LocalDate end = plan.isVisitBased()
                ? start.plusMonths(12) // для абонементов по посещениям задаём большой срок, чтобы не мешал (KISS)
                : start.plusMonths(plan.getDurationMonths());
        Subscription sub = subscriptionService.create(new Subscription(0, clientId, plan, start, end));
        System.out.println("Абонемент добавлен, id=" + sub.getId());
    }

    private void listAllSubscriptions() {
        List<Subscription> list = subscriptionService.getAll();
        if (list.isEmpty()) {
            System.out.println("Абонементов нет.");
            return;
        }
        for (Subscription s : list) {
            System.out.println("  id=" + s.getId()
                    + " | id Клиента =" + s.getClientId()
                    + " | " + s.getType()
                    + " | " + s.getStartDate() + " — " + s.getEndDate()
                    + " | посещений " + s.formatVisits()
                    + " | статус: " + (s.isActive() ? "активен" : "закончен"));
        }
    }

    private void listSubscriptionsByClient() {
        int clientId = readIntId("id клиента: ");
        List<Subscription> list = subscriptionService.getByClientId(clientId);
        if (list.isEmpty()) {
            System.out.println("У клиента нет абонементов.");
            return;
        }
        for (Subscription s : list) {
            System.out.println("  id=" + s.getId()
                    + " | " + s.getType()
                    + " | " + s.getStartDate() + " — " + s.getEndDate()
                    + " | посещений " + s.formatVisits()
                    + " | статус: " + (s.isActive() ? "активен" : "закончен"));
        }
    }

    private void addScheduleEntry() {
        int trainingId = readIntId("id типа тренировки: ");
        LocalDate date = readDate("Дата занятия");
        LocalTime time = readTime("Время начала");
        String trainer = readLine("Имя тренера: ").trim();
        ScheduleEntry e = scheduleService.addEntry(new ScheduleEntry(0, trainingId, date, time, trainer));
        System.out.println("Занятие добавлено в расписание, id=" + e.getId());
    }

    private void listScheduleByDate() {
        LocalDate date = readDate("Дата");
        List<ScheduleEntry> list = scheduleService.getByDate(date);
        if (list.isEmpty()) {
            System.out.println("На эту дату занятий нет.");
            return;
        }
        for (ScheduleEntry e : list) {
            System.out.println("  id=" + e.getId() + " | теренировка=" + trainingService.getNameById(e.getTrainingId()) + " | " + e.getDate() + " " + e.getStartTime() + " | " + e.getTrainerName());
        }
    }

    private void listAllSchedule() {
        List<ScheduleEntry> list = scheduleService.getAll();
        if (list.isEmpty()) {
            System.out.println("Расписание пусто.");
            return;
        }
        for (ScheduleEntry e : list) {
            System.out.println("  id=" + e.getId() + " | теренировка=" + trainingService.getNameById(e.getTrainingId()) + " | " + e.getDate() + " " + e.getStartTime() + " | " + e.getTrainerName());
        }
    }

    private void checkIn() {
        int subscriptionId = readIntId("id абонемента: ");
        Attendance a = attendanceService.checkInBySubscription(subscriptionId);
        System.out.println("Посещение зафиксировано, id=" + a.getId());
    }

    private void listAttendancesByClient() {
        int clientId = readIntId("id клиента: ");
        List<Attendance> list = attendanceService.getByClient(clientId);
        if (list.isEmpty()) {
            System.out.println("Посещений нет.");
            return;
        }
        for (Attendance a : list) {
            System.out.println( a.getId() + " | id клиента = " + a.getSubscriptionId() + " | " + a.getCheckedInAt());
        }
    }

    private void deleteClient() {
        int clientId = readIntId("id клиента для удаления: ");
        clientService.delete(clientId);
        System.out.println("Клиент удалён, id=" + clientId);
    }

    private void deleteTraining() {
        int trainingId = readIntId("id тренировки для удаления: ");
        trainingService.delete(trainingId);
        System.out.println("Тренировка удалена, id=" + trainingId);
    }

    private void deleteSubscription() {
        int subscriptionId = readIntId("id абонемента для удаления: ");
        subscriptionService.delete(subscriptionId);
        System.out.println("Абонемент удалён, id=" + subscriptionId);
    }

    private void previewSubscriptionPrice() {
        SubscriptionPlan plan = readPlan();
        int age = readInt("Возраст клиента: ");
        String studentRaw = readLine("Студент? (y/n): ").trim().toLowerCase();
        boolean student = studentRaw.equals("y") || studentRaw.equals("yes") || studentRaw.equals("д") || studentRaw.equals("да");

        QuickPriceInput input = new QuickPriceInput(plan, age, student);
        PricePreviewResult result = pricePreviewService.preview(input, clockSim.getToday().toLocalDate());
        System.out.println("Предпросмотр стоимости (без сохранения):");
        System.out.println("  Базовая цена: " + String.format("%.2f", result.getBasePrice()));
        System.out.println("  Скидка: " + String.format("%.2f", result.getDiscountAmount())
                + " (" + String.format("%.0f", result.getDiscountRate() * 100) + "%)");
        System.out.println("  Как рассчитана скидка: " + result.getDiscountSummary());
        System.out.println("  Итог: " + String.format("%.2f", result.getFinalPrice()));
    }

    private void dumpClientsJsonHack() {
        System.out.print("Режим JSON-вывода: ");
        System.out.println(jsonClientConsole.readAndBuildJson(scanner));
    }

    private void seedDemoData() {
        demoDataSeeder.seedIfEmpty();
    }

    private SubscriptionPlan readPlan() {
        System.out.println("Выберите план абонемента:");
        System.out.println("  1 — 10 посещений");
        System.out.println("  2 — 30 посещений");
        System.out.println("  3 — Месяц");
        System.out.println("  4 — Полгода");
        System.out.println("  5 — Год");
        String v = readLine("План: ").trim();
        return switch (v) {
            case "1" -> SubscriptionPlan.VISITS_10;
            case "2" -> SubscriptionPlan.VISITS_30;
            case "3" -> SubscriptionPlan.TIME_1_MONTH;
            case "4" -> SubscriptionPlan.TIME_6_MONTHS;
            case "5" -> SubscriptionPlan.TIME_1_YEAR;
            default -> throw new org.example.fitness.exception.ValidationException("Некорректный выбор плана.");
        };
    }
}
