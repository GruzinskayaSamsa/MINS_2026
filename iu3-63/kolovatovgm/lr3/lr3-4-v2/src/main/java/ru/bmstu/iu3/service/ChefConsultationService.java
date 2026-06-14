package ru.bmstu.iu3.service;

import ru.bmstu.iu3.exception.ValidationException;
import ru.bmstu.iu3.menu.Dish;
import ru.bmstu.iu3.menu.MenuRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Учебный God Object (LR3): намеренно перегруженный класс, где смешаны ввод/вывод,
 * меню, валидация, оплата, форматирование и внутренняя «аналитика».
 */

public class ChefConsultationService {

    /** Фиксированная стоимость консультации по одному блюду (руб.). */
    public static final int CONSULTATION_PRICE_RUB = 100; //magic number

    private final MenuRepository menu;
    private final PaymentService paymentService;
    private final InputReader reader;

    private long chefInvocationsTotal;
    private long consultationRevenueRub;

    /** Авторские комментарии шефа по нормализованному имени блюда (учебный хардкод). */
    private final Map<String, String> chefNotesByDishName = new HashMap<>();

    public ChefConsultationService(MenuRepository menu, PaymentService paymentService, InputReader reader) {
        this.menu = menu;
        this.paymentService = paymentService;
        this.reader = reader;
        seedChefPersonality();
    }

    private void seedChefPersonality() {
        chefNotesByDishName.put("капучино",
                "Капучино у нас — не «пена ради пены»: баланс молока и эспрессо держу как на весах. Если любите послаще — скажите бариста, подберём сироп, но классика — это когда горечь и карамель молока дружат, а не спорят.");
        chefNotesByDishName.put("латте",
                "Латте для меня — про шелковистую текстуру. Пейте не залпом: дайте слоям пожить пару минут, аромат раскроется иначе. И да, это тот случай, когда «ещё молока» почти никогда не бывает лишним.");
        chefNotesByDishName.put("американо",
                "Американо — честный кофе без маски. Вода не «размывает вкус», она его выстраивает: кислинка, шоколад, орех — смотря по зерну. Если кажется водянистым — попробуйте чуть меньше воды в следующий раз, это нормальная настройка под себя.");
        chefNotesByDishName.put("кофе",
                "Наш базовый кофе — это про тепло и будильник в чашке. Я бы пил его без спешки: горячий аромат — половина удовольствия.");
        chefNotesByDishName.put("чай",
                "Чай — это пауза. Заварка, температура, минутка тишины — и вкус совсем другой. Не гонитесь за «крепче любые горы», иногда достаточно аккуратного настоя.");
        chefNotesByDishName.put("пирожное",
                "К десерту я отношусь как к пункту в меню, который не обязан быть «логичным»: пусть будет радость. Маленький кусочек — и день уже не зря.");
    }


    public void runConsultationSession() {
        printChefBanner();

        List<Dish> dishes = menu.getDishes();
        if (dishes.isEmpty()) {
            System.out.println("Меню пустое — не о чем консультировать. Загляните позже.");
            return;
        }

        printNumberedMenuForChef(dishes);

        int number = reader.readInt("Введите номер блюда для консультации шеф-повара:");
        validateDishIndex(number, dishes.size());

        Dish dish = dishes.get(number - 1);

        System.out.println();
        System.out.println(formatMoneyLine(CONSULTATION_PRICE_RUB));
        System.out.println("После оплаты шеф-повар поделится расширенным мнением о блюде.");
        System.out.println();

        paymentService.payService(CONSULTATION_PRICE_RUB);

        chefInvocationsTotal++;
        consultationRevenueRub += CONSULTATION_PRICE_RUB;

        printConsultationAfterPayment(dish);
        printInternalStatsForDemo();
    }

    private void printChefBanner() {
        System.out.println();
        System.out.println("────────── «Консультация шеф-повара» ──────────");
        System.out.println("Выберите блюдо — получите живой комментарий и детали.");
        System.out.println();
    }

    private void printNumberedMenuForChef(List<Dish> dishes) {
        System.out.println("Блюда в меню (для консультации):");
        for (int i = 0; i < dishes.size(); i++) {
            Dish d = dishes.get(i);
            System.out.println((i + 1) + ". " + d.getName() + " — " + d.getPrice() + " руб.");
        }
        System.out.println();
    }

    private void validateDishIndex(int number, int size) {
        if (number < 1 || number > size) {
            throw new ValidationException(
                    "Неверный номер блюда. Пожалуйста, выберите номер от 1 до " + size + ".");
        }
    }

    private String formatMoneyLine(int rub) {
        return "Стоимость консультации: " + rub + " руб.";
    }

    private void printConsultationAfterPayment(Dish dish) {
        System.out.println();
        System.out.println("═══ Мнение шеф-повара ═══");
        System.out.println("Блюдо: " + dish.getName());
        System.out.println();
        System.out.println("Описание из меню:");
        System.out.println("  " + dish.getDescription());
        System.out.println();
        System.out.println("Авторский комментарий шефа:");
        System.out.println("  " + resolveChefComment(dish));
        System.out.println();
        System.out.println("Приятного аппетита и хорошего настроения!");
        System.out.println();
    }

    private String resolveChefComment(Dish dish) {
        String key = dish.getName() == null ? "" : dish.getName().trim().toLowerCase(Locale.ROOT); 
        String note = chefNotesByDishName.get(key);
        if (note != null) {
            return note;
        }
        return "Слушайте, это блюдо мы держим в меню не «для галочки» — вкус простой и понятный, "
                + "а если хотите тонкости, спросите у персонала про свежесть и подачу сегодня: "
                + "каждый день чуть-чуть по-своему, и это нормально.";
    }

    private void printInternalStatsForDemo() {
        System.out.println("[внутренняя статистика консультаций] "
                + "вызовов (успешных, с оплатой): " + chefInvocationsTotal
                + "; выручка с консультаций: " + consultationRevenueRub + " руб.");
        System.out.println();
    }

    public long getChefInvocationsTotal() {
        return chefInvocationsTotal;
    }

    public long getConsultationRevenueRub() {
        return consultationRevenueRub;
    }
}
