package com.example.megacitycab.controllers;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.CustomerDAO;
import com.example.megacitycab.models.Customer;
import com.example.megacitycab.utils.Email;
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
            case "/getCustomers":
                getCustomers(request, response);
                break;
            case "/search":
                searchCustomers(request, response);
                break;
            case "/edit":
                editCustomer(request, response);
                break;
            case "/booking":
                customerSite(request, response);
                break;
            case "/logout":
                logout(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
            case "/send-otp":
                sendOTP(request, response, session);
                break;
            case "/verify-otp":
                verifyOTP(request, response, session);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void sendOTP(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String email = request.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            sendJsonResponse(response, Map.of("status", "error", "message", "Email required"));
            return;
        }

        if (!customerDao.checkCustomerExistsByEmail(email)) {
            sendJsonResponse(response, Map.of("status", "error", "message", "Customer not found"));
            return;
        }

        int otp = (int) (Math.random() * 9000) + 1000;
        session.setAttribute("otp", String.valueOf(otp));
        session.setAttribute("otpEmail", email);
        session.setAttribute("otpTime", System.currentTimeMillis());

        try {
            Email.sendEmail(email, "Login OTP", "Your OTP is: " + otp);
            sendJsonResponse(response, Map.of("status", "success", "message", "OTP sent"));
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(response, Map.of("status", "error", "message", "Failed to send OTP. Please try again."));
        }
    }

    private void verifyOTP(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String email = request.getParameter("email");
        String userOTP = request.getParameter("otp");
        String storedOTP = (String) session.getAttribute("otp");
        String storedEmail = (String) session.getAttribute("otpEmail");
        Long otpTime = (Long) session.getAttribute("otpTime");

        Map<String, Object> responseMap = new HashMap<>();
        if (otpTime == null || System.currentTimeMillis() - otpTime > 300000) { // 5 minutes
            responseMap.put("status", "error");
            responseMap.put("message", "OTP expired");
        } else if (!email.equals(storedEmail) || !userOTP.equals(storedOTP)) {
            responseMap.put("status", "error");
            responseMap.put("message", "Invalid OTP");
        } else {
            Customer customer = customerDao.getCustomerByEmail(email);
            if (customer != null) {
                session.setAttribute("customer", customer);
                responseMap.put("status", "success");
                responseMap.put("customer", customer);
            } else {
                responseMap.put("status", "Customer Not Exist");
            }
            session.removeAttribute("otp");
            session.removeAttribute("otpEmail");
            session.removeAttribute("otpTime");
        }
        sendJsonResponse(response, responseMap);
    }

    private void sendJsonResponse(HttpServletResponse response, Map<String, Object> data) throws IOException {
        response.setContentType("application/json");
        new Gson().toJson(data, response.getWriter());
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> customers = customerDao.getAll();
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/views/sites/admin/customer/customers.jsp").forward(request, response);
    }

    private void logout (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/customers/booking");
    }

    private void customerSite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/sites/customer/index.jsp").forward(request, response);
    }

    private void getCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> customers = customerDao.getAll();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(customers);
        response.getWriter().write(jsonResponse);
    }

    private void searchCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        List<Customer> searchedCustomers = customerDao.search(searchTerm);
        request.setAttribute("customers", searchedCustomers);
        request.getRequestDispatcher("/views/sites/admin/customer/customers.jsp").forward(request, response);
    }

    private void editCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int customerId = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerDao.getById(customerId);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("id", customer.getId());
        customerMap.put("name", customer.getName());
        customerMap.put("email", customer.getEmail());
        customerMap.put("address", customer.getAddress());
        customerMap.put("countryCode", customer.getCountryCode());
        customerMap.put("phoneNumber", customer.getPhoneNumber());
        customerMap.put("nicNumber", customer.getNicNumber());

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(customerMap));
    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        String nicNumber = request.getParameter("nicNumber");
        String email = request.getParameter("email");
        String countryCode = request.getParameter("countryCode");
        String phoneNumber = request.getParameter("phoneNumber");

        Map<String, Boolean> existsMap = customerDao.checkCustomerExists(email, nicNumber, countryCode, phoneNumber);

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

        Customer customer = new Customer.CustomerBuilder()
                .setName(request.getParameter("name"))
                .setEmail(request.getParameter("email"))
                .setCountryCode(request.getParameter("countryCode"))
                .setPhoneNumber(request.getParameter("phoneNumber"))
                .setRegisterNumber(request.getParameter("registerNumber"))
                .setAddress(request.getParameter("address"))
                .setNicNumber(request.getParameter("nicNumber"))
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
                .setCountryCode(request.getParameter("countryCode"))
                .setPhoneNumber(request.getParameter("phoneNumber"))
                .setRegisterNumber(request.getParameter("registerNumber"))
                .setAddress(request.getParameter("address"))
                .setNicNumber(request.getParameter("nicNumber"))
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
