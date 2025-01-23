package com.example.megacitycab.utils;

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

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^[+]?\\d{10,15}$");
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

    // Validate if a password meets complexity requirements
    public static boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$");
    }
}
