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
    public void showMenu() 
    {
        for (int i = 0; i < menuItems.size(); i++) {
            Dish dish = menuItems.get(i);
            System.out.println((i + 1) + ". " + dish.getName() + " --------------------------------- " + dish.getPrice() + " руб.\n Описание: " + dish.getDescription());
        }
    }
    @Override
    public Dish getDishByNumber(int number) {
        if (number < 1 || number > menuItems.size()) {
            throw new ValidationException("Неверный номер блюда. Пожалуйста, выберите номер от 1 до " + menuItems.size() + ".");
        }
        return menuItems.get(number - 1);
    }
    public List<Dish> getDishes() {
        return new ArrayList<Dish>(menuItems);
    }
}
