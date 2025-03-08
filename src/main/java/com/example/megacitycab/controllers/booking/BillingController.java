package com.example.megacitycab.controllers.booking;

import com.example.megacitycab.DTOs.BillingListDTO;
import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.booking.BillingDAO;
import com.example.megacitycab.daos.interfaces.booking.BookingDAO;
import com.example.megacitycab.models.Cab.CabBrand;
import com.example.megacitycab.models.booking.Billing;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/billing/*")
public class BillingController extends HttpServlet {
    private final BillingDAO billingDAO = DAOFactory.getBillingDAO();
    private final BookingDAO bookingDAO = DAOFactory.getBookingDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/get";

        try {
            switch (action) {
                case "/list":
                    listBills(request, response);
                    break;
                case "/get":
                    getBilling(request, response);
                    break;
                case "/by-booking":
                    getBillingByBooking(request, response);
                    break;
                case "/today-total":
                    getTodayTotal(request, response);
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

    private void listBills(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<BillingListDTO> bills = billingDAO.getTableData();
        request.setAttribute("bills", bills);
        request.getRequestDispatcher("/views/sites/admin/booking/viewBills.jsp").forward(request, response);
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
        int billingId = Integer.parseInt(request.getParameter("billingId"));
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        boolean success = billingDAO.delete(billingId) && bookingDAO.updateStatus(bookingId, "ON TRIP");
        sendOperationResult(response, success, "Billing deleted successfully", "Failed to delete billing");
    }

    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(data));
    }

    private void getTodayTotal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        double total = billingDAO.getTotalAmountForToday();
        sendJsonResponse(response, total);
    }

    private void sendOperationResult(HttpServletResponse response, boolean success, String successMsg, String errorMsg) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = "{\"success\": " + success + ", \"message\": \"" + (success ? successMsg : errorMsg) + "\"}";
        response.getWriter().write(jsonResponse);
    }

    private void handleError(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + ex.getMessage() + "\"}");
    }
}