package com.example.megacitycab.daos.impl;

import com.example.megacitycab.daos.interfaces.DriverDAO;
import com.example.megacitycab.models.Driver;
import com.example.megacitycab.models.user.User;
import com.example.megacitycab.utils.DbConfig;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class DriverDAOImpl implements DriverDAO {
    private final DbConfig dbConfig = DbConfig.getInstance();

    private static final String INSERT_DRIVER_SQL = "INSERT INTO driver (name, nicNumber, licenceNumber, phoneNumber, email, avatarUrl) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_DRIVER_BY_ID = "SELECT * FROM driver WHERE id = ?";
    private static final String SELECT_ALL_DRIVERS = "SELECT * FROM driver";
    private static final String UPDATE_DRIVER_SQL = "UPDATE driver SET name = ?, nicNumber = ?, licenceNumber = ?, phoneNumber = ?, email = ? WHERE id = ?";
    private static final String DELETE_DRIVER_SQL = "DELETE FROM driver WHERE id = ?";
    private static final String CHECK_EMAIL_SQL = "SELECT COUNT(*) FROM driver WHERE email = ?";
    private static final String UPDATE_STATUS_SQL = "UPDATE driver SET status = ? WHERE id = ?";
    private static final String SEARCH_DRIVER_SQL = "SELECT * FROM Driver WHERE name LIKE ? OR nicNumber LIKE ? OR licenceNumber LIKE ? OR phoneNumber LIKE ? OR email LIKE ?;";


    @Override
    public boolean addDriver(Driver driver) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_DRIVER_SQL)) {

            stmt.setString(1, driver.getName());
            stmt.setString(2, driver.getNicNumber());
            stmt.setString(3, driver.getLicenceNumber());
            stmt.setString(4, driver.getPhoneNumber());
            stmt.setString(5, driver.getEmail());
            if (driver.getAvatarUrl() != null) {
                stmt.setString(6, driver.getAvatarUrl());
            } else {
                stmt.setNull(6, Types.VARCHAR); // set NULL
            }
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Driver getDriverById(int id) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_DRIVER_BY_ID)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Driver.DriverBuilder(rs.getString("name"),
                        rs.getString("nicNumber"),
                        rs.getString("licenceNumber"),
                        rs.getString("phoneNumber"),
                        rs.getString("email"))
                        .setId(rs.getInt("id"))
                        .setAvatarUrl(rs.getString("avatarUrl"))
                        .setStatus(rs.getString("status"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateDriver(Driver driver) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_DRIVER_SQL)) {

            stmt.setString(1, driver.getName());
            stmt.setString(2, driver.getNicNumber());
            stmt.setString(3, driver.getLicenceNumber());
            stmt.setString(4, driver.getPhoneNumber());
            stmt.setString(5, driver.getEmail());
            stmt.setInt(6, driver.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteDriver(int id) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_DRIVER_SQL)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_DRIVERS)) {

            while (rs.next()) {
                Driver driver = new Driver.DriverBuilder(rs.getString("name"),
                        rs.getString("nicNumber"),
                        rs.getString("licenceNumber"),
                        rs.getString("phoneNumber"),
                        rs.getString("email"))
                        .setId(rs.getInt("id"))
                        .setAvatarUrl(rs.getString("avatarUrl"))
                        .setStatus(rs.getString("status"))
                        .build();
                drivers.add(driver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    @Override
    public boolean isEmailTaken(String email) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_EMAIL_SQL)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // If count > 0, email exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAvatarUrl(Driver driver) {
        String sql = "UPDATE Driver SET avatarUrl=? WHERE id=?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (driver.getAvatarUrl() == null) {
                System.err.println("Avatar URL is null for driver ID: " + driver.getId());
                return false;
            }

            stmt.setString(1, driver.getAvatarUrl());
            stmt.setInt(2, driver.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStatus(int driverId, String status) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STATUS_SQL)) {

            stmt.setString(1, status);
            stmt.setInt(2, driverId);

            return stmt.executeUpdate() > 0; // Returns true if update is successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Driver> getDriversBySearch(String search) {
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = dbConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_DRIVER_SQL)) {

            String searchTerm = "%" + search + "%"; // Add wildcard for partial match
            for (int i = 1; i <= 5; i++) {
                preparedStatement.setString(i, searchTerm);
            }

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Driver driver = new Driver.DriverBuilder(rs.getString("name"),
                        rs.getString("nicNumber"),
                        rs.getString("licenceNumber"),
                        rs.getString("phoneNumber"),
                        rs.getString("email"))
                        .setId(rs.getInt("id"))
                        .setAvatarUrl(rs.getString("avatarUrl"))
                        .setStatus(rs.getString("status"))
                        .build();
                drivers.add(driver);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }
}
