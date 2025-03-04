package com.example.freshfoldlaundrycare.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshfoldlaundrycare.MainActivity;
import com.example.freshfoldlaundrycare.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        usersRef = db.collection("Users");

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.registerName.getEditText().getText().toString().trim();
                String email = binding.registerEmail.getEditText().getText().toString().trim();
                String pwd = binding.registerPassword.getEditText().getText().toString().trim();
                String cnfPwd = binding.registerConfirmPassword.getEditText().getText().toString().trim();

                if (email.isEmpty() && pwd.isEmpty() && cnfPwd.isEmpty() && name.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Some data is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setMessage("please wait");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String userID = mAuth.getCurrentUser().getUid();

                            HashMap usersMap = new HashMap();
                            usersMap.put("Email", email);
                            usersMap.put("Password", pwd);
                            usersMap.put("Name", name);
                            usersMap.put("ProfileUpdated", "NO");
                            usersMap.put("Image", "default");
                            usersMap.put("UserID", userID);
                            usersRef.document(userID).set(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        loadingBar.dismiss();
                                    } else {
                                        String msg = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    public void goBack(View view) {
        finish();
    }
}