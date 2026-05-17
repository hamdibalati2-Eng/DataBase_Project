/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dbproject;

import java.sql.Timestamp;

/**
 *
 * @author Abd Alrahman
 */
public class TrainingSession {
     private String sessionType;
     private int sessionId;
     private Timestamp sessionDate;  // Using Timestamp to include time
    
    // Getters and setters
    public String getSessionType() {
        return sessionType;
    }
    
    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }
    
    public Timestamp getSessionDate() {
        return sessionDate;
    }
    
    public void setSessionDate(Timestamp sessionDate) {
        this.sessionDate = sessionDate;
    }
    public int getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
}
