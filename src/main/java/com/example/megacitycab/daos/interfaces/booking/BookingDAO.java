package com.example.megacitycab.daos.interfaces.booking;

import com.example.megacitycab.DTOs.BookingDetailDTO;
import com.example.megacitycab.DTOs.BookingRecentDTO;
import com.example.megacitycab.DTOs.DailySalesDTO;
import com.example.megacitycab.DTOs.TopBookedCabTypeDTO;
import com.example.megacitycab.daos.GenericDAO;
import com.example.megacitycab.models.booking.Booking;
import com.example.megacitycab.models.booking.Stop;

import java.sql.Timestamp;
import java.util.List;

public interface BookingDAO extends GenericDAO<Booking> {
    boolean add(Booking entity, List<Stop> stops);
    List<Booking> getAll(Timestamp bookingDateTime, String status);
    boolean updateStatus(int id, String status);
    BookingDetailDTO getBookingDetails(int id);
    int getTodayBookingCount();
    List<BookingRecentDTO> getRecentBookingsWithStops();
    List<TopBookedCabTypeDTO> getTop5BookedCabTypes();
    List<DailySalesDTO> getDailySales();
}

