package com.example.freshfoldlaundrycare.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.freshfoldlaundrycare.EditProfileActivity;
import com.example.freshfoldlaundrycare.auth.LoginActivity;
import com.example.freshfoldlaundrycare.databinding.FragmentAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    ProgressDialog dialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference serviceRef, cartRef, usersRef;
    String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        currentUserId = mAuth.getCurrentUser().getUid();

        usersRef = db.collection("Users");

        binding.logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        displayUserData();

        binding.editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void displayUserData() {
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

                    binding.profileAddress.setText(address);
                    binding.profileName.setText(name);
                    binding.profilePhone.setText(phone);
                    binding.profileEmail.setText(email);
                    binding.profileCity.setText(city);
                    binding.profileDelivery.setText(delivery);
                    binding.profilePickup.setText(pickup);
                    binding.profilePincode.setText(pincode);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        displayUserData();
    }
}