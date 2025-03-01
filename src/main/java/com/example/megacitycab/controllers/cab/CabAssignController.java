package com.example.megacitycab.controllers.cab;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.DriverDAO;
import com.example.megacitycab.daos.interfaces.cab.CabAssignDAO;
import com.example.megacitycab.models.Cab.Cab;
import com.example.megacitycab.models.Cab.CabAssign;
import com.example.megacitycab.models.Driver;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/cabassign/*")
public class CabAssignController extends HttpServlet {
    private final CabAssignDAO cabAssignDAO = DAOFactory.getCabAssignDAO();
    private final DriverDAO driverDAO = DAOFactory.getDriverDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";
        switch (action) {
            case "/list":
                listCabAssignments(request, response);
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
                addCabAssign(request, response, session);
                break;
            case "/cancel":
                cancelCabAssign(request, response, session);
                break;
        }
    }

    private void listCabAssignments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CabAssign> cabAssigns = cabAssignDAO.getActiveCabAssigns();
        Gson gson = new Gson();
        String json = gson.toJson(cabAssigns);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(json);
    }

    private void addCabAssign(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        int cabId = Integer.parseInt(request.getParameter("cabId"));
        int driverId = Integer.parseInt(request.getParameter("driverId"));

        CabAssign cabAssign = new CabAssign();
        cabAssign.setCabId(cabId);
        cabAssign.setDriverId(driverId);

        if (cabAssignDAO.addCabAssign(cabAssign)) {
            session.setAttribute("message", "Cab Assignment added successfully.");
            response.sendRedirect(request.getContextPath() + "/cabs/list");
        } else {
            session.setAttribute("error", "Failed to add Cab Assignment.");
            response.sendRedirect(request.getContextPath() + "/cabassign/assign");
        }
    }

    private void cancelCabAssign(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        if (cabAssignDAO.cancelCabAssign(id)) {
            session.setAttribute("message", "Cab Assignment canceled successfully.");
            response.sendRedirect(request.getContextPath() + "/cabassign/list");
        } else {
            session.setAttribute("error", "Failed to cancel Cab Assignment.");
            response.sendRedirect(request.getContextPath() + "/cabassign/list");
        }
    }


}

