package org.example.fitness.pricing.discount;

import java.time.LocalDate;

public interface DiscountPolicy {
    double resolveDiscountRate(LocalDate date, double basePrice);

    default DiscountBreakdown resolveDiscountBreakdown(LocalDate date, double basePrice) {
        double rate = resolveDiscountRate(date, basePrice);
        return new DiscountBreakdown(rate, "Скидка рассчитана без детализации шагов.");
    }
}
