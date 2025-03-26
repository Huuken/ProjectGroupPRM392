package com.example.freshfoldlaundrycare; // Defines the package name for this class

import android.app.Notification; // Imports Notification for creating notifications
import android.app.NotificationManager; // Imports NotificationManager for managing notifications
import android.content.Context; // Imports Context for accessing system services
import android.content.Intent; // Imports Intent for navigating between activities
import android.graphics.Bitmap; // Imports Bitmap for handling image data
import android.graphics.BitmapFactory; // Imports BitmapFactory for decoding bitmap resources
import android.os.Bundle; // Imports Bundle for passing data between activities
import android.view.Menu; // Imports Menu for handling menu inflation
import android.view.MenuItem; // Imports MenuItem for handling menu item selections
import android.view.View; // Imports View for handling UI components
import android.widget.TextView; // Imports TextView for manipulating text views

import androidx.annotation.NonNull; // Imports NonNull annotation for null safety
import androidx.appcompat.app.ActionBarDrawerToggle; // Imports ActionBarDrawerToggle for navigation drawer toggle
import androidx.appcompat.app.AppCompatActivity; // Imports AppCompatActivity as the base class for this activity
import androidx.appcompat.widget.Toolbar; // Imports Toolbar for the app bar
import androidx.core.app.NotificationCompat; // Imports NotificationCompat for backward-compatible notifications
import androidx.drawerlayout.widget.DrawerLayout; // Imports DrawerLayout for the navigation drawer
import androidx.navigation.NavController; // Imports NavController for navigation handling
import androidx.navigation.Navigation; // Imports Navigation for finding navigation controllers
import androidx.navigation.ui.AppBarConfiguration; // Imports AppBarConfiguration for configuring the app bar with navigation
import androidx.navigation.ui.NavigationUI; // Imports NavigationUI for setting up navigation with UI components

import com.example.freshfoldlaundrycare.auth.LoginActivity; // Imports LoginActivity from the auth package
import com.example.freshfoldlaundrycare.databinding.ActivityMainBinding; // Imports the binding class for this activity’s layout
import com.google.android.material.navigation.NavigationView; // Imports NavigationView for the navigation drawer menu
import com.google.firebase.auth.FirebaseAuth; // Imports FirebaseAuth for user authentication
import com.google.firebase.firestore.CollectionReference; // Imports CollectionReference for Firestore collection operations
import com.google.firebase.firestore.FirebaseFirestore; // Imports FirebaseFirestore for Firestore database operations

public class MainActivity extends AppCompatActivity { // Declares the MainActivity class, extending AppCompatActivity

    NavigationView navigationView; // Declares a NavigationView for the navigation drawer menu
    DrawerLayout drawerLayout; // Declares a DrawerLayout for the navigation drawer container
    ActionBarDrawerToggle actionBarDrawerToggle; // Declares an ActionBarDrawerToggle for drawer toggle functionality
    Toolbar mToolbar; // Declares a Toolbar for the app bar
    TextView tv_logout_header; // Declares a TextView for the logout option in the navigation header
    // Navigation Controller --> To handle all the navigation in the app
    NavController navController; // Declares a NavController for handling navigation
    AppBarConfiguration appBarConfiguration; // Declares an AppBarConfiguration for configuring the app bar
    FirebaseAuth mAuth; // Declares a FirebaseAuth object for authentication

    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Initializes Firestore database instance

    String userId; // Declares a string to store the current user’s ID
    // FirebaseFirestore db = FirebaseFirestore.getInstance(); // (Commented out) Redundant Firestore initialization
    // CollectionReference usersRef; // (Commented out) Unused CollectionReference for "Users"
    ActivityMainBinding binding; // Declares a binding object for accessing UI elements

    CollectionReference cartRef; // Declares a CollectionReference for the "Orders" subcollection

    private static final int NOTIFICATION_ID = 1; // Defines a constant for the notification ID

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Overrides onCreate method, called when the activity is created
        super.onCreate(savedInstanceState); // Calls the parent class’s onCreate method
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // Inflates the layout using View Binding
        setContentView(binding.getRoot()); // Sets the content view to the root of the binding layout

