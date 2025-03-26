package com.example.freshfoldlaundrycare.fragments; // Defines the package name for this class, organizing it under the "fragments" subpackage.

import android.app.AlertDialog; // Imports AlertDialog class (unused in this code but imported).
import android.app.ProgressDialog; // Imports ProgressDialog class to display a loading dialog.
import android.content.DialogInterface; // Imports DialogInterface class (unused in this code but imported).
import android.content.Intent; // Imports Intent class to navigate between activities.
import android.os.Bundle; // Imports Bundle class to manage fragment state.
import android.view.LayoutInflater; // Imports LayoutInflater class to inflate layouts.
import android.view.View; // Imports View class to handle UI components.
import android.view.ViewGroup; // Imports ViewGroup class to represent the container for the fragment's view.
import android.widget.TextView; // Imports TextView class for displaying text.

import androidx.annotation.NonNull; // Imports NonNull annotation to indicate non-null parameters or return values.
import androidx.core.content.ContextCompat; // Imports ContextCompat to access resources like colors in a compatible way.
import androidx.fragment.app.Fragment; // Imports Fragment class as the base class for this fragment.
import androidx.recyclerview.widget.LinearLayoutManager; // Imports LinearLayoutManager for RecyclerView layout management.
import androidx.recyclerview.widget.RecyclerView; // Imports RecyclerView class for displaying a scrollable list.

import com.example.freshfoldlaundrycare.Modal.Orders; // Imports Orders model class to represent order data.
import com.example.freshfoldlaundrycare.R; // Imports R class for resource references.
import com.example.freshfoldlaundrycare.ViewOrderDetailActivity; // Imports ViewOrderDetailActivity for navigation.
import com.example.freshfoldlaundrycare.admin.ViewServiceRequestedActivity; // Imports ViewServiceRequestedActivity (unused in this code but imported).
import com.example.freshfoldlaundrycare.databinding.FragmentMyOrdersBinding; // Imports binding class for the my orders fragment layout.
import com.firebase.ui.firestore.FirestoreRecyclerAdapter; // Imports FirestoreRecyclerAdapter for binding Firestore data to RecyclerView.
import com.firebase.ui.firestore.FirestoreRecyclerOptions; // Imports FirestoreRecyclerOptions for configuring Firestore queries.
import com.google.android.gms.tasks.OnSuccessListener; // Imports listener for successful task completion in Firebase.
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth class for authentication.
import com.google.firebase.firestore.CollectionReference; // Imports CollectionReference for Firestore collections.
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore for Firestore database operations.
import com.google.firebase.firestore.Query; // Imports Query class for Firestore queries.
import com.google.firebase.firestore.QuerySnapshot; // Imports QuerySnapshot for query result sets.

public class MyOrdersFragment extends Fragment { // Declares the MyOrdersFragment class, extending Fragment.

    FragmentMyOrdersBinding binding; // Declares a binding object to access UI elements.
    FirebaseAuth mAuth; // Declares FirebaseAuth for authentication.
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initializes Firestore instance.
    CollectionReference cartRef, usersRef, ordersRef; // Declares Firestore collection references (usersRef is unused).
    ProgressDialog dialog; // Declares a ProgressDialog for loading states.
    String userID; // Declares a variable for the current user's ID.

    @Override // Overrides the superclass method.
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // Defines method to create the fragment's view.
        binding = FragmentMyOrdersBinding.inflate(getLayoutInflater(), container, false); // Inflates the my orders fragment layout.

        mAuth = FirebaseAuth.getInstance(); // Initializes FirebaseAuth.
        dialog = new ProgressDialog(getContext()); // Initializes ProgressDialog with fragment context.
        cartRef = db.collection("Cart"); // References the "Cart" collection (unused in this code).
        ordersRef = db.collection("Orders"); // References the "Orders" collection.
        userID = mAuth.getCurrentUser().getUid(); // Gets the current user's ID.

        binding.newOrdersRecyclerView.setHasFixedSize(true); // Optimizes RecyclerView performance with a fixed size.
        binding.newOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Sets a vertical LinearLayoutManager for RecyclerView.

        dialog.setMessage("please wait"); // Sets the ProgressDialog message.
        dialog.setCanceledOnTouchOutside(false); // Prevents dialog dismissal on outside touch.
        dialog.show(); // Shows the ProgressDialog.
        startListen(); // Calls method to fetch and display orders.

