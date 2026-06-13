package delivery.service;

import delivery.model.Courier;
import java.util.ArrayList;
import java.util.List;

public class CourierService {

    private final List<Courier> couriers = new ArrayList<>();

    public void addCourier(Courier courier) {
        couriers.add(courier);
    }

    public Courier findAvailableCourier() {

        for (Courier courier : couriers) {
            if (courier.isAvailable()) {
                courier.setAvailable(false);
                return courier;
            }
        }

        return null;
    }
}