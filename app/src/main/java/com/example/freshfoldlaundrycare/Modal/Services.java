package com.example.freshfoldlaundrycare.Modal;

// Define a class named Services
public class Services {

    // Declare four string variables to store service details
    String ServiceCloth; // Type of cloth for the service
    String ServicePrice; // Price of the service
    String ServiceType;  // Type of service (e.g., washing, ironing)
    String ServiceID;    // Unique identifier for the service

    // Default constructor (no-argument constructor)
    public Services() {
    }

    // Parameterized constructor to initialize the service details
    public Services(String serviceCloth, String servicePrice, String serviceType, String serviceID) {
        ServiceCloth = serviceCloth; // Assign input value to ServiceCloth
        ServicePrice = servicePrice; // Assign input value to ServicePrice
        ServiceType = serviceType;   // Assign input value to ServiceType
        ServiceID = serviceID;       // Assign input value to ServiceID
    }

    // Getter method to retrieve the value of ServiceCloth
    public String getServiceCloth() {
        return ServiceCloth;
    }

    // Setter method to set the value of ServiceCloth
    public void setServiceCloth(String serviceCloth) {
        ServiceCloth = serviceCloth;
    }

    // Getter method to retrieve the value of ServicePrice
    public String getServicePrice() {
        return ServicePrice;
    }

    // Setter method to set the value of ServicePrice
    public void setServicePrice(String servicePrice) {
        ServicePrice = servicePrice;
    }

    // Getter method to retrieve the value of ServiceType
    public String getServiceType() {
        return ServiceType;
    }

    // Setter method to set the value of ServiceType
    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    // Getter method to retrieve the value of ServiceID
    public String getServiceID() {
        return ServiceID;
    }

    // Setter method to set the value of ServiceID
    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }
}
