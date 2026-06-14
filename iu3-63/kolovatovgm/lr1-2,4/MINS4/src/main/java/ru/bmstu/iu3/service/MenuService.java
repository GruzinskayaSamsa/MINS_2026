package ru.bmstu.iu3.service;

import ru.bmstu.iu3.menu.MenuRepository;
import ru.bmstu.iu3.reference.ReferenceMenuClient;

public class MenuService {
    private final MenuRepository menu;

    public MenuService() {
        this(new ReferenceMenuClient("localhost", 50051));
    }

    public MenuService(MenuRepository menu) {
        this.menu = menu;
    }

    public MenuRepository getMenu() {
        return menu;
    }

    public void displayMenu() {
        menu.showMenu();
    }
}
