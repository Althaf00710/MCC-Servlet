package com.example.megacitycab.DTOs;

import java.sql.Date;
import java.sql.Timestamp;

public class BillingListDTO {
    private int billingId;
    private int bookingId;
    private String bookingNo;
    private Timestamp billDate;
    private String customerRegisterNo;
    private String customerName;
    private String customerPhone;
    private String cabType;
    private double totalDistance;
    private int totalWaitTime;
    private double totalAmount;

    // Private constructor to enforce the use of the Builder
    private BillingListDTO(Builder builder) {
        this.billingId = builder.billingId;
        this.bookingId = builder.bookingId;
        this.bookingNo = builder.bookingNo;
        this.billDate = builder.billDate;
        this.customerRegisterNo = builder.customerRegisterNo;
        this.customerName = builder.customerName;
        this.customerPhone = builder.customerPhone;
        this.cabType = builder.cabType;
        this.totalDistance = builder.totalDistance;
        this.totalWaitTime = builder.totalWaitTime;
        this.totalAmount = builder.totalAmount;
    }

    // Static inner Builder class
    public static class Builder {
        private int billingId;
        private int bookingId;
        private String bookingNo;
        private Timestamp billDate;
        private String customerRegisterNo;
        private String customerName;
        private String customerPhone;
        private String cabType;
        private double totalDistance;
        private int totalWaitTime;
        private double totalAmount;

        public Builder billingId(int billingId) {
            this.billingId = billingId;
            return this;
        }

        public Builder bookingId(int bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder bookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
            return this;
        }

        public Builder billDate(Timestamp billDate) {
            this.billDate = billDate;
            return this;
        }

        public Builder customerRegisterNo(String customerRegisterNo) {
            this.customerRegisterNo = customerRegisterNo;
            return this;
        }

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder customerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
            return this;
        }

        public Builder cabType(String cabType) {
            this.cabType = cabType;
            return this;
        }

        public Builder totalDistance(double totalDistance) {
            this.totalDistance = totalDistance;
            return this;
        }

        public Builder totalWaitTime(int totalWaitTime) {
            this.totalWaitTime = totalWaitTime;
            return this;
        }

        public Builder totalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public BillingListDTO build() {
            return new BillingListDTO(this);
        }
    }

    // Getters (No setters since this class is immutable)
    public int getBillingId() {
        return billingId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public Timestamp getBillDate() {
        return billDate;
    }

    public String getCustomerRegisterNo() {
        return customerRegisterNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getCabType() {
        return cabType;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public int getTotalWaitTime() {
        return totalWaitTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
