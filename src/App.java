import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.*;
import payment.*;
import util.*;

public class App {
    private static User currentUser;
    private static List<Product> inventory = new ArrayList<>();
    private static List<Product> cart = new ArrayList<>();
    private static List<Sale> sales = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static DefaultTableModel inventoryTableModel;
    private static DefaultTableModel cartTableModel;
    private static JFrame mainFrame;
    private static JTable inventoryTable;
    private static JTable cartTable;

    public static void main(String[] args) {
        loadData();
        showLoginDialog();
    }

    private static void loadData() {
        try {
            inventory = StoreUtils.loadProductsFromCSV("data/products.csv");
            users = StoreUtils.loadUsersFromCSV("data/users.csv");
            // Load sales data
            List<String> saleStrings = StoreUtils.loadSaleStringsFromCSV("data/sales.csv");
            for (String saleStr : saleStrings) {
                // For simplicity, we'll just count the sales but not fully reconstruct objects
                // In a full implementation, you'd parse and recreate Sale objects
            }
        } catch (Exception e) {
            // Initialize with default data if files don't exist
            initializeDefaultData();
        }

        // If no data loaded, initialize defaults
        if (inventory.isEmpty() || users.isEmpty()) {
            initializeDefaultData();
        }
    }

    private static void initializeDefaultData() {
        try {
            // Create default data files if they don't exist
            java.io.File dataDir = new java.io.File("data");
            dataDir.mkdirs();

            java.io.File productsFile = new java.io.File("data/products.csv");
            if (!productsFile.exists()) {
                try (java.io.PrintWriter writer = new java.io.PrintWriter(productsFile)) {
                    writer.println("name,price,quantity");
                    writer.println("Apple,100.0,50");
                    writer.println("Banana,60.0,30");
                    writer.println("Orange,80.0,40");
                }
            }

            java.io.File usersFile = new java.io.File("data/users.csv");
            if (!usersFile.exists()) {
                try (java.io.PrintWriter writer = new java.io.PrintWriter(usersFile)) {
                    writer.println("username,password,role");
                    writer.println("admin,admin123,Admin");
                    writer.println("cashier,cash123,Cashier");
                }
            }

            // Now load the data
            inventory = StoreUtils.loadProductsFromCSV("data/products.csv");
            users = StoreUtils.loadUsersFromCSV("data/users.csv");

        } catch (Exception e) {
            // If all else fails, add minimal defaults to memory
            inventory.add(new Product("Apple", 100.0, 50));
            users.add(new User("admin", "admin123", "Admin"));
        }
    }

    private static void showLoginDialog() {
        JDialog loginDialog = new JDialog((Frame) null, "Login", true);
        loginDialog.setSize(500, 300);
        loginDialog.setLayout(new BorderLayout());
        loginDialog.setLocationRelativeTo(null);

        // Main content panel with margins
        JPanel contentPanel = new JPanel(new GridLayout(4, 2, 20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Top, Left, Bottom, Right margins

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        JButton btnCancel = new JButton("Cancel");

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            for (User user : users) {
                if (user.getUsername().equals(username) && user.authenticate(password)) {
                    currentUser = user;
                    loginDialog.dispose();
                    showMainWindow();
                    return;
                }
            }
            JOptionPane.showMessageDialog(loginDialog, "Invalid credentials!");
        });

        btnCancel.addActionListener(e -> System.exit(0));

        contentPanel.add(lblUsername);
        contentPanel.add(txtUsername);
        contentPanel.add(lblPassword);
        contentPanel.add(txtPassword);
        contentPanel.add(new JLabel());
        contentPanel.add(btnLogin);
        contentPanel.add(new JLabel());
        contentPanel.add(btnCancel);

        loginDialog.add(contentPanel, BorderLayout.CENTER);
        loginDialog.setVisible(true);
    }

