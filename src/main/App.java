package main;

import javax.swing.*;
import java.awt.event.*;
import model.*;
import payment.*;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Retail Store Management");
        frame.setSize(420, 380);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblName = new JLabel("Product Name:");
        lblName.setBounds(30, 30, 100, 25);
        JTextField txtName = new JTextField();
        txtName.setBounds(150, 30, 200, 25);

        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setBounds(30, 70, 100, 25);
        JTextField txtPrice = new JTextField();
        txtPrice.setBounds(150, 70, 200, 25);

        JLabel lblQty = new JLabel("Quantity:");
        lblQty.setBounds(30, 110, 100, 25);
        JTextField txtQty = new JTextField();
        txtQty.setBounds(150, 110, 200, 25);

        JLabel lblCustType = new JLabel("Customer Type:");
        lblCustType.setBounds(30, 150, 100, 25);
        String[] custOptions = {"Regular", "VIP"};
        JComboBox<String> cmbCustType = new JComboBox<>(custOptions);
        cmbCustType.setBounds(150, 150, 200, 25);

        JLabel lblPay = new JLabel("Payment Method:");
        lblPay.setBounds(30, 190, 120, 25);
        String[] payOptions = {"Cash", "Card"};
        JComboBox<String> cmbPay = new JComboBox<>(payOptions);
        cmbPay.setBounds(150, 190, 200, 25);

        JButton btnCalc = new JButton("Calculate Total");
        btnCalc.setBounds(120, 230, 150, 30);

        JLabel lblResult = new JLabel("Total: Rs. 0.00");
        lblResult.setBounds(30, 280, 300, 25);

        // Button Action
        btnCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = txtName.getText();
                    double price = Double.parseDouble(txtPrice.getText());
                    int qty = Integer.parseInt(txtQty.getText());
                    String custType = (String) cmbCustType.getSelectedItem();
                    String payType = (String) cmbPay.getSelectedItem();

                    Product product = new Product(name, price, qty);
                    Customer customer = custType.equals("VIP")
                            ? new VIPCustomer("Chamath")
                            : new Customer("Chamath");

                    double total = product.getTotal();
                    double discount = total * customer.getDiscountRate();
                    double finalAmount = total - discount;

                    Payment payment = payType.equals("Cash") ? new CashPayment() : new CardPayment();
                    payment.pay(finalAmount);

                    lblResult.setText(String.format("Total: Rs. %.2f (Discount: Rs. %.2f)", finalAmount, discount));

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input! Enter valid price and quantity.");
                }
            }
        });

        frame.add(lblName);
        frame.add(txtName);
        frame.add(lblPrice);
        frame.add(txtPrice);
        frame.add(lblQty);
        frame.add(txtQty);
        frame.add(lblCustType);
        frame.add(cmbCustType);
        frame.add(lblPay);
        frame.add(cmbPay);
        frame.add(btnCalc);
        frame.add(lblResult);

        frame.setVisible(true);
    }
}
