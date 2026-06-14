package ru.bmstu.iu3.menu;

import java.util.List;

public final class MenuPrinter {

    public void print(MenuRepository menu) {
        List<Dish> dishes = menu.getDishes();
        for (int i = 0; i < dishes.size(); i++) {
            Dish dish = dishes.get(i);
            System.out.println((i + 1) + ". " + dish.getName() + " --------------------------------- " + dish.getPrice() + " руб.\n Описание: " + dish.getDescription());
        }
    }
}
