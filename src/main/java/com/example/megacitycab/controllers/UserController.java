package com.example.megacitycab.controllers;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.impl.UserDAOImpl;
import com.example.megacitycab.daos.interfaces.UserDAO;
import com.example.megacitycab.models.user.User;
import com.example.megacitycab.services.OTPService;
import com.example.megacitycab.utils.ImageUploadHandler;
import com.example.megacitycab.utils.PasswordHasher;
import com.example.megacitycab.utils.Validations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.MultipartConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB before written to disk
        maxFileSize = 5 * 1024 * 1024, // 5MB max file size
        maxRequestSize = 20 * 1024 * 1024 // 20MB max request size
)
@WebServlet("/users/*")
public class UserController extends HttpServlet {
    private final UserDAO userDao = DAOFactory.getUserDAO();
    private final OTPService otpService = new OTPService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        HttpSession session = request.getSession();
        if (action == null) action = "/list";
        switch (action) {
            case "/list":
                List<User> users = userDao.getAllUsers();
                request.setAttribute("users", users);
                request.getRequestDispatcher("/views/sites/admin/user.jsp").forward(request, response);
                break;
            case "/edit":
                int userId = Integer.parseInt(request.getParameter("id"));
                User user = userDao.getUserById(userId);

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getId());
                userMap.put("firstName", user.getFirstName());
                userMap.put("lastName", user.getLastName());
                userMap.put("username", user.getUsername());
                userMap.put("email", user.getEmail());
                userMap.put("countryCode", user.getCountryCode());
                userMap.put("phoneNumber", user.getPhoneNumber());
                userMap.put("avatarUrl", user.getAvatarUrl());

