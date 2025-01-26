package com.example.megacitycab;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private static final String VALID_USERNAME = "User";
    private static final String VALID_PASSWORD = "password";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if(username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Hello World</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Welcome " + username + "</h1> <br>");
            out.println("<a href='index.jsp'>Logout</a>");
            out.println("</body>");
            out.println("</html>");
        }
        else {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Hello World</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Wrong</h1>");
            out.println("<a href='index.jsp'>Go Back</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    public void destroy() {
    }
}