package com.example.megacitycab.controllers.booking;

import com.example.megacitycab.DTOs.BookingDetailDTO;
import com.example.megacitycab.DTOs.BookingRecentDTO;
import com.example.megacitycab.DTOs.DailySalesDTO;
import com.example.megacitycab.DTOs.TopBookedCabTypeDTO;
import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.booking.BookingDAO;
import com.example.megacitycab.models.booking.Booking;
import com.example.megacitycab.models.booking.Stop;

import com.google.gson.*;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/booking/*")
public class BookingController extends HttpServlet {
    private final BookingDAO bookingDAO = DAOFactory.getBookingDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";

        try {
            switch (action) {
                case "/add-booking":
                    request.getRequestDispatcher("/views/sites/admin/booking/addBooking.jsp").forward(request, response);
                    break;
                case "/list":
                    listBookings(request, response);
                    break;
                case "/get":
                    getBooking(request, response);
                    break;
                case "/search":
                    searchBookings(request, response);
                    break;
                case "/booking-details":
                    getBookingWithStops(request, response);
                    break;
                case "/booking-count":
                    getBookingTodayCount(request, response);
                    break;
                case "/new-booking":
                    getBookingNew(request, response);
                    break;
                case "/get-recent-pickup":
                    getRecentPickup(request, response);
                    break;
                case "/get-topCabType":
                    getTopCabType(request, response);
                    break;
                case "/getDailySales":
                    getDailySales(request, response);
                    break;
                default:
                    show404error(request, response);
            }
        } catch (Exception ex) {
            handleError(response, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        HttpSession session = request.getSession();
        if (action == null) action = "/add";

        try {
            switch (action) {
                case "/add":
                    addBooking(request, response);
                    break;
                case "/updateStatus":
                    updateStatus(request, response, session);
                    break;
                case "/delete":
                    deleteBooking(request, response);
                    break;
                default:
                    show404error(request, response);
            }
        } catch (Exception ex) {
            handleError(response, ex);
        }
    }

    private void show404error(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/errors/404.jsp").forward(request, response);
    }

    private void listBookings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String status = request.getParameter("status");
            String dateParam = request.getParameter("bookingDateTime");
            Timestamp bookingDateTime = null;

            if (dateParam != null && !dateParam.isEmpty()) {
                try {
                    bookingDateTime = Timestamp.valueOf(dateParam + " 00:00:00"); // Convert only if valid
                } catch (IllegalArgumentException e) {
                    bookingDateTime = null; // Ensure it's null if conversion fails
                }
            }

            List<Booking> bookings = bookingDAO.getAll(bookingDateTime, status);

            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("/views/sites/admin/booking/viewBooking.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving bookings.");
        }
    }

    private void getBookingTodayCount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int total = bookingDAO.getTodayBookingCount();
        sendJsonResponse(response, total);
    }

    private void getDailySales(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<DailySalesDTO> sales = bookingDAO.getDailySales();
        sendJsonResponse(response, sales);
    }

    private void getTopCabType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<TopBookedCabTypeDTO> cabTypes= bookingDAO.getTop5BookedCabTypes();
        sendJsonResponse(response, cabTypes);
    }

    private void getRecentPickup(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<BookingRecentDTO> booking = bookingDAO.getRecentBookingsWithStops();
        sendJsonResponse(response, booking);
    }

    private void getBookingNew(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("status");
        String dateParam = request.getParameter("bookingDateTime");
        Timestamp bookingDateTime = null;

        if (dateParam != null && !dateParam.isEmpty()) {
            try {
                bookingDateTime = Timestamp.valueOf(dateParam + " 00:00:00"); // Convert only if valid
            } catch (IllegalArgumentException e) {
                bookingDateTime = null; // Ensure it's null if conversion fails
            }
        }
        List<Booking> bookings = bookingDAO.getAll(bookingDateTime, status);
        sendJsonResponse(response, bookings);
    }

    private void getBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Booking booking = bookingDAO.getById(id);
        sendJsonResponse(response, booking);
    }

    private void getBookingWithStops(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        BookingDetailDTO booking = bookingDAO.getBookingDetails(id);
        sendJsonResponse(response, booking);
    }

    private void searchBookings(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String searchTerm = request.getParameter("term");
        List<Booking> results = bookingDAO.search(searchTerm);
        sendJsonResponse(response, results);
    }

    private void addBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            JsonObject jsonObject = JsonParser.parseReader(request.getReader()).getAsJsonObject();
            JsonArray stopsArray = jsonObject.getAsJsonArray("stops");
            JsonObject bookingJson = jsonObject.deepCopy();
            bookingJson.remove("stops");

            // Now parse both parts
            Booking booking = gson.fromJson(bookingJson, Booking.class);
            List<Stop> stops = gson.fromJson(stopsArray, new TypeToken<List<Stop>>(){}.getType());

            // Validate at least one stop
            if (stops == null || stops.isEmpty()) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                        "At least one stop is required.");
                return;
            }

            boolean success = bookingDAO.add(booking, stops);

            sendOperationResult(response, success,
                    "Booking with stops added successfully",
                    "Failed to add booking with stops");

        } catch (JsonSyntaxException | IllegalStateException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error processing request: " + e.getMessage());
        }
    }

    // Helper method for error responses
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message)
            throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        JsonObject errorResponse = new JsonObject();
        errorResponse.addProperty("success", false);
        errorResponse.addProperty("message", message);
        response.getWriter().write(gson.toJson(errorResponse));
    }

    private void updateStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String status = request.getParameter("status");

        if (bookingDAO.updateStatus(id, status)) {
            session.setAttribute("success", "Cab brand updated successfully!");
        } else {
            session.setAttribute("error", "Failed to update cab brand.");
        }

        response.sendRedirect(request.getContextPath() + "/booking/list");

    }

    private void deleteBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = bookingDAO.delete(id);
        sendOperationResult(response, success, "Booking deleted successfully", "Failed to delete booking");
    }

    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(data));
    }

    private void sendOperationResult(HttpServletResponse response, boolean success,
                                     String successMsg, String errorMsg) throws IOException {
        response.setContentType("application/json");
        if (success) {
            response.getWriter().write("{\"status\":\"success\", \"message\":\"" + successMsg + "\"}");
        } else {
            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + errorMsg + "\"}");
        }
    }

    private void handleError(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
    }
}

//{
//        "cabId": 1,
//        "customerId": 1,
//        "userId": 1,
//        "bookingDateTime": "2023-08-01T12:00:00",
//        "status": "CONFIRMED",
//        "pickupLocation": "123 Main St",
//        "longitude": 79.8612,
//        "latitude": 6.9271,
//        "placeId": "ChIJ123...",
//        "stops": [
//        {
//        "stopLocation": "123 Main St",
//        "longitude": 79.8612,
//        "latitude": 6.9271,
//        "placeId": "ChIJ123...",
//        "distanceFromLastStop": 0.0,
//        "waitMinutes": 15
//        },
//        {
//        "stopLocation": "456 Oak St",
//        "longitude": 79.8620,
//        "latitude": 6.9280,
//        "placeId": "ChIJ456...",
//        "distanceFromLastStop": 1.5,
//        "waitMinutes": 0
//        }
//        ]
//        }