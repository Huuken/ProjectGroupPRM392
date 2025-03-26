package com.example.freshfoldlaundrycare.fragments; // Defines the package name for this class, organizing it under the "fragments" subpackage.

import android.app.ProgressDialog; // Imports ProgressDialog class to display a loading dialog.
import android.content.Intent; // Imports Intent class to navigate between activities.
import android.os.Bundle; // Imports Bundle class to manage fragment state.
import android.view.LayoutInflater; // Imports LayoutInflater class to inflate layouts.
import android.view.View; // Imports View class to handle UI components.
import android.view.ViewGroup; // Imports ViewGroup class to represent the container for the fragment's view.
import android.widget.ImageView; // Imports ImageView class for displaying images.
import android.widget.TextView; // Imports TextView class for displaying text.
import android.widget.Toast; // Imports Toast class to show short pop-up messages.

import androidx.annotation.NonNull; // Imports NonNull annotation to indicate non-null parameters or return values.
import androidx.cardview.widget.CardView; // Imports CardView class (unused in this code but imported).
import androidx.fragment.app.Fragment; // Imports Fragment class as the base class for this fragment.
import androidx.recyclerview.widget.LinearLayoutManager; // Imports LinearLayoutManager for RecyclerView layout management.
import androidx.recyclerview.widget.RecyclerView; // Imports RecyclerView class for displaying a scrollable list.

import com.bumptech.glide.Glide; // Imports Glide library for loading and displaying images.
import com.example.freshfoldlaundrycare.Modal.Services; // Imports Services model class to represent service data.
import com.example.freshfoldlaundrycare.R; // Imports R class for resource references.
import com.example.freshfoldlaundrycare.ServiceListsActivity; // Imports ServiceListsActivity for navigation.
import com.example.freshfoldlaundrycare.auth.SetupActivity; // Imports SetupActivity for navigation.
import com.example.freshfoldlaundrycare.databinding.FragmentHomeBinding; // Imports binding class for the home fragment layout.
import com.firebase.ui.firestore.FirestoreRecyclerAdapter; // Imports FirestoreRecyclerAdapter for binding Firestore data to RecyclerView.
import com.firebase.ui.firestore.FirestoreRecyclerOptions; // Imports FirestoreRecyclerOptions for configuring Firestore queries.
import com.google.android.gms.tasks.OnCompleteListener; // Imports listener for task completion in Firebase.
import com.google.android.gms.tasks.OnSuccessListener; // Imports listener for successful task completion in Firebase.
import com.google.android.gms.tasks.Task; // Imports Task class for asynchronous Firebase operations.
import com.google.android.material.button.MaterialButton; // Imports MaterialButton class for styled buttons.
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth class for authentication.
import com.google.firebase.firestore.CollectionReference; // Imports CollectionReference for Firestore collections.
import com.google.firebase.firestore.DocumentSnapshot; // Imports DocumentSnapshot for Firestore document data.
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore for Firestore database operations.
import com.google.firebase.firestore.Query; // Imports Query class for Firestore queries.
import com.google.firebase.firestore.QuerySnapshot; // Imports QuerySnapshot for query result sets.

import java.util.HashMap; // Imports HashMap class to store key-value pairs.

public class HomeFragment extends Fragment { // Declares the HomeFragment class, extending Fragment.

    FragmentHomeBinding binding; // Declares a binding object to access UI elements.
    ProgressDialog dialog; // Declares a ProgressDialog for loading states.
    FirebaseAuth mAuth; // Declares FirebaseAuth for authentication.
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initializes Firestore instance.
    CollectionReference serviceRef, cartRef, usersRef; // Declares Firestore collection references.
    String currentUserId; // Declares a variable for the current user's ID.

