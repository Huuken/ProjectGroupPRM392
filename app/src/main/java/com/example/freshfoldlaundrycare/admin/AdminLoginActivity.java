package com.example.freshfoldlaundrycare.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    // View Binding for accessing UI elements
    com.example.freshfoldlaundrycare.databinding.ActivityAdminLoginBinding binding;

    // Firebase Authentication instance
    FirebaseAuth mAuth;

    // Progress dialog to show loading status
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = com.example.freshfoldlaundrycare.databinding.ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Progress Dialog
        loadingBar = new ProgressDialog(this);

        // Set click listener for login button
        binding.adminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieve email and password input by the admin
                String email = binding.adminEmail.getEditText().getText().toString().trim();
                String password = binding.adminPassword.getEditText().getText().toString().trim();

                // Show loading dialog
                loadingBar.setTitle("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                // Authenticate admin using Firebase Authentication
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If login is successful, navigate to AdminMainActivity
                            Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                            startActivity(intent);
                        } else {
                            // If login fails, show an error message
                            Toast.makeText(AdminLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        // Dismiss the loading dialog
                        loadingBar.dismiss();
                    }
                });
            }
        });
    }
}
