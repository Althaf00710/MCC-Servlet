package com.example.megacitycab.daos.impl;

import com.example.megacitycab.daos.interfaces.UserDAO;
import com.example.megacitycab.models.user.User;
import com.example.megacitycab.utils.DbConfig;
import com.example.megacitycab.utils.PasswordHasher;

import java.sql.*;

import java.util.ArrayList;
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
                return new User.Builder()
                        .id(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .firstName(rs.getString("firstName"))
                        .lastName(rs.getString("lastName"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phoneNumber"))
                        .avatarUrl(rs.getString("avatarUrl"))
                        .lastActive(rs.getString("lastActive"))
                        .build();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO User (username, password, firstName, lastName, email, phoneNumber, avatarUrl) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, PasswordHasher.hash(user.getPassword())); // Hash password
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPhoneNumber());
            if (user.getAvatarUrl() != null) {
                stmt.setString(7, user.getAvatarUrl());
            } else {
                stmt.setNull(7, Types.VARCHAR); // Explicitly set NULL for VARCHAR column
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE User SET username=?, firstName=?, lastName=?, email=?, phoneNumber=?, avatarUrl=? " +
                "WHERE id=?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getAvatarUrl());
            stmt.setInt(7, user.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM User WHERE id=?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User.Builder()
                        .id(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .firstName(rs.getString("firstName"))
                        .lastName(rs.getString("lastName"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phoneNumber"))
                        .avatarUrl(rs.getString("avatarUrl"))
                        .lastActive(rs.getString("lastActive"))
                        .build();
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    @Override
    public User getUserById(int userId) {
        String sql = "SELECT * FROM User WHERE id=?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User.Builder()
                        .id(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .firstName(rs.getString("firstName"))
                        .lastName(rs.getString("lastName"))
                        .email(rs.getString("email"))
                        .phoneNumber(rs.getString("phoneNumber"))
                        .avatarUrl(rs.getString("avatarUrl"))
                        .lastActive(rs.getString("lastActive"))
                        .build();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void updateLastActive(int userId, String lastActive) {
        String sql = "UPDATE User SET lastActive=? WHERE id=?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lastActive);
            stmt.setInt(2, userId);
            stmt.executeUpdate();

            System.out.println("Updated lastActive for user to " + lastActive);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
