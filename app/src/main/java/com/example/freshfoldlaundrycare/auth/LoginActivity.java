package com.example.freshfoldlaundrycare.auth; // Defines the package name for this class, organizing it under the "auth" subpackage.

import android.app.ProgressDialog; // Imports ProgressDialog class to display a loading dialog.
import android.content.Intent; // Imports Intent class to navigate between activities.
import android.os.Bundle; // Imports Bundle class to manage activity state (e.g., saving/restoring data).
import android.view.View; // Imports View class to handle UI components and their interactions.
import android.widget.Toast; // Imports Toast class to show short pop-up messages to the user.

import androidx.annotation.NonNull; // Imports NonNull annotation to indicate parameters or return values cannot be null.
import androidx.appcompat.app.AppCompatActivity; // Imports base class for activities with modern Android features.

import com.example.freshfoldlaundrycare.MainActivity; // Imports MainActivity class for navigation after login.
import com.example.freshfoldlaundrycare.admin.AdminLoginActivity; // Imports AdminLoginActivity class for admin login navigation.
import com.example.freshfoldlaundrycare.admin.AdminMainActivity; // Imports AdminMainActivity class for admin dashboard navigation.
import com.example.freshfoldlaundrycare.databinding.ActivityLoginBinding; // Imports auto-generated binding class for the login layout.
import com.google.android.gms.tasks.OnCompleteListener; // Imports listener interface for handling task completion in Firebase.
import com.google.android.gms.tasks.Task; // Imports Task class to represent asynchronous operations in Firebase.
import com.google.firebase.auth.AuthResult; // Imports AuthResult class to handle authentication results from Firebase.
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth class to manage authentication operations.

public class LoginActivity extends AppCompatActivity { // Declares the LoginActivity class, extending AppCompatActivity for activity functionality.

    ActivityLoginBinding binding; // Declares a variable for the binding object to access UI elements in the layout.
    FirebaseAuth mAuth; // Declares a variable for FirebaseAuth to manage authentication.
    ProgressDialog loadingBar; // Declares a variable for ProgressDialog to show a loading indicator.

    @Override // Indicates that the following method overrides a method from the superclass.
    protected void onCreate(Bundle savedInstanceState) { // Defines the onCreate method, called when the activity is created.
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method for necessary setup.
        binding = ActivityLoginBinding.inflate(getLayoutInflater()); // Inflates the login layout using the binding class and assigns it to the binding variable.
        setContentView(binding.getRoot()); // Sets the activity's content view to the root of the inflated layout.

        mAuth = FirebaseAuth.getInstance(); // Initializes the FirebaseAuth instance to interact with Firebase authentication.
        loadingBar = new ProgressDialog(this); // Initializes the ProgressDialog with the current activity context.

        binding.loginBtn.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the "Login" button in the layout.
            @Override // Indicates that the following method overrides the onClick method from OnClickListener.
            public void onClick(View v) { // Defines the onClick method, executed when the button is clicked.
                String email = binding.loginEmail.getEditText().getText().toString().trim(); // Gets the email input from the EditText, removes spaces, and stores it.
                String password = binding.loginPassword.getEditText().getText().toString().trim(); // Gets the password input from the EditText, removes spaces, and stores it.

                if (email.isEmpty()) { // Checks if the email string is empty.
                    Toast.makeText(LoginActivity.this, "Email is empty...", Toast.LENGTH_SHORT).show(); // Shows a short toast message if email is empty.
                } else if (password.isEmpty()) { // Checks if the password string is empty.
                    Toast.makeText(LoginActivity.this, "Password is empty...", Toast.LENGTH_SHORT).show(); // Shows a short toast message if password is empty.
                } else { // Executes if both email and password are provided.
                    loadingBar.setTitle("please wait..."); // Sets the title of the loading dialog.
                    loadingBar.setCanceledOnTouchOutside(false); // Prevents the dialog from being dismissed by touching outside.
                    loadingBar.show(); // Displays the loading dialog.
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> { // Attempts to sign in with email and password, adds a completion listener using a lambda expression.
                        if (task.isSuccessful()) { // Checks if the sign-in task completed successfully.
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class); // Creates an intent to navigate to MainActivity.
                            startActivity(intent); // Starts the MainActivity.
                            loadingBar.dismiss(); // Dismisses the loading dialog.
                            finish(); // Closes the LoginActivity so the user can't return to it with the back button.
                        } else { // Executes if the sign-in task failed.
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show(); // Shows a long toast with the error message.
                            loadingBar.dismiss(); // Dismisses the loading dialog (Note: This is missing in your code but should be added here).
                        }
                    }); // Closes the addOnCompleteListener method call.
                } // Closes the else block.
            } // Closes the onClick method.
        }); // Closes the setOnClickListener method call.
    } // Closes the onCreate method.

    @Override // Indicates that the following method overrides a method from the superclass.
    protected void onStart() { // Defines the onStart method, called when the activity becomes visible.
        super.onStart(); // Calls the superclass's onStart method for necessary setup.
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getUid().equals("pss9rodcRgYoVVbO5GQa6exCzQr1")) { // Checks if a user is logged in and has a specific UID (admin).
            Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class); // Creates an intent to navigate to AdminMainActivity.
            startActivity(intent); // Starts the AdminMainActivity.
            finish(); // Closes the LoginActivity.
        } else if (mAuth.getCurrentUser() != null) { // Checks if a user is logged in (non-admin).
            Intent intent = new Intent(getApplicationContext(), MainActivity.class); // Creates an intent to navigate to MainActivity.
            startActivity(intent); // Starts the MainActivity.
            finish(); // Closes the LoginActivity.
        } // Closes the else-if block.
    } // Closes the onStart method.

    public void goToForgotPassword(View view) { // Defines a method to navigate to ForgotPasswordActivity, likely linked to a UI button via XML.
        Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class); // Creates an intent to navigate to ForgotPasswordActivity.
        startActivity(intent); // Starts the ForgotPasswordActivity.
    } // Closes the goToForgotPassword method.

    public void goToRegisterScreen(View view) { // Defines a method to navigate to RegisterActivity, likely linked to a UI button via XML.
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class); // Creates an intent to navigate to RegisterActivity.
        startActivity(intent); // Starts the RegisterActivity.
    } // Closes the goToRegisterScreen method.

    public void goToAdminLogin(View view) { // Defines a method to navigate to AdminLoginActivity, likely linked to a UI button via XML.
        Intent intent = new Intent(getApplicationContext(), AdminLoginActivity.class); // Creates an intent to navigate to AdminLoginActivity.
        startActivity(intent); // Starts the AdminLoginActivity.
    } // Closes the goToAdminLogin method.
} // Closes the LoginActivity class.