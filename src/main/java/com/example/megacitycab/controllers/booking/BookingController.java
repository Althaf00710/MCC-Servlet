package com.example.megacitycab.controllers.booking;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.booking.BookingDAO;
import com.example.megacitycab.models.booking.Booking;
import com.example.megacitycab.models.booking.Stop;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
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
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception ex) {
            handleError(response, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/add";

        try {
            switch (action) {
                case "/add":
                    addBooking(request, response);
                    break;
                case "/update":
                    updateBooking(request, response);
                    break;
                case "/delete":
                    deleteBooking(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception ex) {
            handleError(response, ex);
        }
    }

    private void listBookings(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Booking> bookings = bookingDAO.getAll();
        sendJsonResponse(response, bookings);
    }

    private void getBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Booking booking = bookingDAO.getById(id);
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

            JsonObject bookingJson = jsonObject.getAsJsonObject();
            bookingJson.remove("stops");
            Booking booking = gson.fromJson(bookingJson, Booking.class);

            JsonArray stopsArray = jsonObject.getAsJsonArray("stops");
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

    private void updateBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Booking booking = gson.fromJson(request.getReader(), Booking.class);
        boolean success = bookingDAO.update(booking);
        sendOperationResult(response, success, "Booking updated successfully", "Failed to update booking");
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