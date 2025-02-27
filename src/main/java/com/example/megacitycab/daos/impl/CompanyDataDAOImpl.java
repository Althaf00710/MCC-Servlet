package com.example.megacitycab.daos.impl;

import com.example.megacitycab.daos.interfaces.CompanyDataDAO;
import com.example.megacitycab.models.CompanyData;
import com.example.megacitycab.utils.DbConfig;

import java.sql.*;

public class CompanyDataDAOImpl implements CompanyDataDAO {
    private final DbConfig dbConfig = DbConfig.getInstance();

    @Override
    public boolean updateData(CompanyData companyData) {
        String query = "UPDATE company_data SET address = ?, phone_number = ?, email = ?, tax = ?, discount = ?, min_amount_for_discount = ? WHERE id = 1";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, companyData.getAddress());
            stmt.setString(2, companyData.getPhoneNumber());
            stmt.setString(3, companyData.getEmail());
            stmt.setDouble(4, companyData.getTax());
            stmt.setDouble(5, companyData.getDiscount());
            stmt.setDouble(6, companyData.getMinAmountForDiscount());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CompanyData getCompanyData() {
        String query = "SELECT * FROM companydata WHERE id = 1";
        CompanyData companyData = null;

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                companyData = new CompanyData();
                companyData.setAddress(rs.getString("address"));
                companyData.setPhoneNumber(rs.getString("phoneNumber"));
                companyData.setEmail(rs.getString("email"));
                companyData.setTax(rs.getDouble("tax"));
                companyData.setDiscount(rs.getDouble("discount"));
                companyData.setMinAmountForDiscount(rs.getDouble("minAmountForDiscount"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companyData;
    }

}
