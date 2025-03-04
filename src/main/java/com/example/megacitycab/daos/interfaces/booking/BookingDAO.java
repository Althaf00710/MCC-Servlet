package com.example.megacitycab.daos.interfaces.booking;

import com.example.megacitycab.daos.GenericDAO;
import com.example.megacitycab.models.booking.Booking;
import com.example.megacitycab.models.booking.Stop;

import java.util.List;

public interface BookingDAO extends GenericDAO<Booking> {
    boolean add(Booking entity, List<Stop> stops);
}
