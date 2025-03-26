package com.example.freshfoldlaundrycare; // Defines the package name for this class

import android.app.ProgressDialog; // Imports ProgressDialog for displaying loading indicators
import android.os.Bundle; // Imports Bundle for passing data between activities
import android.view.LayoutInflater; // Imports LayoutInflater for inflating layout resources
import android.view.View; // Imports View for handling UI components
import android.view.ViewGroup; // Imports ViewGroup for RecyclerView’s parent views
import android.widget.ImageView; // Imports ImageView for displaying images
import android.widget.TextView; // Imports TextView for displaying text
import android.widget.Toast; // Imports Toast for showing short messages

import androidx.annotation.NonNull; // Imports NonNull annotation for null safety
import androidx.appcompat.app.AppCompatActivity; // Imports AppCompatActivity as the base class for this activity
import androidx.cardview.widget.CardView; // Imports CardView for card-based UI elements
import androidx.recyclerview.widget.LinearLayoutManager; // Imports LinearLayoutManager for RecyclerView layout
import androidx.recyclerview.widget.RecyclerView; // Imports RecyclerView for displaying lists

import com.bumptech.glide.Glide; // Imports Glide for loading images efficiently
import com.example.freshfoldlaundrycare.Modal.Services; // Imports the Services model class
import com.example.freshfoldlaundrycare.databinding.ActivityServiceListsBinding; // Imports the binding class for this activity’s layout
import com.firebase.ui.firestore.FirestoreRecyclerAdapter; // Imports FirestoreRecyclerAdapter for Firestore-RecyclerView integration
import com.firebase.ui.firestore.FirestoreRecyclerOptions; // Imports FirestoreRecyclerOptions for configuring Firestore queries
import com.google.android.gms.tasks.OnCompleteListener; // Imports OnCompleteListener for Firebase task completion
import com.google.android.gms.tasks.OnSuccessListener; // Imports OnSuccessListener for successful Firebase task results
import com.google.android.gms.tasks.Task; // Imports Task for handling Firebase asynchronous operations
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth for user authentication
import com.google.firebase.firestore.CollectionReference; // Imports CollectionReference for Firestore collection operations
import com.google.firebase.firestore.DocumentSnapshot; // Imports DocumentSnapshot for retrieving Firestore document data
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore for Firestore database operations
import com.google.firebase.firestore.Query; // Imports Query for building Firestore queries
import com.google.firebase.firestore.QuerySnapshot; // Imports QuerySnapshot for handling Firestore query result sets

import java.util.HashMap; // Imports HashMap for storing key-value pairs

public class ServiceListsActivity extends AppCompatActivity { // Declares the ServiceListsActivity class, extending AppCompatActivity

    String serviceTitle; // Declares a string to store the service title passed via Intent
    ActivityServiceListsBinding binding; // Declares a binding object for accessing UI elements
    ProgressDialog dialog; // Declares a ProgressDialog for showing a loading indicator
    FirebaseAuth mAuth; // Declares a FirebaseAuth object for authentication
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initializes Firestore database instance
    CollectionReference serviceRef, cartRef; // Declares CollectionReference objects for "Services" and "Cart" collections
    String currentUserId; // Declares a string to store the current user’s ID

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Overrides onCreate method, called when the activity is created
        super.onCreate(savedInstanceState); // Calls the parent class’s onCreate method
        binding = ActivityServiceListsBinding.inflate(getLayoutInflater()); // Inflates the layout using View Binding
        setContentView(binding.getRoot()); // Sets the content view to the root of the binding layout

        serviceTitle = getIntent().getStringExtra("serviceTitle"); // Retrieves the service title from Intent extras
        binding.serviceTitle.setText(serviceTitle + " Service"); // Sets the title in the UI with " Service" appended

        mAuth = FirebaseAuth.getInstance(); // Initializes FirebaseAuth instance
        dialog = new ProgressDialog(this); // Initializes the ProgressDialog with the current context
        currentUserId = mAuth.getCurrentUser().getUid(); // Gets the current user’s unique ID from FirebaseAuth
        serviceRef = db.collection("Services"); // References the "Services" collection in Firestore
        cartRef = db.collection("Cart"); // References the "Cart" collection in Firestore

        binding.serviceListRecView.setHasFixedSize(true); // Optimizes RecyclerView by setting a fixed size
        binding.serviceListRecView.setLayoutManager(new LinearLayoutManager(this)); // Sets a LinearLayoutManager for the RecyclerView

