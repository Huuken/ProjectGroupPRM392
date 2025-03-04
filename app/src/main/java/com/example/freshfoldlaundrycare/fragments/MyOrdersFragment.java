package com.example.freshfoldlaundrycare.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshfoldlaundrycare.Modal.Orders;
import com.example.freshfoldlaundrycare.R;
import com.example.freshfoldlaundrycare.admin.ViewServiceRequestedActivity;
import com.example.freshfoldlaundrycare.databinding.FragmentMyOrdersBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MyOrdersFragment extends Fragment {

    FragmentMyOrdersBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cartRef, usersRef, ordersRef;
    ProgressDialog dialog;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyOrdersBinding.inflate(getLayoutInflater(), container, false);

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        cartRef = db.collection("Cart");
        ordersRef = db.collection("Orders");
        userID = mAuth.getCurrentUser().getUid();

        binding.newOrdersRecyclerView.setHasFixedSize(true);
        binding.newOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dialog.setMessage("please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        startListen();

        return binding.getRoot();
    }

    private void startListen() {
        ordersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                if (snapshot.isEmpty()) {
                    binding.emptyOrderLayout.setVisibility(View.VISIBLE);
                    binding.availableOrderLayout.setVisibility(View.GONE);
                    dialog.dismiss();
                } else {
                    binding.availableOrderLayout.setVisibility(View.VISIBLE);
                    binding.emptyOrderLayout.setVisibility(View.GONE);
                    Query query = ordersRef.whereEqualTo("UserID",userID).orderBy("OrderDate", Query.Direction.ASCENDING);
                    FirestoreRecyclerOptions<Orders> options = new FirestoreRecyclerOptions.Builder<Orders>().setQuery(query, Orders.class).build();
                    FirestoreRecyclerAdapter<Orders, UsersOrdersViewHolder> fireAdapter = new FirestoreRecyclerAdapter<Orders, UsersOrdersViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull UsersOrdersViewHolder holder, int position, @NonNull Orders model) {
                            holder.orderDate.setText(model.getOrderDate());
                            holder.orderID.setText(model.getOrderID());
                            holder.orderStatus.setText(model.getOrderStatus());

                            CollectionReference productsRef = db.collection("Users").document(model.getUserID()).collection("Products");
                            productsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int productCount = queryDocumentSnapshots.size();
                                    holder.orderProductQty.setText(String.valueOf(productCount));
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public UsersOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_orders_layout, parent, false);
                            return new UsersOrdersViewHolder(view);
                        }
                    };

                    binding.newOrdersRecyclerView.setAdapter(fireAdapter);
                    fireAdapter.startListening();
                    dialog.dismiss();
                }
            }
        });
    }

    private class UsersOrdersViewHolder extends RecyclerView.ViewHolder {

        TextView orderProductQty, orderDate, orderID, orderStatus;

        public UsersOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            orderProductQty = itemView.findViewById(R.id.orderProductQty);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderID = itemView.findViewById(R.id.orderID);
            orderStatus = itemView.findViewById(R.id.orderStatus);

        }
    }

}