package org.example.fitness.model;

import java.time.LocalDate;

/**
 * Модель абонемента.
 */
public class Subscription {

    private int id;
    private int clientId;
    private SubscriptionPlan plan;
    private LocalDate startDate;
    private LocalDate endDate;
    private int visitsUsed;
    private boolean isActive;
    // использовано



    public Subscription(int id, int clientId, SubscriptionPlan plan, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.clientId = clientId;
        this.plan = plan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.visitsUsed = 0;
        this.isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public SubscriptionPlan getPlan() {
        return plan;
    }

    public void setPlan(SubscriptionPlan plan) {
        this.plan = plan;
    }

    public String getType() {
        return plan == null ? "" : plan.getDisplayName();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getVisitsTotal() {
        return plan == null ? 0 : plan.getVisitsTotal();
    }

    public int getVisitsUsed() {
        return visitsUsed;
    }

    public void setVisitsUsed(int visitsUsed) {
        this.visitsUsed = visitsUsed;
    }

    public boolean isExpired(LocalDate date) {
        return date.isAfter(endDate) || date.equals(endDate);
    }

    public boolean hasVisitsLeft() {
        if (plan == null) {
            return false;
        }
        if (!plan.isVisitBased()) {
            return true; // ограничение только по сроку
        }
        return visitsUsed < plan.getVisitsTotal();
    }

    public boolean isVisitBased() {
        return plan != null && plan.isVisitBased();
    }


    public String formatVisits() {
        if (isVisitBased()) {
            return getVisitsUsed() + "/" + getVisitsTotal();
        }
        return String.valueOf(getVisitsUsed());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return id == that.id;
    }
}
