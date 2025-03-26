package com.example.freshfoldlaundrycare; // Defines the package name for this class

import android.app.ProgressDialog; // Imports ProgressDialog for displaying loading indicators
import android.app.TimePickerDialog; // Imports TimePickerDialog for selecting time
import android.os.Bundle; // Imports Bundle for passing data between activities
import android.view.View; // Imports View for handling UI components
import android.widget.TimePicker; // Imports TimePicker for the time selection widget
import android.widget.Toast; // Imports Toast for showing short messages

import androidx.annotation.NonNull; // Imports NonNull annotation for null safety
import androidx.appcompat.app.AppCompatActivity; // Imports AppCompatActivity as the base class for this activity

import com.example.freshfoldlaundrycare.databinding.ActivityEditProfileBinding; // Imports the binding class for this activity’s layout
import com.google.android.gms.tasks.OnCompleteListener; // Imports OnCompleteListener for Firebase task completion
import com.google.android.gms.tasks.Task; // Imports Task for handling Firebase asynchronous operations
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth for user authentication
import com.google.firebase.firestore.CollectionReference; // Imports CollectionReference for Firestore collection operations
import com.google.firebase.firestore.DocumentSnapshot; // Imports DocumentSnapshot for retrieving Firestore document data
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore for Firestore database operations

import java.util.Calendar; // Imports Calendar for getting current date and time
import java.util.HashMap; // Imports HashMap for storing key-value pairs
import java.util.Locale; // Imports Locale for formatting strings based on the device’s locale

public class EditProfileActivity extends AppCompatActivity { // Declares the EditProfileActivity class, extending AppCompatActivity

