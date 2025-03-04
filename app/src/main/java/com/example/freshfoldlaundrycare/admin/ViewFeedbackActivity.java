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
    ActivityViewFeedbackBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cartRef, usersRef, feedbackRef;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        cartRef = db.collection("Cart");
        feedbackRef = db.collection("Feedback");

        binding.newFeedbackRecyclerView.setHasFixedSize(true);
        binding.newFeedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog.setMessage("please wait");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        startListen();

    }

    private void startListen() {
        feedbackRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                if (snapshot.isEmpty()) {
                    /*binding.emptyProductLayout.setVisibility(View.VISIBLE);
                    binding.productAvailableLayout.setVisibility(View.GONE);*/
                    dialog.dismiss();
                } else {
                    /*binding.productAvailableLayout.setVisibility(View.VISIBLE);
                    binding.emptyProductLayout.setVisibility(View.GONE);*/
                    Query query = feedbackRef.orderBy("UserRating", Query.Direction.ASCENDING);
                    FirestoreRecyclerOptions<Feedback> options = new FirestoreRecyclerOptions.Builder<Feedback>().setQuery(query, Feedback.class).build();
                    FirestoreRecyclerAdapter<Feedback, FeedbackViewHolder> fireAdapter = new FirestoreRecyclerAdapter<Feedback, FeedbackViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position, @NonNull Feedback model) {
                            holder.userPhone.setText(model.getUserPhone());
                            holder.userEmail.setText(model.getUserEmail());
                            holder.userRating.setText(model.getUserRating());
                            holder.userName.setText(model.getUserName());
                            holder.feedbackMessage.setText(model.getUserFeedback());
                        }

                        @NonNull
                        @Override
                        public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_layout, parent, false);
                            return new FeedbackViewHolder(view);
                        }
                    };
                    binding.newFeedbackRecyclerView.setAdapter(fireAdapter);
                    fireAdapter.startListening();
                    dialog.dismiss();
                }
            }
        });
    }

    private class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView userPhone, userEmail, userRating, userName, feedbackMessage;
        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhone = itemView.findViewById(R.id.userPhone);
            userEmail = itemView.findViewById(R.id.userEmail);
            userRating = itemView.findViewById(R.id.userRating);
            userName = itemView.findViewById(R.id.userName);
            feedbackMessage = itemView.findViewById(R.id.feedbackMessage);

        }
    }

}