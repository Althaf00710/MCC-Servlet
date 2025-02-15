package com.example.megacitycab.controllers.cab;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.cab.CabTypeDAO;
import com.example.megacitycab.models.Cab.CabType;
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

@WebServlet("/cabtypes/*")
public class CabTypeController extends HttpServlet {
    private final CabTypeDAO cabTypeDAO = DAOFactory.getCabTypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";
        switch (action) {
            case "/list":
                listCabTypes(request, response);
                break;
            case "/search":
                searchCabTypes(request, response);
                break;
            case "/edit":
                editCabType(request, response);
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
                addCabType(request, response, session);
                break;
            case "/update":
                updateCabType(request, response, session);
                break;
            case "/delete":
                deleteCabType(request, response, session);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listCabTypes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CabType> cabTypes = cabTypeDAO.getAll();
        request.setAttribute("cabTypes", cabTypes);
        request.getRequestDispatcher("/views/sites/admin/cab/cabtypes.jsp").forward(request, response);
    }

    private void searchCabTypes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        List<CabType> searchedCabTypes = cabTypeDAO.search(searchTerm);
        request.setAttribute("cabTypes", searchedCabTypes);
        request.getRequestDispatcher("/views/sites/admin/cab/cabtypes.jsp").forward(request, response);
    }

    private void editCabType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int cabTypeId = Integer.parseInt(request.getParameter("id"));
        CabType cabType = cabTypeDAO.getById(cabTypeId);

        Map<String, Object> cabTypeMap = new HashMap<>();
        cabTypeMap.put("id", cabType.getId());
        cabTypeMap.put("typeName", cabType.getTypeName());
        cabTypeMap.put("description", cabType.getDescription());
        cabTypeMap.put("capacity", cabType.getCapacity());
        cabTypeMap.put("baseFare", cabType.getBaseFare());
        cabTypeMap.put("baseWaitFare", cabType.getBaseWaitTimeFare());

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(cabTypeMap));
    }

    private void addCabType(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        String typeName = request.getParameter("typeName");

        if (cabTypeDAO.checkExist(typeName)) {
            session.setAttribute("error", "Cab type already exists!");
            response.sendRedirect(request.getContextPath() + "/cabtypes/list");
            return;
        }

        CabType cabType = new CabType.CabTypeBuilder()
                .setTypeName(request.getParameter("typeName"))
                .setDescription(request.getParameter("description"))
                .setCapacity(Integer.parseInt(request.getParameter("capacity")))
                .setBaseFare(Double.parseDouble(request.getParameter("baseFare")))
                .setBaseWaitTimeFare(Double.parseDouble(request.getParameter("baseWaitFare")))
                .build();

        boolean success = cabTypeDAO.add(cabType);
        session.setAttribute(success ? "success" : "error", success ? "Cab type added successfully!" : "Failed to add cab type!");
        response.sendRedirect(request.getContextPath() + "/cabtypes/list");
    }

    private void updateCabType(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int cabTypeId = Integer.parseInt(request.getParameter("id"));
        CabType existingCabType = cabTypeDAO.getById(cabTypeId);

        if (existingCabType == null) {
            session.setAttribute("error", "Cab type not found!");
            response.sendRedirect(request.getContextPath() + "/cabtypes/list");
            return;
        }

        CabType updatedCabType = new CabType.CabTypeBuilder()
                .setId(cabTypeId)
                .setTypeName(request.getParameter("typeName"))
                .setDescription(request.getParameter("description"))
                .setCapacity(Integer.parseInt(request.getParameter("capacity")))
                .setBaseFare(Double.parseDouble(request.getParameter("baseFare")))
                .setBaseWaitTimeFare(Double.parseDouble(request.getParameter("baseWaitFare")))
                .build();

        boolean success = cabTypeDAO.update(updatedCabType);
        session.setAttribute(success ? "success" : "error", success ? "Cab type updated successfully!" : "Failed to update cab type!");
        response.sendRedirect(request.getContextPath() + "/cabtypes/list");
    }

    private void deleteCabType(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int cabTypeId = Integer.parseInt(request.getParameter("id"));
        boolean success = cabTypeDAO.delete(cabTypeId);

        session.setAttribute(success ? "success" : "error", success ? "Cab type deleted successfully!" : "Failed to delete cab type!");
        response.sendRedirect(request.getContextPath() + "/cabtypes/list");
    }
}
