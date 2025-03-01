package com.example.megacitycab.controllers;

import com.example.megacitycab.daos.DAOFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import com.example.megacitycab.daos.impl.DriverDAOImpl;
import com.example.megacitycab.daos.interfaces.DriverDAO;
import com.example.megacitycab.models.Driver;
import com.example.megacitycab.utils.ImageUploadHandler;
import com.example.megacitycab.utils.Validations;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.MultipartConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)

@WebServlet("/drivers/*")
public class DriverController extends HttpServlet {
    private final DriverDAO driverDao = DAOFactory.getDriverDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";
        switch (action) {
            case "/list":
                listDrivers(request, response);
                break;
            case "/search":
                searchDrivers(request, response);
                break;
            case "/edit":
                editDriver(request, response);
                break;
            case "/nonassigned":
                nonAssigned(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        HttpSession session = request.getSession();

        switch (action) {
            case "/add":
                addDriver(request, response, session);
                break;
            case "/update":
                updateDriver(request, response, session);
                break;
            case "/delete":
                deleteDriver(request, response, session);
                break;
            case "/updateStatus":
                updateStatus(request, response, session);
                break;
            case "/updateImage":
                updateImage(request, response, session);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listDrivers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Driver> drivers = driverDao.getAllDrivers();
        request.setAttribute("drivers", drivers);
        request.getRequestDispatcher("/views/sites/admin/drivers.jsp").forward(request, response);
    }

    private void nonAssigned(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int cabId = Integer.parseInt(request.getParameter("cabId"));
        List<Driver> drivers = driverDao.getNonAssignedDrivers(cabId);

        Gson gson = new Gson();
        String json = gson.toJson(drivers);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(json);
    }

    private void searchDrivers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        List<Driver> searchedDrivers = driverDao.getDriversBySearch(searchTerm);
        request.setAttribute("drivers", searchedDrivers);
        request.getRequestDispatcher("/views/sites/admin/drivers.jsp").forward(request, response);
    }

    private void editDriver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int driverId = Integer.parseInt(request.getParameter("id"));
        Driver driver = driverDao.getDriverById(driverId);

        Map<String, Object> driverMap = new HashMap<>();
        driverMap.put("id", driver.getId());
        driverMap.put("name", driver.getName());
        driverMap.put("nicNumber", driver.getNicNumber());
        driverMap.put("licenceNumber", driver.getLicenceNumber());
        driverMap.put("phoneNumber", driver.getPhoneNumber());
        driverMap.put("email", driver.getEmail());
        driverMap.put("avatarUrl", driver.getAvatarUrl());

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(driverMap));
    }

    private void addDriver(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        // Extract parameters
        String name = request.getParameter("name");
        String nicNumber = request.getParameter("nicNumber");
        String licenceNumber = request.getParameter("licenceNumber");
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");

        // Validate inputs
        if (!Validations.isValidEmail(email) || driverDao.isEmailTaken(email)) {
            session.setAttribute("error", "Invalid or existing email!");
            response.sendRedirect(request.getContextPath() + "/drivers/list");
            return;
        }

        // Handle avatar upload
        String avatarUrl = null;
        try {
            avatarUrl = ImageUploadHandler.getInstance().uploadImage(request, "avatar", "drivers");
        } catch (Exception ex) {
            session.setAttribute("error", "Failed to upload driver image!");
            response.sendRedirect(request.getContextPath() + "/drivers/list");
            return;
        }

        // Build Driver object
        Driver driver = new Driver.DriverBuilder(name, nicNumber, licenceNumber, phoneNumber, email)
                .setAvatarUrl(avatarUrl)
                .setStatus("INACTIVE") // Default status
                .build();

        // Add to database
        boolean success = driverDao.addDriver(driver);
        if (success) {
            session.setAttribute("success", "Driver added successfully!");
        } else {
            session.setAttribute("error", "Failed to add driver!");
        }
        response.sendRedirect(request.getContextPath() + "/drivers/list");
    }

    private void updateDriver(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        int driverId = Integer.parseInt(request.getParameter("id"));
        Driver existingDriver = driverDao.getDriverById(driverId);

        if (existingDriver == null) {
            session.setAttribute("error", "Driver not found!");
            response.sendRedirect(request.getContextPath() + "/drivers/list");
            return;
        }

        // Build updated Driver
        Driver updatedDriver = new Driver.DriverBuilder(
                request.getParameter("name"),
                request.getParameter("nicNumber"),
                request.getParameter("licenceNumber"),
                request.getParameter("phoneNumber"),
                request.getParameter("email"))
                .setId(driverId)
                .setAvatarUrl(existingDriver.getAvatarUrl())
                .setStatus(existingDriver.getStatus())
                .build();

        boolean success = driverDao.updateDriver(updatedDriver);
        if (success) {
            session.setAttribute("success", "Driver updated successfully!");
        } else {
            session.setAttribute("error", "Failed to update driver!");
        }
        response.sendRedirect(request.getContextPath() + "/drivers/list");
    }

    private void deleteDriver(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int driverId = Integer.parseInt(request.getParameter("id"));
        boolean success = driverDao.deleteDriver(driverId);

        if (success) {
            session.setAttribute("success", "Driver deleted successfully!");
        } else {
            session.setAttribute("error", "Failed to delete driver!");
        }
        response.sendRedirect(request.getContextPath() + "/drivers/list");
    }

    private void updateImage(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        int driverId = Integer.parseInt(request.getParameter("id"));
        Driver driver = driverDao.getDriverById(driverId);

        if (driver == null) {
            session.setAttribute("error", "Driver not found!");
            response.sendRedirect(request.getContextPath() + "/drivers/list");
            return;
        }

        // Handle avatar upload
        String avatarUrl = null;
        try {
            avatarUrl = ImageUploadHandler.getInstance().uploadImage(request, "avatar", "drivers");

            // Delete old image if exists
            if (avatarUrl != null && driver.getAvatarUrl() != null) {
                ImageUploadHandler.getInstance().deleteImage(driver.getAvatarUrl());
            }
        } catch (Exception ex) {
            session.setAttribute("error", "Failed to upload driver image!");
            response.sendRedirect(request.getContextPath() + "/drivers/list");
            return;
        }

        // Update driver avatar
        driver.setAvatarUrl(avatarUrl);
        boolean success = driverDao.updateAvatarUrl(driver);
        if (success) {
            session.setAttribute("success", "Driver avatar updated successfully!");
        } else {
            session.setAttribute("error", "Failed to update driver avatar!");
        }
        response.sendRedirect(request.getContextPath() + "/drivers/list");
    }

    private void updateStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int driverId = Integer.parseInt(request.getParameter("id"));
        String newStatus = request.getParameter("status");

        boolean success = driverDao.updateStatus(driverId, newStatus);
        if (success) {
            session.setAttribute("success", "Driver status updated successfully!");
        } else {
            session.setAttribute("error", "Failed to update driver status!");
        }
        response.sendRedirect(request.getContextPath() + "/drivers/list");
    }

    private void handleDriverSearch(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            List<Driver> drivers = driverDao.getDriversBySearch(request.getParameter("searchTerm"));
            request.setAttribute("drivers", drivers);
            request.getRequestDispatcher("/views/sites/admin/drivers.jsp").forward(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"An error occurred while searching drivers\"}");
            e.printStackTrace();
        }
    }
}
