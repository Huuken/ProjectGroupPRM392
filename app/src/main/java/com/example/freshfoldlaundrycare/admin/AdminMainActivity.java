package com.example.freshfoldlaundrycare.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.freshfoldlaundrycare.auth.LoginActivity;
import com.example.freshfoldlaundrycare.databinding.ActivityAdminMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminMainActivity extends AppCompatActivity {

    // View Binding for the activity
    ActivityAdminMainBinding binding;
    // Firebase Authentication instance
    FirebaseAuth mAuth;
    // Firebase Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // References to specific Firestore collections and documents
    DocumentReference serviceRef;
    CollectionReference cartRef, usersRef, ordersRef;
    // ProgressDialog to show loading state
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize View Binding
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Authentication and Firestore
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        usersRef = db.collection("Users");
        ordersRef = db.collection("Orders");

        // Logout button click listener
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the current user
                mAuth.signOut();
                // Navigate to the LoginActivity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });

        // Add Service button click listener
        binding.addServiceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddServiceActivity
                Intent intent = new Intent(getApplicationContext(), AddServiceActivity.class);
                startActivity(intent);
            }
        });

        // View Feedback button click listener (Text)
        binding.viewFeedbackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewFeedbackActivity
                Intent intent = new Intent(getApplicationContext(), ViewFeedbackActivity.class);
                startActivity(intent);
            }
        });

        // View Feedback button click listener (Image)
        binding.feedbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewFeedbackActivity
                Intent intent = new Intent(getApplicationContext(), ViewFeedbackActivity.class);
                startActivity(intent);
            }
        });

        // View Services button click listener (Image)
        binding.viewServiceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewServicesActivity
                Intent intent = new Intent(getApplicationContext(), ViewServicesActivity.class);
                startActivity(intent);
            }
        });

        // View Services button click listener (Text)
        binding.viewServiceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ViewServicesActivity
                Intent intent = new Intent(getApplicationContext(), ViewServicesActivity.class);
                startActivity(intent);
            }
        });

        // Query the "Orders" collection to get the total number of orders
        ordersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Get the count of documents in the QuerySnapshot
                int orderCount = queryDocumentSnapshots.size();
                // Display the order count in the corresponding TextView
                binding.totalOrdersValueTextView.setText(String.valueOf(orderCount));
            }
        });

        // Query the "Users" collection to get the total number of active users
        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Get the count of documents in the QuerySnapshot
                int userCount = queryDocumentSnapshots.size();
                // Display the user count in the corresponding TextView
                binding.activeUsersValueTextView.setText(String.valueOf(userCount));
            }
        });
    }

    // Method to navigate to the ViewAllOrdersActivity when a button is clicked
    public void goToAllOrders(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewAllOrdersActivity.class);
        startActivity(intent);
    }
}
