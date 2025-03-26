package com.example.freshfoldlaundrycare.auth; // Defines the package name for this class, organizing it under the "auth" subpackage.

import android.app.ProgressDialog; // Imports ProgressDialog class to display a loading dialog.
import android.content.Intent; // Imports Intent class to navigate between activities.
import android.os.Bundle; // Imports Bundle class to manage activity state (e.g., saving/restoring data).
import android.view.View; // Imports View class to handle UI components and their interactions.
import android.widget.Toast; // Imports Toast class to show short pop-up messages to the user.

import androidx.annotation.NonNull; // Imports NonNull annotation to indicate parameters or return values cannot be null.
import androidx.appcompat.app.AppCompatActivity; // Imports base class for activities with modern Android features.

import com.example.freshfoldlaundrycare.MainActivity; // Imports MainActivity class for navigation after registration.
import com.example.freshfoldlaundrycare.databinding.ActivityRegisterBinding; // Imports auto-generated binding class for the register layout.
import com.google.android.gms.tasks.OnCompleteListener; // Imports listener interface for handling task completion in Firebase.
import com.google.android.gms.tasks.Task; // Imports Task class to represent asynchronous operations in Firebase.
import com.google.firebase.auth.AuthResult; // Imports AuthResult class to handle authentication results from Firebase.
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth class to manage authentication operations.
import com.google.firebase.firestore.CollectionReference; // Imports CollectionReference class to reference a Firestore collection.
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore class to interact with Firestore database.

import java.util.HashMap; // Imports HashMap class to store key-value pairs for user data.

public class RegisterActivity extends AppCompatActivity { // Declares the RegisterActivity class, extending AppCompatActivity for activity functionality.

    ActivityRegisterBinding binding; // Declares a variable for the binding object to access UI elements in the layout.
    ProgressDialog loadingBar; // Declares a variable for ProgressDialog to show a loading indicator.
    FirebaseAuth mAuth; // Declares a variable for FirebaseAuth to manage authentication.
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initializes a FirebaseFirestore instance to interact with Firestore and assigns it to db.
    CollectionReference usersRef; // Declares a variable for CollectionReference to reference the "Users" collection in Firestore.

    @Override // Indicates that the following method overrides a method from the superclass.
    protected void onCreate(Bundle savedInstanceState) { // Defines the onCreate method, called when the activity is created.
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method for necessary setup.
        binding = ActivityRegisterBinding.inflate(getLayoutInflater()); // Inflates the register layout using the binding class and assigns it to the binding variable.
        setContentView(binding.getRoot()); // Sets the activity's content view to the root of the inflated layout.

        loadingBar = new ProgressDialog(this); // Initializes the ProgressDialog with the current activity context.

        mAuth = FirebaseAuth.getInstance(); // Initializes the FirebaseAuth instance to interact with Firebase authentication.
        usersRef = db.collection("Users"); // Assigns the "Users" collection reference from Firestore to usersRef.

        binding.registerBtn.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the "Register" button in the layout.
            @Override // Indicates that the following method overrides the onClick method from OnClickListener.
            public void onClick(View v) { // Defines the onClick method, executed when the button is clicked.
                String name = binding.registerName.getEditText().getText().toString().trim(); // Gets the name input from the EditText, removes spaces, and stores it.
                String email = binding.registerEmail.getEditText().getText().toString().trim(); // Gets the email input from the EditText, removes spaces, and stores it.
                String pwd = binding.registerPassword.getEditText().getText().toString().trim(); // Gets the password input from the EditText, removes spaces, and stores it.
                String cnfPwd = binding.registerConfirmPassword.getEditText().getText().toString().trim(); // Gets the confirm password input from the EditText, removes spaces, and stores it.

                if (email.isEmpty() && pwd.isEmpty() && cnfPwd.isEmpty() && name.isEmpty()) { // Checks if all input fields (email, password, confirm password, name) are empty.
                    Toast.makeText(RegisterActivity.this, "Some data is empty!", Toast.LENGTH_SHORT).show(); // Shows a short toast message if all fields are empty.
                } else { // Executes if at least one field is filled (though ideally, all should be required).
                    loadingBar.setMessage("please wait"); // Sets the message of the loading dialog.
                    loadingBar.setCanceledOnTouchOutside(false); // Prevents the dialog from being dismissed by touching outside.
                    loadingBar.show(); // Displays the loading dialog.

                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() { // Creates a new user with email and password, adds a completion listener.
                        @Override // Indicates that the following method overrides the onComplete method from OnCompleteListener.
                        public void onComplete(@NonNull Task<AuthResult> task) { // Defines the onComplete method, called when the user creation task finishes.
                            String userID = mAuth.getCurrentUser().getUid(); // Gets the unique user ID of the newly created user from FirebaseAuth.

                            HashMap usersMap = new HashMap(); // Creates a new HashMap to store user data as key-value pairs.
                            usersMap.put("Email", email); // Adds the email to the HashMap with key "Email".
                            usersMap.put("Password", pwd); // Adds the password to the HashMap with key "Password" (Note: Storing passwords in plaintext is not recommended).
                            usersMap.put("Name", name); // Adds the name to the HashMap with key "Name".
                            usersMap.put("ProfileUpdated", "NO"); // Adds a flag indicating the profile hasn’t been updated, with key "ProfileUpdated".
                            usersMap.put("Image", "default"); // Adds a default image value to the HashMap with key "Image".
                            usersMap.put("UserID", userID); // Adds the user ID to the HashMap with key "UserID".
                            usersRef.document(userID).set(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() { // Sets the user data in Firestore under the user’s UID, adds a completion listener.
                                @Override // Indicates that the following method overrides the onComplete method from OnCompleteListener.
                                public void onComplete(@NonNull Task<Void> task) { // Defines the onComplete method, called when the Firestore save task finishes.
                                    if (task.isSuccessful()) { // Checks if the Firestore save task completed successfully.
                                        Toast.makeText(RegisterActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show(); // Shows a short toast confirming account creation.
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class); // Creates an intent to navigate to MainActivity.
                                        startActivity(intent); // Starts the MainActivity.
                                        finish(); // Closes the RegisterActivity.
                                        loadingBar.dismiss(); // Dismisses the loading dialog.
                                    } else { // Executes if the Firestore save task failed.
                                        String msg = task.getException().getMessage(); // Gets the error message from the failed task.
                                        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show(); // Shows a short toast with the error message.
                                        loadingBar.dismiss(); // Dismisses the loading dialog.
                                    }
                                } // Closes the inner onComplete method.
                            }); // Closes the addOnCompleteListener for Firestore save.
                        } // Closes the outer onComplete method.
                    }); // Closes the addOnCompleteListener for user creation.
                } // Closes the else block.
            } // Closes the onClick method.
        }); // Closes the setOnClickListener method call.
    } // Closes the onCreate method.

    public void goBack(View view) { // Defines a method to go back, likely linked to a UI button via XML.
        finish(); // Closes the RegisterActivity, returning to the previous screen.
    } // Closes the goBack method.
} // Closes the RegisterActivity class.