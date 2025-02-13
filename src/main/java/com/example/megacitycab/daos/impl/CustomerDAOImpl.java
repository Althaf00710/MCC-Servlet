package com.example.megacitycab.daos.impl;

import com.example.megacitycab.daos.BaseDAOImpl;
import com.example.megacitycab.daos.interfaces.CustomerDAO;
import com.example.megacitycab.models.Customer;
import com.example.megacitycab.utils.DbConfig;
import com.example.megacitycab.utils.NumberGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDAOImpl extends BaseDAOImpl<Customer> implements CustomerDAO {
    private static final Logger LOGGER = Logger.getLogger(CustomerDAOImpl.class.getName());
    private final DbConfig dbConfig = DbConfig.getInstance();
    private final NumberGenerator numberGenerator = NumberGenerator.getInstance();
    private static final String TABLE_NAME = "customer";

    @Override
    protected Customer mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Customer.CustomerBuilder()
                .setId(rs.getInt("id"))
                .setRegisterNumber(rs.getString("registerNumber"))
                .setName(rs.getString("name"))
                .setAddress(rs.getString("address"))
                .setPhoneNumber(rs.getString("phoneNumber"))
                .setEmail(rs.getString("email"))
                .setNicNumber(rs.getString("nicNumber"))
                .setAvatarUrl(rs.getString("avatarUrl"))
                .build();
    }

    private static final String INSERT_CUSTOMER_SQL = "INSERT INTO "+ TABLE_NAME + " (name, registerNumber, address, phoneNumber, email, nicNumber, avatarUrl) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_CUSTOMER_SQL = "UPDATE "+ TABLE_NAME +" SET name = ?, registerNumber = ?, address = ?, phoneNumber = ?, email = ?, nicNumber = ? WHERE id = ?";
    private static final String SEARCH_CUSTOMER_SQL = "SELECT * FROM "+ TABLE_NAME +" WHERE name LIKE ? OR registerNumber LIKE ? OR phoneNumber LIKE ? OR email LIKE ?";
    private static final String CHECK_CUSTOMER_SQL = "SELECT email, nicNumber, phoneNumber FROM customer WHERE email = ? OR nicNumber = ? OR phoneNumber = ?";

    @Override
    public boolean add(Customer customer) {
        if (customer.getRegisterNumber() == null || customer.getRegisterNumber().isEmpty()) {
            customer.setRegisterNumber(numberGenerator.CustomerRegisterNumber());
        }

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_CUSTOMER_SQL)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getRegisterNumber());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setString(5, customer.getEmail());
            stmt.setString(6, customer.getNicNumber());
            stmt.setString(7, customer.getAvatarUrl());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting customer", e);
        }
        return false;
    }

    @Override
    public Customer getById(int id) {
        return super.getById(id, TABLE_NAME);
    }

    @Override
    public List<Customer> getAll() {
        return super.getAll(TABLE_NAME);
    }

    @Override
    public boolean update(Customer customer) {
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_CUSTOMER_SQL)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getRegisterNumber());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setString(5, customer.getEmail());
            stmt.setString(6, customer.getNicNumber());
            stmt.setInt(7, customer.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating customer", e);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        return super.deleteById(id, TABLE_NAME);
    }

    @Override
    public List<Customer> search(String search) {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_CUSTOMER_SQL)) {

            String searchTerm = "%" + search + "%";
            ParameterMetaData metaData = stmt.getParameterMetaData();
            int paramCount = metaData.getParameterCount();

            for (int i = 1; i <= paramCount; i++) {
                stmt.setString(i, searchTerm);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToEntity(rs)); // Using the existing mapping method
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching customers", e);
        }
        return customers;
    }

    @Override
    public Map<String, Boolean> checkCustomerExists(String email, String nicNumber, String phoneNumber) {
        Map<String, Boolean> existsMap = new HashMap<>();
        existsMap.put("email", false);
        existsMap.put("nicNumber", false);
        existsMap.put("phoneNumber", false);

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_CUSTOMER_SQL)) {

            stmt.setString(1, email);
            stmt.setString(2, nicNumber);
            stmt.setString(3, phoneNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (email.equals(rs.getString("email"))) {
                        existsMap.put("email", true);
                    }
                    if (nicNumber.equals(rs.getString("nicNumber"))) {
                        existsMap.put("nicNumber", true);
                    }
                    if (phoneNumber.equals(rs.getString("phoneNumber"))) {
                        existsMap.put("phoneNumber", true);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking customer existence", e);
        }
        return existsMap;
    }

}
