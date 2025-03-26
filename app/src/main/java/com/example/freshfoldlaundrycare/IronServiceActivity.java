package com.example.freshfoldlaundrycare; // Defines the package name for the application

import androidx.appcompat.app.AppCompatActivity; // Imports the AppCompatActivity class for compatibility support
import android.os.Bundle; // Imports the Bundle class to handle instance state

public class IronServiceActivity extends AppCompatActivity { // Defines the IronServiceActivity class, which extends AppCompatActivity

    @Override // Overrides the onCreate method from AppCompatActivity
    protected void onCreate(Bundle savedInstanceState) { // Called when the activity is created
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method to maintain activity lifecycle behavior
        setContentView(R.layout.activity_iron_service); // Sets the layout file for this activity
    }
}
