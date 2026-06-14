package org.example.fitness.model;


public enum SubscriptionPlan {
    VISITS_1("1 посещение", 0, 1),
    VISITS_10("10 посещений", 0, 10),
    VISITS_30("30 посещений", 0, 30),
    TIME_1_MONTH("Месяц", 1, 0),
    TIME_6_MONTHS("Полгода", 6, 0),
    TIME_1_YEAR("Год", 12, 0);

    private final String displayName;
    private final int durationMonths;
    private final int visitsTotal;

    SubscriptionPlan(String displayName, int durationMonths, int visitsTotal) {
        this.displayName = displayName;
        this.durationMonths = durationMonths;
        this.visitsTotal = visitsTotal;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public int getVisitsTotal() {
        return visitsTotal;
    }

    public boolean isVisitBased() {
        return visitsTotal > 0;
    }
}

