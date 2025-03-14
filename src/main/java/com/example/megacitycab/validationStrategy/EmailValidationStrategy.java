package com.example.megacitycab.validationStrategy;

import java.util.regex.Pattern;

public class EmailValidationStrategy implements ValidationStrategy {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Override
    public boolean validate(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

}
