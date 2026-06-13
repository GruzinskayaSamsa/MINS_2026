package delivery.bad;

// Намеренно "плохой" калькулятор для задания по антипаттернам.
// Здесь оставлены God Class + Magic Numbers + Hardcoded data.
public class QuickOrderCalculator {

    // Захардкоженная база по зонам (1..5).
    private static final double[] LEGACY_BASE_BY_ZONE = {5.0, 7.0, 6.0, 8.0, 10.0};

    // Быстрый расчет только по локальной таблице.
    public static double calculate(int zoneChoice, int itemsCount, boolean express) {

        double basePrice;
        int idx = zoneChoice - 1;
        if (idx >= 0 && idx < LEGACY_BASE_BY_ZONE.length) {
            basePrice = LEGACY_BASE_BY_ZONE[idx];
        } else {
            basePrice = 10;
        }

        double itemsPrice = itemsCount * 3.5;

        if (express) {
            basePrice = basePrice * 2;
        }

        double total = basePrice + itemsPrice;

        if (itemsCount > 10) {
            total = total - 4.123;
        }

        return total;
    }

    // Расчет с базой, полученной из Service B.
    public static double calculateWithBase(double basePrice, int itemsCount, boolean express) {

        double base = basePrice;
        double itemsPrice = itemsCount * 3.5;

        if (express) {
            base = base * 2;
        }

        double total = base + itemsPrice;

        if (itemsCount > 10) {
            total = total - 4.123;
        }

        return total;
    }
}
