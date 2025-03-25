package com.example.freshfoldlaundrycare.admin;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshfoldlaundrycare.Modal.Orders;
import com.example.freshfoldlaundrycare.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewAllOrdersActivity extends AppCompatActivity {

    com.example.freshfoldlaundrycare.databinding.ActivityViewAllOrdersBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cartRef, usersRef, ordersRef;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.freshfoldlaundrycare.databinding.ActivityViewAllOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        cartRef = db.collection("Cart");
        ordersRef = db.collection("Orders");

        binding.newOrdersRecyclerView.setHasFixedSize(true);
        binding.newOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog.setMessage("please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        startListen();

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
                    Query query = ordersRef.orderBy("OrderDate", Query.Direction.ASCENDING);
                    FirestoreRecyclerOptions<Orders> options = new FirestoreRecyclerOptions.Builder<Orders>().setQuery(query, Orders.class).build();
                    FirestoreRecyclerAdapter<Orders, OrdersViewHolder> fireAdapter = new FirestoreRecyclerAdapter<Orders, OrdersViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull OrdersViewHolder holder, int position, @NonNull Orders model) {
                            holder.orderDate.setText(model.getOrderDate());
                            holder.orderID.setText(model.getOrderID());
                            holder.orderUserAddress.setText(model.getAddress());
                            holder.orderUserName.setText(model.getName());
                            holder.orderStatus.setText(model.getOrderStatus());

                            CollectionReference productsRef = db.collection("Orders").document(model.getUserID()).collection("Products");
                            productsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int productCount = queryDocumentSnapshots.size();
                                    holder.orderProductQty.setText(String.valueOf(productCount));
                                }
                            });

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Create a dialog builder
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllOrdersActivity.this);
                                    builder.setTitle("Choose Action");

                                    // Add "View Services" button
                                    builder.setPositiveButton("View Services", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), ViewServiceRequestedActivity.class);
                                            intent.putExtra("userID", model.getUserID());
                                            startActivity(intent);
                                        }
                                    });

                                    // Add "Update Status" button
                                    builder.setNegativeButton("Update Status", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ordersRef.document(model.getOrderID()).update("OrderStatus", "Completed");//.addOnCompleteListener(new OnCompleteListener<Void>() {
////                                                @Override
////                                                public void onComplete(@NonNull Task<Void> task) {
////                                                    if (task.isSuccessful()){
////                                                        ordersRef.document(model.getUserID()).delete();
////                                                    }
////                                                }
//                                            });
                                        }
                                    });

                                    // Create and show the dialog
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });

                        }

                        @NonNull
                        @Override
                        public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                            return new OrdersViewHolder(view);
                        }
                    };
                    binding.newOrdersRecyclerView.setAdapter(fireAdapter);
                    fireAdapter.startListening();
                    dialog.dismiss();
                }
            }
        });
    }

    private class OrdersViewHolder extends RecyclerView.ViewHolder {

        TextView orderProductQty, orderDate, orderUserAddress, orderUserName, orderID, orderStatus;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            orderProductQty = itemView.findViewById(R.id.orderProductQty);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderUserAddress = itemView.findViewById(R.id.orderUserAddress);
            orderUserName = itemView.findViewById(R.id.orderUserName);
            orderID = itemView.findViewById(R.id.orderID);
            orderStatus = itemView.findViewById(R.id.orderStatus);

        }
    }

}