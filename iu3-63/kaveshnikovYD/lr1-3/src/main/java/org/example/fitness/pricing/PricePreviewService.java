package org.example.fitness.pricing;

import org.example.fitness.pricing.base.BasePriceCalculator;
import org.example.fitness.pricing.discount.DiscountBreakdown;
import org.example.fitness.pricing.discount.DiscountPolicy;

import java.time.LocalDate;

/**
 * Тонкий фасад вокруг двух независимых расчетчиков.
 */
public class PricePreviewService {
    private final BasePriceCalculator baseCalculator;
    private final DiscountPolicy discountEngine;

    public PricePreviewService(BasePriceCalculator baseCalculator,
                               DiscountPolicy discountEngine) {
        this.baseCalculator = baseCalculator;
        this.discountEngine = discountEngine;
    }

    public PricePreviewResult preview(QuickPriceInput input, LocalDate date) {
        double base = baseCalculator.calculateBasePrice(input);
        DiscountBreakdown breakdown = discountEngine.resolveDiscountBreakdown(date, base);
        double discount = base * breakdown.getRate();
        double finalPrice = base - discount;
        return new PricePreviewResult(base, breakdown.getRate(), discount, finalPrice, breakdown.getSummary());
    }
}
