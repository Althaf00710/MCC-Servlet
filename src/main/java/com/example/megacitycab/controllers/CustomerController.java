package com.example.megacitycab.controllers;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.CustomerDAO;
import com.example.megacitycab.models.Customer;
import com.example.megacitycab.utils.ImageUploadHandler;
import com.example.megacitycab.utils.Validations;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
@WebServlet("/customers/*")
public class CustomerController extends HttpServlet {
    private final CustomerDAO customerDao = DAOFactory.getCustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";
        switch (action) {
            case "/list":
                listCustomers(request, response);
                break;
            case "/search":
                searchCustomers(request, response);
                break;
            case "/edit":
                editCustomer(request, response);
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
                addCustomer(request, response, session);
                break;
            case "/update":
                updateCustomer(request, response, session);
                break;
            case "/delete":
                deleteCustomer(request, response, session);
                break;
//            case "/updateImage":
//                updateImage(request, response, session);
//                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> customers = customerDao.getAll();
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/views/sites/admin/customers.jsp").forward(request, response);
    }

    private void searchCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        List<Customer> searchedCustomers = customerDao.search(searchTerm);
        request.setAttribute("customers", searchedCustomers);
        request.getRequestDispatcher("/views/sites/admin/customers.jsp").forward(request, response);
    }

    private void editCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int customerId = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerDao.getById(customerId);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("id", customer.getId());
        customerMap.put("name", customer.getName());
        customerMap.put("email", customer.getEmail());
        customerMap.put("phoneNumber", customer.getPhoneNumber());
        customerMap.put("avatarUrl", customer.getAvatarUrl());

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(customerMap));
    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        String nicNumber = request.getParameter("nicNumber");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        Map<String, Boolean> existsMap = customerDao.checkCustomerExists(email, nicNumber, phoneNumber);

        if (existsMap.get("email") || existsMap.get("nicNumber") || existsMap.get("phoneNumber")) {
            session.setAttribute("error", "Customer already exists! " + existsMap);
            response.sendRedirect(request.getContextPath() + "/customers/list");
            return;
        }

        if (!Validations.isValidEmail(email)) {
            session.setAttribute("error", "Invalid or existing email!");
            response.sendRedirect(request.getContextPath() + "/customers/list");
            return;
        }

        String avatarUrl = null;
        try {
            avatarUrl = ImageUploadHandler.getInstance().uploadImage(request, "avatar", "customers");
        } catch (Exception ex) {
            session.setAttribute("error", "Failed to upload customer image!");
            response.sendRedirect(request.getContextPath() + "/customers/list");
            return;
        }

        Customer customer = new Customer.CustomerBuilder()
                .setName(request.getParameter("name"))
                .setEmail(request.getParameter("email"))
                .setPhoneNumber(request.getParameter("phoneNumber"))
                .setRegisterNumber(request.getParameter("registerNumber"))
                .setAddress(request.getParameter("address"))
                .setNicNumber(request.getParameter("nicNumber"))
                .setAvatarUrl(avatarUrl)
                .build();

        boolean success = customerDao.add(customer);
        session.setAttribute(success ? "success" : "error", success ? "Customer added successfully!" : "Failed to add customer!");
        response.sendRedirect(request.getContextPath() + "/customers/list");
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        int customerId = Integer.parseInt(request.getParameter("id"));
        Customer existingCustomer = customerDao.getById(customerId);

        if (existingCustomer == null) {
            session.setAttribute("error", "Customer not found!");
            response.sendRedirect(request.getContextPath() + "/customers/list");
            return;
        }

        Customer updatedCustomer = new Customer.CustomerBuilder()
                .setId(customerId)
                .setName(request.getParameter("name"))
                .setEmail(request.getParameter("email"))
                .setPhoneNumber(request.getParameter("phoneNumber"))
                .setRegisterNumber(request.getParameter("registerNumber"))
                .setAddress(request.getParameter("address"))
                .setNicNumber(request.getParameter("nicNumber"))
                .setAvatarUrl(existingCustomer.getAvatarUrl())
                .build();

        boolean success = customerDao.update(updatedCustomer);
        session.setAttribute(success ? "success" : "error", success ? "Customer updated successfully!" : "Failed to update customer!");
        response.sendRedirect(request.getContextPath() + "/customers/list");
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int customerId = Integer.parseInt(request.getParameter("id"));
        boolean success = customerDao.delete(customerId);

        session.setAttribute(success ? "success" : "error", success ? "Customer deleted successfully!" : "Failed to delete customer!");
        response.sendRedirect(request.getContextPath() + "/customers/list");
    }
}
