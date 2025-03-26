package com.example.freshfoldlaundrycare.auth; // Defines the package name for this class, organizing it under the "auth" subpackage.

import androidx.appcompat.app.AppCompatActivity; // Imports the base class for activities with modern Android features.

import android.content.Intent; // Imports Intent class to navigate between activities.
import android.os.Bundle; // Imports Bundle class to manage activity state (e.g., saving/restoring data).
import android.os.Handler; // Imports Handler class to schedule tasks, such as delayed execution.

import com.example.freshfoldlaundrycare.R; // Imports the R class, which contains references to resources (e.g., layouts) in the app.

public class SplashActivity extends AppCompatActivity { // Declares the SplashActivity class, extending AppCompatActivity for activity functionality.

    @Override // Indicates that the following method overrides a method from the superclass.
    protected void onCreate(Bundle savedInstanceState) { // Defines the onCreate method, called when the activity is created.
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method for necessary setup.
        setContentView(R.layout.activity_splash); // Sets the activity's content view to the splash screen layout defined in activity_splash.xml.

        new Handler().postDelayed(new Runnable() { // Creates a new Handler to schedule a task to run after a delay.
            @Override // Indicates that the following method overrides the run method from Runnable.
            public void run() { // Defines the run method, which contains the code to execute after the delay.
                //IF LOGGED IN CHECK SHOULD BE DONE HERE // A comment indicating that logic to check if the user is logged in should be added here (e.g., using FirebaseAuth).

                Intent login = new Intent(getApplicationContext(), LoginActivity.class); // Creates an Intent to navigate to the LoginActivity.
                startActivity(login); // Starts the LoginActivity using the created Intent.
                finish(); // Closes the SplashActivity so the user cannot return to it with the back button.
            } // Closes the run method.
        }, 3000); // Specifies the delay in milliseconds (3000 ms = 3 seconds) before the Runnable executes.
    } // Closes the onCreate method.
} // Closes the SplashActivity class.