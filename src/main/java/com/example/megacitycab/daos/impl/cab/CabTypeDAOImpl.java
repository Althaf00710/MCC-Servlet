package com.example.megacitycab.daos.impl.cab;

import com.example.megacitycab.daos.BaseDAOImpl;
import com.example.megacitycab.daos.interfaces.cab.CabTypeDAO;
import com.example.megacitycab.models.Cab.CabType;
import com.example.megacitycab.utils.DbConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CabTypeDAOImpl extends BaseDAOImpl<CabType> implements CabTypeDAO {

    private static final Logger LOGGER = Logger.getLogger(CabTypeDAOImpl.class.getName());
    private final DbConfig dbConfig = DbConfig.getInstance();
    private static final String TABLE_NAME = "cabtype";

    private static final String INSERT_CAB_TYPE_SQL = "INSERT INTO " + TABLE_NAME + " (typeName, description, capacity, baseFare, baseWaitTimeFare) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_CAB_TYPE_SQL = "UPDATE " + TABLE_NAME + " SET typeName = ?, description = ?, capacity = ?, baseFare = ?, baseWaitTimeFare = ? WHERE id = ?";
    private static final String SEARCH_CAB_TYPE_SQL = "SELECT * FROM " + TABLE_NAME + " WHERE typeName LIKE ?";
    private static final String CHECK_CAB_TYPE_SQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE typeName = ?";

    @Override
    protected CabType mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new CabType.CabTypeBuilder()
                .setId(rs.getInt("id"))
                .setTypeName(rs.getString("typeName"))
                .setDescription(rs.getString("description"))
                .setCapacity(rs.getInt("capacity"))
                .setBaseFare(rs.getDouble("baseFare"))
                .setBaseWaitTimeFare(rs.getDouble("baseWaitTimeFare"))
                .build();
    }

    @Override
    public boolean add(CabType cabType) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CAB_TYPE_SQL)) {

            stmt.setString(1, cabType.getTypeName());
            stmt.setString(2, cabType.getDescription());
            stmt.setInt(3, cabType.getCapacity());
            stmt.setDouble(4, cabType.getBaseFare());
            stmt.setDouble(5, cabType.getBaseWaitTimeFare());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting cab type", e);
        }
        return false;
    }

    @Override
    public CabType getById(int id) {
        return super.getById(id, TABLE_NAME);
    }

    @Override
    public List<CabType> getAll() {
        return super.getAll(TABLE_NAME);
    }

    @Override
    public boolean update(CabType cabType) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CAB_TYPE_SQL)) {

            stmt.setString(1, cabType.getTypeName());
            stmt.setString(2, cabType.getDescription());
            stmt.setInt(3, cabType.getCapacity());
            stmt.setDouble(4, cabType.getBaseFare());
            stmt.setDouble(5, cabType.getBaseWaitTimeFare());
            stmt.setInt(6, cabType.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating cab type", e);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        return super.deleteById(id, TABLE_NAME);
    }

    @Override
    public List<CabType> search(String search) {
        List<CabType> cabTypes = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_CAB_TYPE_SQL)) {
            stmt.setString(1, "%" + search + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cabTypes.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching cab types", e);
        }
        return cabTypes;
    }

    @Override
    public boolean checkExist(String typeName) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_CAB_TYPE_SQL)) {
            stmt.setString(1, typeName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking cab type existence", e);
        }
        return false;
    }
}