        dialog.setMessage("please wait"); // Sets the message for the loading dialog
        dialog.setCanceledOnTouchOutside(false); // Prevents dismissing the dialog by touching outside
        dialog.show(); // Shows the loading dialog
        startListen(); // Calls the method to start listening for Firestore data
    }

    private void startListen() { // Defines a method to fetch and display services from Firestore
        serviceRef.whereEqualTo("ServiceType", serviceTitle).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() { // Queries services by ServiceType
            @Override
            public void onSuccess(QuerySnapshot snapshot) { // Callback for successful query
                if (snapshot.isEmpty()) { // Checks if no services are found
                    dialog.dismiss(); // Dismisses the loading dialog
                } else { // If services are found
                    Query query = serviceRef.whereEqualTo("ServiceType", serviceTitle).orderBy("ServiceCloth", Query.Direction.ASCENDING); // Builds a query to filter and sort services
                    FirestoreRecyclerOptions<Services> options = new FirestoreRecyclerOptions.Builder<Services>().setQuery(query, Services.class).build(); // Configures FirestoreRecyclerOptions with the query
                    FirestoreRecyclerAdapter<Services, ServicesHolder> fireAdapter = new FirestoreRecyclerAdapter<Services, ServicesHolder>(options) { // Creates a FirestoreRecyclerAdapter
                        @Override
                        protected void onBindViewHolder(@NonNull ServicesHolder holder, int position, @NonNull Services model) { // Binds data to each RecyclerView item
                            holder.serviceCloth.setText(model.getServiceCloth()); // Sets the service cloth name in the TextView
                            holder.servicePrice.setText("Rs. " + model.getServicePrice() + " /-"); // Sets the service price with currency formatting

                            if (serviceTitle.equals("Iron")) { // Checks if the service type is "Iron"
                                Glide.with(getApplicationContext()).load(R.drawable.item_blanket).into(holder.serviceIcon); // Loads a blanket icon using Glide
                            } else if (serviceTitle.equals("Wash Iron")) { // Checks if the service type is "Wash Iron"
                                Glide.with(getApplicationContext()).load(R.drawable.ic_washing_machine).into(holder.serviceIcon); // Loads a washing machine icon
                            } else { // For other service types
                                Glide.with(getApplicationContext()).load(R.drawable.ic_shirt).into(holder.serviceIcon); // Loads a shirt icon
                            }

                            cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() { // Checks if the service is in the cart
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) { // Callback for successful document fetch
                                    if (documentSnapshot.exists()) { // If the service is in the cart
                                        holder.addCard.setVisibility(View.GONE); // Hides the "Add" card
                                        holder.removeCard.setVisibility(View.VISIBLE); // Shows the "Remove" card
                                    } else { // If the service is not in the cart
                                        holder.addCard.setVisibility(View.VISIBLE); // Shows the "Add" card
                                        holder.removeCard.setVisibility(View.GONE); // Hides the "Remove" card
                                    }
                                }
                            });

                            holder.serviceIcon.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the service icon
                                @Override
                                public void onClick(View v) { // Defines the action when the icon is clicked
                                    cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() { // Checks cart status again
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) { // Callback for successful document fetch
                                            if (documentSnapshot.exists()) { // If the service is already in the cart
                                                holder.addCard.setVisibility(View.GONE); // Hides the "Add" card
                                                holder.removeCard.setVisibility(View.VISIBLE); // Shows the "Remove" card
                                            } else { // If the service is not in the cart
                                                dialog.setMessage("please wait..."); // Sets the loading dialog message
                                                dialog.setCanceledOnTouchOutside(false); // Prevents dismissing the dialog
                                                dialog.show(); // Shows the loading dialog

                                                String price = model.getServicePrice(); // Gets the service price
                                                int quantity = 1; // Sets the initial quantity to 1
                                                int total = Integer.parseInt(price) * quantity; // Calculates the total price

                                                HashMap<String, Object> cartItem = new HashMap<>(); // Creates a HashMap for cart item data
                                                cartItem.put("UserId", currentUserId); // Adds the user ID
                                                cartItem.put("ProductId", model.getServiceID()); // Adds the service ID
                                                cartItem.put("ProductName", model.getServiceCloth()); // Adds the service cloth name
                                                cartItem.put("ProductPrice", model.getServicePrice()); // Adds the service price
                                                cartItem.put("Category", model.getServiceType()); // Adds the service type
                                                cartItem.put("Quantity", 1); // Adds the quantity
                                                cartItem.put("TotalPrice", String.valueOf(total)); // Adds the total price as a string

                                                cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).set(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() { // Adds the item to the cart
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) { // Callback for task completion
                                                        if (task.isSuccessful()) { // If the addition is successful
                                                            dialog.dismiss(); // Dismisses the loading dialog
                                                            holder.addCard.setVisibility(View.GONE); // Hides the "Add" card
                                                            holder.removeCard.setVisibility(View.VISIBLE); // Shows the "Remove" card
                                                            Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show(); // Shows a success message
                                                        } else { // If the addition fails
                                                            String msg = task.getException().getMessage(); // Gets the error message
                                                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show(); // Shows the error message
                                                            dialog.dismiss(); // Dismisses the loading dialog
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });



                            holder.itemView.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the entire item view
                                @Override
                                public void onClick(View v) {
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Creates a new ViewHolder
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item_layout, parent, false); // Inflates the item layout
                            return new ServicesHolder(view); // Returns a new ServicesHolder instance
                        }
                    };
                    binding.serviceListRecView.setAdapter(fireAdapter); // Sets the adapter for the RecyclerView
                    fireAdapter.startListening(); // Starts listening for Firestore data changes
                    dialog.dismiss(); // Dismisses the loading dialog
                }
            }
        });
    }

    public class ServicesHolder extends RecyclerView.ViewHolder { // Defines the ViewHolder class for RecyclerView items
        TextView serviceCloth, servicePrice; // Declares TextViews for service cloth and price
        ImageView serviceIcon, updateServiceIcon; // Declares ImageViews for service icon and update icon
        CardView addCard, removeCard; // Declares CardViews for add and remove actions
        ImageView removeServiceBtn; // Declares an ImageView for the remove button

        public ServicesHolder(@NonNull View itemView) { // Constructor for the ViewHolder
            super(itemView); // Calls the parent constructor
            serviceCloth = itemView.findViewById(R.id.serviceCloth); // Finds the service cloth TextView by ID
            servicePrice = itemView.findViewById(R.id.servicePrice); // Finds the service price TextView by ID
            serviceIcon = itemView.findViewById(R.id.serviceIcon); // Finds the service icon ImageView by ID
            updateServiceIcon = itemView.findViewById(R.id.updateServiceIcon); // Finds the update icon ImageView by ID
            addCard = itemView.findViewById(R.id.addCard); // Finds the add CardView by ID
            removeCard = itemView.findViewById(R.id.removeCard); // Finds the remove CardView by ID
            removeServiceBtn = itemView.findViewById(R.id.removeServiceBtn); // Finds the remove button ImageView by ID
        }
    }
}