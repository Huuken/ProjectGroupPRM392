package com.example.freshfoldlaundrycare.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.freshfoldlaundrycare.databinding.FragmentFeedbackBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FeedbackFragment extends Fragment {

    FragmentFeedbackBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    ProgressDialog dialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference serviceRef, feedbackRef, usersRef;
    String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFeedbackBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        currentUserId = mAuth.getCurrentUser().getUid();

        usersRef = db.collection("Users");
        feedbackRef = db.collection("Feedback");

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            String profileUpdated = snapshot.getString("ProfileUpdated");
                            String address = snapshot.getString("Address");
                            String name = snapshot.getString("Name");
                            String phone = snapshot.getString("Phone");
                            String email = snapshot.getString("Email");
                            String city = snapshot.getString("City");
                            String delivery = snapshot.getString("DeliveryTime");
                            String pickup = snapshot.getString("PickupTime");
                            String pincode = snapshot.getString("Pincode");

                            String rating = String.valueOf(binding.ratingBar.getRating());
                            String feedback = binding.feedbackField.getEditText().getText().toString().trim();

                            if (feedback.isEmpty()){
                                Toast.makeText(getContext(), "Please add message...", Toast.LENGTH_SHORT).show();
                            } else {
                                loadingBar.setTitle("Please wait...");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();
                                HashMap feedbackMap = new HashMap();
                                feedbackMap.put("UserName",name);
                                feedbackMap.put("UserPhone",phone);
                                feedbackMap.put("UserEmail",email);
                                feedbackMap.put("UserAddress",address);
                                feedbackMap.put("UserPincode",pincode);
                                feedbackMap.put("UserCity",city);
                                feedbackMap.put("UserUID",currentUserId);
                                feedbackMap.put("UserRating",rating);
                                feedbackMap.put("UserFeedback",feedback);
                                feedbackRef.document(currentUserId).set(feedbackMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        } else {
                                            String msg = task.getException().getMessage();
                                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        return binding.getRoot();
    }
}