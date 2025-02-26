package com.example.megacitycab.models.Cab;

import java.util.Date;

public class Cab {
    private int id;
    private String cabBrandName;
    private int cabBrandId;
    private String cabName;
    private String cabTypeName;
    private int cabTypeId;
    private String registrationNumber;
    private String plateNumber;
    private String status;
    private Date lastService;

    private Cab(CabBuilder builder) {
        this.id = builder.id;
        this.cabBrandName = builder.cabBrandName;
        this.cabBrandId = builder.cabBrandId;
        this.cabName = builder.cabName;
        this.cabTypeName = builder.cabTypeName;
        this.cabTypeId = builder.cabTypeId;
        this.registrationNumber = builder.registrationNumber;
        this.plateNumber = builder.plateNumber;
        this.status = builder.status;
        this.lastService = builder.lastService;
    }

    public int getId() {
        return id;
    }

    public int getCabBrandId() {
        return cabBrandId;
    }

    public String getCabName() {
        return cabName;
    }

    public int getCabTypeId() {
        return cabTypeId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getStatus() {
        return status;
    }

    public Date getLastService() {
        return lastService;
    }

    public String getCabBrandName() {
        return cabBrandName;
    }

    public String getCabTypeName() {
        return cabTypeName;
    }

    public static class CabBuilder {
        private int id;
        private String cabBrandName;
        private int cabBrandId;
        private String cabName;
        private String cabTypeName;
        private int cabTypeId;
        private String registrationNumber;
        private String plateNumber;
        private String status;
        private Date lastService;

        public CabBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public CabBuilder setCabBrandName(String cabBrandName) {
            this.cabBrandName = cabBrandName;
            return this;
        }

        public CabBuilder setCabBrandId(int cabBrandId) {
            this.cabBrandId = cabBrandId;
            return this;
        }

        public CabBuilder setCabName(String cabName) {
            this.cabName = cabName;
            return this;
        }

        public CabBuilder setCabTypeName(String cabTypeName) {
            this.cabTypeName = cabTypeName;
            return this;
        }

        public CabBuilder setCabTypeId(int cabTypeId) {
            this.cabTypeId = cabTypeId;
            return this;
        }

        public CabBuilder setRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public CabBuilder setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
            return this;
        }

        public CabBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public CabBuilder setLastService(Date lastService) {
            this.lastService = lastService;
            return this;
        }

        public Cab build() {
            return new Cab(this);
        }
    }
}
