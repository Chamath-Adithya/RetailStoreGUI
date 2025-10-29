package utils;

public class StoreUtils {
    public static double calculateDiscount(double total, double discountRate) {
        return total * discountRate;
    }

    public static String formatCurrency(double amount) {
        return String.format("Rs. %.2f", amount);
    }
}
