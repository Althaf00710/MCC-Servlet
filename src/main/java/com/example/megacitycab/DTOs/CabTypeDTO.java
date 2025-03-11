package com.example.megacitycab.DTOs;

public class CabTypeDTO {
    public String typeName;
    public double baseFare;
    public double baseWaitTimeFare;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }

    public double getBaseWaitTimeFare() {
        return baseWaitTimeFare;
    }

    public void setBaseWaitTimeFare(double baseWaitTimeFare) {
        this.baseWaitTimeFare = baseWaitTimeFare;
    }
}
