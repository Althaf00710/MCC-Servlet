package com.example.megacitycab.daos.impl.cab;

import com.example.megacitycab.daos.BaseDAOImpl;
import com.example.megacitycab.daos.interfaces.cab.CabBrandDAO;
import com.example.megacitycab.models.Cab.CabBrand;
import com.example.megacitycab.models.Cab.CabType;
import com.example.megacitycab.utils.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CabBrandDAOImpl extends BaseDAOImpl<CabBrand> implements CabBrandDAO {

    private static final Logger LOGGER = Logger.getLogger(CabTypeDAOImpl.class.getName());
    private final DbConfig dbConfig = DbConfig.getInstance();
    private static final String TABLE_NAME = "cabbrand";

    private static final String INSERT_CAB_BRAND_SQL = "INSERT INTO " + TABLE_NAME + " (brandName) VALUES (?)";
    private static final String UPDATE_CAB_BRAND_SQL = "UPDATE " + TABLE_NAME + " SET brandName = ? WHERE id = ?";
    private static final String SEARCH_CAB_BRAND_SQL = "SELECT * FROM " + TABLE_NAME + " WHERE brandName LIKE ?";
    private static final String CHECK_CAB_BRAND_SQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE brandName = ?";

    @Override
    protected CabBrand mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new CabBrand.CabBrandBuilder()
                .setId(rs.getInt("id"))
                .setBrandName(rs.getString("brandName"))
                .build();
    }

    @Override
    public boolean checkExist(String brandName) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_CAB_BRAND_SQL)) {
            stmt.setString(1, brandName);

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

    @Override
    public boolean add(CabBrand entity) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CAB_BRAND_SQL)) {
            stmt.setString(1, entity.getBrandName());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding CabBrand", e);
            return false;
        }
    }

    @Override
    public CabBrand getById(int id) {
        return super.getById(id, TABLE_NAME);
    }

    @Override
    public List<CabBrand> getAll() {
        return super.getAll(TABLE_NAME);
    }

    @Override
    public boolean update(CabBrand entity) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CAB_BRAND_SQL)) {
            stmt.setString(1, entity.getBrandName());
            stmt.setInt(2, entity.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating CabBrand", e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        return super.deleteById(id, TABLE_NAME);
    }

    @Override
    public List<CabBrand> search(String search) {
        List<CabBrand> brands = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_CAB_BRAND_SQL)) {
            stmt.setString(1, "%" + search + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    brands.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching CabBrands", e);
        }
        return brands;
    }
}
