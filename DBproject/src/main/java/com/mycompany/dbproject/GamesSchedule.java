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
public class GamesSchedule {
    private String tournamentName;
    private Date gameDate;
    private String tactics;
    private String stadium;
    private String jerseyType;
    private int gameId;
    
    
    // Getters and setters
    public String getTournamentName() {
        return tournamentName;
    }
    
    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }
    
    public Date getGameDate() {
        return gameDate;
    }
    
    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
    }
    
    public String getTactics() {
        return tactics;
    }
    
    public void setTactics(String tactics) {
        this.tactics = tactics;
    }
    
    public String getStadium() {
        return stadium;
    }
    
    public void setStadium(String stadium) {
        this.stadium = stadium;
    }
    
    public String getJerseyType() {
        return jerseyType;
    }
    
    public void setJerseyType(String jerseyType) {
        this.jerseyType = jerseyType;
    }

    public int getGameId() {
    return gameId;
}

public void setGameId(int gameId) {
    this.gameId = gameId;
}
}
