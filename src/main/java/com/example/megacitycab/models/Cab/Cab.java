package com.example.megacitycab.models.Cab;

import java.util.Date;

public class Cab {
    private int id;
    private int cabBrandId;
    private String cabName;
    private int cabTypeId;
    private String registrationNumber;
    private String plateNumber;
    private String status;
    private Date lastService;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCabBrandId() {
        return cabBrandId;
    }

    public void setCabBrandId(int cabBrandId) {
        this.cabBrandId = cabBrandId;
    }

    public String getCabName() {
        return cabName;
    }

    public void setCabName(String cabName) {
        this.cabName = cabName;
    }

    public int getCabTypeId() {
        return cabTypeId;
    }

    public void setCabTypeId(int cabTypeId) {
        this.cabTypeId = cabTypeId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastService() {
        return lastService;
    }

    public void setLastService(Date lastService) {
        this.lastService = lastService;
    }
}
