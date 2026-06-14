package org.example.fitness.pricing.discount;

import java.time.DayOfWeek;
import java.time.LocalDate;


public class ChaoticDiscountEngine implements DiscountPolicy {

    @Override
    public double resolveDiscountRate(LocalDate date, double basePrice) {
        return resolveDiscountBreakdown(date, basePrice).getRate();
    }

    @Override
    public DiscountBreakdown resolveDiscountBreakdown(LocalDate date, double basePrice) {
        int day = date.getDayOfMonth();
        int lastDay = date.lengthOfMonth();
        boolean evenDay = day % 2 == 0;
        boolean friday = date.getDayOfWeek() == DayOfWeek.FRIDAY;
        StringBuilder trace = new StringBuilder();

        double discount = 0.0;
        if (friday) {
            if (evenDay) {
                if (day == 1) {
                    discount = 0.10;
                    trace.append("Пятница + четный день + первый день: 10%");
                } else if (day == lastDay) {
                    discount = 0.13;
                    trace.append("Пятница + четный день + последний день: 13%");
                } else {
                    if (basePrice > 3500) {
                        discount = 0.18;
                        trace.append("Пятница + четный день + база > 3500: 18%");
                    } else {
                        discount = 0.15;
                        trace.append("Пятница + четный день: 15%");
                    }
                }
            } else {
                if (day == 1 || day == lastDay) {
                    discount = 0.11;
                    trace.append("Пятница + нечетный день + край месяца: 11%");
                } else {
                    discount = 0.09;
                    trace.append("Пятница + нечетный день: 9%");
                }
            }
        } else {
            if (day == 1) {
                discount = 0.07;
                trace.append("Не пятница + первый день месяца: 7%");
            } else if (day == lastDay) {
                discount = 0.08;
                trace.append("Не пятница + последний день месяца: 8%");
            } else {
                if (evenDay) {
                    if (date.getMonthValue() % 2 == 0) {
                        discount = 0.06;
                        trace.append("Не пятница + четный день + четный месяц: 6%");
                    } else {
                        discount = 0.03;
                        trace.append("Не пятница + четный день + нечетный месяц: 3%");
                    }
                } else {
                    if (basePrice > 5000) {
                        discount = 0.04;
                        trace.append("Не пятница + нечетный день + база > 5000: 4%");
                    } else if (basePrice > 2500) {
                        discount = 0.02;
                        trace.append("Не пятница + нечетный день + база > 2500: 2%");
                    } else {
                        discount = 0.0;
                        trace.append("Не пятница + нечетный день + низкая база: 0%");
                    }
                }
            }
        }

        if (day == 1 && friday) {
            discount += 0.02;
            trace.append(" -> бонус первого дня в пятницу: +2%");
        } else if (day == lastDay && evenDay) {
            discount += 0.01;
            trace.append(" -> бонус четного последнего дня: +1%");
        }

        if (discount > 0.35) {
            discount = 0.35;
            trace.append(" -> ограничение максимумом: 35%");
        }
        trace.append(" | Итого: ").append(Math.round(discount * 100)).append("%");
        return new DiscountBreakdown(discount, trace.toString());
    }
}
