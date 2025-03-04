package com.example.freshfoldlaundrycare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshfoldlaundrycare.databinding.ActivityConfirmOrderBinding;
import com.example.freshfoldlaundrycare.helper.UniqueIdGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ConfirmOrderActivity extends AppCompatActivity {

    ActivityConfirmOrderBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    ProgressDialog dialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ordersRef, cartRef, usersRef;
    String currentUserId, totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        currentUserId = mAuth.getCurrentUser().getUid();

        totalPrice = getIntent().getStringExtra("totalPrice");

        usersRef = db.collection("Users");
        cartRef = db.collection("Cart").document(currentUserId).collection("Cart");
        ordersRef = db.collection("Orders");

        usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    String profileUpdated = snapshot.getString("ProfileUpdated");
                    String address = snapshot.getString("Address");
                    String name = snapshot.getString("Name");
                    String phone = snapshot.getString("Phone");
                    String email = snapshot.getString("Email");
                    String city = snapshot.getString("City");
                    String delivery = snapshot.getString("DeliveryTime");
                    String pickup = snapshot.getString("PickupTime");
                    String pincode = snapshot.getString("Pincode");

                    binding.profileAddress.setText(address);
                    binding.profileName.setText(name);
                    binding.profilePhone.setText(phone);
                    binding.profileEmail.setText(email);
                    binding.profileCity.setText(city);
                    binding.profileDelivery.setText(delivery);
                    binding.profilePickup.setText(pickup);
                    binding.profilePincode.setText(pincode);
                }
            }
        });

        binding.confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            String profileUpdated = snapshot.getString("ProfileUpdated");
                            String address = snapshot.getString("Address");
                            String name = snapshot.getString("Name");
                            String phone = snapshot.getString("Phone");
                            String email = snapshot.getString("Email");
                            String city = snapshot.getString("City");
                            String delivery = snapshot.getString("DeliveryTime");
                            String pickup = snapshot.getString("PickupTime");
                            String pincode = snapshot.getString("Pincode");
                            String orderID = UniqueIdGenerator.generateOrderID();

                            Calendar calendar = Calendar.getInstance();

                            //Format the date and time using SimpleDateFormat
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Change the format as needed
                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a"); // Adjusted format for 12-hour time with AM/PM

                            //Get formatted date and time strings
                            String currentDate = dateFormat.format(calendar.getTime());
                            String currentTime = timeFormat.format(calendar.getTime());


                            HashMap<String, Object> cartItem = new HashMap<>();
                            cartItem.put("UserId", currentUserId);
                            cartItem.put("OrderID", orderID);
                            cartItem.put("Address", address);
                            cartItem.put("Name", name);
                            cartItem.put("Phone", phone);
                            cartItem.put("Email", email);
                            cartItem.put("City", city);
                            cartItem.put("DeliveryTime", delivery);
                            cartItem.put("PickupTime", pickup);
                            cartItem.put("Pincode", pincode);
                            cartItem.put("Quantity", 1);
                            cartItem.put("TotalPrice", totalPrice);

                            HashMap dummy = new HashMap();
                            dummy.put("OrderStatus", "Placed");
                            dummy.put("UserID", currentUserId);
                            dummy.put("OrderID", orderID);
                            dummy.put("OrderDate", currentDate);
                            dummy.put("OrderTime", currentTime);
                            dummy.put("TotalPrice", totalPrice);
                            dummy.put("Address", address);
                            dummy.put("Name", name);
                            dummy.put("Phone", phone);
                            dummy.put("Email", email);
                            dummy.put("City", city);
                            dummy.put("DeliveryTime", delivery);
                            dummy.put("PickupTime", pickup);
                            dummy.put("Pincode", pincode);

                            /*ordersRef.document(orderID).set(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    } else {
                                        String msg = task.getException().getMessage();
                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });*/
                            ordersRef.document(currentUserId).set(dummy).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ConfirmOrderActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                }
                            });

                            cartRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    // Check if there are documents in the cart
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        // Iterate through each document in the cart
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            // Get the data from the cart item
                                            String productId = document.getString("ProductId");
                                            String productName = document.getString("ProductName");
                                            String productPrice = document.getString("ProductPrice");
                                            String category = document.getString("Category");
                                            long quantity = document.getLong("Quantity");
                                            String totalPrice = document.getString("TotalPrice");

                                            // Create a map to store the cart item data
                                            Map<String, Object> cartItemData = new HashMap<>();
                                            cartItemData.put("ProductId", productId);
                                            cartItemData.put("ProductName", productName);
                                            cartItemData.put("ProductPrice", productPrice);
                                            cartItemData.put("OrderDate", currentDate);
                                            cartItemData.put("OrderTime", currentTime);
                                            cartItemData.put("Category", category);
                                            cartItemData.put("Quantity", quantity);
                                            cartItemData.put("TotalPrice", totalPrice);

                                            // Add the cart item data to the new reference in the "Order" collection
                                            ordersRef.document(currentUserId).collection("Products").document(productId).set(cartItemData);
                                            usersRef.document(currentUserId).collection("Products").document(productId).set(cartItemData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Cart item successfully added to the new reference
                                                            // You can optionally remove the item from the cart after adding it to the order
                                                            cartRef.document(document.getId()).delete();
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(ConfirmOrderActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}