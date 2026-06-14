package org.example.fitness.pricing;

import org.example.fitness.model.SubscriptionPlan;


public class QuickPriceInput {
    private final SubscriptionPlan plan;
    private final int age;
    private final boolean student;

    public QuickPriceInput(SubscriptionPlan plan, int age, boolean student) {
        this.plan = plan;
        this.age = age;
        this.student = student;
    }

    public SubscriptionPlan getPlan() {
        return plan;
    }

    public int getAge() {
        return age;
    }

    public boolean isStudent() {
        return student;
    }
}
