/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dbproject;

import java.sql.Date;

/**
 *
 * @author Abd Alrahman
 */
public class Tournament {
    private String tournamentName;
    private Date startDate;
    private Date endDate;
    private String location;
    private String status;
    
    // Getters and setters
    public String getTournamentName() {
        return tournamentName;
    }
    
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
