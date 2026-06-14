package ru.bmstu.iu3.menu;

public interface MenuRepository {
    void addDish(Dish dish);
    public void removeDish(Dish dish);
    public void showMenu() ;
    public Dish getDishByNumber(int number);
}
