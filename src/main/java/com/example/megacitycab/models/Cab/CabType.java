package com.example.megacitycab.models.Cab;

public class CabType {
    private int id;
    private String typeName;
    private String description;
    private int capacity;
    private double baseFare;
    private double baseWaitTimeFare;

    public double getBaseWaitFare() {
        return baseWaitTimeFare;
    }

    public void setBaseWaitFare(double baseWaitTimeFare) {
        this.baseWaitTimeFare = baseWaitTimeFare;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }
}
