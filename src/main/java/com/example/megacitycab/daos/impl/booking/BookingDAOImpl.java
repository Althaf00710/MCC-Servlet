package com.example.megacitycab.daos.impl.booking;

import com.example.megacitycab.DTOs.*;
import com.example.megacitycab.daos.BaseDAOImpl;
import com.example.megacitycab.daos.interfaces.booking.BookingDAO;
import com.example.megacitycab.models.booking.Booking;
import com.example.megacitycab.models.booking.Stop;
import com.example.megacitycab.utils.DbConfig;
import com.example.megacitycab.utils.NumberGenerator;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl extends BaseDAOImpl<Booking> implements BookingDAO {
    private final DbConfig dbConfig = DbConfig.getInstance();
    private final String TABLE_NAME = "booking";
    private final NumberGenerator numberGenerator  = NumberGenerator.getInstance();

    @Override
    protected Booking mapResultSetToEntity(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setBookingNumber(rs.getString("bookingNumber"));
        booking.setCabId(rs.getInt("cabId"));
        booking.setCustomerId(rs.getInt("customerId"));
        booking.setUserId(rs.getInt("userId"));
        booking.setBookingDateTime(rs.getTimestamp("bookingDateTime"));
        booking.setDateTimeCreated(rs.getTimestamp("dateTimeCreated"));
        booking.setStatus(rs.getString("status"));
        booking.setPickupLocation(rs.getString("pickupLocation"));
        booking.setLongitude(rs.getDouble("longitude"));
        booking.setLatitude(rs.getDouble("latitude"));
        booking.setPlaceId(rs.getString("placeId"));
        return booking;
    }

    @Override
    public boolean add(Booking entity) {
        String sql = "INSERT INTO booking (bookingNumber, cabId, customerId, userId, bookingDateTime, " +
                "status, pickupLocation, longitude, latitude, placeId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, numberGenerator.generateBookingNumber());
            stmt.setInt(2, entity.getCabId());
            stmt.setInt(3, entity.getCustomerId());
            stmt.setInt(4, entity.getUserId());
            stmt.setTimestamp(5, new Timestamp(entity.getBookingDateTime().getTime()));
            stmt.setString(6, entity.getStatus());
            stmt.setString(7, entity.getPickupLocation());
            stmt.setDouble(8, entity.getLongitude());
            stmt.setDouble(9, entity.getLatitude());
            stmt.setString(10, entity.getPlaceId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(Booking entity, List<Stop> stops) {
        if (stops == null || stops.isEmpty()) {
            throw new IllegalArgumentException("At least one stop is required");
        }

        Connection conn = null;
        try {
            conn = dbConfig.getConnection();
            conn.setAutoCommit(false);  // Start transaction

            // 1. Insert Booking
            String bookingSql = "INSERT INTO booking (bookingNumber, cabId, customerId, userId, bookingDateTime, " +
                    "status, pickupLocation, longitude, latitude, placeId) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement bookingStmt = conn.prepareStatement(bookingSql, Statement.RETURN_GENERATED_KEYS)) {
                bookingStmt.setString(1, numberGenerator.generateBookingNumber());
                bookingStmt.setInt(2, entity.getCabId());
                bookingStmt.setInt(3, entity.getCustomerId());
                bookingStmt.setInt(4, entity.getUserId());
                bookingStmt.setTimestamp(5, new Timestamp(entity.getBookingDateTime().getTime()));
                bookingStmt.setString(6, entity.getStatus());
                bookingStmt.setString(7, entity.getPickupLocation());
                bookingStmt.setDouble(8, entity.getLongitude());
                bookingStmt.setDouble(9, entity.getLatitude());
                bookingStmt.setString(10, entity.getPlaceId());

                int affectedRows = bookingStmt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet rs = bookingStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // 2. Insert Stops
            String stopSql = "INSERT INTO stop (bookingId, stopLocation, longitude, latitude, " +
                    "placeId, distanceFromLastStop, waitMinutes) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stopStmt = conn.prepareStatement(stopSql)) {
                for (Stop stop : stops) {
                    stopStmt.setInt(1, entity.getId());
                    stopStmt.setString(2, stop.getStopLocation());
                    stopStmt.setDouble(3, stop.getLongitude());
                    stopStmt.setDouble(4, stop.getLatitude());
                    stopStmt.setString(5, stop.getPlaceId());
                    stopStmt.setDouble(6, stop.getDistanceFromLastStop());
                    stopStmt.setInt(7, stop.getWaitMinutes());
                    stopStmt.addBatch();
                }

                int[] batchResults = stopStmt.executeBatch();
                for (int result : batchResults) {
                    if (result == PreparedStatement.EXECUTE_FAILED) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Booking getById(int id) {
        return super.getById(id, TABLE_NAME);
    }

    @Override
    public List<Booking> getAll() {
        return List.of();
    }

    @Override
    public List<Booking> getAll(Timestamp bookingDateTime, String status) {
        System.out.println("Searching "+ bookingDateTime + " " + status);
        List<Booking> bookings = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT " +
                        "    b.id AS bookingId, " +
                        "    b.bookingNumber, " +
                        "    b.cabId, " +
                        "    CONCAT(cb.brandName, ' ', c.cabName, ' | ', c.plateNumber) AS cabName, " +
                        "    b.customerId, " +
                        "    CONCAT(cu.registerNumber, ' | ', cu.name) AS customerName, " +
                        "    b.userId, " +
                        "    u.username AS userName, " +
                        "    b.bookingDateTime, " +
                        "    b.dateTimeCreated, " +
                        "    b.status, " +
                        "    b.pickupLocation, " +
                        "    b.longitude AS pickupLongitude, " +
                        "    b.latitude AS pickupLatitude, " +
                        "    b.placeId AS pickupPlaceId " +
                        "FROM Booking b " +
                        "LEFT JOIN Cab c ON b.cabId = c.id " +
                        "LEFT JOIN CabBrand cb ON c.cabBrandId = cb.id " +
                        "LEFT JOIN Customer cu ON b.customerId = cu.id " +
                        "LEFT JOIN User u ON b.userId = u.id "
        );

        List<Object> params = new ArrayList<>();

        // Add WHERE conditions dynamically
        if (bookingDateTime != null || (status != null && !status.isEmpty())) {
            sql.append("WHERE ");
            boolean addAnd = false;

            if (bookingDateTime != null) {
                sql.append("DATE(b.bookingDateTime) = DATE(?) ");
                params.add(bookingDateTime);
                addAnd = true;
            }

            if (status != null && !status.isEmpty()) {
                if (addAnd) sql.append("AND ");
                sql.append("b.status = ? ");
                params.add(status);
            }
        }

        sql.append("ORDER BY b.id DESC");

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Set parameters dynamically
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i) instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) params.get(i));
                } else {
                    stmt.setString(i + 1, (String) params.get(i));
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setId(rs.getInt("bookingId"));
                    booking.setBookingNumber(rs.getString("bookingNumber"));
                    booking.setCabId(rs.getInt("cabId"));
                    booking.setCustomerId(rs.getInt("customerId"));
                    booking.setUserId(rs.getInt("userId"));
                    booking.setBookingDateTime(rs.getTimestamp("bookingDateTime"));
                    booking.setDateTimeCreated(rs.getTimestamp("dateTimeCreated"));
                    booking.setStatus(rs.getString("status"));
                    booking.setPickupLocation(rs.getString("pickupLocation"));
                    booking.setLongitude(rs.getDouble("pickupLongitude"));
                    booking.setLatitude(rs.getDouble("pickupLatitude"));
                    booking.setPlaceId(rs.getString("pickupPlaceId"));

                    booking.setCabName(rs.getString("cabName"));
                    booking.setCustomerName(rs.getString("customerName"));
                    booking.setUserName(rs.getString("userName"));

                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE Booking SET status = ? WHERE id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public BookingDetailDTO getBookingDetails(int id) {
        String sql = "SELECT\n" +
                "    b.pickupLocation AS pickup_location,\n" +
                "    b.placeId AS pickup_place_id,\n" +
                "    b.dateTimeCreated AS booking_created,\n" +
                "    s.stopLocation AS stop_location,\n" +
                "    s.placeId AS stop_place_id,\n" +
                "    s.distanceFromLastStop,\n" +
                "    s.waitMinutes,\n" +
                "    cd.tax,\n" +
                "    cd.discount,\n" +
                "    cd.minAmountForDiscount,\n" +
                "    ca.driverId AS driver_id,\n" +
                "    d.name AS driver_name,\n" +
                "    d.avatarUrl AS driver_avatar,\n" +
                "    d.phoneNumber AS driver_phone,\n" +
                "    ct.baseFare,\n" +
                "    ct.baseWaitTimeFare,\n" +
                "    ct.typeName AS cab_type,\n" +
                "    CONCAT(c.countryCode, c.phoneNumber) AS customer_phone,\n" +
                "    c.email AS customer_email,\n" +
                "    c.name AS customer_name\n" +
                "FROM Booking b\n" +
                "         JOIN Customer c ON b.customerId = c.id\n" +
                "         JOIN Cab cb ON b.cabId = cb.id\n" +
                "         JOIN CabType ct ON cb.cabTypeId = ct.id\n" +
                "         LEFT JOIN Stop s ON b.id = s.bookingId\n" +
                "         LEFT JOIN CabAssign ca ON cb.id = ca.cabId\n" +
                "    AND ca.assignDate = (\n" +
                "        SELECT MAX(assignDate)\n" +
                "        FROM CabAssign\n" +
                "        WHERE cabId = cb.id\n" +
                "    )\n" +
                "         LEFT JOIN Driver d ON ca.driverId = d.id\n" +
                "         CROSS JOIN CompanyData cd\n" +
                "WHERE b.id = ?\n" +
                "ORDER BY b.id, s.id;";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                BookingDetailDTO booking = null;
                List<StopDTO> stops = new ArrayList<>();

                while (rs.next()) {
                    if (booking == null) {
                        // Initialize booking only once
                        booking = new BookingDetailDTO();

                        // Map booking-level fields
                        booking.setPickupLocation(rs.getString("pickup_location"));
                        booking.setPickupPlaceId(rs.getString("pickup_place_id"));
                        booking.setBookingCreated(rs.getTimestamp("booking_created"));

                        // Map company data
                        CompanyDataDTO companyData = new CompanyDataDTO();
                        companyData.setTax(rs.getDouble("tax"));
                        companyData.setDiscount(rs.getDouble("discount"));
                        companyData.setMinAmountForDiscount(rs.getDouble("minAmountForDiscount"));
                        booking.setCompanyData(companyData);

                        // Map driver data
                        DriverDTO driver = new DriverDTO();
                        driver.setId(rs.getInt("driver_id"));
                        driver.setName(rs.getString("driver_name"));
                        driver.setAvatarUrl(rs.getString("driver_avatar"));
                        driver.setPhoneNumber(rs.getString("driver_phone"));
                        booking.setDriver(driver);

                        // Map cab type data
                        CabTypeDTO cabType = new CabTypeDTO();
                        cabType.setTypeName(rs.getString("cab_type"));
                        cabType.setBaseFare(rs.getDouble("baseFare"));
                        cabType.setBaseWaitTimeFare(rs.getDouble("baseWaitTimeFare"));
                        booking.setCabType(cabType);

                        // Map customer data
                        CustomerDTO customer = new CustomerDTO();
                        customer.setPhone(rs.getString("customer_phone"));
                        customer.setEmail(rs.getString("customer_email"));
                        customer.setName(rs.getString("customer_name"));
                        booking.setCustomer(customer);
                    }

                    // Process stops for EVERY row
                    String stopLocation = rs.getString("stop_location");
                    if (!rs.wasNull()) {
                        StopDTO stop = new StopDTO();
                        stop.setLocation(stopLocation);
                        stop.setPlaceId(rs.getString("stop_place_id"));
                        stop.setDistanceFromLastStop(rs.getDouble("distanceFromLastStop"));
                        stop.setWaitMinutes(rs.getInt("waitMinutes"));
                        stops.add(stop);
                    }
                }

                if (booking != null) {
                    booking.setStops(stops);  // Set all collected stops
                    return booking;
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Booking entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return super.deleteById(id, TABLE_NAME);
    }

    @Override
    public List<Booking> search(String search) {
        return List.of();
    }

    @Override
    public int getTodayBookingCount() {
        String query = "SELECT COUNT(*) AS TodayBookingCount FROM Booking WHERE DATE(bookingDateTime) = CURRENT_DATE";
        int bookingCount = 0;

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                bookingCount = rs.getInt("TodayBookingCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the exception as needed
        }
        return bookingCount;
    }

    @Override
    public List<BookingRecentDTO> getRecentBookingsWithStops() {
        String query = "SELECT "
                + "b.bookingNumber, "
                + "b.placeId AS BookingPlaceId, "
                + "s.placeId AS StopPlaceId, "
                + "b.bookingDateTime AS BookingDateTime "
                + "FROM Booking b "
                + "JOIN Stop s ON b.id = s.bookingId "
                + "WHERE b.bookingDateTime = ( "
                + "    SELECT MIN(bookingDateTime) "
                + "    FROM Booking "
                + "    WHERE bookingDateTime >= CURRENT_TIMESTAMP "
                + ") "
                + "ORDER BY b.bookingDateTime ASC";

        List<BookingRecentDTO> bookingList = new ArrayList<>();
        BookingRecentDTO currentBooking = null;
        List<String> stopPlaceIds = new ArrayList<>();

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String bookingNumber = rs.getString("bookingNumber");
                String bookingPlaceId = rs.getString("BookingPlaceId");
                Timestamp bookingDateTime = rs.getTimestamp("BookingDateTime");
                String stopPlaceId = rs.getString("StopPlaceId");

                if (currentBooking == null || !currentBooking.getBookingNumber().equals(bookingNumber)) {
                    if (currentBooking != null) {
                        currentBooking.setStopPlaceIds(stopPlaceIds);
                        bookingList.add(currentBooking);
                    }

                    stopPlaceIds = new ArrayList<>();
                    currentBooking = new BookingRecentDTO();
                    currentBooking.setBookingNumber(bookingNumber);
                    currentBooking.setBookingPlaceId(bookingPlaceId);
                    currentBooking.setBookingDateTime(bookingDateTime);
                }

                stopPlaceIds.add(stopPlaceId);
            }

            if (currentBooking != null) {
                currentBooking.setStopPlaceIds(stopPlaceIds);
                bookingList.add(currentBooking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    @Override
    public List<TopBookedCabTypeDTO> getTop5BookedCabTypes() {
        List<TopBookedCabTypeDTO> topCabTypes = new ArrayList<>();
        String sql = "SELECT ct.typeName AS CabType, COUNT(b.id) AS BookingsCount " +
                "FROM Booking b " +
                "JOIN Cab c ON b.cabId = c.id " +
                "JOIN CabType ct ON c.cabTypeId = ct.id " +
                "GROUP BY ct.typeName " +
                "ORDER BY BookingsCount DESC " +
                "LIMIT 5";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Process the result set
            while (resultSet.next()) {
                TopBookedCabTypeDTO topCabType = new TopBookedCabTypeDTO();
                topCabType.setCabType(resultSet.getString("CabType"));
                topCabType.setBookingsCount(resultSet.getInt("BookingsCount"));
                topCabTypes.add(topCabType);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topCabTypes;
    }

    @Override
    public List<DailySalesDTO> getDailySales() {
        List<DailySalesDTO> dailySales = new ArrayList<>();
        String sql = "SELECT DATE(billDate) AS BillDate, SUM(totalAmount) AS TotalSales " +
                "FROM Billing " +
                "GROUP BY DATE(billDate) " +
                "ORDER BY BillDate ASC LIMIT 30";

        try (Connection connection = dbConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Process the result set
            while (resultSet.next()) {
                DailySalesDTO dailySale = new DailySalesDTO();
                dailySale.setBillDate(resultSet.getDate("BillDate"));
                dailySale.setTotalSales(resultSet.getDouble("TotalSales"));
                dailySales.add(dailySale);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Log the exception and handle it as needed
        }

        return dailySales;
    }

}
