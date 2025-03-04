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

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        currentUserId = mAuth.getCurrentUser().getUid();
        cartRef = db.collection("Cart").document(currentUserId).collection("Cart");
        usersRef = db.collection("Users");

        binding.cartProductRecyclerView.setHasFixedSize(true);
        binding.cartProductRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateTheAddressData();
        setupListeners();
        startListen();
    }

    private void updateTheAddressData() {
        usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();

                    String profileUpdated = snapshot.getString("ProfileUpdated");
                    String address = snapshot.getString("Address");

                    if (profileUpdated.equals("NO")) {
                        binding.addressText.setText("Click to update address");
                    } else {
                        binding.addressText.setText(address);
                    }

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
                    Query query = cartRef.orderBy("ProductName", Query.Direction.ASCENDING);
                    FirestoreRecyclerOptions<Cart> options = new FirestoreRecyclerOptions.Builder<Cart>().setQuery(query, Cart.class).build();
                    FirestoreRecyclerAdapter<Cart, CartHolder> fireAdapter = new FirestoreRecyclerAdapter<Cart, CartHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull CartHolder holder, int position, @NonNull Cart model) {

                            holder.productName.setText(model.getProductName());
                            holder.productPrice.setText(model.getProductPrice());
                            holder.productQtyText.setText(String.valueOf(model.getQuantity()));
                            holder.productTotalPrice.setText(model.getTotalPrice());
                            holder.productType.setText(model.getCategory());

                            if (model.getCategory().equals("Iron")) {
                                Glide.with(getApplicationContext()).load(R.drawable.ic_iron).into(holder.serviceIcon);
                            } else if (model.getCategory().equals("Wash Iron")) {
                                Glide.with(getApplicationContext()).load(R.drawable.ic_washing_machine).into(holder.serviceIcon);
                            } else {
                                Glide.with(getApplicationContext()).load(R.drawable.ic_shirt).into(holder.serviceIcon);
                            }

                            final int[] quantity = {model.getQuantity()};

                            int oneTypeProductTPrice = Integer.parseInt(model.getProductPrice()) * model.getQuantity();
                            overAllTotalPrice += oneTypeProductTPrice;
                            binding.totalCheckoutPrice.setText(String.valueOf(overAllTotalPrice));

                            holder.minusBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (quantity[0] > 1) {
                                        quantity[0]--;
                                        holder.productQtyText.setText(String.valueOf(quantity[0]));
                                        int totalPrice = Integer.parseInt(model.getTotalPrice()) - Integer.parseInt(model.getProductPrice());
                                        overAllTotalPrice -= Integer.parseInt(model.getProductPrice());
                                        updateCartData(model.getProductId(), quantity[0], String.valueOf(totalPrice));
                                    }
                                }
                            });

                            holder.plusBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    quantity[0]++;
                                    holder.productQtyText.setText(String.valueOf(quantity[0]));
                                    int totalPrice = Integer.parseInt(model.getTotalPrice()) + Integer.parseInt(model.getProductPrice());
                                    overAllTotalPrice += Integer.parseInt(model.getProductPrice());
                                    updateCartData(model.getProductId(), quantity[0], String.valueOf(totalPrice));
                                }
                            });

                            holder.removeProductBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cartRef.document(model.getProductId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                holder.productQtyText.setText(String.valueOf(quantity[0]));
                                                overAllTotalPrice -= (model.getQuantity() * Integer.parseInt(model.getProductPrice()));
                                                Toast.makeText(getApplicationContext(), model.getProductName() + " removed successfully", Toast.LENGTH_LONG).show();
                                                // Finish and restart the activity to refresh the page
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        }
                                    });
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product, parent, false);
                            return new CartHolder(view);
                        }
                    };
                    binding.cartProductRecyclerView.setAdapter(fireAdapter);
                    fireAdapter.startListening();
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

    private class CartHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQtyText, productTotalPrice, productType;
        ImageView minusBtn, plusBtn, removeProductBtn, serviceIcon;

        public CartHolder(@NonNull View itemView) {
            super(itemView);

            productQtyText = itemView.findViewById(R.id.productQtyText);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productTotalPrice = itemView.findViewById(R.id.productTotalPrice);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            removeProductBtn = itemView.findViewById(R.id.removeProductBtn);
            productType = itemView.findViewById(R.id.productType);
            serviceIcon = itemView.findViewById(R.id.serviceIcon);
        }
    }
}
