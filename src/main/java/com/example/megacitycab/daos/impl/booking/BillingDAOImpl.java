package com.example.megacitycab.daos.impl.booking;

import com.example.megacitycab.daos.BaseDAOImpl;
import com.example.megacitycab.daos.interfaces.booking.BillingDAO;
import com.example.megacitycab.daos.interfaces.booking.BookingDAO;
import com.example.megacitycab.models.booking.Billing;
import com.example.megacitycab.models.booking.Booking;
import com.example.megacitycab.utils.DbConfig;

import java.sql.*;
import java.sql.SQLException;
import java.sql.Statement;
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
        billing.setBookingId(rs.getInt("booking_id"));
        billing.setTotalDistanceFare(rs.getDouble("total_distance_fare"));
        billing.setTotalWaitFare(rs.getDouble("total_wait_fare"));
        billing.setBillDate(rs.getDate("bill_date"));
        billing.setTax(rs.getDouble("tax"));
        billing.setDiscount(rs.getDouble("discount"));
        billing.setUserId(rs.getInt("user_id"));
        billing.setCash(rs.getDouble("cash"));
        billing.setDeposit(rs.getDouble("deposit"));
        billing.setCard(rs.getDouble("card"));
        billing.setTotalAmount(rs.getDouble("total_amount"));
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
}
