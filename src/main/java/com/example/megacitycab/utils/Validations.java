package com.example.megacitycab.utils;

import java.util.Map;

public class Validations {
    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isNumeric(String value) {
        return value != null && value.matches("\\d+");
    }

    public static boolean isValidPhoneNumber(String countryCode, String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^\\d+$")) {
            return false;
        }

        int length = phoneNumber.length();

        Map<String, Integer> validLengths = Map.of(
                "+94", 9,  // Sri Lanka
                "+1", 10,  // USA
                "+44", 10, // UK
                "+91", 10, // India
                "+61", 9,  // Australia
                "+971", 9, // UAE
                "+33", 9,  // France
                "+49", 10  // Germany
        );

        return validLengths.getOrDefault(countryCode, 10) == length;
    }

    // Validate if an integer is within a specific range
    public static boolean isWithinRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    // Check if it's a future date
    public static boolean isValidDate(java.util.Date date, boolean allowFuture) {
        if (date == null) {
            return false;
        }

        java.util.Date currentDate = new java.util.Date();
        return allowFuture || !date.after(currentDate);
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && password.matches(".*\\d.*");
    }

}
