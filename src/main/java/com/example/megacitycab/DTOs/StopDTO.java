package com.example.megacitycab.DTOs;

public class StopDTO {
    private String location;
    private String placeId;
    private double distanceFromLastStop;
    private int waitMinutes;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getDistanceFromLastStop() {
        return distanceFromLastStop;
    }

    public void setDistanceFromLastStop(double distanceFromLastStop) {
        this.distanceFromLastStop = distanceFromLastStop;
    }

    public int getWaitMinutes() {
        return waitMinutes;
    }

    public void setWaitMinutes(int waitMinutes) {
        this.waitMinutes = waitMinutes;
    }
}
