package org.example.fitness.model;

import java.time.LocalDateTime;


public class Attendance {

    private int id;
    private int clientId;
    private int subscriptionId;
    private LocalDateTime checkedInAt;

    public Attendance() {
    }

    public Attendance(int id, int clientId, int subscriptionId, LocalDateTime checkedInAt) {
        this.id = id;
        this.clientId = clientId;
        this.subscriptionId = subscriptionId;
        this.checkedInAt = checkedInAt;
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

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return id == that.id;
    }
}
