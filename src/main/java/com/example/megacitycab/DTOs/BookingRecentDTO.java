package com.example.megacitycab.DTOs;

import java.sql.Timestamp;
import java.util.List;

public class BookingRecentDTO {
    private String bookingNumber;
    private String bookingPlaceId;
    private Timestamp bookingDateTime;
    private List<String> stopPlaceIds;

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getBookingPlaceId() {
        return bookingPlaceId;
    }

    public void setBookingPlaceId(String bookingPlaceId) {
        this.bookingPlaceId = bookingPlaceId;
    }

    public Timestamp getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(Timestamp bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public List<String> getStopPlaceIds() {
        return stopPlaceIds;
    }

    public void setStopPlaceIds(List<String> stopPlaceIds) {
        this.stopPlaceIds = stopPlaceIds;
    }
}