package ru.bmstu.iu3.menu;

import java.util.Objects;

public final class SimpleDish implements Dish {
    private final String name;
    private final int price;
    private final String description;

    public SimpleDish(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Dish)) {
            return false;
        }
        Dish dish = (Dish) other;
        return price == dish.getPrice()
                && Objects.equals(name, dish.getName())
                && Objects.equals(description, dish.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, description);
    }
}
