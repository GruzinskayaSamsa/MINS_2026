package delivery.strategy;

import delivery.model.DeliveryZone;

public class ExpressDeliveryCalculator implements DeliveryTimeCalculator {

    @Override
    public int calculate(DeliveryZone zone) {

        // экспресс всегда быстрее
        return 15;
    }
}