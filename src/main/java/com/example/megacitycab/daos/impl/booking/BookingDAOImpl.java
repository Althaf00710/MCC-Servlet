package com.example.megacitycab.daos.impl.booking;

import com.example.megacitycab.daos.BaseDAOImpl;
import com.example.megacitycab.daos.interfaces.booking.BookingDAO;
import com.example.megacitycab.models.booking.Booking;
import com.example.megacitycab.utils.DbConfig;
import com.example.megacitycab.utils.NumberGenerator;

import java.sql.*;
import java.sql.SQLException;
import java.util.List;

public class BookingDAOImpl extends BaseDAOImpl<Booking> implements BookingDAO {
    private final DbConfig dbConfig = DbConfig.getInstance();
    private final String TABLE_NAME = "booking";
    private final NumberGenerator numberGenerator  = NumberGenerator.getInstance();

    @Override
    protected Booking mapResultSetToEntity(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setBookingNumber(rs.getString("bookingNumber"));
        booking.setCabId(rs.getInt("cabId"));
        booking.setCustomerId(rs.getInt("customerId"));
        booking.setUserId(rs.getInt("userId"));
        booking.setBookingDateTime(rs.getTimestamp("bookingDateTime"));
        booking.setDateTimeCreated(rs.getTimestamp("dateTimeCreated"));
        booking.setStatus(rs.getString("status"));
        booking.setPickupLocation(rs.getString("pickupLocation"));
        booking.setLongitude(rs.getDouble("longitude"));
        booking.setLatitude(rs.getDouble("latitude"));
        booking.setPlaceId(rs.getString("placeId"));
        return booking;
    }

    @Override
    public boolean add(Booking entity) {
        String sql = "INSERT INTO booking (bookingNumber, cabId, customerId, userId, bookingDateTime, " +
                "status, pickupLocation, longitude, latitude, placeId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, numberGenerator.generateBookingNumber());
            stmt.setInt(2, entity.getCabId());
            stmt.setInt(3, entity.getCustomerId());
            stmt.setInt(4, entity.getUserId());
            stmt.setTimestamp(5, new Timestamp(entity.getBookingDateTime().getTime()));
            stmt.setString(6, entity.getStatus());
            stmt.setString(7, entity.getPickupLocation());
            stmt.setDouble(8, entity.getLongitude());
            stmt.setDouble(9, entity.getLatitude());
            stmt.setString(10, entity.getPlaceId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Booking getById(int id) {
        return super.getById(id, TABLE_NAME);
    }

    @Override
    public List<Booking> getAll() {
        return super.getAll(TABLE_NAME);
    }

    @Override
    public boolean update(Booking entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return super.deleteById(id, TABLE_NAME);
    }

    @Override
    public List<Booking> search(String search) {
        return List.of();
    }
}
