package com.example.freshfoldlaundrycare; // Defines the package name for this application

import android.app.ProgressDialog; // Imports ProgressDialog to show a loading dialog
import android.os.Bundle; // Imports Bundle for activity state management
import android.util.Log; // Imports Log for debugging purposes
import android.widget.Button; // Imports Button for UI interactions
import android.widget.Toast; // Imports Toast to show short messages

import androidx.annotation.NonNull; // Imports NonNull annotation to prevent null values
import androidx.appcompat.app.AppCompatActivity; // Imports AppCompatActivity for activity compatibility

import com.example.freshfoldlaundrycare.databinding.ActivityOrderDetailBinding; // Imports view binding for activity layout
import com.google.firebase.firestore.DocumentReference; // Imports DocumentReference to refer to a Firestore document
import com.google.firebase.firestore.DocumentSnapshot; // Imports DocumentSnapshot to access Firestore document data
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore for database operations
import com.google.firebase.firestore.FirebaseFirestoreException; // Imports FirebaseFirestoreException for error handling
import com.google.firebase.firestore.QueryDocumentSnapshot; // Imports QueryDocumentSnapshot to read Firestore query results

public class ViewOrderDetailActivity extends AppCompatActivity { // Defines the ViewOrderDetailActivity class extending AppCompatActivity

    private ActivityOrderDetailBinding binding; // Declares a binding object for UI interactions
    private FirebaseFirestore db; // Declares a FirebaseFirestore instance to interact with Firestore
    private ProgressDialog progressDialog; // Declares a ProgressDialog to indicate loading state
    private String OrderID; // Declares a variable to store the order ID

    @Override // Overrides the onCreate method
    protected void onCreate(Bundle savedInstanceState) { // Called when the activity is created
        super.onCreate(savedInstanceState); // Calls the superclass onCreate method
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater()); // Initializes view binding
        setContentView(binding.getRoot()); // Sets the root view for this activity

        db = FirebaseFirestore.getInstance(); // Initializes Firebase Firestore instance
        initProgressDialog(); // Calls the method to initialize and show a progress dialog

        String orderId = getIntent().getStringExtra("ORDER_ID"); // Retrieves the order ID from the intent
        OrderID = orderId; // Assigns the retrieved order ID to the class variable
        if (orderId != null && !orderId.isEmpty()) { // Checks if order ID is valid
            loadOrderDetails(orderId); // Calls method to load order details from Firestore
        } else {
            showErrorMessage("Không tìm thấy đơn hàng!"); // Shows an error message if order ID is not found
        }
    }

    private void initProgressDialog() { // Initializes the progress dialog
        progressDialog = new ProgressDialog(this); // Creates a new ProgressDialog instance
        progressDialog.setMessage("Đang tải dữ liệu..."); // Sets the message for the progress dialog
        progressDialog.setCanceledOnTouchOutside(false); // Prevents dismissal when tapping outside the dialog
        progressDialog.show(); // Displays the progress dialog
    }

    private void loadOrderDetails(@NonNull String orderId) { // Loads order details from Firestore
        DocumentReference orderRef = db.collection("Orders").document(orderId); // Gets a reference to the Firestore document

        orderRef.get().addOnSuccessListener(documentSnapshot -> { // Retrieves the document and handles success case
            progressDialog.dismiss(); // Dismisses the progress dialog
            if (documentSnapshot.exists()) { // Checks if the document exists
                Log.d("FIRESTORE_DATA", "Dữ liệu nhận được: " + documentSnapshot.getData()); // Logs the retrieved data
                updateUI(documentSnapshot); // Calls method to update UI with order details
                binding.btnBack.setOnClickListener(v -> finish()); // Sets the back button to close the activity
            } else {
                showErrorMessage("Không tìm thấy đơn hàng!"); // Shows an error message if the document is not found
            }
        }).addOnFailureListener(e -> { // Handles failure case when retrieving data
            progressDialog.dismiss(); // Dismisses the progress dialog
            Log.e("FIRESTORE_ERROR", "Lỗi khi tải dữ liệu", e); // Logs the error message
            showErrorMessage("Lỗi khi tải dữ liệu: " + e.getMessage()); // Shows an error message with error details
        });
    }

    private void updateUI(DocumentSnapshot documentSnapshots) { // Updates UI with retrieved order details
        binding.orderID.setText(OrderID); // Displays the order ID
        binding.orderDate.setText(documentSnapshots.getString("OrderDate")); // Displays the order date
        binding.orderTime.setText(documentSnapshots.getString("OrderTime")); // Displays the order time
        binding.userName.setText(documentSnapshots.getString("Name")); // Displays the user's name
        binding.phoneNumber.setText(documentSnapshots.getString("Phone")); // Displays the user's phone number
        binding.addressText.setText(documentSnapshots.getString("Address")); // Displays the delivery address
        binding.totalPrice.setText(documentSnapshots.getString("TotalPrice")); // Displays the total price
        binding.cityText.setText(documentSnapshots.getString("City")); // Displays the city
        binding.emailText.setText(documentSnapshots.getString("Email")); // Displays the email
        //binding.orderProductQty.setText(documentSnapshots.getString("orderProductQty")); // (Commented out) Displays product quantity if needed
        binding.orderStatus.setText(documentSnapshots.getString("OrderStatus")); // Displays the order status
    }

    private void showErrorMessage(String message) { // Shows an error message using a Toast
        progressDialog.dismiss(); // Dismisses the progress dialog
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); // Displays a Toast message with the error
    }
}
