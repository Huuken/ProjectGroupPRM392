package com.example.freshfoldlaundrycare.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freshfoldlaundrycare.R;
import com.example.freshfoldlaundrycare.databinding.ActivityAddServiceBinding;
import com.example.freshfoldlaundrycare.helper.UniqueIdGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddServiceActivity extends AppCompatActivity {

    ActivityAddServiceBinding binding; // View Binding instance
    ProgressDialog loadingBar; // Progress dialog for loading indication
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Firestore database instance
    CollectionReference serviceRef; // Reference to "Services" collection in Firestore
    private boolean isUpdate = false; // Flag to check if updating an existing service
    private String serviceID; // ID of the service being updated

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddServiceBinding.inflate(getLayoutInflater()); // Initialize View Binding
        setContentView(binding.getRoot()); // Set the content view

        loadingBar = new ProgressDialog(this); // Initialize ProgressDialog
        serviceRef = db.collection("Services"); // Get reference to "Services" collection

        // Setup Spinner with service types from resources
        ArrayAdapter<CharSequence> adapter4Category = ArrayAdapter.createFromResource(this, R.array.service_type, android.R.layout.simple_spinner_item);
        adapter4Category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.serviceTypeSpinner.setAdapter(adapter4Category);
        binding.serviceTypeSpinner.setOnItemSelectedListener(new TypeServiceSpinner());

        Intent intent = getIntent(); // Get intent data
        isUpdate = intent.getBooleanExtra("isUpdate", false); // Check if it is an update operation

        if (isUpdate) {
            // Populate fields with existing service data
            binding.serviceCloth.getEditText().setText(intent.getStringExtra("serviceCloth"));
            binding.servicePrice.getEditText().setText(intent.getStringExtra("servicePrice"));
            String serviceType = intent.getStringExtra("serviceType");
            serviceID = intent.getStringExtra("serviceID");

            // Set default selection for Spinner
            if (serviceType != null) {
                int spinnerPosition = adapter4Category.getPosition(serviceType);
                binding.serviceTypeSpinner.setSelection(spinnerPosition);
                binding.serviceTypeText.setText(serviceType);
            }
            setTitle("Update Service"); // Set activity title
            binding.addServiceButton.setText("Update"); // Change button text
        } else {
            setTitle("Add New Service"); // Set title for new service
            binding.addServiceButton.setText("Add"); // Set button text for new service
        }

        binding.addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String serviceCloth = binding.serviceCloth.getEditText().getText().toString().trim();
                String servicePrice = binding.servicePrice.getEditText().getText().toString().trim();
                String serviceType = binding.serviceTypeText.getText().toString().trim();

                // Validate inputs
                if (serviceCloth.isEmpty() || servicePrice.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please add data!", Toast.LENGTH_SHORT).show();
                } else if (serviceType.equals("NA")) {
                    Toast.makeText(getApplicationContext(), "Please select service type!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setMessage("Please wait..."); // Show loading message
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    HashMap<String, Object> usersMap = new HashMap<>(); // Create data map
                    usersMap.put("ServiceCloth", serviceCloth);
                    usersMap.put("ServicePrice", servicePrice);
                    usersMap.put("ServiceType", serviceType);
                    usersMap.put("ServiceID", serviceID);

                    if (isUpdate && serviceID != null) {
                        // Update existing service
                        usersMap.put("ServiceID", serviceID);
                        serviceRef.document(serviceID).set(usersMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Service Updated!", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                            Intent intent = new Intent(AddServiceActivity.this, ViewServicesActivity.class);
                                            startActivity(intent);
                                            finish(); // Close activity after update
                                        } else {
                                            String msg = task.getException().getMessage();
                                            Toast.makeText(getApplicationContext(), "Error: " + msg, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                    } else {
                        // Add new service
                        String newServiceID = UniqueIdGenerator.generateServiceID();
                        usersMap.put("ServiceID", newServiceID);
                        serviceRef.document(newServiceID).set(usersMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Service Added!", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();

                                            // Reset input fields
                                            binding.serviceCloth.getEditText().setText("");
                                            binding.servicePrice.getEditText().setText("");
                                            binding.serviceTypeSpinner.setSelection(0); // Reset Spinner to default
                                            binding.serviceCloth.requestFocus();

                                            Intent intent = new Intent(AddServiceActivity.this, AdminMainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            String msg = task.getException().getMessage();
                                            Toast.makeText(getApplicationContext(), "Error: " + msg, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    private class TypeServiceSpinner implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Update selected service type text
            String itemSpinner = parent.getItemAtPosition(position).toString();
            binding.serviceTypeText.setText(itemSpinner);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Default to "NA" if nothing is selected
            binding.serviceTypeText.setText("NA");
        }
    }
}
