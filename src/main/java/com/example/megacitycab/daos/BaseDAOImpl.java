package com.example.megacitycab.daos;

import com.example.megacitycab.utils.DbConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAOImpl<T> {
    protected final DbConfig dbConfig = DbConfig.getInstance();

    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    public T getById(int id, String tableName) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> getAll(String tableName) {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
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

    public boolean deleteById(int id, String tableName) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
