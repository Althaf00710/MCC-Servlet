package com.example.megacitycab.daos.impl.booking;

import com.example.megacitycab.daos.BaseDAOImpl;
import com.example.megacitycab.daos.interfaces.booking.StopDAO;
import com.example.megacitycab.models.booking.Stop;
import com.example.megacitycab.utils.DbConfig;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StopDAOImpl extends BaseDAOImpl<Stop> implements StopDAO {
    private final DbConfig dbConfig = DbConfig.getInstance();
    private final String TABLE_NAME = "Stop";

    @Override
    protected Stop mapResultSetToEntity(ResultSet rs) throws SQLException {
        Stop stop = new Stop();
        stop.setId(rs.getInt("id"));
        stop.setBookingId(rs.getInt("bookingId"));
        stop.setStopLocation(rs.getString("stopLocation"));
        stop.setLongitude(rs.getDouble("longitude"));
        stop.setLatitude(rs.getDouble("latitude"));
        stop.setPlaceId(rs.getString("placeId"));
        stop.setDistanceFromLastStop(rs.getDouble("distanceFromLastStop"));
        stop.setWaitMinutes(rs.getInt("waitMinutes"));
        return stop;
    }

    @Override
    public boolean add(Stop entity) {
        String sql = "INSERT INTO stop (bookingId, stopLocation, longitude, latitude, " +
                "placeId, distanceFromLastStop, waitMinutes) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, entity.getBookingId());
            stmt.setString(2, entity.getStopLocation());
            stmt.setDouble(3, entity.getLongitude());
            stmt.setDouble(4, entity.getLatitude());
            stmt.setString(5, entity.getPlaceId());
            stmt.setDouble(6, entity.getDistanceFromLastStop());
            stmt.setInt(7, entity.getWaitMinutes());

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
    public Stop getById(int id) {
        return super.getById(id, TABLE_NAME);
    }

    @Override
    public List<Stop> getAll() {
        return super.getAll(TABLE_NAME);
    }

    @Override
    public boolean update(Stop entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return super.deleteById(id, TABLE_NAME);
    }

    @Override
    public List<Stop> search(String search) {
        return List.of();
    }

    @Override
    public List<Stop> getByBookingId(int bookingId) {
        List<Stop> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE bookingId = ?";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
