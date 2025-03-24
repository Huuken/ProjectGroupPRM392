package com.example.freshfoldlaundrycare.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freshfoldlaundrycare.Modal.Services;
import com.example.freshfoldlaundrycare.R;
import com.example.freshfoldlaundrycare.databinding.ActivityViewServicesBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewServicesActivity extends AppCompatActivity {
    ActivityViewServicesBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference servicesRef;
    ProgressDialog dialog;
    FirebaseAuth mAuth;
    private FirestoreRecyclerAdapter<Services, ServiceViewHolder> fireAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewServicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        servicesRef = db.collection("Services");
        mAuth = FirebaseAuth.getInstance();

        binding.servicesRecyclerView.setHasFixedSize(true);
        binding.servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        startListen();
    }

    private void startListen() {
        servicesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {
                if (snapshot.isEmpty()) {
                    Toast.makeText(ViewServicesActivity.this, "No services found", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    // Sắp xếp theo ServiceCloth
                    Query query = servicesRef.orderBy("ServiceCloth", Query.Direction.ASCENDING);
                    FirestoreRecyclerOptions<Services> options = new FirestoreRecyclerOptions.Builder<Services>()
                            .setQuery(query, Services.class)
                            .build();

                    fireAdapter = new FirestoreRecyclerAdapter<Services, ServiceViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ServiceViewHolder holder, int position, @NonNull Services model) {
                            // Hiển thị thông tin dịch vụ
                            holder.serviceCloth.setText(model.getServiceCloth());
                            holder.servicePrice.setText("Rs. " + model.getServicePrice());

                            // Hiển thị biểu tượng dựa trên ServiceType
                            if (model.getServiceType() != null) {
                                if (model.getServiceType().equals("Iron")) {
                                    Glide.with(ViewServicesActivity.this)
                                            .load(R.drawable.ic_iron)
                                            .into(holder.serviceIcon);
                                } else if (model.getServiceType().equals("Wash Iron")) {
                                    Glide.with(ViewServicesActivity.this)
                                            .load(R.drawable.ic_washing_machine)
                                            .into(holder.serviceIcon);
                                } else {
                                    Glide.with(ViewServicesActivity.this)
                                            .load(R.drawable.ic_shirt)
                                            .into(holder.serviceIcon);
                                }
                            } else {
                                Glide.with(ViewServicesActivity.this)
                                        .load(R.drawable.ic_shirt)
                                        .into(holder.serviceIcon);
                            }

                            // Hiển thị nút xóa (nếu cần)
                            holder.removeServiceBtn.setVisibility(View.VISIBLE);

                            // Sự kiện cho nút chỉnh sửa (trước đây là thêm): Chuyển sang AddServiceActivity với dữ liệu
                            holder.updateServiceIcon.setOnClickListener(v -> {
                                Intent intent = new Intent(ViewServicesActivity.this, AddServiceActivity.class);
                                intent.putExtra("serviceCloth", model.getServiceCloth());
                                intent.putExtra("servicePrice", model.getServicePrice());
                                intent.putExtra("serviceType", model.getServiceType());
                                intent.putExtra("serviceID", model.getServiceID());
                                intent.putExtra("isUpdate", true); // Chuyển sang chế độ cập nhật
                                startActivity(intent);
                            });

                            // Sự kiện cho nút xóa dịch vụ
                            holder.removeServiceBtn.setOnClickListener(v -> {
                                if (model.getServiceID() == null) {
                                    Toast.makeText(ViewServicesActivity.this, "Service ID is missing", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                servicesRef.document(model.getServiceID()).delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(ViewServicesActivity.this, "Service removed: " + model.getServiceCloth(), Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(ViewServicesActivity.this, "Error removing service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            });

                            // Sự kiện khi nhấp vào item: Chuyển sang AddServiceActivity để cập nhật
                            holder.itemView.setOnClickListener(v -> {
                                Intent intent = new Intent(ViewServicesActivity.this, AddServiceActivity.class);
                                intent.putExtra("serviceCloth", model.getServiceCloth());
                                intent.putExtra("servicePrice", model.getServicePrice());
                                intent.putExtra("serviceType", model.getServiceType());
                                intent.putExtra("serviceID", model.getServiceID());
                                intent.putExtra("isUpdate", true); // Cờ để phân biệt thêm mới hay cập nhật
                                startActivity(intent);
                            });
                        }

                        @NonNull
                        @Override
                        public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item_layout, parent, false);
                            return new ServiceViewHolder(view);
                        }
                    };
                    binding.servicesRecyclerView.setAdapter(fireAdapter);
                    fireAdapter.startListening();
                    dialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewServicesActivity.this, "Error fetching services: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceCloth, servicePrice;
        ImageView serviceIcon, updateServiceIcon, removeServiceBtn;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceCloth = itemView.findViewById(R.id.serviceCloth);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceIcon = itemView.findViewById(R.id.serviceIcon);
            updateServiceIcon = itemView.findViewById(R.id.updateServiceIcon);
            removeServiceBtn = itemView.findViewById(R.id.removeServiceBtn);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fireAdapter != null) {
            fireAdapter.stopListening();
        }
    }
}
