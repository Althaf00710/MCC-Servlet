package com.example.megacitycab.listeners;

import com.example.megacitycab.utils.Email;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String mailAddress = sce.getServletContext().getInitParameter("MAIL_ADDRESS");
        String mailPassword = sce.getServletContext().getInitParameter("MAIL_PASSWORD");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}