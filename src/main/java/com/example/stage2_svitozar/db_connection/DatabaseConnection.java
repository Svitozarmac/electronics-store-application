package com.example.stage2_svitozar.db_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection { // Set Database Connection using Singleton Pattern
    // Static member holds only one instance of the DatabaseConnection class
    private static DatabaseConnection instance = null;

    // My DB credentials
    private final String url = "jdbc:mysql://localhost:3306/Store";
    private final String user = "root";
    protected final String password = "rootroot";

    // Private constructor
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found." + e.getMessage());
        }
    }

    // Providing Global point of access
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException{
        // Return a new, fresh connection
        try {
            // Establish the connection
            Connection conn = DriverManager.getConnection(url, user, password);
            // Display success message
            System.out.println("✅ Database Connection is active.");
            return conn;
        }
        catch(SQLException sqle) {
            System.out.println("❌ Failed to connect to database:\n" + sqle.getMessage() + " This message from DatabaseConnection.java, where connection is established.");
            throw sqle; // StoreController or caller handle this
        }
    }
}
