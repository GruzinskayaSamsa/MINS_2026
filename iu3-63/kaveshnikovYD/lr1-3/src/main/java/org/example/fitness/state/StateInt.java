package org.example.fitness.state;

import org.example.fitness.clock.ClockSim;
import org.example.fitness.service.IntSubscriptionService;

@FunctionalInterface
public interface StateInt {

    void contribute();

}
