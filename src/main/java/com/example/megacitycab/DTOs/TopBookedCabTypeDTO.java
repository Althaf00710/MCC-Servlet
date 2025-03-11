package com.example.megacitycab.DTOs;

public class TopBookedCabTypeDTO {
    private String cabType;
    private int bookingsCount;

    public String getCabType() {
        return cabType;
    }

    public void setCabType(String cabType) {
        this.cabType = cabType;
    }

    public int getBookingsCount() {
        return bookingsCount;
    }

    public void setBookingsCount(int bookingsCount) {
        this.bookingsCount = bookingsCount;
    }
}
