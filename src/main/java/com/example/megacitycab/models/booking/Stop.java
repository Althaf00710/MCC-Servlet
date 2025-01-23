package com.example.megacitycab.models.booking;

public class Stop {
    private int id;
    private int bookingId;
    private String stopLocation;
    private String longitude;
    private String latitude;
    private double distanceFromLastStop;
    private int waitMinutes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getStopLocation() {
        return stopLocation;
    }

    public void setStopLocation(String stopLocation) {
        this.stopLocation = stopLocation;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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
