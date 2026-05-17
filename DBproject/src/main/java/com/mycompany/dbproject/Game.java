/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dbproject;

/**
 *
 * @author Abd Alrahman
 */
// Game.java
public class Game {
    private int gameId;
    private String tournament;
    private String date;
    private String tactics;
    private String stadium;
    private String jerseyType;
    
    // Constructor
    public Game(int gameId, String tournament, String date, String tactics, String stadium, String jerseyType) {
        this.gameId = gameId;
        this.tournament = tournament;
        this.date = date;
        this.tactics = tactics;
        this.stadium = stadium;
        this.jerseyType = jerseyType;
    }
    
    // Getters and Setters
    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }
    
    public String getTournament() { return tournament; }
    public void setTournament(String tournament) { this.tournament = tournament; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getTactics() { return tactics; }
    public void setTactics(String tactics) { this.tactics = tactics; }
    
    public String getStadium() { return stadium; }
    public void setStadium(String stadium) { this.stadium = stadium; }
    
    public String getJerseyType() { return jerseyType; }
    public void setJerseyType(String jerseyType) { this.jerseyType = jerseyType; }
}
