package com.example.megacitycab.models;

public class Customer {
    private int id;
    private String registerNumber;
    private String name;
    private String address;
    private String countryCode;
    private String phoneNumber;
    private String email;
    private String nicNumber;
    private String joinedDate;

    // Private constructor to enforce the use of Builder
    private Customer(CustomerBuilder builder) {
        this.id = builder.id;
        this.registerNumber = builder.registerNumber;
        this.name = builder.name;
        this.address = builder.address;
        this.countryCode = builder.countryCode;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.nicNumber = builder.nicNumber;
        this.joinedDate = builder.joinedDate;
    }

    // Getters
    public int getId() { return id; }
    public String getRegisterNumber() { return registerNumber; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getNicNumber() { return nicNumber; }
    public String getJoinedDate() { return joinedDate; }
    public String getCountryCode() { return countryCode; }
    public void setRegisterNumber(String registerNumber) { this.registerNumber = registerNumber; }

    // Builder Class
    public static class CustomerBuilder {
        private int id;
        private String registerNumber;
        private String name;
        private String address;
        private String countryCode;
        private String phoneNumber;
        private String email;
        private String nicNumber;
        private String joinedDate;

        public CustomerBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder setRegisterNumber(String registerNumber) {
            this.registerNumber = registerNumber;
            return this;
        }

        public CustomerBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CustomerBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public CustomerBuilder setCountryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public CustomerBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public CustomerBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public CustomerBuilder setNicNumber(String nicNumber) {
            this.nicNumber = nicNumber;
            return this;
        }

        public CustomerBuilder setJoinedDate(String joinedDate) {
            this.joinedDate = joinedDate;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
