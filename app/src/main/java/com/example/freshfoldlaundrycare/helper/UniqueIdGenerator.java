package com.example.freshfoldlaundrycare.helper;

import java.util.Random;

public class UniqueIdGenerator {
    private static final String ALPHABET_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC_CHARS = "0123456789";
    private static final int ALPHABET_LENGTH = 8;
    private static final int NUMERIC_LENGTH = 4;

    //For eventID
    private static final String PREFIX = "VEN";
    private static final int NUM_DIGITS = 8;

    //LOGIC FOR ORDER ID
    private static final String SERVICE_PREFIX = "PROD";
    private static final String ORDER_PREFIX = "ORDER";
    private static final int HOS_NUM_DIGITS = 9;

    /*public static String generateEventId() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        // Generate alphabetic characters
        for (int i = 0; i < ALPHABET_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(ALPHABET_CHARS.length());
            char randomChar = ALPHABET_CHARS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        // Generate numeric characters
        for (int i = 0; i < NUMERIC_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(NUMERIC_CHARS.length());
            char randomChar = NUMERIC_CHARS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }*/

    public static String generateServiceID() {
        StringBuilder sb = new StringBuilder(SERVICE_PREFIX);

        // Generate 8 random digits
        Random random = new Random();
        for (int i = 0; i < NUM_DIGITS; i++) {
            int randomDigit = random.nextInt(10); // Generate a random digit between 0 and 9
            sb.append(randomDigit);
        }

        return sb.toString();
    }

    /*public static String generateProductID() {
        StringBuilder sb = new StringBuilder(PRO_PREFIX);

        // Generate 8 random digits
        Random random = new Random();
        for (int i = 0; i < NUM_DIGITS; i++) {
            int randomDigit = random.nextInt(10); // Generate a random digit between 0 and 9
            sb.append(randomDigit);
        }

        return sb.toString();
    }*/

    public static String generateOrderID() {
        StringBuilder sb = new StringBuilder(ORDER_PREFIX);

        // Generate 8 random digits
        Random random = new Random();
        for (int i = 0; i < NUM_DIGITS; i++) {
            int randomDigit = random.nextInt(10); // Generate a random digit between 0 and 9
            sb.append(randomDigit);
        }

        return sb.toString();
    }

}