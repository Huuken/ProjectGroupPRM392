package com.example.freshfoldlaundrycare.fragments; // Defines the package name for this class, organizing it under the "fragments" subpackage.

import android.app.ProgressDialog; // Imports ProgressDialog class to display a loading dialog (unused in this code but imported).
import android.content.Intent; // Imports Intent class to navigate between activities.
import android.os.Bundle; // Imports Bundle class to manage fragment state (e.g., saving/restoring data).
import android.view.LayoutInflater; // Imports LayoutInflater class to inflate the fragment's layout.
import android.view.View; // Imports View class to handle UI components and their interactions.
import android.view.ViewGroup; // Imports ViewGroup class to represent the container for the fragment's view.

import androidx.annotation.NonNull; // Imports NonNull annotation to indicate parameters or return values cannot be null.
import androidx.fragment.app.Fragment; // Imports Fragment class as the base class for this fragment.

import com.example.freshfoldlaundrycare.EditProfileActivity; // Imports EditProfileActivity class for navigation.
import com.example.freshfoldlaundrycare.auth.LoginActivity; // Imports LoginActivity class for navigation after logout.
import com.example.freshfoldlaundrycare.databinding.FragmentAccountBinding; // Imports auto-generated binding class for the account fragment layout.
import com.google.android.gms.tasks.OnCompleteListener; // Imports listener interface for handling task completion in Firebase.
import com.google.android.gms.tasks.Task; // Imports Task class to represent asynchronous operations in Firebase.
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth class to manage authentication operations.
import com.google.firebase.firestore.CollectionReference; // Imports CollectionReference class to reference a Firestore collection.
import com.google.firebase.firestore.DocumentSnapshot; // Imports DocumentSnapshot class to represent a Firestore document's data.
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore class to interact with Firestore database.

public class AccountFragment extends Fragment { // Declares the AccountFragment class, extending Fragment for fragment functionality.

    FragmentAccountBinding binding; // Declares a variable for the binding object to access UI elements in the layout.
    FirebaseAuth mAuth; // Declares a variable for FirebaseAuth to manage authentication.
    ProgressDialog loadingBar; // Declares a variable for ProgressDialog (unused in this code).
    ProgressDialog dialog; // Declares another ProgressDialog variable (unused in this code).
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initializes a FirebaseFirestore instance to interact with Firestore and assigns it to db.
    CollectionReference serviceRef, cartRef, usersRef; // Declares variables for CollectionReference (serviceRef and cartRef are unused).
    String currentUserId; // Declares a variable to store the current user's ID.

    @Override // Indicates that the following method overrides a method from the superclass.
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // Defines the onCreateView method, called to create the fragment's view.
        binding = FragmentAccountBinding.inflate(inflater, container, false); // Inflates the account fragment layout using the binding class.

        mAuth = FirebaseAuth.getInstance(); // Initializes the FirebaseAuth instance to interact with Firebase authentication.
        loadingBar = new ProgressDialog(getContext()); // Initializes the loadingBar ProgressDialog with the fragment's context (unused).
        mAuth = FirebaseAuth.getInstance(); // Re-initializes mAuth (redundant, as it’s already initialized above).
        dialog = new ProgressDialog(getContext()); // Initializes the dialog ProgressDialog with the fragment's context (unused).
        currentUserId = mAuth.getCurrentUser().getUid(); // Gets the unique ID of the currently authenticated user and assigns it to currentUserId.

        usersRef = db.collection("Users"); // Assigns the "Users" collection reference from Firestore to usersRef.

        binding.logoutUser.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the "Logout" button or view in the layout.
            @Override // Indicates that the following method overrides the onClick method from OnClickListener.
            public void onClick(View v) { // Defines the onClick method, executed when the logout button is clicked.
                mAuth.signOut(); // Signs the user out of Firebase authentication.
                Intent intent = new Intent(getContext(), LoginActivity.class); // Creates an Intent to navigate to LoginActivity.
                startActivity(intent); // Starts the LoginActivity.
                getActivity().finish(); // Closes the hosting activity (e.g., MainActivity) to prevent returning to it.
            } // Closes the onClick method.
        }); // Closes the setOnClickListener for logout.

        displayUserData(); // Calls the displayUserData method to fetch and display the user's profile data.

        binding.editUser.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the "Edit User" button or view in the layout.
            @Override // Indicates that the following method overrides the onClick method from OnClickListener.
            public void onClick(View v) { // Defines the onClick method, executed when the edit button is clicked.
                Intent intent = new Intent(getContext(), EditProfileActivity.class); // Creates an Intent to navigate to EditProfileActivity.
                startActivity(intent); // Starts the EditProfileActivity.
            } // Closes the onClick method.
        }); // Closes the setOnClickListener for edit user.

        return binding.getRoot(); // Returns the root view of the inflated layout to be displayed by the fragment.
    } // Closes the onCreateView method.

    private void displayUserData() { // Defines a private method to fetch and display the user's data from Firestore.
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
                    String delivery = snapshot.getString("DeliveryTime"); // Retrieves the "DeliveryTime" field as a string.
                    String pickup = snapshot.getString("PickupTime"); // Retrieves the "PickupTime" field as a string.
                    String pincode = snapshot.getString("Pincode"); // Retrieves the "Pincode" field as a string.

                    binding.profileAddress.setText(address); // Sets the address in the corresponding TextView.
                    binding.profileName.setText(name); // Sets the name in the corresponding TextView.
                    binding.profilePhone.setText(phone); // Sets the phone number in the corresponding TextView.
                    binding.profileEmail.setText(email); // Sets the email in the corresponding TextView.
                    binding.profileCity.setText(city); // Sets the city in the corresponding TextView.
                    binding.profileDelivery.setText(delivery); // Sets the delivery time in the corresponding TextView.
                    binding.profilePickup.setText(pickup); // Sets the pickup time in the corresponding TextView.
                    binding.profilePincode.setText(pincode); // Sets the pincode in the corresponding TextView.
                } // Closes the if block.
            } // Closes the onComplete method.
        }); // Closes the addOnCompleteListener method call.
    } // Closes the displayUserData method.

    @Override // Indicates that the following method overrides a method from the superclass.
    public void onResume() { // Defines the onResume method, called when the fragment becomes visible again.
        super.onResume(); // Calls the superclass’s onResume method for necessary setup.
        displayUserData(); // Calls displayUserData to refresh the user’s profile data when the fragment resumes (e.g., after editing the profile).
    } // Closes the onResume method.
} // Closes the AccountFragment class.