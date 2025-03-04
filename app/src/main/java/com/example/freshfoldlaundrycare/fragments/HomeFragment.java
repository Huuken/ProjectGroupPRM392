package com.example.freshfoldlaundrycare.fragments;

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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freshfoldlaundrycare.Modal.Services;
import com.example.freshfoldlaundrycare.R;
import com.example.freshfoldlaundrycare.ServiceListsActivity;
import com.example.freshfoldlaundrycare.auth.SetupActivity;
import com.example.freshfoldlaundrycare.databinding.FragmentHomeBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ProgressDialog dialog;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference serviceRef, cartRef, usersRef;
    String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        currentUserId = mAuth.getCurrentUser().getUid();
        serviceRef = db.collection("Services");
        cartRef = db.collection("Cart");
        usersRef = db.collection("Users");

        updateTheAddressData();

        binding.ironService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ServiceListsActivity.class);
                intent.putExtra("serviceTitle", "Iron");
                startActivity(intent);
            }
        });

        binding.washIronService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ServiceListsActivity.class);
                intent.putExtra("serviceTitle", "Wash Iron");
                startActivity(intent);
            }
        });

        binding.dryCleaningService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ServiceListsActivity.class);
                intent.putExtra("serviceTitle", "Dry Clean");
                startActivity(intent);
            }
        });

        binding.recyclerviewRecommended.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerviewRecommended.setLayoutManager(layoutManager);

        dialog.setMessage("please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        startListen();

        return binding.getRoot();
    }

    private void startListen() {
        serviceRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                if (snapshot.isEmpty()) {
                    /*binding.emptyProductLayout.setVisibility(View.VISIBLE);
                    binding.productAvailableLayout.setVisibility(View.GONE);*/
                    dialog.dismiss();
                } else {
                    /*binding.productAvailableLayout.setVisibility(View.VISIBLE);
                    binding.emptyProductLayout.setVisibility(View.GONE);*/
                    Query query = serviceRef.orderBy("ServiceCloth", Query.Direction.ASCENDING).limit(10);
                    FirestoreRecyclerOptions<Services> options = new FirestoreRecyclerOptions.Builder<Services>().setQuery(query, Services.class).build();
                    FirestoreRecyclerAdapter<Services, ServicesHolder> fireAdapter = new FirestoreRecyclerAdapter<Services, ServicesHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ServicesHolder holder, int position, @NonNull Services model) {
                            holder.serviceCloth.setText(model.getServiceCloth());
                            holder.servicePrice.setText("Rs. " + model.getServicePrice() + " /-");

                            if (model.getServiceType().equals("Iron")) {
                                Glide.with(getContext()).load(R.drawable.ic_iron).into(holder.serviceIcon);
                            } else if (model.getServiceType().equals("Wash Iron")) {
                                Glide.with(getContext()).load(R.drawable.ic_washing_machine).into(holder.serviceIcon);
                            } else {
                                Glide.with(getContext()).load(R.drawable.ic_shirt).into(holder.serviceIcon);
                            }


                            cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        holder.addServiceIcon.setVisibility(View.GONE);
                                        holder.removeCartBtn.setVisibility(View.VISIBLE);
                                    } else {
                                        holder.addServiceIcon.setVisibility(View.VISIBLE);
                                        holder.removeCartBtn.setVisibility(View.GONE);
                                    }
                                }
                            });

                            //holder.productPriceCard.setText("Rs. " + model.getProdPrice() + " /-");
                            holder.addServiceIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                //addToCartBtn.setText("Added to cart");
                                                holder.addServiceIcon.setVisibility(View.GONE);
                                                holder.removeCartBtn.setVisibility(View.VISIBLE);
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
                                                            holder.addServiceIcon.setVisibility(View.GONE);
                                                            holder.removeCartBtn.setVisibility(View.VISIBLE);
                                                            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            String msg = task.getException().getMessage();
                                                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });

                            holder.removeCartBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dialog.dismiss();
                                                Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
                                                holder.addServiceIcon.setVisibility(View.VISIBLE);
                                                holder.removeCartBtn.setVisibility(View.GONE);
                                            } else {
                                                String msg = task.getException().getMessage();
                                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });

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
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended, parent, false);
                            return new ServicesHolder(view);
                        }
                    };
                    binding.recyclerviewRecommended.setAdapter(fireAdapter);
                    fireAdapter.startListening();
                    dialog.dismiss();
                }
            }
        });
    }

    private class ServicesHolder extends RecyclerView.ViewHolder {

        TextView serviceCloth, servicePrice;
        ImageView serviceIcon;

        MaterialButton addServiceIcon, removeCartBtn;

        public ServicesHolder(@NonNull View itemView) {
            super(itemView);

            serviceCloth = itemView.findViewById(R.id.serviceCloth);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceIcon = itemView.findViewById(R.id.serviceIcon);
            addServiceIcon = itemView.findViewById(R.id.addServiceIcon);
            removeCartBtn = itemView.findViewById(R.id.removeServiceBtn);

        }
    }

    private void updateTheAddressData() {
        usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();

                    String profileUpdated = snapshot.getString("ProfileUpdated");
                    String address = snapshot.getString("Address");

                    if (profileUpdated != null && profileUpdated.equals("NO")) {
                        binding.addressText.setText("Click to update address");
                    } else {
                        binding.addressText.setText(address);
                    }

                    binding.addressText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (profileUpdated != null && profileUpdated.equals("NO")) {
                                Intent intent = new Intent(getContext(), SetupActivity.class);
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

    @Override
    public void onResume() {
        super.onResume();
        updateTheAddressData();
    }

}