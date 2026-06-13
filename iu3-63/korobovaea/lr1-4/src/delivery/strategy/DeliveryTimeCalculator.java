package delivery.strategy;

import delivery.model.DeliveryZone;

public interface DeliveryTimeCalculator {

    int calculate(DeliveryZone zone);
}