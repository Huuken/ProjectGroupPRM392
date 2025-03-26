package com.example.freshfoldlaundrycare;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freshfoldlaundrycare.Modal.Cart;
import com.example.freshfoldlaundrycare.auth.SetupActivity;
import com.example.freshfoldlaundrycare.databinding.ActivityCartBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    // View binding for easier UI access
    ActivityCartBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference serviceRef;
    CollectionReference cartRef, usersRef;
    ProgressDialog dialog;
    String currentUserId;
    int overAllTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase authentication and database references
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        currentUserId = mAuth.getCurrentUser().getUid();
        cartRef = db.collection("Cart").document(currentUserId).collection("Cart");
        usersRef = db.collection("Users");

        // Set up RecyclerView for displaying cart items
        binding.cartProductRecyclerView.setHasFixedSize(true);
        binding.cartProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load address data and set up button listeners
        updateTheAddressData();
        setupListeners();
        startListen();
    }

    // Retrieve and update address information
    private void updateTheAddressData() {
        usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    String profileUpdated = snapshot.getString("ProfileUpdated");
                    String address = snapshot.getString("Address");

                    // If profile is not updated, prompt user to update
                    if (profileUpdated.equals("NO")) {
                        binding.addressText.setText("Click to update address");
                    } else {
                        binding.addressText.setText(address);
                    }

                    // Set click listener to update address if needed
                    binding.addressText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (profileUpdated.equals("NO")) {
                                Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
                                startActivity(intent);
                            } else {
                                binding.addressText.setText(address);
                            }
                        }
                    });
                }
            }
        });
    }

    // Set up button click listeners
    private void setupListeners() {
        binding.checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectPaymentMehodActivity.class);
                intent.putExtra("totalPrice", String.valueOf(overAllTotalPrice));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTheAddressData();
        dialog.setMessage("please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        overAllTotalPrice = 0;
        startListen();
    }

    // Retrieve and display cart items
    private void startListen() {
        cartRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                if (snapshot.isEmpty()) {
                    binding.emptyCartLayout.setVisibility(View.VISIBLE);
                    binding.availableCartLayout.setVisibility(View.GONE);
                } else {
                    binding.emptyCartLayout.setVisibility(View.GONE);
                    binding.availableCartLayout.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        overAllTotalPrice = 0;
    }

    // Update cart data in Firestore
    private void updateCartData(String productId, int quantity, String totalPrice) {
        Map<String, Object> cartMap = new HashMap<>();
        cartMap.put("Quantity", quantity);
        cartMap.put("TotalPrice", totalPrice);

        cartRef.document(productId)
                .update(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onResume(); // Refresh the activity after updating cart data
                        }
                    }
                });
    }
}
