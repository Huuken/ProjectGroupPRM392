package com.example.freshfoldlaundrycare.Modal; // Defines the package name for this class, organizing it under the "Modal" subpackage (likely intended as "Model").

public class Cart { // Declares the Cart class, representing an item in a shopping cart.

    // Declares instance variables for the Cart class with their respective data types.
    String UserId, ProductId, ProductName, ProductPrice, Category, TotalPrice; // String variables for user ID, product ID, name, price, category, and total price.
    int Quantity; // Integer variable for the quantity of the product in the cart.

    public Cart() { // Defines a no-argument (default) constructor.
        // Required empty constructor, typically used by Firebase/Firestore for deserialization.
        // No initialization is performed here; fields will take default values (null for Strings, 0 for int).
    } // Closes the default constructor.

    // Defines a parameterized constructor to initialize all fields of the Cart object.
    public Cart(String userId, String productId, String productName, String productPrice, String category, String totalPrice, int quantity) {
        UserId = userId; // Assigns the provided userId to the UserId field.
        ProductId = productId; // Assigns the provided productId to the ProductId field.
        ProductName = productName; // Assigns the provided productName to the ProductName field.
        ProductPrice = productPrice; // Assigns the provided productPrice to the ProductPrice field.
        Category = category; // Assigns the provided category to the Category field.
        TotalPrice = totalPrice; // Assigns the provided totalPrice to the TotalPrice field.
        Quantity = quantity; // Assigns the provided quantity to the Quantity field.
    } // Closes the parameterized constructor.

    // Getter method for UserId.
    public String getUserId() {
        return UserId; // Returns the value of the UserId field.
    } // Closes the getUserId method.

    // Setter method for UserId.
    public void setUserId(String userId) {
        UserId = userId; // Sets the UserId field to the provided userId value.
    } // Closes the setUserId method.

    // Getter method for ProductId.
    public String getProductId() {
        return ProductId; // Returns the value of the ProductId field.
    } // Closes the getProductId method.

    // Setter method for ProductId.
    public void setProductId(String productId) {
        ProductId = productId; // Sets the ProductId field to the provided productId value.
    } // Closes the setProductId method.

    // Getter method for ProductName.
    public String getProductName() {
        return ProductName; // Returns the value of the ProductName field.
    } // Closes the getProductName method.

    // Setter method for ProductName.
    public void setProductName(String productName) {
        ProductName = productName; // Sets the ProductName field to the provided productName value.
    } // Closes the setProductName method.

    // Getter method for ProductPrice.
    public String getProductPrice() {
        return ProductPrice; // Returns the value of the ProductPrice field.
    } // Closes the getProductPrice method.

    // Setter method for ProductPrice.
    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice; // Sets the ProductPrice field to the provided productPrice value.
    } // Closes the setProductPrice method.

    // Getter method for Category.
    public String getCategory() {
        return Category; // Returns the value of the Category field.
    } // Closes the getCategory method.

    // Setter method for Category.
    public void setCategory(String category) {
        Category = category; // Sets the Category field to the provided category value.
    } // Closes the setCategory method.

    // Getter method for TotalPrice.
    public String getTotalPrice() {
        return TotalPrice; // Returns the value of the TotalPrice field.
    } // Closes the getTotalPrice method.

    // Setter method for TotalPrice.
    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice; // Sets the TotalPrice field to the provided totalPrice value.
    } // Closes the setTotalPrice method.

    // Getter method for Quantity.
    public int getQuantity() {
        return Quantity; // Returns the value of the Quantity field.
    } // Closes the getQuantity method.

    // Setter method for Quantity.
    public void setQuantity(int quantity) {
        Quantity = quantity; // Sets the Quantity field to the provided quantity value.
    } // Closes the setQuantity method.
} // Closes the Cart class.