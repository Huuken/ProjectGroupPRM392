package com.example.freshfoldlaundrycare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshfoldlaundrycare.databinding.ActivityOrderDetailBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ViewOrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private String OrderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        initProgressDialog();

        String orderId = getIntent().getStringExtra("ORDER_ID");
        OrderID = orderId;
        if (orderId != null && !orderId.isEmpty()) {
            loadOrderDetails(orderId);
        } else {
            showErrorMessage("Không tìm thấy đơn hàng!");
        }
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải dữ liệu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void loadOrderDetails(@NonNull String orderId) {
        DocumentReference orderRef = db.collection("Orders").document(orderId);

        orderRef.get().addOnSuccessListener(documentSnapshot -> {
            progressDialog.dismiss();
            if (documentSnapshot.exists()) {
                Log.d("FIRESTORE_DATA", "Dữ liệu nhận được: " + documentSnapshot.getData());
                updateUI(documentSnapshot);
                binding.btnBack.setOnClickListener(v -> finish());

            } else {
                showErrorMessage("Không tìm thấy đơn hàng!");
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Log.e("FIRESTORE_ERROR", "Lỗi khi tải dữ liệu", e);
            showErrorMessage("Lỗi khi tải dữ liệu: " + e.getMessage());
        });
    }

    private void updateUI(DocumentSnapshot documentSnapshots) {
        binding.orderID.setText(OrderID);
        binding.orderDate.setText(documentSnapshots.getString("OrderDate"));
        binding.orderTime.setText(documentSnapshots.getString("OrderTime"));
        binding.userName.setText(documentSnapshots.getString("Name"));
        binding.phoneNumber.setText(documentSnapshots.getString("Phone"));
        binding.addressText.setText(documentSnapshots.getString("Address"));
        binding.totalPrice.setText(documentSnapshots.getString("TotalPrice"));
        binding.cityText.setText(documentSnapshots.getString("City"));
        binding.emailText.setText(documentSnapshots.getString("Email"));
        //binding.orderProductQty.setText(documentSnapshots.getString("orderProductQty"));
        binding.orderStatus.setText(documentSnapshots.getString("OrderStatus"));
    }

    private void showErrorMessage(String message) {
        progressDialog.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
