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

    ActivityAdminMainBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference serviceRef;
    CollectionReference cartRef, usersRef, ordersRef;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Khởi tạo View Binding
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Khởi tạo Firebase Authentication và Firestore
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        usersRef = db.collection("Users");
        ordersRef = db.collection("Orders");

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.addServiceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddServiceActivity.class);
                startActivity(intent);
            }
        });

        binding.viewFeedbackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewFeedbackActivity.class);
                startActivity(intent);
            }
        });

        binding.feedbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewFeedbackActivity.class);
                startActivity(intent);
            }
        });

        binding.viewServiceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewServicesActivity.class);
                startActivity(intent);
            }
        });
        binding.viewServiceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewServicesActivity.class);
                startActivity(intent);
            }
        });

        // Query the "Orders" collection
        ordersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Get the count of documents in the QuerySnapshot
                int orderCount = queryDocumentSnapshots.size();
                binding.totalOrdersValueTextView.setText(String.valueOf(orderCount));
            }
        });

        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Get the count of documents in the QuerySnapshot
                int orderCount = queryDocumentSnapshots.size();
                binding.activeUsersValueTextView.setText(String.valueOf(orderCount));
            }
        });

    }

    public void goToAllOrders(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewAllOrdersActivity.class);
        startActivity(intent);
    }
}