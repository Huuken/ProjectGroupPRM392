package com.example.freshfoldlaundrycare.helper; // Defines the package name for this class, organizing it under the "helper" subpackage.

import java.util.Random; // Imports Random class to generate random numbers.

public class UniqueIdGenerator { // Declares the UniqueIdGenerator class to generate unique IDs for various entities.
    private static final String ALPHABET_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Defines a constant string of uppercase alphabetic characters.
    private static final String NUMERIC_CHARS = "0123456789"; // Defines a constant string of numeric characters (0-9).
    private static final int ALPHABET_LENGTH = 8; // Defines a constant for the number of alphabetic characters in an ID (unused in active methods).
    private static final int NUMERIC_LENGTH = 4; // Defines a constant for the number of numeric characters in an ID (unused in active methods).

    // For eventID
    private static final String PREFIX = "VEN"; // Defines a constant prefix "VEN" for event IDs (unused in active methods).
    private static final int NUM_DIGITS = 8; // Defines a constant for the number of digits in event, service, and order IDs.

    // LOGIC FOR ORDER ID
    private static final String SERVICE_PREFIX = "PROD"; // Defines a constant prefix "PROD" for service IDs.
    private static final String ORDER_PREFIX = "ORDER"; // Defines a constant prefix "ORDER" for order IDs.
    private static final int HOS_NUM_DIGITS = 9; // Defines a constant for a different digit length (unused in active methods).


    public static String generateServiceID() { // Defines a method to generate a unique service ID.
        StringBuilder sb = new StringBuilder(SERVICE_PREFIX); // Creates a StringBuilder initialized with "PROD".

        // Generate 8 random digits
        Random random = new Random(); // Creates a Random instance for generating random numbers.
        for (int i = 0; i < NUM_DIGITS; i++) { // Loops 8 times (NUM_DIGITS) to add random digits.
            int randomDigit = random.nextInt(10); // Generates a random digit between 0 and 9.
            sb.append(randomDigit); // Appends the random digit to the StringBuilder.
        }

        return sb.toString(); // Returns the final service ID as a string (e.g., "PROD12345678").
    }


    public static String generateOrderID() { // Defines a method to generate a unique order ID.
        StringBuilder sb = new StringBuilder(ORDER_PREFIX); // Creates a StringBuilder initialized with "ORDER".

        // Generate 8 random digits
        Random random = new Random(); // Creates a Random instance.
        for (int i = 0; i < NUM_DIGITS; i++) { // Loops 8 times (NUM_DIGITS) to add random digits.
            int randomDigit = random.nextInt(10); // Generates a random digit between 0 and 9.
            sb.append(randomDigit); // Appends the random digit to the StringBuilder.
        }

        return sb.toString(); // Returns the final order ID as a string (e.g., "ORDER12345678").
    }
} // Closes the UniqueIdGenerator class.