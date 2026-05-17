/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dbproject;

// DatabaseConnection.java


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres"; // Change to your PostgreSQL username
    private static final String PASSWORD = "123456"; // Change to your PostgreSQL password
    
    static {
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found", e);
        }
    }
    
    /**
     * Gets a new database connection every time it's called
     * @return A new Connection object
     * @throws RuntimeException if connection fails
     */
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Verify the connection is valid
            if (!conn.isValid(5)) {
                conn.close();
                throw new SQLException("Connection validation failed");
            }
            
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to create database connection: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }
    
    /**
     * Test method to verify database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Database connection test successful!");
            return true;
        } catch (Exception e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
