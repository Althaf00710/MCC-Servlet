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

    private static DbConfig instance;
    private Connection connection;

    private DbConfig() {
        try {
            Class.forName(DRIVER_CLASS);
            System.out.println("Database driver loaded successfully.");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, "Database initialization error", ex);
        }
    }

    // provide access to the Singleton instance
    public static DbConfig getInstance() {
        if (instance == null) {
            synchronized (DbConfig.class) {
                if (instance == null) {
                    instance = new DbConfig();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Re-established database connection.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, "Failed to re-establish connection", ex);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed successfully.");
            } catch (SQLException ex) {
                Logger.getLogger(DbConfig.class.getName()).log(Level.SEVERE, "Failed to close connection", ex);
            }
        }
    }
}
