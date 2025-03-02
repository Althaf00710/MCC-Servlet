package com.example.megacitycab.controllers.booking;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.booking.BookingDAO;
import com.example.megacitycab.models.booking.Booking;
import com.google.gson.Gson;
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
        Booking booking = gson.fromJson(request.getReader(), Booking.class);
        boolean success = bookingDAO.add(booking);
        sendOperationResult(response, success, "Booking added successfully", "Failed to add booking");
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
