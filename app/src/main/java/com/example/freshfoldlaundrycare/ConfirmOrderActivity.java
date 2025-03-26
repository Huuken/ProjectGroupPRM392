package com.example.freshfoldlaundrycare; // Defines the package name for this class

import android.app.ProgressDialog; // Imports ProgressDialog to show loading indicators
import android.content.Intent; // Imports Intent for navigating between activities
import android.os.Bundle; // Imports Bundle for passing data between activities
import android.view.View; // Imports View for handling UI components
import android.widget.Toast; // Imports Toast for displaying short messages

import androidx.annotation.NonNull; // Imports NonNull annotation for null safety
import androidx.appcompat.app.AppCompatActivity; // Imports AppCompatActivity as the base class for this activity

import com.example.freshfoldlaundrycare.auth.LoginActivity; // Imports LoginActivity from the auth package
import com.example.freshfoldlaundrycare.databinding.ActivityConfirmOrderBinding; // Imports the binding class for this activity’s layout
import com.example.freshfoldlaundrycare.helper.UniqueIdGenerator; // Imports a helper class to generate unique IDs
import com.google.android.gms.tasks.OnCompleteListener; // Imports OnCompleteListener for Firebase task completion
import com.google.android.gms.tasks.OnSuccessListener; // Imports OnSuccessListener for successful Firebase task results
import com.google.android.gms.tasks.Task; // Imports Task for handling Firebase asynchronous operations
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth for user authentication
import com.google.firebase.firestore.CollectionReference; // Imports CollectionReference for Firestore collection operations
import com.google.firebase.firestore.DocumentSnapshot; // Imports DocumentSnapshot for retrieving Firestore document data
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore for Firestore database operations
import com.google.firebase.firestore.QueryDocumentSnapshot; // Imports QueryDocumentSnapshot for Firestore query results
import com.google.firebase.firestore.QuerySnapshot; // Imports QuerySnapshot for handling Firestore query result sets

import java.text.SimpleDateFormat; // Imports SimpleDateFormat for formatting dates and times
import java.util.Calendar; // Imports Calendar for getting current date and time
import java.util.HashMap; // Imports HashMap for storing key-value pairs
import java.util.Map; // Imports Map interface for key-value pair operations

public class ConfirmOrderActivity extends AppCompatActivity { // Declares the ConfirmOrderActivity class, extending AppCompatActivity

    ActivityConfirmOrderBinding binding; // Declares a binding object for accessing UI elements
    FirebaseAuth mAuth; // Declares a FirebaseAuth object for authentication
    ProgressDialog loadingBar; // Declares a ProgressDialog for showing a loading indicator
    ProgressDialog dialog; // Declares another ProgressDialog instance (possibly redundant)
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initializes Firestore database instance
    CollectionReference ordersRef, cartRef, usersRef; // Declares CollectionReference objects for Firestore collections
    String currentUserId, totalPrice; // Declares strings for storing the current user ID and total price

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Overrides onCreate method, called when the activity is created
        super.onCreate(savedInstanceState); // Calls the parent class’s onCreate method
        binding = ActivityConfirmOrderBinding.inflate(getLayoutInflater()); // Inflates the layout using View Binding
        setContentView(binding.getRoot()); // Sets the content view to the root of the binding layout

        mAuth = FirebaseAuth.getInstance(); // Initializes FirebaseAuth instance
        loadingBar = new ProgressDialog(this); // Initializes the loadingBar ProgressDialog with the current context
        mAuth = FirebaseAuth.getInstance(); // Re-initializes FirebaseAuth (redundant)
        dialog = new ProgressDialog(this); // Initializes the dialog ProgressDialog with the current context
        currentUserId = mAuth.getCurrentUser().getUid(); // Gets the current user’s unique ID from FirebaseAuth

        totalPrice = getIntent().getStringExtra("totalPrice"); // Retrieves the total price passed via Intent extras

