package ru.bmstu.iu3.menu;

import ru.bmstu.iu3.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class Menu implements MenuRepository {
    /* По хорошему тут должен быть файл меню внешний
    который бы считывался этой имбой, но мне лееееееееееееень */
    List<Dish> menuItems = new ArrayList<Dish>();
    @Override
    public void addDish(Dish dish) 
    {
        menuItems.add(dish);
    }
    @Override
    public void removeDish(Dish dish) 
    {
            menuItems.remove(dish);
    }

    @Override
    public List<Dish> getDishes() {
        return new ArrayList<>(menuItems);
    }

    @Override
    public Dish getDishByNumber(int number) {
        if (number < 1 || number > menuItems.size()) {
            throw new ValidationException("Неверный номер блюда. Пожалуйста, выберите номер от 1 до " + menuItems.size() + ".");
        }
        return menuItems.get(number - 1);
    }
}
