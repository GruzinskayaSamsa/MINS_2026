package delivery.strategy;

import delivery.model.DeliveryZone;

public class DefaultDeliveryTimeCalculator implements DeliveryTimeCalculator {

    @Override
    public int calculate(DeliveryZone zone) {

        switch (zone) {
            case CENTER:
                return 20;

            case NORTH:
            case SOUTH:
                return 30;

            case EAST:
            case WEST:
                return 40;

            default:
                return 35;
        }
    }
}