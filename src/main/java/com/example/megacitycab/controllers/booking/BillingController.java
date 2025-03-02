package com.example.megacitycab.controllers.booking;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.booking.BillingDAO;
import com.example.megacitycab.models.booking.Billing;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/billing/*")
public class BillingController extends HttpServlet {
    private final BillingDAO billingDAO = DAOFactory.getBillingDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/get";

        try {
            switch (action) {
                case "/get":
                    getBilling(request, response);
                    break;
                case "/by-booking":
                    getBillingByBooking(request, response);
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
        if (action == null) action = "/create";

        try {
            switch (action) {
                case "/create":
                    createBilling(request, response);
                    break;
                case "/update":
                    updateBilling(request, response);
                    break;
                case "/delete":
                    deleteBilling(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception ex) {
            handleError(response, ex);
        }
    }

    private void getBilling(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Billing billing = billingDAO.getById(id);
        sendJsonResponse(response, billing);
    }

    private void getBillingByBooking(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        Billing billing = billingDAO.getByBookingId(bookingId);
        sendJsonResponse(response, billing);
    }

    private void createBilling(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Billing billing = gson.fromJson(request.getReader(), Billing.class);
        boolean success = billingDAO.add(billing);
        sendOperationResult(response, success, "Billing created successfully", "Failed to create billing");
    }

    private void updateBilling(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Billing billing = gson.fromJson(request.getReader(), Billing.class);
        boolean success = billingDAO.update(billing);
        sendOperationResult(response, success, "Billing updated successfully", "Failed to update billing");
    }

    private void deleteBilling(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = billingDAO.delete(id);
        sendOperationResult(response, success, "Billing deleted successfully", "Failed to delete billing");
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