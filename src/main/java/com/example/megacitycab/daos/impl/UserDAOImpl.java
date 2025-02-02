package com.example.megacitycab.daos.impl;

import com.example.megacitycab.daos.interfaces.UserDAO;
import com.example.megacitycab.models.user.User;
import com.example.megacitycab.utils.DbConfig;
import java.sql.*;

import java.util.List;

public class UserDAOImpl implements UserDAO {
    private final DbConfig dbConfig = DbConfig.getInstance();

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM User WHERE username = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }

    @Override
    public boolean deleteUser(int userId) {
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }
}
