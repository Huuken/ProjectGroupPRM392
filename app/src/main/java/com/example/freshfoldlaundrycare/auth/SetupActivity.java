package com.example.freshfoldlaundrycare.auth;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshfoldlaundrycare.MainActivity;
import com.example.freshfoldlaundrycare.databinding.ActivitySetupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SetupActivity extends AppCompatActivity {

    ActivitySetupBinding binding;
    ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        usersRef = db.collection("Users");

        binding.selectPickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current time
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        SetupActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Determine AM or PM
                                String amPm;
                                if (hourOfDay < 12) {
                                    amPm = "AM";
                                } else {
                                    amPm = "PM";
                                    hourOfDay -= 12; // Convert 24-hour format to 12-hour format
                                }

                                // Update the TextView with the selected time
                                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm);
                                binding.setupPickupTime.getEditText().setText(formattedTime);
                            }
                        },
                        hour, minute, false); // false for 24-hour format

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        binding.selectDeliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current time
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        SetupActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Determine AM or PM
                                String amPm;
                                if (hourOfDay < 12) {
                                    amPm = "AM";
                                } else {
                                    amPm = "PM";
                                    hourOfDay -= 12; // Convert 24-hour format to 12-hour format
                                }

                                // Update the TextView with the selected time
                                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm);
                                binding.setupDeliveryTime.getEditText().setText(formattedTime);
                            }
                        },
                        hour, minute, false); // false for 24-hour format

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        binding.completeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.setupPhone.getEditText().getText().toString().trim();
                String address = binding.setupAddress.getEditText().getText().toString().trim();
                String city = binding.setupCity.getEditText().getText().toString().trim();
                String pincode = binding.setupPincode.getEditText().getText().toString().trim();
                String pickupTime = binding.setupPickupTime.getEditText().getText().toString().trim();
                String deliveryTime = binding.setupDeliveryTime.getEditText().getText().toString().trim();

                if (phone.isEmpty() || address.isEmpty() || pickupTime.isEmpty() || deliveryTime.isEmpty()) {
                    Toast.makeText(SetupActivity.this, "Some data is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setMessage("please wait");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    HashMap usersMap = new HashMap();
                    usersMap.put("Phone", phone);
                    usersMap.put("Address", address);
                    usersMap.put("City", city);
                    usersMap.put("Pincode", pincode);
                    usersMap.put("PickupTime", pickupTime);
                    usersMap.put("DeliveryTime", deliveryTime);
                    usersMap.put("ProfileUpdated", "YES");
                    usersRef.document(userID).update(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Account Updated successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                                loadingBar.dismiss();
                            } else {
                                String msg = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }
}