package com.example.megacitycab.daos.interfaces.booking;

import com.example.megacitycab.daos.GenericDAO;
import com.example.megacitycab.models.booking.Stop;

import java.util.List;

public interface StopDAO extends GenericDAO<Stop> {
    List<Stop> getByBookingId(int bookingId);
}
