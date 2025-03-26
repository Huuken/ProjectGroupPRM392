package com.example.freshfoldlaundrycare.Modal; // Defines the package where this class resides

public class Products { // Declares a public class named Products

    // Declares instance variables (fields) of type String to store product details
    String Category, OrderDate, OrderTime, ProductId, ProductName, ProductPrice, TotalPrice;
    int Quantity; // Declares an integer field to store the quantity of the product

    public Products() { // Default constructor with no parameters
    }

    // Parameterized constructor to initialize all fields when creating a Products object
    public Products(String category, String orderDate, String orderTime, String productId, String productName, String productPrice, String totalPrice, int quantity) {
        Category = category; // Assigns the passed category parameter to the Category field
        OrderDate = orderDate; // Assigns the passed orderDate parameter to the OrderDate field
        OrderTime = orderTime; // Assigns the passed orderTime parameter to the OrderTime field
        ProductId = productId; // Assigns the passed productId parameter to the ProductId field
        ProductName = productName; // Assigns the passed productName parameter to the ProductName field
        ProductPrice = productPrice; // Assigns the passed productPrice parameter to the ProductPrice field
        TotalPrice = totalPrice; // Assigns the passed totalPrice parameter to the TotalPrice field
        Quantity = quantity; // Assigns the passed quantity parameter to the Quantity field
    }

    // Getter method to retrieve the value of Category
    public String getCategory() {
        return Category; // Returns the current value of Category
    }

    // Setter method to update the value of Category
    public void setCategory(String category) {
        Category = category; // Assigns the passed category parameter to the Category field
    }

    // Getter method to retrieve the value of OrderDate
    public String getOrderDate() {
        return OrderDate; // Returns the current value of OrderDate
    }

    // Setter method to update the value of OrderDate
    public void setOrderDate(String orderDate) {
        OrderDate = orderDate; // Assigns the passed orderDate parameter to the OrderDate field
    }

    // Getter method to retrieve the value of OrderTime
    public String getOrderTime() {
        return OrderTime; // Returns the current value of OrderTime
    }

    // Setter method to update the value of OrderTime
    public void setOrderTime(String orderTime) {
        OrderTime = orderTime; // Assigns the passed orderTime parameter to the OrderTime field
    }

    // Getter method to retrieve the value of ProductId
    public String getProductId() {
        return ProductId; // Returns the current value of ProductId
    }

    // Setter method to update the value of ProductId
    public void setProductId(String productId) {
        ProductId = productId; // Assigns the passed productId parameter to the ProductId field
    }

    // Getter method to retrieve the value of ProductName
    public String getProductName() {
        return ProductName; // Returns the current value of ProductName
    }

    // Setter method to update the value of ProductName
    public void setProductName(String productName) {
        ProductName = productName; // Assigns the passed productName parameter to the ProductName field
    }

    // Getter method to retrieve the value of ProductPrice
    public String getProductPrice() {
        return ProductPrice; // Returns the current value of ProductPrice
    }

    // Setter method to update the value of ProductPrice
    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice; // Assigns the passed productPrice parameter to the ProductPrice field
    }

    // Getter method to retrieve the value of TotalPrice
    public String getTotalPrice() {
        return TotalPrice; // Returns the current value of TotalPrice
    }

    // Setter method to update the value of TotalPrice
    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice; // Assigns the passed totalPrice parameter to the TotalPrice field
    }

    // Getter method to retrieve the value of Quantity
    public int getQuantity() {
        return Quantity; // Returns the current value of Quantity
    }

    // Setter method to update the value of Quantity
    public void setQuantity(int quantity) {
        Quantity = quantity; // Assigns the passed quantity parameter to the Quantity field
    }
} // Closes the Products class