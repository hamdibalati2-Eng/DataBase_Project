/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dbproject;

/**
 *
 * @author Abd Alrahman
 */
// Tournament.java
public class Tournamentsforediting {
    private int tournamentId;  // Read-only, set by database
    private String name;
    private String startDate;
    private String endDate;
    private String status;
    private String location;
    
    // Constructor (without ID - for creating new tournaments)
    public Tournamentsforediting(String name, String startDate, String endDate, String status, String location) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.location = location;
    }
    
    // Constructor with ID (for reading from database)
    public Tournamentsforediting(int tournamentId, String name, String startDate, String endDate, String status, String location) {
        this.tournamentId = tournamentId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.location = location;
    }
    
    // Default constructor
    public Tournamentsforediting() {}
    
    // Getters and Setters
    public int getTournamentId() { return tournamentId; }
    // Note: No setter for tournamentId since it's auto-generated
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    @Override
    public String toString() {
        return name; // This will be displayed in the combo box
    }
}
