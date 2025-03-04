package com.example.freshfoldlaundrycare.Modal;

public class Feedback {

    String UserName, UserPhone, UserEmail, UserAddress, UserPincode, UserCity, UserUID, UserRating, UserFeedback;

    public Feedback() {
    }

    public Feedback(String userName, String userPhone, String userEmail, String userAddress, String userPincode, String userCity, String userUID, String userRating, String userFeedback) {
        UserName = userName;
        UserPhone = userPhone;
        UserEmail = userEmail;
        UserAddress = userAddress;
        UserPincode = userPincode;
        UserCity = userCity;
        UserUID = userUID;
        UserRating = userRating;
        UserFeedback = userFeedback;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String userAddress) {
        UserAddress = userAddress;
    }

    public String getUserPincode() {
        return UserPincode;
    }

    public void setUserPincode(String userPincode) {
        UserPincode = userPincode;
    }

    public String getUserCity() {
        return UserCity;
    }

    public void setUserCity(String userCity) {
        UserCity = userCity;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public String getUserRating() {
        return UserRating;
    }

    public void setUserRating(String userRating) {
        UserRating = userRating;
    }

    public String getUserFeedback() {
        return UserFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        UserFeedback = userFeedback;
    }
}