    ActivityEditProfileBinding binding; // Declares a binding object for accessing UI elements
    ProgressDialog loadingBar; // Declares a ProgressDialog for showing a loading indicator
    FirebaseAuth mAuth; // Declares a FirebaseAuth object for authentication
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initializes Firestore database instance
    CollectionReference usersRef; // Declares a CollectionReference for the "Users" Firestore collection
    String userID; // Declares a string to store the current user’s ID

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Overrides onCreate method, called when the activity is created
        super.onCreate(savedInstanceState); // Calls the parent class’s onCreate method
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater()); // Inflates the layout using View Binding
        setContentView(binding.getRoot()); // Sets the content view to the root of the binding layout

        mAuth = FirebaseAuth.getInstance(); // Initializes FirebaseAuth instance
        loadingBar = new ProgressDialog(this); // Initializes the ProgressDialog with the current context
        userID = mAuth.getCurrentUser().getUid(); // Gets the current user’s unique ID from FirebaseAuth
        usersRef = db.collection("Users"); // References the "Users" collection in Firestore

        displayUserData(); // Calls the method to display the user’s current data in the UI

        binding.selectPickupTime.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the pickup time selection button
            @Override
            public void onClick(View v) { // Defines the action when the button is clicked
                // Get the current time
                Calendar currentTime = Calendar.getInstance(); // Creates a Calendar instance with the current time
                int hour = currentTime.get(Calendar.HOUR_OF_DAY); // Gets the current hour in 24-hour format
                int minute = currentTime.get(Calendar.MINUTE); // Gets the current minute

                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog( // Initializes a TimePickerDialog
                        EditProfileActivity.this, // Context (this activity)
                        new TimePickerDialog.OnTimeSetListener() { // Listener for when a time is set
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) { // Callback when time is selected
                                // Determine AM or PM
                                String amPm; // Declares a string for AM/PM indicator
                                if (hourOfDay < 12) { // Checks if the hour is before noon
                                    amPm = "AM"; // Sets AM if before noon
                                } else { // If the hour is noon or later
                                    amPm = "PM"; // Sets PM
                                    hourOfDay -= 12; // Converts 24-hour format to 12-hour format
                                }

                                // Update the TextView with the selected time
                                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm); // Formats the time as HH:MM AM/PM
                                binding.editPickupTime.getEditText().setText(formattedTime); // Sets the formatted time in the pickup time EditText
                            }
                        },
                        hour, minute, false); // Initial hour, minute, and 12-hour format (false for 24-hour)

                // Show the TimePickerDialog
                timePickerDialog.show(); // Displays the time picker dialog
            }
        });

        binding.selectDeliveryTime.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the delivery time selection button
            @Override
            public void onClick(View v) { // Defines the action when the button is clicked
                // Get the current time
                Calendar currentTime = Calendar.getInstance(); // Creates a Calendar instance with the current time
                int hour = currentTime.get(Calendar.HOUR_OF_DAY); // Gets the current hour in 24-hour format
                int minute = currentTime.get(Calendar.MINUTE); // Gets the current minute

                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog( // Initializes a TimePickerDialog
                        EditProfileActivity.this, // Context (this activity)
                        new TimePickerDialog.OnTimeSetListener() { // Listener for when a time is set
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) { // Callback when time is selected
                                // Determine AM or PM
                                String amPm; // Declares a string for AM/PM indicator
                                if (hourOfDay < 12) { // Checks if the hour is before noon
                                    amPm = "AM"; // Sets AM if before noon
                                } else { // If the hour is noon or later
                                    amPm = "PM"; // Sets PM
                                    hourOfDay -= 12; // Converts 24-hour format to 12-hour format
                                }

                                // Update the TextView with the selected time
                                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm); // Formats the time as HH:MM AM/PM
                                binding.editDeliveryTime.getEditText().setText(formattedTime); // Sets the formatted time in the delivery time EditText
                            }
                        },
                        hour, minute, false); // Initial hour, minute, and 12-hour format (false for 24-hour)

                // Show the TimePickerDialog
                timePickerDialog.show(); // Displays the time picker dialog
            }
        });

        binding.editProfileBtn.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the edit profile button
            @Override
            public void onClick(View v) { // Defines the action when the button is clicked
                String name = binding.editName.getEditText().getText().toString().trim(); // Gets and trims the name from the EditText
                String phone = binding.editPhone.getEditText().getText().toString().trim(); // Gets and trims the phone number from the EditText
                String address = binding.editAddress.getEditText().getText().toString().trim(); // Gets and trims the address from the EditText
                String city = binding.editCity.getEditText().getText().toString().trim(); // Gets and trims the city from the EditText
                String pincode = binding.editPincode.getEditText().getText().toString().trim(); // Gets and trims the pincode from the EditText
                String pickupTime = binding.editPickupTime.getEditText().getText().toString().trim(); // Gets and trims the pickup time from the EditText
                String deliveryTime = binding.editDeliveryTime.getEditText().getText().toString().trim(); // Gets and trims the delivery time from the EditText

                if (phone.isEmpty() || address.isEmpty() || pickupTime.isEmpty() || deliveryTime.isEmpty()) { // Checks if required fields are empty
                    Toast.makeText(EditProfileActivity.this, "Some data is empty!", Toast.LENGTH_SHORT).show(); // Shows an error message
                } else { // If all required fields are filled
                    loadingBar.setMessage("please wait"); // Sets the message for the loading dialog
                    loadingBar.setCanceledOnTouchOutside(false); // Prevents dismissing the dialog by touching outside
                    loadingBar.show(); // Shows the loading dialog

                    HashMap usersMap = new HashMap(); // Creates a HashMap to store updated user data
                    usersMap.put("Phone", phone); // Adds the phone number to the HashMap
                    usersMap.put("Name", name); // Adds the name to the HashMap
                    usersMap.put("Address", address); // Adds the address to the HashMap
                    usersMap.put("City", city); // Adds the city to the HashMap
                    usersMap.put("Pincode", pincode); // Adds the pincode to the HashMap
                    usersMap.put("PickupTime", pickupTime); // Adds the pickup time to the HashMap
                    usersMap.put("DeliveryTime", deliveryTime); // Adds the delivery time to the HashMap

                    usersRef.document(userID).update(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() { // Updates the user document in Firestore
                        @Override
                        public void onComplete(@NonNull Task<Void> task) { // Callback for when the task completes
                            if (task.isSuccessful()) { // Checks if the update was successful
                                Toast.makeText(getApplicationContext(), "Account Updated successfully!", Toast.LENGTH_SHORT).show(); // Shows a success message
                                finish(); // Closes the activity
                                loadingBar.dismiss(); // Dismisses the loading dialog
                            } else { // If the update fails
                                String msg = task.getException().getMessage(); // Gets the error message
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show(); // Shows the error message
                                loadingBar.dismiss(); // Dismisses the loading dialog
                            }
                        }
                    });
                }
            }
        });
    }

    private void displayUserData() { // Defines a method to display the user’s current data in the UI
        usersRef.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { // Fetches the user’s document from Firestore
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) { // Callback for when the task completes
                if (task.isSuccessful()) { // Checks if the task was successful
                    DocumentSnapshot snapshot = task.getResult(); // Gets the document snapshot
                    String profileUpdated = snapshot.getString("ProfileUpdated"); // Retrieves the "ProfileUpdated" field (not used)
                    String address = snapshot.getString("Address"); // Retrieves the "Address" field
                    String name = snapshot.getString("Name"); // Retrieves the "Name" field
                    String phone = snapshot.getString("Phone"); // Retrieves the "Phone" field
                    String email = snapshot.getString("Email"); // Retrieves the "Email" field (not used)
                    String city = snapshot.getString("City"); // Retrieves the "City" field
                    String delivery = snapshot.getString("DeliveryTime"); // Retrieves the "DeliveryTime" field
                    String pickup = snapshot.getString("PickupTime"); // Retrieves the "PickupTime" field
                    String pincode = snapshot.getString("Pincode"); // Retrieves the "Pincode" field

                    binding.editAddress.getEditText().setText(address); // Sets the address in the EditText
                    binding.editName.getEditText().setText(name); // Sets the name in the EditText
                    binding.editPhone.getEditText().setText(phone); // Sets the phone number in the EditText
                    binding.editCity.getEditText().setText(city); // Sets the city in the EditText
                    binding.editDeliveryTime.getEditText().setText(delivery); // Sets the delivery time in the EditText
                    binding.editPickupTime.getEditText().setText(pickup); // Sets the pickup time in the EditText
                    binding.editPincode.getEditText().setText(pincode); // Sets the pincode in the EditText
                }
            }
        });
    }
}