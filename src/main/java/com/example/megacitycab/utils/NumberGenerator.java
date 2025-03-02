package com.example.megacitycab.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
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

    public String generateBookingNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String todayDate = sdf.format(new Date());

        String uniqueValue = generateRandomString(4);

        return todayDate + "-" + uniqueValue;
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }
}
