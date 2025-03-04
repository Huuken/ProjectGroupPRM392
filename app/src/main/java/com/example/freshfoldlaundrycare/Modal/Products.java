package com.example.freshfoldlaundrycare.Modal;

public class Products {

    String Category, OrderDate, OrderTime, ProductId, ProductName, ProductPrice, TotalPrice;
    int Quantity;

    public Products() {
    }

    public Products(String category, String orderDate, String orderTime, String productId, String productName, String productPrice, String totalPrice, int quantity) {
        Category = category;
        OrderDate = orderDate;
        OrderTime = orderTime;
        ProductId = productId;
        ProductName = productName;
        ProductPrice = productPrice;
        TotalPrice = totalPrice;
        Quantity = quantity;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}