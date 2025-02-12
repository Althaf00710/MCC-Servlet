package com.example.megacitycab.models;

public class Driver {
    private final int id;
    private final String name;
    private final String nicNumber;
    private final String licenceNumber;
    private final String phoneNumber;
    private final String email;
    private String avatarUrl;
    private final String status;

    // Private constructor to enforce usage of Builder
    private Driver(DriverBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.nicNumber = builder.nicNumber;
        this.licenceNumber = builder.licenceNumber;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.avatarUrl = builder.avatarUrl;
        this.status = builder.status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getNicNumber() { return nicNumber; }
    public String getLicenceNumber() { return licenceNumber; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getStatus() { return status; }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    // Static Inner Builder Class
    public static class DriverBuilder {
        private int id;
        private String name;
        private String nicNumber;
        private String licenceNumber;
        private String phoneNumber;
        private String email;
        private String avatarUrl;
        private String status;

        public DriverBuilder(String name, String nicNumber, String licenceNumber, String phoneNumber, String email) {
            this.name = name;
            this.nicNumber = nicNumber;
            this.licenceNumber = licenceNumber;
            this.phoneNumber = phoneNumber;
            this.email = email;
        }

        public DriverBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public DriverBuilder setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public DriverBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Driver build() {
            return new Driver(this);
        }
    }
}
