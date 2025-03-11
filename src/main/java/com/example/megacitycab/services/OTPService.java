package com.example.megacitycab.services;

import com.example.megacitycab.utils.Email;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {
    private final Map<String, String> otpStorage = new HashMap<>();

    public String generateAndStoreOTP(String email) {
        String otp = String.valueOf(new Random().nextInt(9000) + 1000);
        otpStorage.put(email, otp);
        return otp;
    }

    public boolean verifyOTP(String email, String otp) {
        return otpStorage.containsKey(email) && otpStorage.get(email).equals(otp);
    }

    public boolean sendOtpEmail(String email, String otp) {
        try{
            Email.sendEmail(email, "Reset OTP", "Your OTP is: " + otp);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
