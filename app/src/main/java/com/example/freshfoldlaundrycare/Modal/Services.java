package com.example.freshfoldlaundrycare.Modal;

public class Services {

    String ServiceCloth, ServicePrice, ServiceType, ServiceID;

    public Services() {
    }

    public Services(String serviceCloth, String servicePrice, String serviceType, String serviceID) {
        ServiceCloth = serviceCloth;
        ServicePrice = servicePrice;
        ServiceType = serviceType;
        ServiceID = serviceID;
    }

    public String getServiceCloth() {
        return ServiceCloth;
    }

    public void setServiceCloth(String serviceCloth) {
        ServiceCloth = serviceCloth;
    }

    public String getServicePrice() {
        return ServicePrice;
    }

    public void setServicePrice(String servicePrice) {
        ServicePrice = servicePrice;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getServiceID() {
        return ServiceID;
    }

    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }
}