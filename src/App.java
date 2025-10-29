import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App {
    public static void main(String[] args) {
        // Create frame (window)
        JFrame frame = new JFrame("Retail Store Management");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Labels and text fields
        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setBounds(30, 30, 100, 25);
        JTextField nameField = new JTextField();
        nameField.setBounds(150, 30, 200, 25);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(30, 70, 100, 25);
        JTextField priceField = new JTextField();
        priceField.setBounds(150, 70, 200, 25);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 110, 100, 25);
        JTextField quantityField = new JTextField();
        quantityField.setBounds(150, 110, 200, 25);

        JButton calcButton = new JButton("Calculate Total");
        calcButton.setBounds(120, 160, 150, 30);

        JLabel totalLabel = new JLabel("Total: ");
        totalLabel.setBounds(30, 210, 300, 25);

        // Add action when button clicked
        calcButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double price = Double.parseDouble(priceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    double total = price * quantity;
                    totalLabel.setText("Total: Rs. " + total);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers!");
                }
            }
        });

        // Add all components
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(priceLabel);
        frame.add(priceField);
        frame.add(quantityLabel);
        frame.add(quantityField);
        frame.add(calcButton);
        frame.add(totalLabel);

        // Show window
        frame.setVisible(true);
    }
}
