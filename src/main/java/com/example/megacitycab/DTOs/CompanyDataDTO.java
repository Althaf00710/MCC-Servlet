package com.example.megacitycab.DTOs;

public class CompanyDataDTO {
    public double tax;
    public double discount;
    public double minAmountForDiscount;

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
