package org.example.fitness.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationBoard {

    private final List<String> messages = new ArrayList<>();


    public synchronized void add(String message) {
        if (message != null && !message.isBlank()) {
            messages.add(message);
        }
    }


    public synchronized void printToConsole() {
        if (messages.isEmpty()) {
            return;
        }
        for (String line : messages) {
            System.out.println(line);
        }
        messages.clear();
        System.out.println();
    }
}
