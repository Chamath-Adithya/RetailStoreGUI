package payment;

public class CashPayment extends Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Paid in Cash: Rs. " + amount);
    }
}
