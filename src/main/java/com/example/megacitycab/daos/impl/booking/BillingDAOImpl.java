package com.example.megacitycab.daos.impl.booking;

import com.example.megacitycab.DTOs.BillingListDTO;
import com.example.megacitycab.daos.BaseDAOImpl;
import com.example.megacitycab.daos.interfaces.booking.BillingDAO;
import com.example.megacitycab.daos.interfaces.booking.BookingDAO;
import com.example.megacitycab.models.booking.Billing;
import com.example.megacitycab.models.booking.Booking;
import com.example.megacitycab.utils.DbConfig;

import java.sql.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillingDAOImpl extends BaseDAOImpl<Billing> implements BillingDAO {
    private final DbConfig dbConfig = DbConfig.getInstance();
    private final String TABLE_NAME = "billing";
    private final BookingDAO bookingDAO = new BookingDAOImpl();

    @Override
    protected Billing mapResultSetToEntity(ResultSet rs) throws SQLException {
        Billing billing = new Billing();
        billing.setId(rs.getInt("id"));
        billing.setBookingId(rs.getInt("bookingId"));
        billing.setTotalDistanceFare(rs.getDouble("totalDistanceFare"));
        billing.setTotalWaitFare(rs.getDouble("totalWaitFare"));
        billing.setBillDate(rs.getDate("billDate"));
        billing.setTax(rs.getDouble("tax"));
        billing.setDiscount(rs.getDouble("discount"));
        billing.setUserId(rs.getInt("userId"));
        billing.setCash(rs.getDouble("cash"));
        billing.setDeposit(rs.getDouble("deposit"));
        billing.setCard(rs.getDouble("card"));
        billing.setTotalAmount(rs.getDouble("totalAmount"));
        return billing;
    }

    @Override
    public boolean add(Billing entity) {
        String sql = "INSERT INTO billing (bookingId, totalDistanceFare, totalWaitFare, " +
                "tax, discount, userId, cash, deposit, card, totalAmount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, entity.getBookingId());
            stmt.setDouble(2, entity.getTotalDistanceFare());
            stmt.setDouble(3, entity.getTotalWaitFare());
            stmt.setDouble(4, entity.getTax());
            stmt.setDouble(5, entity.getDiscount());
            stmt.setInt(6, entity.getUserId());
            stmt.setDouble(7, entity.getCash());
            stmt.setDouble(8, entity.getDeposit());
            stmt.setDouble(9, entity.getCard());
            stmt.setDouble(10, entity.getTotalAmount());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    }
                }
                boolean statusUpdated = bookingDAO.updateStatus(entity.getBookingId(), "COMPLETED");
                System.out.println("Update status result: " + statusUpdated);
                return statusUpdated;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Billing getById(int id) {
        return super.getById(id, TABLE_NAME);
    }

    @Override
    public List<BillingListDTO> getTableData() {
        List<BillingListDTO> billingList = new ArrayList<>();
        String sql = """
            SELECT
                b.id AS BillingId,
                bk.id AS BookingId,
                bk.bookingNumber AS BookingNo,
                b.billDate AS BillDate,
                c.registerNumber AS CustomerRegisterNo,
                c.name AS CustomerName,
                CONCAT(c.countryCode, c.phoneNumber) AS CustomerPhone,
                ct.typeName AS CabType,
                (SELECT COALESCE(SUM(s.distanceFromLastStop), 0) FROM Stop s WHERE s.bookingId = bk.id) AS TotalDistance,
                (SELECT COALESCE(SUM(s.waitMinutes), 0) FROM Stop s WHERE s.bookingId = bk.id) AS TotalWaitTime,
                b.totalAmount AS TotalAmount
            FROM Billing b
                     JOIN Booking bk ON b.bookingId = bk.id
                     JOIN Customer c ON bk.customerId = c.id
                     JOIN Cab cb ON bk.cabId = cb.id
                     JOIN CabType ct ON cb.cabTypeId = ct.id
            ORDER BY b.id DESC
            """;

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BillingListDTO billing = new BillingListDTO.Builder()
                        .billingId(rs.getInt("BillingId"))
                        .bookingId(rs.getInt("BookingId"))
                        .bookingNo(rs.getString("BookingNo"))
                        .billDate(rs.getTimestamp("BillDate"))
                        .customerRegisterNo(rs.getString("CustomerRegisterNo"))
                        .customerName(rs.getString("CustomerName"))
                        .customerPhone(rs.getString("CustomerPhone"))
                        .cabType(rs.getString("CabType"))
                        .totalDistance(rs.getDouble("TotalDistance"))
                        .totalWaitTime(rs.getInt("TotalWaitTime"))
                        .totalAmount(rs.getDouble("TotalAmount"))
                        .build();

                billingList.add(billing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return billingList;
    }

    @Override
    public List<Billing> getAll() {
        return super.getAll(TABLE_NAME);
    }

    @Override
    public boolean update(Billing entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return super.deleteById(id, TABLE_NAME);
    }

    @Override
    public List<Billing> search(String search) {
        return List.of();
    }

    @Override
    public Billing getByBookingId(int bookingId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE bookingId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public double getTotalAmountForToday() {
        String query = "SELECT SUM(b.totalAmount) AS TotalAmountToday " +
                "FROM Billing b WHERE DATE(b.billDate) = CURRENT_DATE";
        double totalAmount = 0.0;

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                totalAmount = rs.getDouble("TotalAmountToday");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalAmount;
    }
}
