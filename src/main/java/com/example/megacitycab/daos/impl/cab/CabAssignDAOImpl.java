package com.example.megacitycab.daos.impl.cab;

import com.example.megacitycab.daos.interfaces.cab.CabAssignDAO;
import com.example.megacitycab.models.Cab.CabAssign;
import com.example.megacitycab.models.Driver;
import com.example.megacitycab.utils.DbConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CabAssignDAOImpl implements CabAssignDAO {
    private final DbConfig dbConfig = DbConfig.getInstance();
    private static final String TABLE_NAME = "cabassign";

    @Override
    public boolean addCabAssign(CabAssign cabAssign) {
        String query = "INSERT INTO cabassign (cabId, driverId, status) VALUES (?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, cabAssign.getCabId());
            stmt.setInt(2, cabAssign.getDriverId());
            stmt.setString(3, "ACTIVE");

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cancelCabAssign(int id) {
        String query = "UPDATE cabassign SET status=? WHERE id=?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "CANCEL");
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<CabAssign> getActiveCabAssigns() {
        List<CabAssign> cabAssigns = new ArrayList<>();
        String query = "SELECT ca.id, ca.cabId, ca.driverId, ca.assignDate, " +
                "d.id AS driverId, d.name AS driverName, d.nicNumber, d.licenceNumber, " +
                "d.phoneNumber, d.email, d.avatarUrl, d.status " +
                "FROM " + TABLE_NAME + " ca " +
                "JOIN Driver d ON ca.driverId = d.id " +
                "WHERE ca.status = 'ACTIVE'";


        try (var connection = dbConfig.getConnection();
             var preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                CabAssign cabAssign = new CabAssign();
                cabAssign.setId(resultSet.getInt("ca.id"));
                cabAssign.setCabId(resultSet.getInt("cabId"));
                cabAssign.setDriverId(resultSet.getInt("driverId"));
                cabAssign.setAssignDate(resultSet.getTimestamp("ca.assignDate"));

                // Set driver details
                com.example.megacitycab.models.Driver driver = new Driver.DriverBuilder(resultSet.getString("driverName"),
                        resultSet.getString("nicNumber"),
                        resultSet.getString("licenceNumber"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email"))
                        .setId(resultSet.getInt("id"))
                        .setAvatarUrl(resultSet.getString("avatarUrl"))
                        .setStatus(resultSet.getString("status"))
                        .build();

                cabAssign.setDriver(driver);

                cabAssigns.add(cabAssign);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle properly in production
        }

        return cabAssigns;
    }

    @Override
    public boolean isUserAssigned(int driverId) {
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE status='ACTIVE' AND driverId=?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