    @Override // Overrides the superclass method.
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // Defines method to create the fragment's view.
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false); // Inflates the home fragment layout.

        mAuth = FirebaseAuth.getInstance(); // Initializes FirebaseAuth.
        dialog = new ProgressDialog(getContext()); // Initializes ProgressDialog with fragment context.
        currentUserId = mAuth.getCurrentUser().getUid(); // Gets the current user's ID.
        serviceRef = db.collection("Services"); // References the "Services" collection.
        cartRef = db.collection("Cart"); // References the "Cart" collection.
        usersRef = db.collection("Users"); // References the "Users" collection.

        updateTheAddressData(); // Calls method to update and display the user's address.

        binding.ironService.setOnClickListener(new View.OnClickListener() { // Sets click listener for "Iron" service button.
            @Override
            public void onClick(View v) { // Defines the click action.
                Intent intent = new Intent(getContext(), ServiceListsActivity.class); // Creates intent for ServiceListsActivity.
                intent.putExtra("serviceTitle", "Iron"); // Adds "Iron" as an extra.
                startActivity(intent); // Starts the activity.
            }
        });

        binding.washIronService.setOnClickListener(new View.OnClickListener() { // Sets click listener for "Wash Iron" service button.
            @Override
            public void onClick(View v) { // Defines the click action.
                Intent intent = new Intent(getContext(), ServiceListsActivity.class); // Creates intent for ServiceListsActivity.
                intent.putExtra("serviceTitle", "Wash Iron"); // Adds "Wash Iron" as an extra.
                startActivity(intent); // Starts the activity.
            }
        });

        binding.dryCleaningService.setOnClickListener(new View.OnClickListener() { // Sets click listener for "Dry Cleaning" service button.
            @Override
            public void onClick(View v) { // Defines the click action.
                Intent intent = new Intent(getContext(), ServiceListsActivity.class); // Creates intent for ServiceListsActivity.
                intent.putExtra("serviceTitle", "Dry Clean"); // Adds "Dry Clean" as an extra.
                startActivity(intent); // Starts the activity.
            }
        });

        binding.recyclerviewRecommended.setHasFixedSize(true); // Optimizes RecyclerView performance with a fixed size.
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false); // Creates a horizontal LinearLayoutManager.
        binding.recyclerviewRecommended.setLayoutManager(layoutManager); // Sets the layout manager for RecyclerView.

        dialog.setMessage("please wait"); // Sets the ProgressDialog message.
        dialog.setCanceledOnTouchOutside(false); // Prevents dialog dismissal on outside touch.
        dialog.show(); // Shows the ProgressDialog.
        startListen(); // Calls method to fetch and display services.

        return binding.getRoot(); // Returns the root view of the layout.
    }

    private void startListen() { // Defines method to fetch and display services in RecyclerView.
        serviceRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() { // Fetches all documents from "Services".
            @Override
            public void onSuccess(QuerySnapshot snapshot) { // Called when the query succeeds.
                if (snapshot.isEmpty()) { // Checks if the result set is empty.
                    dialog.dismiss(); // Dismisses the ProgressDialog.
                } else { // Executes if there are results.
                    Query query = serviceRef.orderBy("ServiceCloth", Query.Direction.ASCENDING).limit(10); // Creates a query to order services by "ServiceCloth" and limit to 10.
                    FirestoreRecyclerOptions<Services> options = new FirestoreRecyclerOptions.Builder<Services>().setQuery(query, Services.class).build(); // Configures Firestore options.
                    FirestoreRecyclerAdapter<Services, ServicesHolder> fireAdapter = new FirestoreRecyclerAdapter<Services, ServicesHolder>(options) { // Creates a Firestore adapter.
                        @Override
                        protected void onBindViewHolder(@NonNull ServicesHolder holder, int position, @NonNull Services model) { // Binds data to RecyclerView items.
                            holder.serviceCloth.setText(model.getServiceCloth()); // Sets the service cloth name.
                            holder.servicePrice.setText("Rs. " + model.getServicePrice() + " /-"); // Sets the service price with currency.

                            if (model.getServiceType().equals("Iron")) { // Checks if service type is "Iron".
                                Glide.with(getContext()).load(R.drawable.ic_iron).into(holder.serviceIcon); // Loads iron icon.
                            } else if (model.getServiceType().equals("Wash Iron")) { // Checks if service type is "Wash Iron".
                                Glide.with(getContext()).load(R.drawable.ic_washing_machine).into(holder.serviceIcon); // Loads washing machine icon.
                            } else { // Default case (assumes "Dry Clean").
                                Glide.with(getContext()).load(R.drawable.ic_shirt).into(holder.serviceIcon); // Loads shirt icon.
                            }

                            cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() { // Checks if service is in cart.
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) { // Called when cart check succeeds.
                                    if (documentSnapshot.exists()) { // If item is in cart.
                                        holder.addServiceIcon.setVisibility(View.GONE); // Hides "Add" button.
                                        holder.removeCartBtn.setVisibility(View.VISIBLE); // Shows "Remove" button.
                                    } else { // If item is not in cart.
                                        holder.addServiceIcon.setVisibility(View.VISIBLE); // Shows "Add" button.
                                        holder.removeCartBtn.setVisibility(View.GONE); // Hides "Remove" button.
                                    }
                                }
                            });

                            holder.addServiceIcon.setOnClickListener(new View.OnClickListener() { // Sets click listener for "Add" button.
                                @Override
                                public void onClick(View v) { // Defines the click action.
                                    cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() { // Checks cart status.
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) { // Called when cart check succeeds.
                                            if (documentSnapshot.exists()) { // If item is already in cart.
                                                holder.addServiceIcon.setVisibility(View.GONE); // Hides "Add" button.
                                                holder.removeCartBtn.setVisibility(View.VISIBLE); // Shows "Remove" button.
                                            } else { // If item is not in cart.
                                                dialog.setMessage("please wait..."); // Sets ProgressDialog message.
                                                dialog.setCanceledOnTouchOutside(false); // Prevents dialog dismissal.
                                                dialog.show(); // Shows the ProgressDialog.

                                                String price = model.getServicePrice(); // Gets service price.
                                                int quantity = 1; // Sets initial quantity.
                                                int total = Integer.parseInt(price) * quantity; // Calculates total price.

                                                HashMap<String, Object> cartItem = new HashMap<>(); // Creates HashMap for cart item.
                                                cartItem.put("UserId", currentUserId); // Adds user ID.
                                                cartItem.put("ProductId", model.getServiceID()); // Adds service ID.
                                                cartItem.put("ProductName", model.getServiceCloth()); // Adds service name.
                                                cartItem.put("ProductPrice", model.getServicePrice()); // Adds service price.
                                                cartItem.put("Category", model.getServiceType()); // Adds service type.
                                                cartItem.put("Quantity", 1); // Adds quantity.
                                                cartItem.put("TotalPrice", String.valueOf(total)); // Adds total price.
                                                cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).set(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() { // Adds item to cart.
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) { // Called when add operation completes.
                                                        if (task.isSuccessful()) { // If successful.
                                                            dialog.dismiss(); // Dismisses ProgressDialog.
                                                            holder.addServiceIcon.setVisibility(View.GONE); // Hides "Add" button.
                                                            holder.removeCartBtn.setVisibility(View.VISIBLE); // Shows "Remove" button.
                                                            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show(); // Shows success toast.
                                                        } else { // If failed.
                                                            String msg = task.getException().getMessage(); // Gets error message.
                                                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show(); // Shows error toast.
                                                            dialog.dismiss(); // Dismisses ProgressDialog.
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });

                            holder.removeCartBtn.setOnClickListener(new View.OnClickListener() { // Sets click listener for "Remove" button.
                                @Override
                                public void onClick(View v) { // Defines the click action.
                                    cartRef.document(currentUserId).collection("Cart").document(model.getServiceID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() { // Deletes item from cart.
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) { // Called when delete operation completes.
                                            if (task.isSuccessful()) { // If successful.
                                                dialog.dismiss(); // Dismisses ProgressDialog (though not shown here).
                                                Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show(); // Shows success toast.
                                                holder.addServiceIcon.setVisibility(View.VISIBLE); // Shows "Add" button.
                                                holder.removeCartBtn.setVisibility(View.GONE); // Hides "Remove" button.
                                            } else { // If failed.
                                                String msg = task.getException().getMessage(); // Gets error message.
                                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show(); // Shows error toast.
                                                dialog.dismiss(); // Dismisses ProgressDialog (though not shown here).
                                            }
                                        }
                                    });
                                }
                            });

                            holder.itemView.setOnClickListener(new View.OnClickListener() { // Sets click listener for entire item (currently empty).
                                @Override
                                public void onClick(View v) { // Defines the click action (no implementation).
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Creates a new ViewHolder.
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended, parent, false); // Inflates item layout.
                            return new ServicesHolder(view); // Returns new ServicesHolder.
                        }
                    };
                    binding.recyclerviewRecommended.setAdapter(fireAdapter); // Sets adapter for RecyclerView.
                    fireAdapter.startListening(); // Starts real-time Firestore updates.
                    dialog.dismiss(); // Dismisses ProgressDialog.
                }
            }
        });
    }

    private class ServicesHolder extends RecyclerView.ViewHolder { // Defines ViewHolder class for RecyclerView items.
        TextView serviceCloth, servicePrice; // Declares TextViews for service name and price.
        ImageView serviceIcon; // Declares ImageView for service icon.
        MaterialButton addServiceIcon, removeCartBtn; // Declares buttons for add/remove actions.

        public ServicesHolder(@NonNull View itemView) { // Constructor for ViewHolder.
            super(itemView); // Calls superclass constructor.
            serviceCloth = itemView.findViewById(R.id.serviceCloth); // Finds service cloth TextView.
            servicePrice = itemView.findViewById(R.id.servicePrice); // Finds service price TextView.
            serviceIcon = itemView.findViewById(R.id.serviceIcon); // Finds service icon ImageView.
            addServiceIcon = itemView.findViewById(R.id.addServiceIcon); // Finds "Add" button.
            removeCartBtn = itemView.findViewById(R.id.removeServiceBtn); // Finds "Remove" button.
        }
    }

    private void updateTheAddressData() { // Defines method to update and display user's address.
        usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { // Fetches user's document.
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) { // Called when fetch completes.
                if (task.isSuccessful()) { // If successful.
                    DocumentSnapshot snapshot = task.getResult(); // Gets document snapshot.
                    String profileUpdated = snapshot.getString("ProfileUpdated"); // Retrieves "ProfileUpdated" field.
                    String address = snapshot.getString("Address"); // Retrieves "Address" field.

                    if (profileUpdated != null && profileUpdated.equals("NO")) { // Checks if profile is not updated.
                        binding.addressText.setText("Click to update address"); // Prompts user to update address.
                    } else { // If profile is updated.
                        binding.addressText.setText(address); // Displays user's address.
                    }

                    binding.addressText.setOnClickListener(new View.OnClickListener() { // Sets click listener for address text.
                        @Override
                        public void onClick(View v) { // Defines the click action.
                            if (profileUpdated != null && profileUpdated.equals("NO")) { // If profile is not updated.
                                Intent intent = new Intent(getContext(), SetupActivity.class); // Creates intent for SetupActivity.
                                startActivity(intent); // Starts the activity.
                            } else { // If profile is updated.
                                binding.addressText.setText(address); // Ensures address remains displayed (redundant here).
                            }
                        }
                    });
                }
            }
        });
    }

    @Override // Overrides the superclass method.
    public void onResume() { // Defines method called when fragment resumes.
        super.onResume(); // Calls superclass method.
        updateTheAddressData(); // Refreshes address data.
    }
}