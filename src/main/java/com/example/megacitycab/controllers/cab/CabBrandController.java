package com.example.megacitycab.controllers.cab;

import com.example.megacitycab.daos.DAOFactory;
import com.example.megacitycab.daos.interfaces.cab.CabBrandDAO;
import com.example.megacitycab.daos.interfaces.cab.CabTypeDAO;
import com.example.megacitycab.models.Cab.CabBrand;
import com.example.megacitycab.models.Cab.CabType;
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

@WebServlet("/cabbrand/*")
public class CabBrandController extends HttpServlet {
    private final CabBrandDAO cabBrandDAO = DAOFactory.getCabBrandDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) action = "/list";
        switch (action) {
            case "/list":
                listCabBrands(request, response);
                break;
            case "/edit":
                editCabBrand(request, response);
                break;
            case "/get":
                getCabBrands(request, response);
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
                addCabBrand(request, response, session);
                break;
            case "/update":
                updateCabBrand(request, response, session);
                break;
            case "/delete":
                deleteCabBrand(request, response, session);
                break;
            default:
                show404error(request, response);
        }
    }

    private void show404error(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/errors/404.jsp").forward(request, response);
    }

    private void deleteCabBrand(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        if (cabBrandDAO.delete(id)) {
            session.setAttribute("success", "Cab brand deleted successfully!");
        } else {
            session.setAttribute("error", "Failed to delete cab brand.");
        }

        response.sendRedirect(request.getContextPath() + "/cabbrand/list");
    }

    private void updateCabBrand(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String brandName = request.getParameter("brandName");

        CabBrand cabBrand = new CabBrand.CabBrandBuilder()
                .setId(id)
                .setBrandName(brandName)
                .build();

        if (cabBrandDAO.update(cabBrand)) {
            session.setAttribute("success", "Cab brand updated successfully!");
        } else {
            session.setAttribute("error", "Failed to update cab brand.");
        }

        response.sendRedirect(request.getContextPath() + "/cabbrand/list");
    }

    private void addCabBrand(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String brandName = request.getParameter("brandName");

        if (cabBrandDAO.checkExist(brandName)) {
            session.setAttribute("error", "Cab brand already exists!");
            response.sendRedirect(request.getContextPath() + "/cabbrand/list");
            return;
        }

        CabBrand cabBrand = new CabBrand.CabBrandBuilder()
                .setBrandName(brandName)
                .build();

        if (cabBrandDAO.add(cabBrand)) {
            session.setAttribute("success", "Cab brand added successfully!");
        } else {
            session.setAttribute("error", "Failed to add cab brand.");
        }

        response.sendRedirect(request.getContextPath() + "/cabbrand/list");
    }

    private void listCabBrands(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CabBrand> cabBrand = cabBrandDAO.getAll();
        request.setAttribute("cabBrand", cabBrand);
        request.getRequestDispatcher("/views/sites/admin/cab/cabBrands.jsp").forward(request, response);
    }

    private void getCabBrands(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CabBrand> cabBrands = cabBrandDAO.getAll();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(cabBrands);

        response.getWriter().write(jsonResponse);
    }

    private void editCabBrand(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        CabBrand cabBrand = cabBrandDAO.getById(id);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (cabBrand != null) {
            Map<String, Object> cabBrandMap = new HashMap<>();
            cabBrandMap.put("id", cabBrand.getId());
            cabBrandMap.put("brandName", cabBrand.getBrandName());

            response.getWriter().write(new Gson().toJson(cabBrandMap));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"Brand not found\"}");
        }
    }
    
    
    
}
