package com.example.freshfoldlaundrycare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freshfoldlaundrycare.Modal.Services;
import com.example.freshfoldlaundrycare.databinding.ActivityServiceListsBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class ServiceListsActivity extends AppCompatActivity {

    String serviceTitle;
    ActivityServiceListsBinding binding;
    ProgressDialog dialog;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference serviceRef, cartRef;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceListsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        serviceTitle = getIntent().getStringExtra("serviceTitle");
        binding.serviceTitle.setText(serviceTitle + " Service");

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        currentUserId = mAuth.getCurrentUser().getUid();
        serviceRef = db.collection("Services");
        cartRef = db.collection("Cart");

        binding.serviceListRecView.setHasFixedSize(true);
        binding.serviceListRecView.setLayoutManager(new LinearLayoutManager(this));

        dialog.setMessage("please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        startListen();

    }

    private void startListen() {
        serviceRef.whereEqualTo("ServiceType", serviceTitle).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                if (snapshot.isEmpty()) {
                    /*binding.emptyProductLayout.setVisibility(View.VISIBLE);
                    binding.productAvailableLayout.setVisibility(View.GONE);*/
                    dialog.dismiss();
                } else {
                    /*binding.productAvailableLayout.setVisibility(View.VISIBLE);
                    binding.emptyProductLayout.setVisibility(View.GONE);*/
                    Query query = serviceRef.whereEqualTo("ServiceType", serviceTitle).orderBy("ServiceCloth", Query.Direction.ASCENDING);
                    FirestoreRecyclerOptions<Services> options = new FirestoreRecyclerOptions.Builder<Services>().setQuery(query, Services.class).build();
                    FirestoreRecyclerAdapter<Services, ServicesHolder> fireAdapter = new FirestoreRecyclerAdapter<Services, ServicesHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ServicesHolder holder, int position, @NonNull Services model) {
                            holder.serviceCloth.setText(model.getServiceCloth());
                            holder.servicePrice.setText("Rs. " + model.getServicePrice() + " /-");

                            if (serviceTitle.equals("Iron")) {
                                Glide.with(getApplicationContext()).load(R.drawable.item_blanket).into(holder.serviceIcon);
                            } else if (serviceTitle.equals("Wash Iron")) {
                                Glide.with(getApplicationContext()).load(R.drawable.ic_washing_machine).into(holder.serviceIcon);
                            } else {
                                Glide.with(getApplicationContext()).load(R.drawable.ic_shirt).into(holder.serviceIcon);
                            }


                            cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        holder.addCard.setVisibility(View.GONE);
                                        holder.removeCard.setVisibility(View.VISIBLE);
                                    } else {
                                        holder.addCard.setVisibility(View.VISIBLE);
                                        holder.removeCard.setVisibility(View.GONE);
                                    }
                                }
                            });

                            //holder.productPriceCard.setText("Rs. " + model.getProdPrice() + " /-");
                            holder.serviceIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                //addToCartBtn.setText("Added to cart");
                                                holder.addCard.setVisibility(View.GONE);
                                                holder.removeCard.setVisibility(View.VISIBLE);
                                            } else {
                                                dialog.setMessage("please wait...");
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.show();

                                                String price = model.getServicePrice();
                                                int quantity = 1;
                                                int total = Integer.parseInt(price) * quantity;

                                                HashMap<String, Object> cartItem = new HashMap<>();
                                                cartItem.put("UserId", currentUserId);
                                                cartItem.put("ProductId", model.getServiceID());
                                                cartItem.put("ProductName", model.getServiceCloth());
                                                cartItem.put("ProductPrice", model.getServicePrice());
                                                cartItem.put("Category", model.getServiceType());
                                                cartItem.put("Quantity", 1);
                                                cartItem.put("TotalPrice", String.valueOf(total));
                                                cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).set(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            dialog.dismiss();
                                                            holder.addCard.setVisibility(View.GONE);
                                                            holder.removeCard.setVisibility(View.VISIBLE);
                                                            Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            String msg = task.getException().getMessage();
                                                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });

//                            holder.removeCartBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                dialog.dismiss();
//                                                Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
//                                                holder.addCard.setVisibility(View.VISIBLE);
//                                                holder.removeCard.setVisibility(View.GONE);
//                                            } else {
//                                                String msg = task.getException().getMessage();
//                                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                                                dialog.dismiss();
//                                            }
//                                        }
//                                    });
//                                }
//                            });

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /*Bundle bundle = new Bundle();
                                    bundle.putString("productID", model.getProdID());
                                    bundle.putString("imageUrl", model.getProdImage());
                                    ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                                    productDetailsFragment.setArguments(bundle);
                                    Navigation.findNavController(v).navigate(R.id.action_productListFragment_to_productDetailFragment, bundle);*/
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item_layout, parent, false);
                            return new ServicesHolder(view);
                        }
                    };
                    binding.serviceListRecView.setAdapter(fireAdapter);
                    fireAdapter.startListening();
                    dialog.dismiss();
                }
            }
        });
    }

    public class ServicesHolder extends RecyclerView.ViewHolder {
        TextView serviceCloth, servicePrice;
        ImageView serviceIcon, updateServiceIcon; // Updated
        CardView addCard, removeCard;
        ImageView removeServiceBtn; // Updated

        public ServicesHolder(@NonNull View itemView) {
            super(itemView);
            serviceCloth = itemView.findViewById(R.id.serviceCloth);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceIcon = itemView.findViewById(R.id.serviceIcon);
            updateServiceIcon = itemView.findViewById(R.id.updateServiceIcon); // Matches XML
            addCard = itemView.findViewById(R.id.addCard);
            removeCard = itemView.findViewById(R.id.removeCard);
            removeServiceBtn = itemView.findViewById(R.id.removeServiceBtn); // Matches XML
        }
    }
}