package delivery.strategy;

import delivery.model.DeliveryZone;

import java.util.Random;

public class TrafficDeliveryCalculator implements DeliveryTimeCalculator {

    private final Random random = new Random();

    @Override
    public int calculate(DeliveryZone zone) {

        int baseTime;

        switch (zone) {
            case CENTER:
                baseTime = 20;
                break;

            case NORTH:
            case SOUTH:
                baseTime = 30;
                break;

            case EAST:
            case WEST:
                baseTime = 40;
                break;

            default:
                baseTime = 35;
        }

        // случайная задержка (пробки)
        int trafficDelay = random.nextInt(15); // 0–14 минут

        return baseTime + trafficDelay;
    }
}