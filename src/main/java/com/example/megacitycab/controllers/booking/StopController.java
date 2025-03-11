package com.example.megacitycab.controllers.booking;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.booking.StopDAO;
import com.example.megacitycab.models.booking.Stop;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/stop/*")
public class StopController extends HttpServlet {
    private final StopDAO stopDAO = DAOFactory.getStopDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";

        try {
            switch (action) {
                case "/list":
                    listStops(request, response);
                    break;
                case "/by-booking":
                    getStopsByBooking(request, response);
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
        if (action == null) action = "/add";

        try {
            switch (action) {
                case "/add":
                    addStop(request, response);
                    break;
                case "/update":
                    updateStop(request, response);
                    break;
                case "/delete":
                    deleteStop(request, response);
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

    private void listStops(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Stop> stops = stopDAO.getAll();
        sendJsonResponse(response, stops);
    }

    private void getStopsByBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        // Implement this method in StopDAO
        List<Stop> stops = stopDAO.getByBookingId(bookingId);
        sendJsonResponse(response, stops);
    }

    private void addStop(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Stop stop = gson.fromJson(request.getReader(), Stop.class);
        boolean success = stopDAO.add(stop);
        sendOperationResult(response, success, "Stop added successfully", "Failed to add stop");
    }

    private void updateStop(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Stop stop = gson.fromJson(request.getReader(), Stop.class);
        boolean success = stopDAO.update(stop);
        sendOperationResult(response, success, "Stop updated successfully", "Failed to update stop");
    }

    private void deleteStop(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = stopDAO.delete(id);
        sendOperationResult(response, success, "Stop deleted successfully", "Failed to delete stop");
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
