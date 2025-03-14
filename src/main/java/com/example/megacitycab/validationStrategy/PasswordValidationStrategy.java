package com.example.megacitycab.validationStrategy;

import java.util.regex.Pattern;

public class PasswordValidationStrategy implements ValidationStrategy {
    private static final String PASSWORD_REGEX = "\\d+";

    @Override
    public boolean validate(String password) {
        return password != null && password.length() >= 6 && Pattern.matches(PASSWORD_REGEX, password);
    }

}

