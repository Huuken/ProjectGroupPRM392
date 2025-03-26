package com.example.freshfoldlaundrycare.auth; // Defines the package name for this class, organizing it under the "auth" subpackage.

import android.app.ProgressDialog; // Imports the ProgressDialog class to show a loading dialog.
import android.os.Bundle; // Imports the Bundle class to handle activity state (e.g., saving/restoring data).
import android.view.View; // Imports the View class to handle UI components and their interactions.
import android.widget.Toast; // Imports the Toast class to display short pop-up messages to the user.

import androidx.annotation.NonNull; // Imports the NonNull annotation to indicate parameters or return values cannot be null.
import androidx.appcompat.app.AppCompatActivity; // Imports the base class for activities with modern Android features.

import com.example.freshfoldlaundrycare.databinding.ActivityForgotPasswordBinding; // Imports the auto-generated binding class for the forgot password layout.
import com.google.android.gms.tasks.OnCompleteListener; // Imports the listener interface for handling task completion in Firebase.
import com.google.android.gms.tasks.Task; // Imports the Task class to represent asynchronous operations in Firebase.
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth class to handle authentication-related operations.

public class ForgotPasswordActivity extends AppCompatActivity { // Declares the class ForgotPasswordActivity, extending AppCompatActivity for activity functionality.

    ActivityForgotPasswordBinding binding; // Declares a variable for the binding object to access UI elements in the layout.
    FirebaseAuth mAuth; // Declares a variable for FirebaseAuth to manage authentication.
    ProgressDialog loadingBar; // Declares a variable for ProgressDialog to show a loading indicator.

    @Override // Indicates that the following method overrides a method from the superclass (AppCompatActivity).
    protected void onCreate(Bundle savedInstanceState) { // Defines the onCreate method, called when the activity is created.
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method to perform necessary setup.
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater()); // Inflates the layout using the binding class and assigns it to the binding variable.
        setContentView(binding.getRoot()); // Sets the activity's content view to the root of the inflated layout.

        mAuth = FirebaseAuth.getInstance(); // Initializes the FirebaseAuth instance to interact with Firebase authentication.
        loadingBar = new ProgressDialog(this); // Initializes the ProgressDialog with the current activity context.

        binding.requestLinkButton.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the "Request Link" button in the layout.
            @Override // Indicates that the following method overrides the onClick method from the OnClickListener interface.
            public void onClick(View v) { // Defines the onClick method, executed when the button is clicked.
                String email = binding.emailForgot.getEditText().getText().toString().trim(); // Gets the email input from the EditText, removes leading/trailing spaces, and stores it as a string.
                if (email.isEmpty()) { // Checks if the email string is empty.
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter registered email", Toast.LENGTH_SHORT).show(); // Shows a short toast message asking the user to enter an email.
                } else { // Executes if the email is not empty.
                    loadingBar.setTitle("please wait..."); // Sets the title of the loading dialog.
                    loadingBar.setCanceledOnTouchOutside(false); // Prevents the dialog from being dismissed by touching outside.
                    loadingBar.show(); // Displays the loading dialog.
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() { // Sends a password reset email using FirebaseAuth and adds a completion listener.
                        @Override // Indicates that the following method overrides the onComplete method from OnCompleteListener.
                        public void onComplete(@NonNull Task<Void> task) { // Defines the onComplete method, called when the email-sending task finishes.
                            if (task.isSuccessful()) { // Checks if the task completed successfully.
                                Toast.makeText(ForgotPasswordActivity.this, "Password reset link sent to your Email", Toast.LENGTH_LONG).show(); // Shows a long toast message confirming the reset link was sent.
                            } else { // Executes if the task failed.
                                Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show(); // Shows a short toast message with the error message from the failed task.
                            }
                            loadingBar.dismiss(); // Dismisses the loading dialog after the task completes (success or failure). Note: This line is missing in your code but should be added here.
                        }
                    }); // Closes the addOnCompleteListener method call.
                } // Closes the else block.
            } // Closes the onClick method.
        }); // Closes the setOnClickListener method call.
    } // Closes the onCreate method.
} // Closes the ForgotPasswordActivity class.