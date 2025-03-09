package com.example.megacitycab.controllers;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.CompanyDataDAO;
import com.example.megacitycab.models.CompanyData;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/companydata/*")
public class CompanyDataController extends HttpServlet {
    private final CompanyDataDAO companyDataDAO = DAOFactory.getCompanyDataDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // Get path after "/companydata"

        if ("/get".equals(pathInfo)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            CompanyData companyData = companyDataDAO.getCompanyData();
            if (companyData != null) {
                String jsonResponse = gson.toJson(companyData);
                response.getWriter().write(jsonResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Company Data Not Found\"}");
            }
        } else {
            // Default behavior: forward to JSP
            CompanyData companyData = companyDataDAO.getCompanyData();
            if (companyData != null) {
                request.setAttribute("companyData", companyData);
                request.getRequestDispatcher("/views/sites/admin/companydata.jsp").forward(request, response);
            } else {
                response.sendRedirect("error.jsp?message=Company Data Not Found");
            }
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path != null && path.equals("/update")) {
            updateCompanyData(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }

    private void updateCompanyData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        CompanyData updatedCompanyData = gson.fromJson(reader, CompanyData.class);

        boolean success = companyDataDAO.updateData(updatedCompanyData);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (success) {
            response.getWriter().write("{\"message\": \"Company data updated successfully!\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to update company data\"}");
        }
    }
}
