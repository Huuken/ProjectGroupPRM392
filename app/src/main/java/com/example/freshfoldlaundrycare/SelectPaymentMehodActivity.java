package com.example.freshfoldlaundrycare; // Defines the package name for the application

import androidx.appcompat.app.AppCompatActivity; // Imports the AppCompatActivity class for compatibility support
import android.content.Intent; // Imports the Intent class for activity navigation
import android.os.Bundle; // Imports the Bundle class to handle instance state
import android.view.View; // Imports the View class for handling user interactions

public class SelectPaymentMehodActivity extends AppCompatActivity { // Defines the SelectPaymentMehodActivity class, which extends AppCompatActivity

    String totalPrice; // Declares a variable to store the total price

    @Override // Overrides the onCreate method from AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) { // Called when the activity is created
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method to maintain activity lifecycle behavior
        setContentView(R.layout.activity_select_payment_mehod); // Sets the layout file for this activity

        totalPrice = getIntent().getStringExtra("totalPrice"); // Retrieves the total price from the previous activity
    }

    public void goToFinalOrderPage(View view) { // Method to navigate to the final order confirmation page
        Intent intent = new Intent(getApplicationContext(), ConfirmOrderActivity.class); // Creates an intent to switch to ConfirmOrderActivity
        intent.putExtra("totalPrice", totalPrice); // Passes the total price to the next activity
        startActivity(intent); // Starts the ConfirmOrderActivity
    }

    public void goToCardPayment(View view) { // Method to navigate to the card payment page
        Intent intent = new Intent(getApplicationContext(), CardPaymentActivity.class); // Creates an intent to switch to CardPaymentActivity
        intent.putExtra("totalPrice", totalPrice); // Passes the total price to the next activity
        startActivity(intent); // Starts the CardPaymentActivity
    }
}
