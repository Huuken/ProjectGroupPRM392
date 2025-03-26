package com.example.freshfoldlaundrycare.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshfoldlaundrycare.Modal.Feedback;
import com.example.freshfoldlaundrycare.R;
import com.example.freshfoldlaundrycare.databinding.ActivityViewFeedbackBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewFeedbackActivity extends AppCompatActivity {
    // Declare binding for XML layout
    ActivityViewFeedbackBinding binding;

    // Firebase authentication instance
    FirebaseAuth mAuth;

    // Firebase Firestore database instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Firestore collection references
    CollectionReference cartRef, usersRef, feedbackRef;

    // ProgressDialog to show loading indicator
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityViewFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize progress dialog
        dialog = new ProgressDialog(this);

        // Initialize Firestore collection references
        cartRef = db.collection("Cart");
        feedbackRef = db.collection("Feedback");

        // Set up RecyclerView with fixed size and linear layout manager
        binding.newFeedbackRecyclerView.setHasFixedSize(true);
        binding.newFeedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configure and display progress dialog
        dialog.setMessage("please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        // Start listening for feedback data
        startListen();
    }

    private void startListen() {
        // Fetch all documents from "Feedback" collection
        feedbackRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                // If no feedback found, hide feedback list
                if (snapshot.isEmpty()) {
                    /*binding.emptyProductLayout.setVisibility(View.VISIBLE);
                    binding.productAvailableLayout.setVisibility(View.GONE);*/
                    dialog.dismiss();
                } else {
                    /*binding.productAvailableLayout.setVisibility(View.VISIBLE);
                    binding.emptyProductLayout.setVisibility(View.GONE);*/

                    // Query to order feedback by "UserRating" in ascending order
                    Query query = feedbackRef.orderBy("UserRating", Query.Direction.ASCENDING);

                    // Configure FirestoreRecyclerOptions with the query and Feedback model class
                    FirestoreRecyclerOptions<Feedback> options = new FirestoreRecyclerOptions.Builder<Feedback>()
                            .setQuery(query, Feedback.class)
                            .build();

                    // Set up FirestoreRecyclerAdapter to bind data to RecyclerView
                    FirestoreRecyclerAdapter<Feedback, FeedbackViewHolder> fireAdapter =
                            new FirestoreRecyclerAdapter<Feedback, FeedbackViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position, @NonNull Feedback model) {
                                    // Bind feedback data to ViewHolder fields
                                    holder.userPhone.setText(model.getUserPhone());
                                    holder.userEmail.setText(model.getUserEmail());
                                    holder.userRating.setText(model.getUserRating());
                                    holder.userName.setText(model.getUserName());
                                    holder.feedbackMessage.setText(model.getUserFeedback());
                                }

                                @NonNull
                                @Override
                                public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    // Inflate the feedback layout for each list item
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_layout, parent, false);
                                    return new FeedbackViewHolder(view);
                                }
                            };

                    // Attach adapter to RecyclerView
                    binding.newFeedbackRecyclerView.setAdapter(fireAdapter);

                    // Start listening for real-time updates
                    fireAdapter.startListening();

                    // Dismiss progress dialog once data is loaded
                    dialog.dismiss();
                }
            }
        });
    }

    // ViewHolder class for feedback items in RecyclerView
    private class FeedbackViewHolder extends RecyclerView.ViewHolder {
        // Declare TextView fields for displaying feedback details
        TextView userPhone, userEmail, userRating, userName, feedbackMessage;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize TextView elements with their corresponding IDs from XML layout
            userPhone = itemView.findViewById(R.id.userPhone);
            userEmail = itemView.findViewById(R.id.userEmail);
            userRating = itemView.findViewById(R.id.userRating);
            userName = itemView.findViewById(R.id.userName);
            feedbackMessage = itemView.findViewById(R.id.feedbackMessage);
        }
    }
}
