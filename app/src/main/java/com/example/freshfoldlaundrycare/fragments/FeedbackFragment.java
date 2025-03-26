package com.example.freshfoldlaundrycare.fragments; // Defines the package name for this class, organizing it under the "fragments" subpackage.

import android.app.ProgressDialog; // Imports ProgressDialog class to display a loading dialog.
import android.os.Bundle; // Imports Bundle class to manage fragment state (e.g., saving/restoring data).
import android.view.LayoutInflater; // Imports LayoutInflater class to inflate the fragment's layout.
import android.view.View; // Imports View class to handle UI components and their interactions.
import android.view.ViewGroup; // Imports ViewGroup class to represent the container for the fragment's view.
import android.widget.Toast; // Imports Toast class to show short pop-up messages to the user.

import androidx.annotation.NonNull; // Imports NonNull annotation to indicate parameters or return values cannot be null.
import androidx.fragment.app.Fragment; // Imports Fragment class as the base class for this fragment.

import com.example.freshfoldlaundrycare.databinding.FragmentFeedbackBinding; // Imports auto-generated binding class for the feedback fragment layout.
import com.google.android.gms.tasks.OnCompleteListener; // Imports listener interface for handling task completion in Firebase.
import com.google.android.gms.tasks.Task; // Imports Task class to represent asynchronous operations in Firebase.
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth class to manage authentication operations.
import com.google.firebase.firestore.CollectionReference; // Imports CollectionReference class to reference a Firestore collection.
import com.google.firebase.firestore.DocumentSnapshot; // Imports DocumentSnapshot class to represent a Firestore document's data.
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore class to interact with Firestore database.

import java.util.HashMap; // Imports HashMap class to store key-value pairs for feedback data.

public class FeedbackFragment extends Fragment { // Declares the FeedbackFragment class, extending Fragment for fragment functionality.

    FragmentFeedbackBinding binding; // Declares a variable for the binding object to access UI elements in the layout.
    FirebaseAuth mAuth; // Declares a variable for FirebaseAuth to manage authentication.
    ProgressDialog loadingBar; // Declares a variable for ProgressDialog to show a loading indicator.
    ProgressDialog dialog; // Declares another ProgressDialog variable (unused in this code).
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initializes a FirebaseFirestore instance to interact with Firestore and assigns it to db.
    CollectionReference serviceRef, feedbackRef, usersRef; // Declares variables for CollectionReference (serviceRef is unused).
    String currentUserId; // Declares a variable to store the current user's ID.

    @Override // Indicates that the following method overrides a method from the superclass.
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // Defines the onCreateView method, called to create the fragment's view.
        binding = FragmentFeedbackBinding.inflate(inflater, container, false); // Inflates the feedback fragment layout using the binding class.

        mAuth = FirebaseAuth.getInstance(); // Initializes the FirebaseAuth instance to interact with Firebase authentication.
        loadingBar = new ProgressDialog(getContext()); // Initializes the loadingBar ProgressDialog with the fragment's context.
        mAuth = FirebaseAuth.getInstance(); // Re-initializes mAuth (redundant, as it’s already initialized above).
        dialog = new ProgressDialog(getContext()); // Initializes the dialog ProgressDialog with the fragment's context (unused).
        currentUserId = mAuth.getCurrentUser().getUid(); // Gets the unique ID of the currently authenticated user and assigns it to currentUserId.

        usersRef = db.collection("Users"); // Assigns the "Users" collection reference from Firestore to usersRef.
        feedbackRef = db.collection("Feedback"); // Assigns the "Feedback" collection reference from Firestore to feedbackRef.

