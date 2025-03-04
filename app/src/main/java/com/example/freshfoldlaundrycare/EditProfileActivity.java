package com.example.freshfoldlaundrycare;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshfoldlaundrycare.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        userID = mAuth.getCurrentUser().getUid();
        usersRef = db.collection("Users");

        displayUserData();

        binding.selectPickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current time
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                // Create a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditProfileActivity.this,
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
                                binding.editPickupTime.getEditText().setText(formattedTime);
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
                        EditProfileActivity.this,
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
                                binding.editDeliveryTime.getEditText().setText(formattedTime);
                            }
                        },
                        hour, minute, false); // false for 24-hour format

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        binding.editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editName.getEditText().getText().toString().trim();
                String phone = binding.editPhone.getEditText().getText().toString().trim();
                String address = binding.editAddress.getEditText().getText().toString().trim();
                String city = binding.editCity.getEditText().getText().toString().trim();
                String pincode = binding.editPincode.getEditText().getText().toString().trim();
                String pickupTime = binding.editPickupTime.getEditText().getText().toString().trim();
                String deliveryTime = binding.editDeliveryTime.getEditText().getText().toString().trim();

                if (phone.isEmpty() || address.isEmpty() || pickupTime.isEmpty() || deliveryTime.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Some data is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setMessage("please wait");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    HashMap usersMap = new HashMap();
                    usersMap.put("Phone", phone);
                    usersMap.put("Name", name);
                    usersMap.put("Address", address);
                    usersMap.put("City", city);
                    usersMap.put("Pincode", pincode);
                    usersMap.put("PickupTime", pickupTime);
                    usersMap.put("DeliveryTime", deliveryTime);
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

    private void displayUserData() {
        usersRef.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                    binding.editAddress.getEditText().setText(address);
                    binding.editName.getEditText().setText(name);
                    binding.editPhone.getEditText().setText(phone);
                    binding.editCity.getEditText().setText(city);
                    binding.editDeliveryTime.getEditText().setText(delivery);
                    binding.editPickupTime.getEditText().setText(pickup);
                    binding.editPincode.getEditText().setText(pincode);
                }
            }
        });
    }
}