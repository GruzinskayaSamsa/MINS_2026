package ru.bmstu.iu3.menu;

import java.util.List;

public interface MenuRepository {
    void addDish(Dish dish);

    void removeDish(Dish dish);

    List<Dish> getDishes();

    Dish getDishByNumber(int number);
}
