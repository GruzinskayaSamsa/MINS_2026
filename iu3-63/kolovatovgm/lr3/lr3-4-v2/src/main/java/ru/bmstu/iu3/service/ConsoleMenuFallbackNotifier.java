package ru.bmstu.iu3.service;

public final class ConsoleMenuFallbackNotifier implements MenuFallbackNotifier {

    @Override
    public void onMenuCsvFallback(String reasonSummary, Throwable cause) {
        System.err.println("ВНИМАНИЕ: меню из menu.csv не загружено, подставлено встроенное меню по умолчанию.");
        System.err.println("Причина: " + reasonSummary + ".");
        if (cause != null && cause.getMessage() != null && !cause.getMessage().isEmpty()) {
            System.err.println("Детали: " + cause.getMessage());
        }
    }
}
