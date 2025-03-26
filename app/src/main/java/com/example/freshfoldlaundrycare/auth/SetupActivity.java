package com.example.freshfoldlaundrycare.auth; // Defines the package name for this class, organizing it under the "auth" subpackage.

import android.app.ProgressDialog; // Imports ProgressDialog class to display a loading dialog.
import android.app.TimePickerDialog; // Imports TimePickerDialog class to allow users to select a time.
import android.content.Intent; // Imports Intent class to navigate between activities (unused in this code but imported).
import android.os.Bundle; // Imports Bundle class to manage activity state (e.g., saving/restoring data).
import android.view.View; // Imports View class to handle UI components and their interactions.
import android.widget.TimePicker; // Imports TimePicker class for handling time selection in the TimePickerDialog.
import android.widget.Toast; // Imports Toast class to show short pop-up messages to the user.

import androidx.annotation.NonNull; // Imports NonNull annotation to indicate parameters or return values cannot be null.
import androidx.appcompat.app.AppCompatActivity; // Imports base class for activities with modern Android features.

import com.example.freshfoldlaundrycare.MainActivity; // Imports MainActivity class (unused in this code but imported).
import com.example.freshfoldlaundrycare.databinding.ActivitySetupBinding; // Imports auto-generated binding class for the setup layout.
import com.google.android.gms.tasks.OnCompleteListener; // Imports listener interface for handling task completion in Firebase.
import com.google.android.gms.tasks.Task; // Imports Task class to represent asynchronous operations in Firebase.
import com.google.firebase.auth.AuthResult; // Imports AuthResult class (unused in this code but imported).
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth class to manage authentication operations.
import com.google.firebase.firestore.CollectionReference; // Imports CollectionReference class to reference a Firestore collection.
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore class to interact with Firestore database.

import java.util.Calendar; // Imports Calendar class to get and manipulate the current time.
import java.util.HashMap; // Imports HashMap class to store key-value pairs for user data.
import java.util.Locale; // Imports Locale class to format the time string according to the device's locale.

public class SetupActivity extends AppCompatActivity { // Declares the SetupActivity class, extending AppCompatActivity for activity functionality.

    ActivitySetupBinding binding; // Declares a variable for the binding object to access UI elements in the layout.
    ProgressDialog loadingBar; // Declares a variable for ProgressDialog to show a loading indicator.
    FirebaseAuth mAuth; // Declares a variable for FirebaseAuth to manage authentication.
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initializes a FirebaseFirestore instance to interact with Firestore and assigns it to db.
    CollectionReference usersRef; // Declares a variable for CollectionReference to reference the "Users" collection in Firestore.
    String userID; // Declares a variable to store the current user's ID.

    @Override // Indicates that the following method overrides a method from the superclass.
    protected void onCreate(Bundle savedInstanceState) { // Defines the onCreate method, called when the activity is created.
        super.onCreate(savedInstanceState); // Calls the superclass's onCreate method for necessary setup.
        binding = ActivitySetupBinding.inflate(getLayoutInflater()); // Inflates the setup layout using the binding class and assigns it to the binding variable.
        setContentView(binding.getRoot()); // Sets the activity's content view to the root of the inflated layout.

        loadingBar = new ProgressDialog(this); // Initializes the ProgressDialog with the current activity context.

        mAuth = FirebaseAuth.getInstance(); // Initializes the FirebaseAuth instance to interact with Firebase authentication.
        userID = mAuth.getCurrentUser().getUid(); // Gets the unique ID of the currently authenticated user and assigns it to userID.
        usersRef = db.collection("Users"); // Assigns the "Users" collection reference from Firestore to usersRef.

        binding.selectPickupTime.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the "Select Pickup Time" button or view.
            @Override // Indicates that the following method overrides the onClick method from OnClickListener.
            public void onClick(View v) { // Defines the onClick method, executed when the button is clicked.
                Calendar currentTime = Calendar.getInstance(); // Creates a Calendar instance with the current time.
                int hour = currentTime.get(Calendar.HOUR_OF_DAY); // Gets the current hour in 24-hour format.
                int minute = currentTime.get(Calendar.MINUTE); // Gets the current minute.

                TimePickerDialog timePickerDialog = new TimePickerDialog( // Creates a new TimePickerDialog for selecting pickup time.
                        SetupActivity.this, // Passes the current activity context.
                        new TimePickerDialog.OnTimeSetListener() { // Defines a listener for when the user sets a time.
                            @Override // Indicates that the following method overrides the onTimeSet method.
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) { // Called when the user confirms a time.
                                String amPm; // Declares a variable to store "AM" or "PM".
                                if (hourOfDay < 12) { // Checks if the selected hour is before noon.
                                    amPm = "AM"; // Sets "AM" for morning hours.
                                } else { // Executes if the hour is noon or later.
                                    amPm = "PM"; // Sets "PM" for afternoon/evening hours.
                                    hourOfDay -= 12; // Converts 24-hour format to 12-hour format (e.g., 13 becomes 1 PM).
                                }
                                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm); // Formats the time as "HH:MM AM/PM" (e.g., "02:30 PM").
                                binding.setupPickupTime.getEditText().setText(formattedTime); // Sets the formatted time in the pickup time EditText.
                            } // Closes the onTimeSet method.
                        }, // Closes the OnTimeSetListener.
                        hour, minute, false); // Initializes the dialog with current hour, minute, and 12-hour format (false for 24-hour).

