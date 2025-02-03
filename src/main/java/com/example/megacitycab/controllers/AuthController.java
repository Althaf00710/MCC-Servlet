package com.example.megacitycab.controllers;

import com.example.megacitycab.models.user.User;
import com.example.megacitycab.services.AuthService;
import com.example.megacitycab.utils.Validations;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class AuthController extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (!Validations.isNotNullOrEmpty(username) || !Validations.isValidPassword(password)) {
            request.setAttribute("error", "Invalid input!");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        User user = authService.authenticate(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/views/sites/admin/dashboard.jsp");
        } else {
            request.setAttribute("error", "Invalid credentials!");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
