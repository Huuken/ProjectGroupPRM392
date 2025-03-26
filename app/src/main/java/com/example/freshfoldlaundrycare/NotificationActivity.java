package com.example.freshfoldlaundrycare; // Defines the package name for this application

import android.app.Application; // Imports the Application class to manage app-wide configurations
import android.app.NotificationChannel; // Imports NotificationChannel for Android 8.0+ notifications
import android.app.NotificationManager; // Imports NotificationManager to manage notifications
import android.os.Build; // Imports Build to check the Android version

public class NotificationActivity extends Application { // Defines the NotificationActivity class extending Application
    public static final String CHANNEL_ID = "CHANNEL 1"; // Declares a constant for the notification channel ID

    @Override // Overrides the onCreate method from Application
    public void onCreate() { // Called when the application is created
        super.onCreate(); // Calls the superclass's onCreate method

        createNotificationChannel(); // Calls method to create the notification channel
    }

    private void createNotificationChannel() { // Creates a notification channel for Android 8.0+
        // Create the NotificationChannel only if the Android version is 26 (Oreo) or higher,
        // because the NotificationChannel class is not available in lower versions.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Checks if the device runs Android Oreo (API 26) or higher
            CharSequence name = getString(R.string.channel_name); // Gets the channel name from resources
            String description = getString(R.string.channel_description); // Gets the channel description from resources
            int importance = NotificationManager.IMPORTANCE_DEFAULT; // Sets the importance level for notifications
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance); // Creates a new NotificationChannel
            channel.setDescription(description); // Sets the channel description

            // Registers the channel with the system.
            // The importance level and other settings cannot be changed after creation.
            NotificationManager notificationManager = getSystemService(NotificationManager.class); // Gets the NotificationManager instance
            notificationManager.createNotificationChannel(channel); // Creates the notification channel in the system
        }
    }
}
