package com.example.megacitycab.validationStrategy;

public class ValidationContext {
    private ValidationStrategy strategy;

    public void setStrategy(ValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean validate(String input) {
        if (strategy == null) {
            throw new IllegalStateException("Validation strategy not set.");
        }
        return strategy.validate(input);
    }
}

