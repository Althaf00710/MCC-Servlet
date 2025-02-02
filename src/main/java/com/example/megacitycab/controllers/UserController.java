package com.example.megacitycab.controllers;

import com.example.megacitycab.daos.impl.UserDAOImpl;
import com.example.megacitycab.daos.interfaces.UserDAO;
import com.example.megacitycab.models.user.User;
import com.example.megacitycab.utils.Validations;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users/*")
public class UserController extends HttpServlet {
    private final UserDAO userDao = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";
        switch (action) {
            case "/list":
                List<User> users = userDao.getAllUsers();
                request.setAttribute("users", users);
                request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
                break;
            case "/edit":
                int userId = Integer.parseInt(request.getParameter("id"));
                User user = userDao.getUserById(userId);
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/views/admin/edit-user.jsp").forward(request, response);
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
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        // Extract and validate parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String avatarUrl = request.getParameter("avatarUrl");

        if (!Validations.isValidEmail(email) || !Validations.isValidPhoneNumber(phoneNumber)) {
            session.setAttribute("error", "Invalid email or phone number!");
            response.sendRedirect(request.getContextPath() + "/admin/users/list");
            return;
        }

        // Build User object
        User user = new User.Builder()
                .username(username)
                .password(password) // Password will be hashed in DAO
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .avatarUrl(avatarUrl)
                .build();

        // Add to database
        boolean success = userDao.addUser(user);
        if (success) {
            session.setAttribute("success", "User added successfully!");
        } else {
            session.setAttribute("error", "Failed to add user!");
        }
        response.sendRedirect(request.getContextPath() + "/admin/users/list");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException, ServletException {
        int userId = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDao.getUserById(userId);

        if (existingUser == null) {
            session.setAttribute("error", "User not found!");
            response.sendRedirect(request.getContextPath() + "/admin/users/list");
            return;
        }

        // Build updated User
        User updatedUser = new User.Builder()
                .id(userId)
                .username(request.getParameter("username"))
                .firstName(request.getParameter("firstName"))
                .lastName(request.getParameter("lastName"))
                .email(request.getParameter("email"))
                .phoneNumber(request.getParameter("phoneNumber"))
                .avatarUrl(request.getParameter("avatarUrl"))
                .build();

        boolean success = userDao.updateUser(updatedUser);
        if (success) {
            session.setAttribute("success", "User updated successfully!");
        } else {
            session.setAttribute("error", "Failed to update user!");
        }
        response.sendRedirect(request.getContextPath() + "/admin/users/list");
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
        response.sendRedirect(request.getContextPath() + "/admin/users/list");
    }
}
