package com.example.freshfoldlaundrycare.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freshfoldlaundrycare.Modal.Products;
import com.example.freshfoldlaundrycare.R;
import com.example.freshfoldlaundrycare.databinding.ActivityViewServiceRequestedBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewServiceRequestedActivity extends AppCompatActivity {

    String userID; // Stores the user ID passed from the previous activity
    ActivityViewServiceRequestedBinding binding; // View binding for layout
    ProgressDialog dialog; // Progress dialog to show loading indicator
    FirebaseAuth mAuth; // Firebase authentication instance
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Firestore database instance
    CollectionReference orderServiceRef; // Firestore reference to the user's orders
    String currentUserId; // Stores the currently authenticated user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewServiceRequestedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userID = getIntent().getStringExtra("userID"); // Get user ID from intent

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
        dialog = new ProgressDialog(this); // Initialize ProgressDialog
        currentUserId = mAuth.getCurrentUser().getUid(); // Get current user ID
        orderServiceRef = db.collection("Orders").document(userID).collection("Products"); // Reference to the user's order collection

        binding.serviceListRecView.setHasFixedSize(true);
        binding.serviceListRecView.setLayoutManager(new LinearLayoutManager(this));

        dialog.setMessage("please wait"); // Set progress dialog message
        dialog.setCanceledOnTouchOutside(false);
        dialog.show(); // Show loading indicator
        startListen(); // Start fetching data
    }

    private void startListen() {
        orderServiceRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                if (snapshot.isEmpty()) {
                    dialog.dismiss(); // Dismiss dialog if no orders found
                } else {
                    // Query Firestore to order products by their name in ascending order
                    Query query = orderServiceRef.orderBy("ProductName", Query.Direction.ASCENDING);
                    FirestoreRecyclerOptions<Products> options = new FirestoreRecyclerOptions.Builder<Products>()
                            .setQuery(query, Products.class)
                            .build();

                    // Create FirestoreRecyclerAdapter to display products
                    FirestoreRecyclerAdapter<Products, ProductsHolder> fireAdapter =
                            new FirestoreRecyclerAdapter<Products, ProductsHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull ProductsHolder holder, int position, @NonNull Products model) {
                                    // Bind product details to view elements
                                    holder.serviceCloth.setText(model.getProductName());
                                    holder.servicePrice.setText("Rs. " + model.getProductPrice() + " /-");
                                    holder.serviceQty.setText("Qty " + Integer.valueOf(model.getQuantity()));

                                    // Load appropriate icon based on product category
                                    if (model.getCategory().equals("Iron")) {
                                        Glide.with(getApplicationContext()).load(R.drawable.ic_iron).into(holder.serviceIcon);
                                    } else if (model.getCategory().equals("Wash Iron")) {
                                        Glide.with(getApplicationContext()).load(R.drawable.ic_washing_machine).into(holder.serviceIcon);
                                    } else {
                                        Glide.with(getApplicationContext()).load(R.drawable.ic_shirt).into(holder.serviceIcon);
                                    }
                                }

                                @NonNull
                                @Override
                                public ProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_request_layout, parent, false);
                                    return new ProductsHolder(view);
                                }
                            };

                    binding.serviceListRecView.setAdapter(fireAdapter); // Set adapter to RecyclerView
                    fireAdapter.startListening(); // Start real-time updates
                    dialog.dismiss(); // Dismiss loading indicator
                }
            }
        });
    }

    // ViewHolder class to hold the product views
    private class ProductsHolder extends RecyclerView.ViewHolder {

        TextView serviceCloth, servicePrice, serviceQty;
        ImageView serviceIcon;

        public ProductsHolder(@NonNull View itemView) {
            super(itemView);

            serviceCloth = itemView.findViewById(R.id.serviceCloth);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceIcon = itemView.findViewById(R.id.serviceIcon);
            serviceQty = itemView.findViewById(R.id.serviceQty);
        }
    }
}