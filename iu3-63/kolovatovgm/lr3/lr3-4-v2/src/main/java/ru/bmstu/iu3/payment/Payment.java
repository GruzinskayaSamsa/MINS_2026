package ru.bmstu.iu3.payment;

import ru.bmstu.iu3.service.InputReader;

public interface Payment { // strategy
    void pay(int amount, InputReader reader);
    void description();
}
