package com.example.megacitycab.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getKey/*")
public class MapAPIKeyServlet extends HttpServlet {

    private static final String CONFIG_PARAM_NAME = "MAP_API_KEY";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String apiKey = getServletContext().getInitParameter(CONFIG_PARAM_NAME);

        try (PrintWriter out = response.getWriter()) {
            if (apiKey == null || apiKey.trim().isEmpty()) {
                // Return JSON error
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"API key not configured\"}");
                return;
            }

            out.print("{\"apiKey\": \"" + apiKey + "\"}");

        } catch (Exception e) {
            // Log error and return JSON response
            getServletContext().log("Error retrieving API key", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\": \"Unable to retrieve API key\"}");
        }
    }
}