        binding.submitButton.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the "Submit" button in the layout.
            @Override // Indicates that the following method overrides the onClick method from OnClickListener.
            public void onClick(View v) { // Defines the onClick method, executed when the submit button is clicked.
                usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { // Retrieves the user’s document from Firestore and adds a completion listener.
                    @Override // Indicates that the following method overrides the onComplete method from OnCompleteListener.
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) { // Defines the onComplete method, called when the fetch task finishes.
                        if (task.isSuccessful()) { // Checks if the fetch task completed successfully.
                            DocumentSnapshot snapshot = task.getResult(); // Gets the DocumentSnapshot containing the user’s data.
                            String profileUpdated = snapshot.getString("ProfileUpdated"); // Retrieves the "ProfileUpdated" field as a string (unused in this method).
                            String address = snapshot.getString("Address"); // Retrieves the "Address" field as a string.
                            String name = snapshot.getString("Name"); // Retrieves the "Name" field as a string.
                            String phone = snapshot.getString("Phone"); // Retrieves the "Phone" field as a string.
                            String email = snapshot.getString("Email"); // Retrieves the "Email" field as a string.
                            String city = snapshot.getString("City"); // Retrieves the "City" field as a string.
                            String delivery = snapshot.getString("DeliveryTime"); // Retrieves the "DeliveryTime" field as a string (unused in this method).
                            String pickup = snapshot.getString("PickupTime"); // Retrieves the "PickupTime" field as a string (unused in this method).
                            String pincode = snapshot.getString("Pincode"); // Retrieves the "Pincode" field as a string.

                            String rating = String.valueOf(binding.ratingBar.getRating()); // Gets the rating from the RatingBar and converts it to a string.
                            String feedback = binding.feedbackField.getEditText().getText().toString().trim(); // Gets the feedback text from the EditText and removes leading/trailing spaces.

                            if (feedback.isEmpty()) { // Checks if the feedback field is empty.
                                Toast.makeText(getContext(), "Please add message...", Toast.LENGTH_SHORT).show(); // Shows a short toast message if feedback is empty.
                            } else { // Executes if feedback is provided.
                                loadingBar.setTitle("Please wait..."); // Sets the title of the loading dialog.
                                loadingBar.setCanceledOnTouchOutside(false); // Prevents the dialog from being dismissed by touching outside.
                                loadingBar.show(); // Displays the loading dialog.
                                HashMap feedbackMap = new HashMap(); // Creates a new HashMap to store feedback data as key-value pairs.
                                feedbackMap.put("UserName", name); // Adds the user’s name to the HashMap with key "UserName".
                                feedbackMap.put("UserPhone", phone); // Adds the user’s phone number to the HashMap with key "UserPhone".
                                feedbackMap.put("UserEmail", email); // Adds the user’s email to the HashMap with key "UserEmail".
                                feedbackMap.put("UserAddress", address); // Adds the user’s address to the HashMap with key "UserAddress".
                                feedbackMap.put("UserPincode", pincode); // Adds the user’s pincode to the HashMap with key "UserPincode".
                                feedbackMap.put("UserCity", city); // Adds the user’s city to the HashMap with key "UserCity".
                                feedbackMap.put("UserUID", currentUserId); // Adds the user’s ID to the HashMap with key "UserUID".
                                feedbackMap.put("UserRating", rating); // Adds the rating to the HashMap with key "UserRating".
                                feedbackMap.put("UserFeedback", feedback); // Adds the feedback text to the HashMap with key "UserFeedback".
                                feedbackRef.document(currentUserId).set(feedbackMap).addOnCompleteListener(new OnCompleteListener<Void>() { // Saves the feedback data in Firestore under the user’s UID and adds a completion listener.
                                    @Override // Indicates that the following method overrides the onComplete method from OnCompleteListener.
                                    public void onComplete(@NonNull Task<Void> task) { // Defines the onComplete method, called when the save task finishes.
                                        if (task.isSuccessful()) { // Checks if the save task completed successfully.
                                            Toast.makeText(getContext(), "Added successfully!", Toast.LENGTH_SHORT).show(); // Shows a short toast confirming the feedback was added.
                                            loadingBar.dismiss(); // Dismisses the loading dialog.
                                        } else { // Executes if the save task failed.
                                            String msg = task.getException().getMessage(); // Gets the error message from the failed task.
                                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show(); // Shows a short toast with the error message.
                                            loadingBar.dismiss(); // Dismisses the loading dialog.
                                        } // Closes the if-else block.
                                    } // Closes the onComplete method.
                                }); // Closes the addOnCompleteListener for the save operation.
                            } // Closes the else block for feedback validation.
                        } // Closes the if block for the user data fetch.
                    } // Closes the onComplete method for the user data fetch.
                }); // Closes the addOnCompleteListener for the user data fetch.
            } // Closes the onClick method.
        }); // Closes the setOnClickListener for the submit button.

        return binding.getRoot(); // Returns the root view of the inflated layout to be displayed by the fragment.
    } // Closes the onCreateView method.
} // Closes the FeedbackFragment class.