        mToolbar = findViewById(R.id.main_page_toolbar); // Finds the Toolbar by its ID
        setSupportActionBar(mToolbar); // Sets the Toolbar as the app’s action bar
        getSupportActionBar().setTitle("Dashboard"); // Sets the title of the action bar to "Dashboard"

        mAuth = FirebaseAuth.getInstance(); // Initializes FirebaseAuth instance
        userId = mAuth.getCurrentUser().getUid(); // Gets the current user’s unique ID from FirebaseAuth
        /*usersRef = db.collection("Users");*/ // (Commented out) Unused initialization of "Users" collection reference

        drawerLayout = findViewById(R.id.drawer_layout); // Finds the DrawerLayout by its ID
        navigationView = findViewById(R.id.navigation_view); // Finds the NavigationView by its ID

        navigationView.bringToFront(); // Brings the NavigationView to the front of the view hierarchy

        View navView = navigationView.inflateHeaderView(R.layout.nav_header_main); // Inflates the navigation header layout

        navigationView.bringToFront(); // Brings the NavigationView to the front again (redundant)

        cartRef = db.collection("Orders").document(userId).collection("Orders"); // References the nested "Orders" subcollection for the current user

        if (cartRef != null) { // Checks if cartRef is not null (always true since it’s initialized)
            sendNotification(); // Calls the method to send a notification
        }

        tv_logout_header = navView.findViewById(R.id.tv_logout_header); // Finds the logout TextView in the navigation header

        tv_logout_header.setOnClickListener(new View.OnClickListener() { // Sets a click listener for the logout TextView
            @Override
            public void onClick(View v) { // Defines the action when the logout TextView is clicked
                mAuth.signOut(); // Signs the user out of Firebase Authentication
                Intent intent = new Intent(MainActivity.this, LoginActivity.class); // Creates an Intent to navigate to LoginActivity
                startActivity(intent); // Starts the LoginActivity
                finish(); // Closes the current activity
            }
        });

        navController = Navigation.findNavController(this, R.id.main_container); // Initializes the NavController with the navigation host fragment

        appBarConfiguration = new AppBarConfiguration.Builder( // Builds the AppBarConfiguration with navigation destinations
                R.id.nav_home, // Home destination
                R.id.nav_my_account, // My Account destination
                R.id.nav_orders, // Orders destination
                R.id.nav_feedback, // Feedback destination
                R.id.nav_contact, // Contact destination
                R.id.nav_help, // Help destination
                R.id.nav_location // Location destination
        ).setOpenableLayout(drawerLayout) // Associates the DrawerLayout with the configuration
                .build(); // Builds the configuration

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration); // Sets up the action bar with the NavController
        NavigationUI.setupWithNavController(navigationView, navController); // Sets up the NavigationView with the NavController

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close); // Initializes the drawer toggle
        drawerLayout.addDrawerListener(actionBarDrawerToggle); // Adds the toggle as a listener to the DrawerLayout
        actionBarDrawerToggle.syncState(); // Syncs the toggle state with the drawer
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // Overrides method to inflate the options menu
        getMenuInflater().inflate(R.menu.menu_profile, menu); // Inflates the menu_profile resource into the menu
        return true; // Returns true to indicate the menu was created
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // Overrides method to handle menu item selections
        if (item.getItemId() == R.id.menu_cart) { // Checks if the selected item is the cart menu item
            Intent intent = new Intent(getApplicationContext(), CartActivity.class); // Creates an Intent to navigate to CartActivity
            startActivity(intent); // Starts the CartActivity
        }
        return true; // Returns true to indicate the item selection was handled
    }

    private void sendNotification() { // Defines a method to send a notification
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher); // Decodes the app’s launcher icon as a Bitmap (unused in notification)

        Notification notification = new NotificationCompat.Builder(this, NotificationActivity.CHANNEL_ID) // Builds a notification
                .setContentTitle("Notification From Your Cart !") // Sets the notification title
                .setContentText("Please check your order please...") // Sets the notification text
                .setSmallIcon(R.drawable.ic_help) // Sets the small icon for the notification
                .setColor(getResources().getColor(R.color.primaryDark)) // Sets the notification color
                .build(); // Builds the notification object

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // Gets the NotificationManager service
        notificationManager.notify(NOTIFICATION_ID, notification); // Displays the notification with the specified ID
    }
}