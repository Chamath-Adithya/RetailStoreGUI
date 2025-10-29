# Retail Store Management System (GUI Edition)

A comprehensive Java Swing application demonstrating Object-Oriented Programming (OOP) concepts through a retail store management system with login, inventory management, and transaction processing.

## 🧱 Project Structure

```
RetailStoreGUI/
├── README.md
├── data/               # CSV files for data persistence
│   ├── products.csv
│   ├── users.csv
│   └── transactions.csv
├── lib/                # (Empty for now; add .jar libs later if needed)
├── bin/                # Compiled .class files
└── src/                # All your Java source code lives here
    ├── main/           # GUI and main logic
    │   └── App.java
    ├── model/          # Data models (Encapsulation + Inheritance)
    │   ├── Product.java
    │   ├── Customer.java
    │   ├── VIPCustomer.java
    │   ├── User.java
    │   └── Transaction.java
    ├── payment/        # Abstraction examples (Payment types)
    │   ├── Payment.java
    │   ├── CashPayment.java
    │   └── CardPayment.java
    └── utils/          # Utilities (helpers, calculations, file I/O)
        └── StoreUtils.java
```

## 🧩 OOP Concepts Demonstrated

| OOP Concept         | Where it appears                                                   | Description |
|---------------------|-------------------------------------------------------------------|-------------|
| **Encapsulation**   | `Product` → private fields with getters                            | Data hiding and controlled access to product properties |
| **Inheritance**     | `Customer` → `VIPCustomer`                                         | VIPCustomer inherits from Customer, gaining base functionality |
| **Polymorphism**    | Overridden `getDiscountRate()` and `getCustomerType()`            | Same method names behave differently for different customer types |
| **Abstraction**     | `Payment` abstract class + `CashPayment`, `CardPayment` subclasses | Abstract payment contract with concrete implementations |

### Detailed OOP Breakdown:

#### 1️⃣ **Encapsulation** (Product.java)
```java
public class Product {
    private String name;      // Private fields
    private double price;
    private int quantity;

    public String getName() { return name; }  // Public getters
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}
```
- **Purpose**: Protects data integrity by hiding implementation details
- **Benefits**: Prevents direct manipulation of data, allows validation

#### 2️⃣ **Inheritance** (Customer.java & VIPCustomer.java)
```java
public class Customer {
    protected String name;  // Protected for subclass access

    public double getDiscountRate() { return 0; }  // Base discount
}

public class VIPCustomer extends Customer {
    @Override
    public double getDiscountRate() { return 0.1; }  // 10% discount
}
```
- **Purpose**: Code reuse and hierarchical relationships
- **Benefits**: VIPCustomer automatically gets Customer behavior + adds special features

#### 3️⃣ **Polymorphism** (Method Overriding)
- Same method `getDiscountRate()` returns different values:
  - `Customer`: 0% discount
  - `VIPCustomer`: 10% discount
- **Purpose**: Flexibility in behavior based on object type
- **Benefits**: Write generic code that works with different implementations

#### 4️⃣ **Abstraction** (Payment Classes)
```java
public abstract class Payment {
    public abstract void pay(double amount);  // Contract only
}

public class CashPayment extends Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Paid in Cash: Rs. " + amount);
    }
}
```
- **Purpose**: Define what to do, not how to do it
- **Benefits**: Easy to add new payment types (e.g., DigitalWallet) without changing existing code

## 🏃 How to Run

In VS Code terminal:

```bash
cd src
javac main/App.java model/*.java payment/*.java utils/*.java -d ../bin
cd ../bin
java main.App
```

## 🎯 Features

- **User Authentication**: Login system with Admin and Cashier roles
- **Inventory Management**: JTable display of products with stock levels
- **Shopping Cart**: Add/remove products to/from cart with JTable display
- **Stock Management**: Real-time stock updates, add/remove products (Admin only)
- **Customer Types**: Regular customers (0% discount) and VIP customers (10% discount)
- **Payment Methods**: Cash or Card payment processing with transaction records
- **Data Persistence**: CSV file storage for products, users, and transactions
- **GUI Interface**: Comprehensive Swing interface with tables, menus, and dialogs
- **Transaction Processing**: Complete checkout with discount calculation and payment
- **Role-based Access**: Different features available based on user role

### Additional OOP Concepts in New Features:

#### 5️⃣ **Composition** (Transaction.java)
```java
public class Transaction {
    private Customer customer;
    private List<Product> products;
    private Payment payment;
    // ... contains multiple objects working together
}
```
- **Purpose**: Build complex objects from simpler ones
- **Benefits**: Flexible relationships between classes

#### 6️⃣ **Polymorphism in Collections** (StoreUtils.java)
```java
public static void saveProductsToCSV(List<Product> products, String filename)
public static void saveUsersToCSV(List<User> users, String filename)
```
- **Purpose**: Generic methods that work with different types
- **Benefits**: Code reuse and type safety

## 📚 Learning Objectives

This project teaches:
- How to structure Java projects with packages
- Implementing OOP principles in real applications
- Creating GUI applications with Swing
- Separating concerns (model, view, payment logic)
- Writing maintainable and extensible code
