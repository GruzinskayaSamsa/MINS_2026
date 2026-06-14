package org.example.fitness.pricing.discount;

public class DiscountBreakdown {
    private final double rate;
    private final String summary;

    public DiscountBreakdown(double rate, String summary) {
        this.rate = rate;
        this.summary = summary;
    }

    public double getRate() {
        return rate;
    }

    public String getSummary() {
        return summary;
    }
}
