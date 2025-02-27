package com.example.megacitycab.daos.impl.cab;

import com.example.megacitycab.daos.BaseDAOImpl;
import com.example.megacitycab.daos.interfaces.cab.CabBrandDAO;
import com.example.megacitycab.daos.interfaces.cab.CabDAO;
import com.example.megacitycab.models.Cab.Cab;
import com.example.megacitycab.models.Cab.CabBrand;
import com.example.megacitycab.utils.DbConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CabDAOImpl extends BaseDAOImpl<Cab> implements CabDAO {
    private static final Logger LOGGER = Logger.getLogger(CabDAOImpl.class.getName());
    private final DbConfig dbConfig = DbConfig.getInstance();
    private static final String TABLE_NAME = "cab";

    private static final String GET_ALL_SQL = "SELECT * FROM " + TABLE_NAME;
    private static final String INSERT_CAB_SQL = "INSERT INTO " + TABLE_NAME + " (cabBrandId, cabName, cabTypeId, registrationNumber, plateNumber) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_CAB_SQL = "UPDATE " + TABLE_NAME + " SET cabBrandId = ?, cabName = ?, cabTypeId = ?, registrationNumber = ?, plateNumber = ? WHERE id = ?";
    private static final String SEARCH_CAB_SQL = "SELECT * FROM " + TABLE_NAME + " WHERE cabName LIKE ?";
    private static final String CHECK_CAB_SQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE registrationNumber = ?";




    @Override
    protected Cab mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Cab.CabBuilder()
                .setId(rs.getInt("id"))
                .setCabBrandId(rs.getInt("cabBrandId"))
                .setCabName(rs.getString("cabName"))
                .setCabTypeId(rs.getInt("cabTypeId"))
                .setRegistrationNumber(rs.getString("registrationNumber"))
                .setPlateNumber(rs.getString("plateNumber"))
                .setStatus(rs.getString("status"))
                .setLastService(rs.getDate("lastService"))
                .build();
    }

    @Override
    public boolean checkExist(String registrationNumber) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_CAB_SQL)) {
            stmt.setString(1, registrationNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking cab existence", e);
        }
        return false;
    }


    @Override
    public boolean add(Cab entity) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CAB_SQL)) {
            stmt.setInt(1, entity.getCabBrandId());
            stmt.setString(2, entity.getCabName());
            stmt.setInt(3, entity.getCabTypeId());
            stmt.setString(4, entity.getRegistrationNumber());
            stmt.setString(5, entity.getPlateNumber());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding Cab", e);
            return false;
        }
    }

    @Override
    public Cab getById(int id) {
        return super.getById(id, TABLE_NAME);
    }

    @Override
    public List<Cab> getAll() {
        List<Cab> cabs = new ArrayList<>();
        String sql = "SELECT c.id, c.cabName, c.registrationNumber, c.plateNumber, c.status, c.lastService, cb.brandName, ct.typeName " +
                "FROM Cab c " +
                "JOIN CabBrand cb ON c.cabBrandId = cb.id " +
                "JOIN CabType ct ON c.cabTypeId = ct.id";
        try (Connection conn = dbConfig.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Cab cab = new Cab.CabBuilder()
                        .setId(resultSet.getInt("id"))
                        .setCabName(resultSet.getString("cabName"))
                        .setRegistrationNumber(resultSet.getString("registrationNumber"))
                        .setPlateNumber(resultSet.getString("plateNumber"))
                        .setStatus(resultSet.getString("status"))
                        .setLastService(resultSet.getTimestamp("lastService"))
                        .setCabBrandName(resultSet.getString("brandName"))
                        .setCabTypeName(resultSet.getString("typeName"))
                        .build();
                cabs.add(cab);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving cabs", e);
        }
        return cabs;
    }

    @Override
    public boolean update(Cab entity) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CAB_SQL)) {
            stmt.setInt(1, entity.getCabBrandId());
            stmt.setString(2, entity.getCabName());
            stmt.setInt(3, entity.getCabTypeId());
            stmt.setString(4, entity.getRegistrationNumber());
            stmt.setString(5, entity.getPlateNumber());
            stmt.setInt(6, entity.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating Cab", e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        return super.deleteById(id, TABLE_NAME);
    }

    @Override
    public List<Cab> search(String search) {
        List<Cab> cabs = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_CAB_SQL)) {
            stmt.setString(1, "%" + search + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cabs.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching Cabs", e);
        }
        return cabs;
    }
}
