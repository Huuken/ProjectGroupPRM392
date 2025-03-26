package com.example.freshfoldlaundrycare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshfoldlaundrycare.auth.LoginActivity;
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
                            String address = snapshot.getString("Address");
                            String name = snapshot.getString("Name");
                            String phone = snapshot.getString("Phone");
                            String email = snapshot.getString("Email");
                            String city = snapshot.getString("City");
                            String delivery = snapshot.getString("DeliveryTime");
                            String pickup = snapshot.getString("PickupTime");
                            String pincode = snapshot.getString("Pincode");

                            // 🔹 Tạo OrderID mới cho đơn hàng
                            String orderID = UniqueIdGenerator.generateOrderID(); // Firestore tự tạo ID

                            // Lấy ngày giờ hiện tại
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

                            String currentDate = dateFormat.format(calendar.getTime());
                            String currentTime = timeFormat.format(calendar.getTime());

                            // Tạo HashMap chứa thông tin đơn hàng
                            HashMap<String, Object> orderData = new HashMap<>();
                            orderData.put("OrderID", orderID);
                            orderData.put("UserID", currentUserId);
                            orderData.put("OrderStatus", "Placed");
                            orderData.put("OrderDate", currentDate);
                            orderData.put("OrderTime", currentTime);
                            orderData.put("TotalPrice", totalPrice);
                            orderData.put("Address", address);
                            orderData.put("Name", name);
                            orderData.put("Phone", phone);
                            orderData.put("Email", email);
                            orderData.put("City", city);
                            orderData.put("DeliveryTime", delivery);
                            orderData.put("PickupTime", pickup);
                            orderData.put("Pincode", pincode);

                            // 🔹 Lưu đơn hàng mới vào Firestore
                            ordersRef.document(orderID).set(orderData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ConfirmOrderActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                }
                            });

                            // 🔹 Lấy danh sách sản phẩm trong giỏ hàng
                            cartRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        int totalQuantity = 0; // Đếm tổng số lượng sản phẩm

                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            long quantity = document.getLong("Quantity");
                                            totalQuantity += quantity;
                                        }

                                        // Cập nhật Quantity vào thông tin đơn hàng
                                        orderData.put("Quantity", totalQuantity);

                                        // 🔹 Lưu đơn hàng mới vào Firestore
                                        ordersRef.document(orderID).set(orderData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ConfirmOrderActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            String productId = document.getString("ProductId");
                                            String productName = document.getString("ProductName");
                                            String productPrice = document.getString("ProductPrice");
                                            String category = document.getString("Category");
                                            long quantity = document.getLong("Quantity");
                                            String totalPrice = document.getString("TotalPrice");

                                            // Lưu sản phẩm vào đơn hàng mới (Orders/{OrderID}/Products/)
                                            Map<String, Object> cartItemData = new HashMap<>();
                                            cartItemData.put("ProductId", productId);
                                            cartItemData.put("ProductName", productName);
                                            cartItemData.put("ProductPrice", productPrice);
                                            cartItemData.put("OrderDate", currentDate);
                                            cartItemData.put("OrderTime", currentTime);
                                            cartItemData.put("Category", category);
                                            cartItemData.put("Quantity", quantity);
                                            cartItemData.put("TotalPrice", totalPrice);

                                            ordersRef.document(orderID).collection("Products").document(productId).set(cartItemData);
                                        }
                                        // 🔹 Xóa giỏ hàng sau khi đặt hàng
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            cartRef.document(document.getId()).delete();
                                        }
                                    } else {
                                        Toast.makeText(ConfirmOrderActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(ConfirmOrderActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}