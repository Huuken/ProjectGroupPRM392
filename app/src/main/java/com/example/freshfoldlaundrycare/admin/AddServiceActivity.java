package com.example.freshfoldlaundrycare.admin;

import android.app.ProgressDialog;
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

    ActivityAddServiceBinding binding;
    ProgressDialog loadingBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference serviceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Khởi tạo View Binding truy cập trực tiếp vào các thành phần UI trong XML layout (activity_add_service.xml).
        binding = ActivityAddServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Khởi tạo Firestore
        loadingBar = new ProgressDialog(this);
        serviceRef = db.collection("Services");

        //Thiết lập Spinner (Dropdown menu)
        ArrayAdapter<CharSequence> adapter4Category = ArrayAdapter.createFromResource(this, R.array.service_type, android.R.layout.simple_spinner_item);
        adapter4Category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.serviceTypeSpinner.setAdapter(adapter4Category);
        binding.serviceTypeSpinner.setOnItemSelectedListener(new TypeServiceSpinner());

        binding.addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các trường nhập liệu
                String serviceCloth = binding.serviceCloth.getEditText().getText().toString().trim();
                String servicePrice = binding.servicePrice.getEditText().getText().toString().trim();
                String serviceType = binding.serviceTypeText.getText().toString().trim();
                String serviceID = UniqueIdGenerator.generateServiceID();

                // Kiểm tra dữ liệu đầu vào
                if (serviceCloth.isEmpty() || servicePrice.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please add data!", Toast.LENGTH_SHORT).show();
                } else if (serviceType.equals("NA")) {
                    Toast.makeText(getApplicationContext(), "Please select service type!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setMessage("please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    HashMap usersMap = new HashMap();
                    usersMap.put("ServiceCloth", serviceCloth);
                    usersMap.put("ServicePrice", servicePrice);
                    usersMap.put("ServiceType", serviceType);
                    usersMap.put("ServiceID", serviceID);
                    // Lưu dữ liệu vào Firestore
                    serviceRef.document(serviceID).set(usersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Service Added!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                binding.serviceCloth.getEditText().setText("");
                                binding.servicePrice.getEditText().setText("");
                                binding.serviceCloth.requestFocus();
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

    private class TypeServiceSpinner implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String itemSpinner = parent.getItemAtPosition(position).toString();
            binding.serviceTypeText.setText(itemSpinner);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}