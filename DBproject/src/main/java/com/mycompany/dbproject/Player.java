/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dbproject;

/**
 *
 * @author Abd Alrahman
 */
public class Player {
    private int playerId;
    private String username;
    private int age;
    private String healthStatus;
    private String position;
    private String nationality;
    private int jerseyNumber;
    
    // Constructors
    public Player() {
    }
    
    public Player(int playerId, String username, int age, String healthStatus, 
                 String position, String nationality, int jerseyNumber) {
        this.playerId = playerId;
        this.username = username;
        this.age = age;
        this.healthStatus = healthStatus;
        this.position = position;
        this.nationality = nationality;
        this.jerseyNumber = jerseyNumber;
    }
    
    // Getters and Setters
    public int getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getHealthStatus() {
        return healthStatus;
    }
    
    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public int getJerseyNumber() {
        return jerseyNumber;
    }
    
    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }
}
