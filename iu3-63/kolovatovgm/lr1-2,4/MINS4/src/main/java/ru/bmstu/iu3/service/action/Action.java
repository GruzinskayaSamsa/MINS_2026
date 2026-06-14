package ru.bmstu.iu3.service.action;

public interface Action { // command
    String description();
    void execute();
}

