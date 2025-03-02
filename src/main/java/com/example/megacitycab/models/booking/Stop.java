package com.example.megacitycab.models.booking;

public class Stop {
    private int id;
    private int bookingId;
    private String stopLocation;
    private double longitude;
    private double latitude;
    private String placeId;
    private double distanceFromLastStop;
    private int waitMinutes;


    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
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