        usersRef = db.collection("Users"); // References the "Users" collection in Firestore
        cartRef = db.collection("Cart").document(currentUserId).collection("Cart"); // References the nested "Cart" collection for the current user
        ordersRef = db.collection("Orders"); // References the "Orders" collection in Firestore

        usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { // Fetches the current user’s document from Firestore
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) { // Defines the callback for when the task completes
                if (task.isSuccessful()) { // Checks if the task was successful
                    DocumentSnapshot snapshot = task.getResult(); // Gets the document snapshot from the task result
                    String profileUpdated = snapshot.getString("ProfileUpdated"); // Retrieves the "ProfileUpdated" field (not used later)
                    String address = snapshot.getString("Address"); // Retrieves the "Address" field
                    String name = snapshot.getString("Name"); // Retrieves the "Name" field
                    String phone = snapshot.getString("Phone"); // Retrieves the "Phone" field
                    String email = snapshot.getString("Email"); // Retrieves the "Email" field
                    String city = snapshot.getString("City"); // Retrieves the "City" field
                    String delivery = snapshot.getString("DeliveryTime"); // Retrieves the "DeliveryTime" field
                    String pickup = snapshot.getString("PickupTime"); // Retrieves the "PickupTime" field
                    String pincode = snapshot.getString("Pincode"); // Retrieves the "Pincode" field

                    binding.profileAddress.setText(address); // Sets the address in the UI
                    binding.profileName.setText(name); // Sets the name in the UI
                    binding.profilePhone.setText(phone); // Sets the phone number in the UI
                    binding.profileEmail.setText(email); // Sets the email in the UI
                    binding.profileCity.setText(city); // Sets the city in the UI
                    binding.profileDelivery.setText(delivery); // Sets the delivery time in the UI
                    binding.profilePickup.setText(pickup); // Sets the pickup time in the UI
                    binding.profilePincode.setText(pincode); // Sets the pincode in the UI
                }
            }
        });

        binding.confirmOrderBtn.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the confirm order button
            @Override
            public void onClick(View v) { // Defines the action when the button is clicked
                usersRef.document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() { // Fetches user data again when the button is clicked
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) { // Callback for when the task completes
                        if (task.isSuccessful()) { // Checks if the task was successful
                            DocumentSnapshot snapshot = task.getResult(); // Gets the document snapshot
                            String address = snapshot.getString("Address"); // Retrieves the "Address" field
                            String name = snapshot.getString("Name"); // Retrieves the "Name" field
                            String phone = snapshot.getString("Phone"); // Retrieves the "Phone" field
                            String email = snapshot.getString("Email"); // Retrieves the "Email" field
                            String city = snapshot.getString("City"); // Retrieves the "City" field
                            String delivery = snapshot.getString("DeliveryTime"); // Retrieves the "DeliveryTime" field
                            String pickup = snapshot.getString("PickupTime"); // Retrieves the "PickupTime" field
                            String pincode = snapshot.getString("Pincode"); // Retrieves the "Pincode" field

                            // Creates a new OrderID for the order
                            String orderID = UniqueIdGenerator.generateOrderID(); // Generates a unique order ID using a helper class

                            // Gets the current date and time
                            Calendar calendar = Calendar.getInstance(); // Creates a Calendar instance with the current time
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Defines the date format
                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a"); // Defines the time format with AM/PM

                            String currentDate = dateFormat.format(calendar.getTime()); // Formats the current date
                            String currentTime = timeFormat.format(calendar.getTime()); // Formats the current time

                            // Creates a HashMap to store order information
                            HashMap<String, Object> orderData = new HashMap<>(); // Initializes a HashMap for order data
                            orderData.put("OrderID", orderID); // Adds the order ID to the HashMap
                            orderData.put("UserID", currentUserId); // Adds the user ID to the HashMap
                            orderData.put("OrderStatus", "Placed"); // Sets the order status as "Placed"
                            orderData.put("OrderDate", currentDate); // Adds the current date to the HashMap
                            orderData.put("OrderTime", currentTime); // Adds the current time to the HashMap
                            orderData.put("TotalPrice", totalPrice); // Adds the total price to the HashMap
                            orderData.put("Address", address); // Adds the address to the HashMap
                            orderData.put("Name", name); // Adds the name to the HashMap
                            orderData.put("Phone", phone); // Adds the phone number to the HashMap
                            orderData.put("Email", email); // Adds the email to the HashMap
                            orderData.put("City", city); // Adds the city to the HashMap
                            orderData.put("DeliveryTime", delivery); // Adds the delivery time to the HashMap
                            orderData.put("PickupTime", pickup); // Adds the pickup time to the HashMap
                            orderData.put("Pincode", pincode); // Adds the pincode to the HashMap

                            // Saves the new order to Firestore
                            ordersRef.document(orderID).set(orderData).addOnCompleteListener(new OnCompleteListener<Void>() { // Sets the order data in Firestore
                                @Override
                                public void onComplete(@NonNull Task<Void> task) { // Callback for when the task completes
                                    Toast.makeText(ConfirmOrderActivity.this, "Order Placed", Toast.LENGTH_SHORT).show(); // Shows a success message
                                }
                            });

                            // Retrieves the list of products in the cart
                            cartRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() { // Queries the cart collection
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) { // Callback for successful query
                                    if (!queryDocumentSnapshots.isEmpty()) { // Checks if the cart is not empty
                                        int totalQuantity = 0; // Initializes a variable to count total quantity of products

                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) { // Loops through each cart item
                                            long quantity = document.getLong("Quantity"); // Gets the quantity of the item
                                            totalQuantity += quantity; // Adds the quantity to the total
                                        }

                                        // Updates the Quantity in the order information
                                        orderData.put("Quantity", totalQuantity); // Adds the total quantity to the order data

                                        // Saves the updated order to Firestore
                                        ordersRef.document(orderID).set(orderData).addOnCompleteListener(new OnCompleteListener<Void>() { // Updates the order in Firestore
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) { // Callback for when the task completes
                                                Toast.makeText(ConfirmOrderActivity.this, "Order Placed", Toast.LENGTH_SHORT).show(); // Shows a success message
                                            }
                                        });

                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) { // Loops through cart items again
                                            String productId = document.getString("ProductId"); // Gets the product ID
                                            String productName = document.getString("ProductName"); // Gets the product name
                                            String productPrice = document.getString("ProductPrice"); // Gets the product price
                                            String category = document.getString("Category"); // Gets the product category
                                            long quantity = document.getLong("Quantity"); // Gets the product quantity
                                            String totalPrice = document.getString("TotalPrice"); // Gets the total price for the item

                                            // Saves the product to the order’s Products subcollection
                                            Map<String, Object> cartItemData = new HashMap<>(); // Creates a HashMap for cart item data
                                            cartItemData.put("ProductId", productId); // Adds the product ID
                                            cartItemData.put("ProductName", productName); // Adds the product name
                                            cartItemData.put("ProductPrice", productPrice); // Adds the product price
                                            cartItemData.put("OrderDate", currentDate); // Adds the order date
                                            cartItemData.put("OrderTime", currentTime); // Adds the order time
                                            cartItemData.put("Category", category); // Adds the category
                                            cartItemData.put("Quantity", quantity); // Adds the quantity
                                            cartItemData.put("TotalPrice", totalPrice); // Adds the total price

                                            ordersRef.document(orderID).collection("Products").document(productId).set(cartItemData); // Saves the item to the Products subcollection
                                        }
                                        // Deletes the cart after placing the order
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) { // Loops through cart items again
                                            cartRef.document(document.getId()).delete(); // Deletes each cart item
                                        }
                                    } else { // If the cart is empty
                                        Toast.makeText(ConfirmOrderActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show(); // Shows an error message
                                    }
                                    Intent intent = new Intent(ConfirmOrderActivity.this, MainActivity.class); // Creates an Intent to navigate to MainActivity
                                    startActivity(intent); // Starts the MainActivity
                                    finish(); // Closes the current activity
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}