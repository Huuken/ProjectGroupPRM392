package com.example.freshfoldlaundrycare;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freshfoldlaundrycare.databinding.ActivityCardPaymentBinding;
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
import java.util.Locale;
import java.util.Map;

public class CardPaymentActivity extends AppCompatActivity {

    // View binding object
    ActivityCardPaymentBinding binding;
    // Firebase authentication instance
    FirebaseAuth mAuth;
    // Progress dialog for loading
    ProgressDialog loadingBar;
    ProgressDialog dialog;
    // Firebase Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Firestore references for orders, cart, and users
    CollectionReference ordersRef, cartRef, usersRef;
    // Current user ID and total price
    String currentUserId, totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Inflate layout using view binding
        binding = ActivityCardPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Adjust UI to fit system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        // Initialize progress dialogs
        loadingBar = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        // Get current user ID
        currentUserId = mAuth.getCurrentUser().getUid();
        // Get total price from intent
        totalPrice = getIntent().getStringExtra("totalPrice");

        // Initialize Firestore references
        usersRef = db.collection("Users");
        cartRef = db.collection("Cart").document(currentUserId).collection("Cart");
        ordersRef = db.collection("Orders");

        // Set onClickListener for expiry date input
        binding.expiryDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize calendar instance
                Calendar calendar = Calendar.getInstance();
                // Create DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CardPaymentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear = monthOfYear + 1; // Adjust month (zero-based index)
                                String formattedDate = String.format(Locale.US, "%02d/%04d", monthOfYear, year);
                                Log.d("Selected Date", formattedDate);
                                binding.expiryDateEditText.setText(formattedDate);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                // Set minimum selectable date
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        // Set onClickListener for payment button
        binding.paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values from text fields
                String cardno = binding.cardNumberEditText.getText().toString().trim();
                String expiry = binding.expiryDateEditText.getText().toString().trim();
                String cvv = binding.cvvEditText.getText().toString().trim();
                String name = binding.cardholderNameEditText.getText().toString().trim();

                // Validate inputs
                if (cardno.isEmpty() || expiry.isEmpty() || cvv.isEmpty() || name.isEmpty()) {
                    Toast.makeText(CardPaymentActivity.this, "Field is empty...", Toast.LENGTH_SHORT).show();
                } else {
                    // Retrieve user details from Firestore
                    usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot snapshot = task.getResult();
                                String orderID = UniqueIdGenerator.generateOrderID();

                                // Get current date and time
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
                                String currentDate = dateFormat.format(calendar.getTime());
                                String currentTime = timeFormat.format(calendar.getTime());

                                // Create order data
                                HashMap<String, Object> orderData = new HashMap<>();
                                orderData.put("OrderStatus", "Placed");
                                orderData.put("UserID", currentUserId);
                                orderData.put("OrderID", orderID);
                                orderData.put("OrderDate", currentDate);
                                orderData.put("OrderTime", currentTime);
                                orderData.put("TotalPrice", totalPrice);

                                // Save order to Firestore
                                ordersRef.document(currentUserId).set(orderData).addOnCompleteListener(task1 ->
                                        Toast.makeText(getApplicationContext(), "Order Placed", Toast.LENGTH_SHORT).show()
                                );

                                // Retrieve cart items and move them to order collection
                                cartRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            Map<String, Object> cartItemData = document.getData();
                                            cartItemData.put("OrderDate", currentDate);
                                            cartItemData.put("OrderTime", currentTime);

                                            // Save cart item to order
                                            ordersRef.document(currentUserId).collection("Products").document(document.getId()).set(cartItemData);
                                            // Remove item from cart
                                            cartRef.document(document.getId()).delete();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
