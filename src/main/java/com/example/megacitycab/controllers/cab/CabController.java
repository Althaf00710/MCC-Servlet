package com.example.megacitycab.controllers.cab;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.cab.CabBrandDAO;
import com.example.megacitycab.daos.interfaces.cab.CabDAO;
import com.example.megacitycab.models.Cab.Cab;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/cabs/*")
public class CabController extends HttpServlet {
    private final CabDAO cabDAO = DAOFactory.getCabDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";
        switch (action) {
            case "/list":
                listCabs(request, response);
                break;
            case "/edit":
                editCab(request, response);
                break;
            case "/search":
                searchCab(request, response);
                break;
            case "/getByType":
                getByType(request, response);
                break;
            default:
                show404error(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        HttpSession session = request.getSession();

        switch (action) {
            case "/add":
                addCab(request, response, session);
                break;
            case "/update":
                updateCab(request, response, session);
                break;
            case "/delete":
                deleteCab(request, response, session);
                break;
            default:
                show404error(request, response);
        }
    }

    private void show404error(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/errors/404.jsp").forward(request, response);
    }

    private void getByType(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("cabTypeId"));
        List<Cab> cabs = cabDAO.getCabsByCabType(id);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(cabs);

        response.getWriter().write(jsonResponse);
    }

    private void editCab(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Cab cab = cabDAO.getById(id);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (cab != null) {
            Map<String, Object> cabMap = new HashMap<>();
            cabMap.put("id", cab.getId());
            cabMap.put("cabName", cab.getCabName());
            cabMap.put("registrationNumber", cab.getRegistrationNumber());
            cabMap.put("plateNumber", cab.getPlateNumber());
            cabMap.put("cabBrandId", cab.getCabBrandId());
            cabMap.put("cabTypeId", cab.getCabTypeId());
            cabMap.put("status", cab.getStatus());
            cabMap.put("lastService", cab.getLastService().toString()); // Format date if necessary

            response.getWriter().write(new Gson().toJson(cabMap));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"Cab not found\"}");
        }
    }

    private void listCabs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("cabs", cabDAO.getAll());
        request.getRequestDispatcher("/views/sites/admin/cab/cabs.jsp").forward(request, response);
    }

    private void searchCab(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        request.setAttribute("cabs", cabDAO.search(search));
        request.getRequestDispatcher("/views/sites/admin/cab/cabs.jsp").forward(request, response);
    }

    private void addCab(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        String cabName = request.getParameter("cabName");
        String registrationNumber = request.getParameter("registrationNumber");
        String plateNumber = request.getParameter("plateNumber");
        int cabBrandId = Integer.parseInt(request.getParameter("cabBrandId"));
        int cabTypeId = Integer.parseInt(request.getParameter("cabTypeId"));

        Cab newCab = new Cab.CabBuilder()
                .setCabName(cabName)
                .setRegistrationNumber(registrationNumber)
                .setPlateNumber(plateNumber)
                .setCabBrandId(cabBrandId)
                .setCabTypeId(cabTypeId)
                .build();

        if (cabDAO.add(newCab)) {
            session.setAttribute("message", "Cab added successfully!");
            response.sendRedirect(request.getContextPath() + "/cabs/list");
        } else {
            session.setAttribute("error", "Failed to add the cab.");
            response.sendRedirect(request.getContextPath() + "/cabs/add");
        }
    }

    private void deleteCab(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        int cabId = Integer.parseInt(request.getParameter("id"));

        if (cabDAO.delete(cabId)) {
            session.setAttribute("message", "Cab deleted successfully!");
            response.sendRedirect(request.getContextPath() + "/cabs/list");
        } else {
            session.setAttribute("error", "Failed to delete the cab.");
            response.sendRedirect(request.getContextPath() + "/cabs/list");
        }
    }

    private void updateCab(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        // Handle updating an existing cab
        int cabId = Integer.parseInt(request.getParameter("id"));
        String cabName = request.getParameter("cabName");
        String registrationNumber = request.getParameter("registrationNumber");
        String plateNumber = request.getParameter("plateNumber");
        int cabBrandId = Integer.parseInt(request.getParameter("cabBrandId"));
        int cabTypeId = Integer.parseInt(request.getParameter("cabTypeId"));

        Cab updatedCab = new Cab.CabBuilder()
                .setId(cabId)
                .setCabName(cabName)
                .setRegistrationNumber(registrationNumber)
                .setPlateNumber(plateNumber)
                .setCabBrandId(cabBrandId)
                .setCabTypeId(cabTypeId)
                .build();

        if (cabDAO.update(updatedCab)) {
            session.setAttribute("message", "Cab updated successfully!");
            response.sendRedirect(request.getContextPath() + "/cabs/list");
        } else {
            session.setAttribute("error", "Failed to update the cab.");
            response.sendRedirect(request.getContextPath() + "/cabs/edit?id=" + cabId);
        }
    }

}
