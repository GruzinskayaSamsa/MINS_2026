package org.example.fitness.pricing.base;

import org.example.fitness.model.SubscriptionPlan;
import org.example.fitness.pricing.QuickPriceInput;


public class QuickSubscriptionPriceCalculator implements BasePriceCalculator {

    @Override
    public double calculateBasePrice(QuickPriceInput input) {
        SubscriptionPlan plan = input.getPlan();
        int age = input.getAge();
        boolean student = input.isStudent();

        double price = 1200;
        if (plan.isVisitBased()) {
            price += plan.getVisitsTotal() * 95;
            price += 180;
        } else {
            price += plan.getDurationMonths() * 420;
            price *= 1.14;
        }

        if (age < 18) {
            price -= 250;
        } else if (age > 60) {
            price -= 300;
        } else if (age >= 26 && age <= 35) {
            price += 110;
        }

        if (student) {
            price *= 0.88;
        }

        if (price < 500) {
            price = 500;
        }
        return price;
    }
}