                response.setContentType("application/json");
                response.getWriter().write(new Gson().toJson(userMap));
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
                addUser(request, response, session);
                break;
            case "/update":
                updateUser(request, response, session);
                break;
            case "/delete":
                deleteUser(request, response, session);
                break;
            case "/updateImage":
                updateImage(request, response, session);
                break;
            case "/sendOtp":
                sendOtp(request, response);
                break;
            case "/verifyOtp":
                verifyOtp(request, response);
                break;
            case "/resetPassword":
                resetPassword(request, response);
                break;
            default:
                show404error(request, response);
        }
    }

    private void show404error(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/errors/404.jsp").forward(request, response);
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        // Extract and validate parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String countryCode = request.getParameter("countryCode");
        String phoneNumber = request.getParameter("phoneNumber");

        if (!Validations.isValidEmail(email) || !Validations.isValidPhoneNumber(countryCode, phoneNumber)) {
            session.setAttribute("error", "Invalid email or phone number!");
            response.sendRedirect(request.getContextPath() + "/users/list");
            return;
        }

        if(!Validations.isValidPassword(password)) {
            session.setAttribute("error", "Password does not meet requirements.");
            response.sendRedirect(request.getContextPath() + "/users/list");
            return;
        }

        // Handle avatar image upload
        String avatarUrl = null;
        try {
            avatarUrl = ImageUploadHandler.getInstance().uploadImage(request, "avatar", "users");
        } catch (IOException | ServletException ex) {
            session.setAttribute("error", "Failed to upload user image!");
            response.sendRedirect(request.getContextPath() + "/users/list");
            return;
        }

        // Build User object
        User user = new User.Builder()
                .username(username)
                .password(password) // Password will be hashed in DAO
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .countryCode(countryCode)
                .phoneNumber(phoneNumber)
                .avatarUrl(avatarUrl)
                .build();

        // Add to database
        boolean success = userDao.addUser(user);
        if (success) {
            session.setAttribute("success", "User added successfully!");
        } else {
            session.setAttribute("error", "Password does not meet requirements!");
        }
        response.sendRedirect(request.getContextPath() + "/users/list");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        int userId = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDao.getUserById(userId);

        if (existingUser == null) {
            session.setAttribute("error", "User not found!");
            response.sendRedirect(request.getContextPath() + "/users/list");
            return;
        }

        // Build updated User
        User updatedUser = new User.Builder()
                .id(userId)
                .username(request.getParameter("username"))
                .firstName(request.getParameter("firstName"))
                .lastName(request.getParameter("lastName"))
                .email(request.getParameter("email"))
                .password(request.getParameter("password"))
                .countryCode(request.getParameter("countryCode"))
                .phoneNumber(request.getParameter("phoneNumber"))
                .avatarUrl(request.getParameter("avatarUrl"))
                .build();

        boolean success = userDao.updateUser(updatedUser);
        if (success) {
            session.setAttribute("success", "User updated successfully!");
        } else {
            session.setAttribute("error", "Failed to update user!");
        }
        response.sendRedirect(request.getContextPath() + "/users/list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        boolean success = userDao.deleteUser(userId);

        if (success) {
            session.setAttribute("success", "User deleted successfully!");
        } else {
            session.setAttribute("error", "Failed to delete user!");
        }
        response.sendRedirect(request.getContextPath() + "/users/list");
    }

    private void updateImage(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userDao.getUserById(userId);

        if (user == null) {
            session.setAttribute("error", "User not found!");
            response.sendRedirect(request.getContextPath() + "/users/list");
            return;
        }

        // Handle avatar image upload
        String avatarUrl = null;
        try {
            avatarUrl = ImageUploadHandler.getInstance().uploadImage(request, "avatar", "users");

            if (avatarUrl != null && !avatarUrl.equals(user.getAvatarUrl())) {
                System.out.println(user.getAvatarUrl());
                ImageUploadHandler.getInstance().deleteImage(user.getAvatarUrl());
            }
        } catch (IOException | ServletException ex) {
            session.setAttribute("error", "Failed to upload user image!");
            response.sendRedirect(request.getContextPath() + "/users/list");
            return;
        }

        // Update the avatar URL
        user.setAvatarUrl(avatarUrl);
        boolean success = userDao.updateAvatarUrl(user);
        if (success) {
            session.setAttribute("success", "Avatar updated successfully!");
        } else {
            session.setAttribute("error", "Failed to update avatar!");
        }

        response.sendRedirect(request.getContextPath() + "/users/list");
    }

    private void sendOtp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String input = request.getParameter("input"); // Can be email or username
        User user = userDao.getUserByEmailOrUsername(input);

        if (user == null) {
            response.getWriter().write("{\"error\": \"User not found!\"}");
            return;
        }

        String otp = otpService.generateAndStoreOTP(user.getEmail());
        boolean sent = otpService.sendOtpEmail(user.getEmail(), otp);

        if (sent) {
            response.getWriter().write("{\"success\": \"OTP sent successfully!\"}");
        } else {
            response.getWriter().write("{\"error\": \"Failed to send OTP!\"}");
        }
    }

    private void verifyOtp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String input = request.getParameter("input");
        String otp = request.getParameter("otp");

        User user = userDao.getUserByEmailOrUsername(input);
        if (user == null) {
            response.getWriter().write("{\"error\": \"User not found!\"}");
            return;
        }

        boolean isValid = otpService.verifyOTP(user.getEmail(), otp);
        if (isValid) {
            response.getWriter().write("{\"success\": \"OTP verified!\"}");
        } else {
            response.getWriter().write("{\"error\": \"Invalid OTP!\"}");
        }
    }

    private void resetPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        try {
            // Read JSON payload
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }
            JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);

            String identifier = json.get("input").getAsString();
            String newPassword = json.get("newPassword").getAsString();

            User user = userDao.getUserByEmailOrUsername(identifier);
            if (user == null) {
                out.write(gson.toJson(Map.of("success", false, "error", "User not found")));
                return;
            }

            // Update password using user ID
            boolean success = userDao.updatePassword(user.getEmail(), PasswordHasher.hash(newPassword));

            if (success) {
                out.write(gson.toJson(Map.of("success", true, "message", "Password reset successful")));
            } else {
                out.write(gson.toJson(Map.of("success", false, "error", "Password update failed")));
            }
        } catch (Exception e) {
            out.write(gson.toJson(Map.of("success", false, "error", "Invalid request format")));
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
