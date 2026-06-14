package org.example.fitness.clock;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClockSim {
    LocalDateTime today;
    public ClockSim() {
        today = LocalDateTime.now();
    }
    public LocalDateTime getToday() {
        return today;
    }
    public void advanceDay() {
        today = today.plusDays(1);
    }

}
