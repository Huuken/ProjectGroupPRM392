package com.example.freshfoldlaundrycare.Modal; // Defines the package name for this class, organizing it under the "Modal" subpackage (likely intended as "Model").

public class Feedback { // Declares the Feedback class, representing a user's feedback data.

    // Declares instance variables for the Feedback class, all of type String.
    String UserName, UserPhone, UserEmail, UserAddress, UserPincode, UserCity, UserUID, UserRating, UserFeedback;
    // Fields represent user details (name, phone, email, address, pincode, city, UID) and feedback details (rating, feedback text).

    public Feedback() { // Defines a no-argument (default) constructor.
        // Required empty constructor, typically used by Firebase/Firestore for deserialization.
        // No initialization is performed here; fields will default to null.
    } // Closes the default constructor.

    // Defines a parameterized constructor to initialize all fields of the Feedback object.
    public Feedback(String userName, String userPhone, String userEmail, String userAddress, String userPincode, String userCity, String userUID, String userRating, String userFeedback) {
        UserName = userName; // Assigns the provided userName to the UserName field.
        UserPhone = userPhone; // Assigns the provided userPhone to the UserPhone field.
        UserEmail = userEmail; // Assigns the provided userEmail to the UserEmail field.
        UserAddress = userAddress; // Assigns the provided userAddress to the UserAddress field.
        UserPincode = userPincode; // Assigns the provided userPincode to the UserPincode field.
        UserCity = userCity; // Assigns the provided userCity to the UserCity field.
        UserUID = userUID; // Assigns the provided userUID to the UserUID field.
        UserRating = userRating; // Assigns the provided userRating to the UserRating field.
        UserFeedback = userFeedback; // Assigns the provided userFeedback to the UserFeedback field.
    } // Closes the parameterized constructor.

    // Getter method for UserName.
    public String getUserName() {
        return UserName; // Returns the value of the UserName field.
    } // Closes the getUserName method.

    // Setter method for UserName.
    public void setUserName(String userName) {
        UserName = userName; // Sets the UserName field to the provided userName value.
    } // Closes the setUserName method.

    // Getter method for UserPhone.
    public String getUserPhone() {
        return UserPhone; // Returns the value of the UserPhone field.
    } // Closes the getUserPhone method.

    // Setter method for UserPhone.
    public void setUserPhone(String userPhone) {
        UserPhone = userPhone; // Sets the UserPhone field to the provided userPhone value.
    } // Closes the setUserPhone method.

    // Getter method for UserEmail.
    public String getUserEmail() {
        return UserEmail; // Returns the value of the UserEmail field.
    } // Closes the getUserEmail method.

    // Setter method for UserEmail.
    public void setUserEmail(String userEmail) {
        UserEmail = userEmail; // Sets the UserEmail field to the provided userEmail value.
    } // Closes the setUserEmail method.

    // Getter method for UserAddress.
    public String getUserAddress() {
        return UserAddress; // Returns the value of the UserAddress field.
    } // Closes the getUserAddress method.

    // Setter method for UserAddress.
    public void setUserAddress(String userAddress) {
        UserAddress = userAddress; // Sets the UserAddress field to the provided userAddress value.
    } // Closes the setUserAddress method.

    // Getter method for UserPincode.
    public String getUserPincode() {
        return UserPincode; // Returns the value of the UserPincode field.
    } // Closes the getUserPincode method.

    // Setter method for UserPincode.
    public void setUserPincode(String userPincode) {
        UserPincode = userPincode; // Sets the UserPincode field to the provided userPincode value.
    } // Closes the setUserPincode method.

    // Getter method for UserCity.
    public String getUserCity() {
        return UserCity; // Returns the value of the UserCity field.
    } // Closes the getUserCity method.

    // Setter method for UserCity.
    public void setUserCity(String userCity) {
        UserCity = userCity; // Sets the UserCity field to the provided userCity value.
    } // Closes the setUserCity method.

    // Getter method for UserUID.
    public String getUserUID() {
        return UserUID; // Returns the value of the UserUID field.
    } // Closes the getUserUID method.

    // Setter method for UserUID.
    public void setUserUID(String userUID) {
        UserUID = userUID; // Sets the UserUID field to the provided userUID value.
    } // Closes the setUserUID method.

    // Getter method for UserRating.
    public String getUserRating() {
        return UserRating; // Returns the value of the UserRating field.
    } // Closes the getUserRating method.

    // Setter method for UserRating.
    public void setUserRating(String userRating) {
        UserRating = userRating; // Sets the UserRating field to the provided userRating value.
    } // Closes the setUserRating method.

    // Getter method for UserFeedback.
    public String getUserFeedback() {
        return UserFeedback; // Returns the value of the UserFeedback field.
    } // Closes the getUserFeedback method.

    // Setter method for UserFeedback.
    public void setUserFeedback(String userFeedback) {
        UserFeedback = userFeedback; // Sets the UserFeedback field to the provided userFeedback value.
    } // Closes the setUserFeedback method.
} // Closes the Feedback class.