        return binding.getRoot(); // Returns the root view of the layout.
    }

    private void startListen() { // Defines method to fetch and display orders in RecyclerView.
        ordersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() { // Fetches all documents from "Orders".
            @Override
            public void onSuccess(QuerySnapshot snapshot) { // Called when the query succeeds.
                if (snapshot.isEmpty()) { // Checks if the result set is empty.
                    binding.emptyOrderLayout.setVisibility(View.VISIBLE); // Shows the empty order layout.
                    binding.availableOrderLayout.setVisibility(View.GONE); // Hides the available order layout.
                    dialog.dismiss(); // Dismisses the ProgressDialog.
                } else { // Executes if there are results.
                    binding.availableOrderLayout.setVisibility(View.VISIBLE); // Shows the available order layout.
                    binding.emptyOrderLayout.setVisibility(View.GONE); // Hides the empty order layout.
                    Query query = ordersRef.whereEqualTo("UserID", userID).orderBy("OrderDate", Query.Direction.ASCENDING); // Creates a query to filter orders by userID and sort by "OrderDate".
                    FirestoreRecyclerOptions<Orders> options = new FirestoreRecyclerOptions.Builder<Orders>().setQuery(query, Orders.class).build(); // Configures Firestore options.
                    FirestoreRecyclerAdapter<Orders, UsersOrdersViewHolder> fireAdapter = new FirestoreRecyclerAdapter<Orders, UsersOrdersViewHolder>(options) { // Creates a Firestore adapter.
                        @Override
                        protected void onBindViewHolder(@NonNull UsersOrdersViewHolder holder, int position, @NonNull Orders model) { // Binds data to RecyclerView items.
                            holder.orderDate.setText(model.getOrderDate()); // Sets the order date.
                            holder.orderID.setText(model.getOrderID()); // Sets the order ID.

                            holder.orderID.setOnClickListener(v -> { // Sets click listener for the order ID TextView.
                                Intent intent = new Intent(holder.itemView.getContext(), ViewOrderDetailActivity.class); // Creates intent for ViewOrderDetailActivity.
                                intent.putExtra("ORDER_ID", model.getOrderID()); // Adds the order ID as an extra.
                                holder.itemView.getContext().startActivity(intent); // Starts the activity.
                            });

                            holder.orderStatus.setText(model.getOrderStatus()); // Sets the order status.
                            if (model.getOrderStatus().equals("Completed")) { // Checks if the status is "Completed".
                                holder.orderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green)); // Sets text color to green.
                            } else { // If status is not "Completed".
                                holder.orderStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red)); // Sets text color to red.
                            }

                            CollectionReference productsRef = db.collection("Users").document(model.getUserID()).collection("Products"); // References the "Products" subcollection for the user.
                            productsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() { // Fetches the products in the order.
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) { // Called when the query succeeds.
                                    int productCount = queryDocumentSnapshots.size(); // Gets the number of products.
                                    holder.orderProductQty.setText(String.valueOf(productCount)); // Sets the product count.
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public UsersOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Creates a new ViewHolder.
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_orders_layout, parent, false); // Inflates the item layout.
                            return new UsersOrdersViewHolder(view); // Returns a new UsersOrdersViewHolder.
                        }
                    };

                    binding.newOrdersRecyclerView.setAdapter(fireAdapter); // Sets the adapter for RecyclerView.
                    fireAdapter.startListening(); // Starts real-time Firestore updates.
                    dialog.dismiss(); // Dismisses the ProgressDialog.
                }
            }
        });
    }

    private class UsersOrdersViewHolder extends RecyclerView.ViewHolder { // Defines ViewHolder class for RecyclerView items.
        TextView orderProductQty, orderDate, orderID, orderStatus; // Declares TextViews for order details.

        public UsersOrdersViewHolder(@NonNull View itemView) { // Constructor for ViewHolder.
            super(itemView); // Calls superclass constructor.
            orderProductQty = itemView.findViewById(R.id.orderProductQty); // Finds product quantity TextView.
            orderDate = itemView.findViewById(R.id.orderDate); // Finds order date TextView.
            orderID = itemView.findViewById(R.id.orderID); // Finds order ID TextView.
            orderStatus = itemView.findViewById(R.id.orderStatus); // Finds order status TextView.
        }
    }
}