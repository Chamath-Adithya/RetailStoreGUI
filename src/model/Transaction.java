package model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import payment.Payment;

public class Transaction {
    private String transactionId;
    private Customer customer;
    private List<Product> products;
    private Payment payment;
    private double totalAmount;
    private double discountAmount;
    private double finalAmount;
    private LocalDateTime timestamp;

    public Transaction(String transactionId, Customer customer, List<Product> products, Payment payment) {
        this.transactionId = transactionId;
        this.customer = customer;
        this.products = new ArrayList<>(products);
        this.payment = payment;
        this.timestamp = LocalDateTime.now();
        calculateAmounts();
    }

    private void calculateAmounts() {
        this.totalAmount = products.stream().mapToDouble(Product::getTotal).sum();
        this.discountAmount = totalAmount * customer.getDiscountRate();
        this.finalAmount = totalAmount - discountAmount;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public Customer getCustomer() { return customer; }
    public List<Product> getProducts() { return new ArrayList<>(products); }
    public Payment getPayment() { return payment; }
    public double getTotalAmount() { return totalAmount; }
    public double getDiscountAmount() { return discountAmount; }
    public double getFinalAmount() { return finalAmount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Transaction ID: %s, Customer: %s, Total: Rs. %.2f, Final: Rs. %.2f",
                transactionId, customer.getCustomerType(), totalAmount, finalAmount);
    }
}
