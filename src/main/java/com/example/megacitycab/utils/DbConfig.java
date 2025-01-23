package com.example.megacitycab.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/megacitycab";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "@Lthaf$17";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_CLASS);
            System.out.println("Database driver loaded successfully.");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, "Failed to load database driver", ex);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, "Failed to establish database connection", ex);
            return null;
        }
    }

    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                System.out.println("Connection closed successfully.");
            } catch (SQLException ex) {
                Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, "Failed to close connection", ex);
            }
        }
    }
}
