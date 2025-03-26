package com.example.freshfoldlaundrycare.Modal; // Defines the package where this class resides

public class Orders { // Declares a public class named Orders

    // Declares instance variables (fields) of type String to store order details
    String OrderStatus, OrderTime, OrderDate, OrderID, UserID, Phone, Name, Address, TotalPrice, Pincode, PickupTime, DeliveryTime, City, Email;

    public Orders() { // Default constructor with no parameters
    }

    // Parameterized constructor to initialize all fields when creating an Orders object
    public Orders(String orderStatus, String orderTime, String orderDate, String orderID, String userID, String phone, String name, String address, String totalPrice, String pincode, String pickupTime, String deliveryTime, String city, String email) {
        OrderStatus = orderStatus; // Assigns the passed orderStatus parameter to the OrderStatus field
        OrderTime = orderTime; // Assigns the passed orderTime parameter to the OrderTime field
        OrderDate = orderDate; // Assigns the passed orderDate parameter to the OrderDate field
        OrderID = orderID; // Assigns the passed orderID parameter to the OrderID field
        UserID = userID; // Assigns the passed userID parameter to the UserID field
        Phone = phone; // Assigns the passed phone parameter to the Phone field
        Name = name; // Assigns the passed name parameter to the Name field
        Address = address; // Assigns the passed address parameter to the Address field
        TotalPrice = totalPrice; // Assigns the passed totalPrice parameter to the TotalPrice field
        Pincode = pincode; // Assigns the passed pincode parameter to the Pincode field
        PickupTime = pickupTime; // Assigns the passed pickupTime parameter to the PickupTime field
        DeliveryTime = deliveryTime; // Assigns the passed deliveryTime parameter to the DeliveryTime field
        City = city; // Assigns the passed city parameter to the City field
        Email = email; // Assigns the passed email parameter to the Email field
    }

    // Getter method to retrieve the value of OrderStatus
    public String getOrderStatus() {
        return OrderStatus; // Returns the current value of OrderStatus
    }

    // Setter method to update the value of OrderStatus
    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus; // Assigns the passed orderStatus parameter to the OrderStatus field
    }

    // Getter method to retrieve the value of OrderTime
    public String getOrderTime() {
        return OrderTime; // Returns the current value of OrderTime
    }

    // Setter method to update the value of OrderTime
    public void setOrderTime(String orderTime) {
        OrderTime = orderTime; // Assigns the passed orderTime parameter to the OrderTime field
    }

    // Getter method to retrieve the value of OrderDate
    public String getOrderDate() {
        return OrderDate; // Returns the current value of OrderDate
    }

    // Setter method to update the value of OrderDate
    public void setOrderDate(String orderDate) {
        OrderDate = orderDate; // Assigns the passed orderDate parameter to the OrderDate field
    }

    // Getter method to retrieve the value of OrderID
    public String getOrderID() {
        return OrderID; // Returns the current value of OrderID
    }

    // Setter method to update the value of OrderID
    public void setOrderID(String orderID) {
        OrderID = orderID; // Assigns the passed orderID parameter to the OrderID field
    }

    // Getter method to retrieve the value of UserID
    public String getUserID() {
        return UserID; // Returns the current value of UserID
    }

    // Setter method to update the value of UserID
    public void setUserID(String userID) {
        UserID = userID; // Assigns the passed userID parameter to the UserID field
    }

    // Getter method to retrieve the value of Phone
    public String getPhone() {
        return Phone; // Returns the current value of Phone
    }

    // Setter method to update the value of Phone
    public void setPhone(String phone) {
        Phone = phone; // Assigns the passed phone parameter to the Phone field
    }

    // Getter method to retrieve the value of Name
    public String getName() {
        return Name; // Returns the current value of Name
    }

    // Setter method to update the value of Name
    public void setName(String name) {
        Name = name; // Assigns the passed name parameter to the Name field
    }

    // Getter method to retrieve the value of Address
    public String getAddress() {
        return Address; // Returns the current value of Address
    }

    // Setter method to update the value of Address
    public void setAddress(String address) {
        Address = address; // Assigns the passed address parameter to the Address field
    }

    // Getter method to retrieve the value of TotalPrice
    public String getTotalPrice() {
        return TotalPrice; // Returns the current value of TotalPrice
    }

    // Setter method to update the value of TotalPrice
    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice; // Assigns the passed totalPrice parameter to the TotalPrice field
    }

    // Getter method to retrieve the value of Pincode
    public String getPincode() {
        return Pincode; // Returns the current value of Pincode
    }

    // Setter method to update the value of Pincode
    public void setPincode(String pincode) {
        Pincode = pincode; // Assigns the passed pincode parameter to the Pincode field
    }

    // Getter method to retrieve the value of PickupTime
    public String getPickupTime() {
        return PickupTime; // Returns the current value of PickupTime
    }

    // Setter method to update the value of PickupTime
    public void setPickupTime(String pickupTime) {
        PickupTime = pickupTime; // Assigns the passed pickupTime parameter to the PickupTime field
    }

    // Getter method to retrieve the value of DeliveryTime
    public String getDeliveryTime() {
        return DeliveryTime; // Returns the current value of DeliveryTime
    }

    // Setter method to update the value of DeliveryTime
    public void setDeliveryTime(String deliveryTime) {
        DeliveryTime = deliveryTime; // Assigns the passed deliveryTime parameter to the DeliveryTime field
    }

    // Getter method to retrieve the value of City
    public String getCity() {
        return City; // Returns the current value of City
    }

    // Setter method to update the value of City
    public void setCity(String city) {
        City = city; // Assigns the passed city parameter to the City field
    }

    // Getter method to retrieve the value of Email
    public String getEmail() {
        return Email; // Returns the current value of Email
    }

    // Setter method to update the value of Email
    public void setEmail(String email) {
        Email = email; // Assigns the passed email parameter to the Email field
    }
} // Closes the Orders class