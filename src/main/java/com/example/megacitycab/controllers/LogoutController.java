package com.example.megacitycab.controllers;

import com.example.megacitycab.daos.impl.UserDAOImpl;
import com.example.megacitycab.models.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/logout")
public class LogoutController extends HttpServlet{
    private final UserDAOImpl userDao = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            String lastActive = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            userDao.updateLastActive(user.getId(), lastActive);
            session.invalidate(); // Invalidate the session
        }
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
