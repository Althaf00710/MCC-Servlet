package com.example.megacitycab.utils;

import java.util.UUID;

public class NumberGenerator {
    private static final NumberGenerator INSTANCE = new NumberGenerator();

    private NumberGenerator() {}

    public static NumberGenerator getInstance() {
        return INSTANCE;
    }

    public String CustomerRegisterNumber() {
        return "C-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
