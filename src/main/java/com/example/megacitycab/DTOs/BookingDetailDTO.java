package com.example.megacitycab.DTOs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingDetailDTO {
    private String pickupLocation;
    private String pickupPlaceId;
    private Date bookingCreated;
    private List<StopDTO> stops = new ArrayList<>();
    private CompanyDataDTO companyData;
    private DriverDTO driver;
    private CabTypeDTO cabType;
    private CustomerDTO customer;

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getPickupPlaceId() {
        return pickupPlaceId;
    }

    public void setPickupPlaceId(String pickupPlaceId) {
        this.pickupPlaceId = pickupPlaceId;
    }

    public Date getBookingCreated() {
        return bookingCreated;
    }

    public void setBookingCreated(Date bookingCreated) {
        this.bookingCreated = bookingCreated;
    }

    public List<StopDTO> getStops() {
        return stops;
    }

    public void setStops(List<StopDTO> stops) {
        this.stops = stops;
    }

    public CompanyDataDTO getCompanyData() {
        return companyData;
    }

    public void setCompanyData(CompanyDataDTO companyData) {
        this.companyData = companyData;
    }

    public DriverDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverDTO driver) {
        this.driver = driver;
    }

    public CabTypeDTO getCabType() {
        return cabType;
    }

    public void setCabType(CabTypeDTO cabType) {
        this.cabType = cabType;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }
}
