package ru.bmstu.iu3.service;


public interface MenuFallbackNotifier {

    void onMenuCsvFallback(String reasonSummary, Throwable cause);
}