                timePickerDialog.show(); // Displays the TimePickerDialog to the user.
            } // Closes the onClick method.
        }); // Closes the setOnClickListener for pickup time.

        binding.selectDeliveryTime.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the "Select Delivery Time" button or view.
            @Override // Indicates that the following method overrides the onClick method from OnClickListener.
            public void onClick(View v) { // Defines the onClick method, executed when the button is clicked.
                Calendar currentTime = Calendar.getInstance(); // Creates a Calendar instance with the current time.
                int hour = currentTime.get(Calendar.HOUR_OF_DAY); // Gets the current hour in 24-hour format.
                int minute = currentTime.get(Calendar.MINUTE); // Gets the current minute.

                TimePickerDialog timePickerDialog = new TimePickerDialog( // Creates a new TimePickerDialog for selecting delivery time.
                        SetupActivity.this, // Passes the current activity context.
                        new TimePickerDialog.OnTimeSetListener() { // Defines a listener for when the user sets a time.
                            @Override // Indicates that the following method overrides the onTimeSet method.
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) { // Called when the user confirms a time.
                                String amPm; // Declares a variable to store "AM" or "PM".
                                if (hourOfDay < 12) { // Checks if the selected hour is before noon.
                                    amPm = "AM"; // Sets "AM" for morning hours.
                                } else { // Executes if the hour is noon or later.
                                    amPm = "PM"; // Sets "PM" for afternoon/evening hours.
                                    hourOfDay -= 12; // Converts 24-hour format to 12-hour format (e.g., 13 becomes 1 PM).
                                }
                                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm); // Formats the time as "HH:MM AM/PM" (e.g., "02:30 PM").
                                binding.setupDeliveryTime.getEditText().setText(formattedTime); // Sets the formatted time in the delivery time EditText.
                            } // Closes the onTimeSet method.
                        }, // Closes the OnTimeSetListener.
                        hour, minute, false); // Initializes the dialog with current hour, minute, and 12-hour format (false for 24-hour).

                timePickerDialog.show(); // Displays the TimePickerDialog to the user.
            } // Closes the onClick method.
        }); // Closes the setOnClickListener for delivery time.

        binding.completeProfileBtn.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the "Complete Profile" button.
            @Override // Indicates that the following method overrides the onClick method from OnClickListener.
            public void onClick(View v) { // Defines the onClick method, executed when the button is clicked.
                String phone = binding.setupPhone.getEditText().getText().toString().trim(); // Gets the phone number input from the EditText and removes spaces.
                String address = binding.setupAddress.getEditText().getText().toString().trim(); // Gets the address input from the EditText and removes spaces.
                String city = binding.setupCity.getEditText().getText().toString().trim(); // Gets the city input from the EditText and removes spaces.
                String pincode = binding.setupPincode.getEditText().getText().toString().trim(); // Gets the pincode input from the EditText and removes spaces.
                String pickupTime = binding.setupPickupTime.getEditText().getText().toString().trim(); // Gets the pickup time input from the EditText and removes spaces.
                String deliveryTime = binding.setupDeliveryTime.getEditText().getText().toString().trim(); // Gets the delivery time input from the EditText and removes spaces.

                if (phone.isEmpty() || address.isEmpty() || pickupTime.isEmpty() || deliveryTime.isEmpty()) { // Checks if any required fields (phone, address, pickup time, delivery time) are empty.
                    Toast.makeText(SetupActivity.this, "Some data is empty!", Toast.LENGTH_SHORT).show(); // Shows a short toast message if any required field is empty.
                } else { // Executes if all required fields are filled.
                    loadingBar.setMessage("please wait"); // Sets the message of the loading dialog.
                    loadingBar.setCanceledOnTouchOutside(false); // Prevents the dialog from being dismissed by touching outside.
                    loadingBar.show(); // Displays the loading dialog.

                    HashMap usersMap = new HashMap(); // Creates a new HashMap to store updated user data as key-value pairs.
                    usersMap.put("Phone", phone); // Adds the phone number to the HashMap with key "Phone".
                    usersMap.put("Address", address); // Adds the address to the HashMap with key "Address".
                    usersMap.put("City", city); // Adds the city to the HashMap with key "City".
                    usersMap.put("Pincode", pincode); // Adds the pincode to the HashMap with key "Pincode".
                    usersMap.put("PickupTime", pickupTime); // Adds the pickup time to the HashMap with key "PickupTime".
                    usersMap.put("DeliveryTime", deliveryTime); // Adds the delivery time to the HashMap with key "DeliveryTime".
                    usersMap.put("ProfileUpdated", "YES"); // Adds a flag indicating the profile has been updated, with key "ProfileUpdated".
                    usersRef.document(userID).update(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() { // Updates the user's document in Firestore with the new data, adds a completion listener.
                        @Override // Indicates that the following method overrides the onComplete method from OnCompleteListener.
                        public void onComplete(@NonNull Task<Void> task) { // Defines the onComplete method, called when the update task finishes.
                            if (task.isSuccessful()) { // Checks if the update task completed successfully.
                                Toast.makeText(getApplicationContext(), "Account Updated successfully!", Toast.LENGTH_SHORT).show(); // Shows a short toast confirming the update.
                                finish(); // Closes the SetupActivity.
                                loadingBar.dismiss(); // Dismisses the loading dialog.
                            } else { // Executes if the update task failed.
                                String msg = task.getException().getMessage(); // Gets the error message from the failed task.
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show(); // Shows a short toast with the error message.
                                loadingBar.dismiss(); // Dismisses the loading dialog.
                            }
                        } // Closes the onComplete method.
                    }); // Closes the addOnCompleteListener method call.
                } // Closes the else block.
            } // Closes the onClick method.
        }); // Closes the setOnClickListener for complete profile button.
    } // Closes the onCreate method.
} // Closes the SetupActivity class.