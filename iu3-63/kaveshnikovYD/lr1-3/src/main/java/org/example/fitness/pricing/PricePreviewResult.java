package org.example.fitness.pricing;

public class PricePreviewResult {
    private final double basePrice;
    private final double discountRate;
    private final double discountAmount;
    private final double finalPrice;
    private final String discountSummary;

    public PricePreviewResult(double basePrice, double discountRate, double discountAmount, double finalPrice, String discountSummary) {
        this.basePrice = basePrice;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.finalPrice = finalPrice;
        this.discountSummary = discountSummary;
    }

    public double getBasePrice() {return basePrice;}

    public double getDiscountRate() {return discountRate;}

    public double getDiscountAmount() {return discountAmount;}

    public double getFinalPrice() {return finalPrice;}

    public String getDiscountSummary() {return discountSummary;}
}
