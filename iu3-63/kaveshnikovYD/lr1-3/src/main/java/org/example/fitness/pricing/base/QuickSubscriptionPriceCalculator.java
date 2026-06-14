package org.example.fitness.pricing.base;

import org.example.fitness.model.SubscriptionPlan;
import org.example.fitness.pricing.QuickPriceInput;

/**
 * Намеренно "грязный" калькулятор с magic numbers.
 * Изолирован в отдельном модуле и не влияет на основной workflow.
 */
public class QuickSubscriptionPriceCalculator implements BasePriceCalculator {

    @Override
    public double calculateBasePrice(QuickPriceInput input) {
        SubscriptionPlan plan = input.getPlan();
        int age = input.getAge();
        boolean student = input.isStudent();

        double price = 1200; // magic number
        if (plan.isVisitBased()) {
            price += plan.getVisitsTotal() * 95; // magic number
            price += 180; // magic number
        } else {
            price += plan.getDurationMonths() * 420; // magic number
            price *= 1.14; // magic number
        }

        if (age < 18) {
            price -= 250; // magic number
        } else if (age > 60) {
            price -= 300; // magic number
        } else if (age >= 26 && age <= 35) {
            price += 110; // magic number
        }

        if (student) {
            price *= 0.88; // magic number
        }

        if (price < 500) {
            price = 500; // magic number
        }
        return price;
    }
}
