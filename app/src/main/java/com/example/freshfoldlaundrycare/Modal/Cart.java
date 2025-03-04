package com.example.freshfoldlaundrycare.Modal;

public class Cart {

    String UserId, ProductId, ProductName, ProductPrice, Category, TotalPrice;
    int Quantity;

    public Cart() {
    }

    public Cart(String userId, String productId, String productName, String productPrice, String category, String totalPrice, int quantity) {
        UserId = userId;
        ProductId = productId;
        ProductName = productName;
        ProductPrice = productPrice;
        Category = category;
        TotalPrice = totalPrice;
        Quantity = quantity;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
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

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
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