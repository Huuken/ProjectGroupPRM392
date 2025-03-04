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

    String userID;
    ActivityViewServiceRequestedBinding binding;
    ProgressDialog dialog;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference orderServiceRef;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewServiceRequestedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userID = getIntent().getStringExtra("userID");

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        currentUserId = mAuth.getCurrentUser().getUid();
        orderServiceRef = db.collection("Orders").document(userID).collection("Products");

        binding.serviceListRecView.setHasFixedSize(true);
        binding.serviceListRecView.setLayoutManager(new LinearLayoutManager(this));

        dialog.setMessage("please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        startListen();

    }

    private void startListen() {
        orderServiceRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                if (snapshot.isEmpty()) {
                    /*binding.emptyProductLayout.setVisibility(View.VISIBLE);
                    binding.productAvailableLayout.setVisibility(View.GONE);*/
                    dialog.dismiss();
                } else {
                    /*binding.productAvailableLayout.setVisibility(View.VISIBLE);
                    binding.emptyProductLayout.setVisibility(View.GONE);*/
                    Query query = orderServiceRef.orderBy("ProductName", Query.Direction.ASCENDING);
                    FirestoreRecyclerOptions<Products> options = new FirestoreRecyclerOptions.Builder<Products>().setQuery(query, Products.class).build();
                    FirestoreRecyclerAdapter<Products, ProductsHolder> fireAdapter = new FirestoreRecyclerAdapter<Products, ProductsHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ProductsHolder holder, int position, @NonNull Products model) {
                            holder.serviceCloth.setText(model.getProductName());
                            holder.servicePrice.setText("Rs. " + model.getProductPrice() + " /-");
                            holder.serviceQty.setText("Qty " + Integer.valueOf(model.getQuantity()));

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
                    binding.serviceListRecView.setAdapter(fireAdapter);
                    fireAdapter.startListening();
                    dialog.dismiss();
                }
            }
        });
    }

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