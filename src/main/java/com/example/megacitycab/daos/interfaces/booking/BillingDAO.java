package com.example.megacitycab.daos.interfaces.booking;

import com.example.megacitycab.daos.GenericDAO;
import com.example.megacitycab.models.booking.Billing;
import com.example.megacitycab.models.booking.Booking;

public interface BillingDAO extends GenericDAO<Billing> {
    Billing getByBookingId(int bookingId);
}
