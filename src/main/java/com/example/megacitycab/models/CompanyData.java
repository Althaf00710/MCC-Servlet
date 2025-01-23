package com.example.megacitycab.models;

public class CompanyData {
    private int id;
    private String address;
    private String phoneNumber;
    private String email;
    private double tax;
    private double discount;
    private double minAmountForDiscount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getMinAmountForDiscount() {
        return minAmountForDiscount;
    }

    public void setMinAmountForDiscount(double minAmountForDiscount) {
        this.minAmountForDiscount = minAmountForDiscount;
    }
}