    private static void showMainWindow() {
        mainFrame = new JFrame("Retail Store Management - " + currentUser.getRole());
        mainFrame.setSize(800, 600);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save Data");
        saveItem.addActionListener(e -> saveData());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        if (currentUser.isAdmin()) {
            JMenu adminMenu = new JMenu("Admin");
            JMenuItem addProductItem = new JMenuItem("Add Product");
            addProductItem.addActionListener(e -> showAddProductDialog());
            JMenuItem removeProductItem = new JMenuItem("Remove Product");
            removeProductItem.addActionListener(e -> removeSelectedProduct());
            adminMenu.add(addProductItem);
            adminMenu.add(removeProductItem);
            menuBar.add(adminMenu);
        }

        mainFrame.setJMenuBar(menuBar);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Inventory Table
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        String[] inventoryColumns = {"Name", "Price", "Stock"};
        inventoryTableModel = new DefaultTableModel(inventoryColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryTable = new JTable(inventoryTableModel);
        updateInventoryTable();
        JScrollPane inventoryScroll = new JScrollPane(inventoryTable);
        inventoryPanel.add(inventoryScroll, BorderLayout.CENTER);

        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.addActionListener(e -> addToCart());
        inventoryPanel.add(btnAddToCart, BorderLayout.SOUTH);

        // Cart Table
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));
        String[] cartColumns = {"Name", "Price", "Quantity", "Total"};
        cartTableModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Allow editing quantity
            }
        };
        cartTable = new JTable(cartTableModel);
        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartPanel.add(cartScroll, BorderLayout.CENTER);

        JButton btnRemoveFromCart = new JButton("Remove from Cart");
        btnRemoveFromCart.addActionListener(e -> removeFromCart());
        cartPanel.add(btnRemoveFromCart, BorderLayout.SOUTH);

        // Split Pane for Inventory and Cart
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inventoryPanel, cartPanel);
        splitPane.setDividerLocation(400);

        // Bottom Panel for Checkout
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JLabel lblCustomerName = new JLabel("Customer Name:");
        JTextField txtCustomerName = new JTextField(10);
        txtCustomerName.setText("John Doe");

        String[] custTypes = {"Regular", "VIP"};
        JComboBox<String> cmbCustomerType = new JComboBox<>(custTypes);

        String[] payTypes = {"Cash", "Card"};
        JComboBox<String> cmbPaymentType = new JComboBox<>(payTypes);

        JButton btnCheckout = new JButton("Checkout");
        btnCheckout.addActionListener(e -> checkout(txtCustomerName.getText(),
                (String) cmbCustomerType.getSelectedItem(),
                (String) cmbPaymentType.getSelectedItem()));

        bottomPanel.add(lblCustomerName);
        bottomPanel.add(txtCustomerName);
        bottomPanel.add(cmbCustomerType);
        bottomPanel.add(cmbPaymentType);
        bottomPanel.add(btnCheckout);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private static void updateInventoryTable() {
        inventoryTableModel.setRowCount(0);
        for (Product p : inventory) {
            inventoryTableModel.addRow(new Object[]{p.getName(), p.getPrice(), p.getQuantity()});
        }
    }

    private static void updateCartTable() {
        cartTableModel.setRowCount(0);
        for (Product p : cart) {
            cartTableModel.addRow(new Object[]{p.getName(), p.getPrice(), p.getQuantity(), p.getTotal()});
        }
    }

    private static void addToCart() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product selectedProduct = inventory.get(selectedRow);
            if (selectedProduct.getQuantity() > 0) {
                // Simple add, in real app would handle quantity selection
                Product cartItem = new Product(selectedProduct.getName(), selectedProduct.getPrice(), 1);
                cart.add(cartItem);
                selectedProduct = new Product(selectedProduct.getName(), selectedProduct.getPrice(), selectedProduct.getQuantity() - 1);
                inventory.set(selectedRow, selectedProduct);
                updateInventoryTable();
                updateCartTable();
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Out of stock!");
            }
        }
    }

    private static void removeFromCart() {
        // Implementation for removing from cart
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow >= 0) {
            Product removed = cart.remove(selectedRow);
            // Return to inventory
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.get(i).getName().equals(removed.getName())) {
                    Product p = inventory.get(i);
                    inventory.set(i, new Product(p.getName(), p.getPrice(), p.getQuantity() + removed.getQuantity()));
                    break;
                }
            }
            updateInventoryTable();
            updateCartTable();
        }
    }

    private static void checkout(String customerName, String customerType, String paymentType) {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Cart is empty!");
            return;
        }

        Customer customer = customerType.equals("VIP") ? new VIPCustomer(customerName) : new Customer(customerName);
        Payment payment = paymentType.equals("Cash") ? new CashPayment() : new CardPayment();

        String saleId = "TXN" + System.currentTimeMillis();
        Sale sale = new Sale(saleId, customer, cart, payment);
        sales.add(sale);

        payment.pay(sale.getFinalAmount());

        JOptionPane.showMessageDialog(mainFrame, String.format("Checkout successful!\nTotal: Rs. %.2f\nDiscount: Rs. %.2f\nFinal: Rs. %.2f",
                sale.getTotalAmount(), sale.getDiscountAmount(), sale.getFinalAmount()));

        cart.clear();
        updateCartTable();
    }

    private static void showAddProductDialog() {
        JDialog dialog = new JDialog(mainFrame, "Add Product", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblName = new JLabel("Name:");
        JTextField txtName = new JTextField();
        JLabel lblPrice = new JLabel("Price:");
        JTextField txtPrice = new JTextField();
        JLabel lblQty = new JLabel("Quantity:");
        JTextField txtQty = new JTextField();
        JButton btnAdd = new JButton("Add");
        JButton btnCancel = new JButton("Cancel");

        btnAdd.addActionListener(e -> {
            try {
                String name = txtName.getText();
                double price = Double.parseDouble(txtPrice.getText());
                int qty = Integer.parseInt(txtQty.getText());
                inventory.add(new Product(name, price, qty));
                updateInventoryTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input!");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(lblName);
        dialog.add(txtName);
        dialog.add(lblPrice);
        dialog.add(txtPrice);
        dialog.add(lblQty);
        dialog.add(txtQty);
        dialog.add(btnAdd);
        dialog.add(btnCancel);

        dialog.setVisible(true);
    }

    private static void removeSelectedProduct() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            inventory.remove(selectedRow);
            updateInventoryTable();
        }
    }

    private static void saveData() {
        try {
            new java.io.File("data").mkdirs();
            StoreUtils.saveProductsToCSV(inventory, "data/products.csv");
            StoreUtils.saveUsersToCSV(users, "data/users.csv");
            StoreUtils.saveSalesToCSV(sales, "data/sales.csv");
            JOptionPane.showMessageDialog(mainFrame, "Data saved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, "Error saving data: " + e.getMessage());
        }
    }
}
