package com.example.megacitycab.daos.impl.booking;

import com.example.megacitycab.daos.BaseDAOImpl;
import com.example.megacitycab.daos.interfaces.booking.BookingDAO;
import com.example.megacitycab.models.booking.Booking;
import com.example.megacitycab.models.booking.Stop;
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
    public boolean add(Booking entity, List<Stop> stops) {
        if (stops == null || stops.isEmpty()) {
            throw new IllegalArgumentException("At least one stop is required");
        }

        Connection conn = null;
        try {
            conn = dbConfig.getConnection();
            conn.setAutoCommit(false);  // Start transaction

            // 1. Insert Booking
            String bookingSql = "INSERT INTO booking (bookingNumber, cabId, customerId, userId, bookingDateTime, " +
                    "status, pickupLocation, longitude, latitude, placeId) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement bookingStmt = conn.prepareStatement(bookingSql, Statement.RETURN_GENERATED_KEYS)) {
                bookingStmt.setString(1, numberGenerator.generateBookingNumber());
                bookingStmt.setInt(2, entity.getCabId());
                bookingStmt.setInt(3, entity.getCustomerId());
                bookingStmt.setInt(4, entity.getUserId());
                bookingStmt.setTimestamp(5, new Timestamp(entity.getBookingDateTime().getTime()));
                bookingStmt.setString(6, entity.getStatus());
                bookingStmt.setString(7, entity.getPickupLocation());
                bookingStmt.setDouble(8, entity.getLongitude());
                bookingStmt.setDouble(9, entity.getLatitude());
                bookingStmt.setString(10, entity.getPlaceId());

                int affectedRows = bookingStmt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet rs = bookingStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // 2. Insert Stops
            String stopSql = "INSERT INTO stop (bookingId, stopLocation, longitude, latitude, " +
                    "placeId, distanceFromLastStop, waitMinutes) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stopStmt = conn.prepareStatement(stopSql)) {
                for (Stop stop : stops) {
                    stopStmt.setInt(1, entity.getId());
                    stopStmt.setString(2, stop.getStopLocation());
                    stopStmt.setDouble(3, stop.getLongitude());
                    stopStmt.setDouble(4, stop.getLatitude());
                    stopStmt.setString(5, stop.getPlaceId());
                    stopStmt.setDouble(6, stop.getDistanceFromLastStop());
                    stopStmt.setInt(7, stop.getWaitMinutes());
                    stopStmt.addBatch();
                }

                int[] batchResults = stopStmt.executeBatch();
                for (int result : batchResults) {
                    if (result == PreparedStatement.EXECUTE_FAILED) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
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
