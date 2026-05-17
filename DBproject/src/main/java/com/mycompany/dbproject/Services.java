/*
 * Services Class - Database Operations
 */
package com.mycompany.dbproject;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

public class Services {
    private static Map<String, Integer> userIdMap = new HashMap<>();
    public Services() {
        // Test connection on initialization
        if (!DatabaseConnection.testConnection()) {
            System.err.println("Warning: Database connection test failed during Services initialization");
        }
    }
    
    // Tournament Operations
    public List<Tournamentsforediting> getAllTournaments() {
        List<Tournamentsforediting> tournaments = new ArrayList<>();
        String query = "SELECT tournament_id, name, start_date, end_date, status, location FROM tournaments";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Tournamentsforediting tournament = new Tournamentsforediting(
                    rs.getInt("tournament_id"),
                    rs.getString("name"),
                    rs.getString("start_date"),
                    rs.getString("end_date"),
                    rs.getString("status"),
                    rs.getString("location")
                );
                tournaments.add(tournament);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching tournaments: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tournaments;
    }
    
    public List<String> getTournamentNames() {
        List<String> tournamentNames = new ArrayList<>();
        String query = "SELECT name FROM tournaments";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tournamentNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching tournament names: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tournamentNames;
    }
    
    public List<Tournament> getTournaments() {
        List<Tournament> tournaments = new ArrayList<>();
        String query = "SELECT name, start_date, end_date, location, status FROM tournaments";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Tournament tournament = new Tournament();
                tournament.setTournamentName(rs.getString("name"));
                tournament.setStartDate(rs.getDate("start_date"));
                tournament.setEndDate(rs.getDate("end_date"));
                tournament.setLocation(rs.getString("location"));
                tournament.setStatus(rs.getString("status"));
                
                tournaments.add(tournament);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching tournaments: " + e.getMessage());
            e.printStackTrace();
        }
        
        return tournaments;
    }
    
    // Game Operations
    public boolean addGame(Game game) {
    String query = "INSERT INTO gamesschedule (tournaments_name, match_date, tactics, stadium, jersey_type) VALUES (?, ?, ?, ?, ?::jersey_enum)";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, game.getTournament());
        
        // Convert string date to Timestamp
        try {
            // Try full date-time format first (e.g., "2024-05-01 14:30:00")
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(game.getDate(), formatter);
            stmt.setTimestamp(2, Timestamp.valueOf(dateTime));
        } catch (DateTimeParseException e) {
            // If that fails, try date-only format (e.g., "2024-05-01")
            try {
                DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime dateTime = LocalDateTime.parse(game.getDate() + " 00:00:00", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                stmt.setTimestamp(2, Timestamp.valueOf(dateTime));
            } catch (DateTimeParseException e2) {
                System.err.println("Invalid date format: " + game.getDate());
                System.err.println("Expected format: YYYY-MM-DD HH:MM:SS or YYYY-MM-DD");
                return false;
            }
        }
        
        stmt.setString(3, game.getTactics());
        stmt.setString(4, game.getStadium());
        stmt.setString(5, game.getJerseyType());
        
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error adding game: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    
    public boolean deleteGame(int gameId) {
        String query = "DELETE FROM gamesschedule WHERE game_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, gameId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateGame(Game game) {
        String query = "UPDATE gamesschedule SET tournaments_name = ?, match_date = ?, tactics = ?, stadium = ?, jersey_type = ? WHERE game_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, game.getTournament());
            stmt.setString(2, game.getDate());
            stmt.setString(3, game.getTactics());
            stmt.setString(4, game.getStadium());
            stmt.setString(5, game.getJerseyType());
            stmt.setInt(6, game.getGameId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public List<GamesSchedule> getGameSchedules() {
        List<GamesSchedule> schedules = new ArrayList<>();
        String query = "SELECT game_id, tournaments_name, match_date, tactics, stadium, jersey_type FROM gamesschedule";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                GamesSchedule schedule = new GamesSchedule();
                schedule.setGameId(rs.getInt("game_id"));
                schedule.setTournamentName(rs.getString("tournaments_name"));
                schedule.setGameDate(rs.getDate("match_date"));
                schedule.setTactics(rs.getString("tactics"));
                schedule.setStadium(rs.getString("stadium"));
                schedule.setJerseyType(rs.getString("jersey_type"));
                
                schedules.add(schedule);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching game schedules: " + e.getMessage());
            e.printStackTrace();
        }
        
        return schedules;
    }
    
    // User Authentication
    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            
            statement.setString(1, username);
            statement.setString(2, password);
            
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public String getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            
            statement.setString(1, username);
            
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getString("role");
                }
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error getting user role: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Player Operations
    public Player getPlayerById(int playerId) {
        Player player = null;
        String query = "SELECT user_name, age, healthstatus, position, nationality, jerseynumber FROM players WHERE player_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, playerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    player = new Player();
                    player.setUsername(rs.getString("user_name"));
                    player.setAge(rs.getInt("age"));
                    player.setHealthStatus(rs.getString("healthstatus"));
                    player.setPosition(rs.getString("position"));
                    player.setNationality(rs.getString("nationality"));
                    player.setJerseyNumber(rs.getInt("jerseynumber"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching player data: " + e.getMessage());
            e.printStackTrace();
        }
        
        return player;
    }
    
    public Player getPlayerByUsername(String username) {
        Player player = null;
        String query = "SELECT player_id, user_name, age, healthstatus, position, nationality, jerseynumber FROM players WHERE user_name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    player = new Player();
                    player.setPlayerId(rs.getInt("player_id"));
                    player.setUsername(rs.getString("user_name"));
                    player.setAge(rs.getInt("age"));
                    player.setHealthStatus(rs.getString("healthstatus"));
                    player.setPosition(rs.getString("position"));
                    player.setNationality(rs.getString("nationality"));
                    player.setJerseyNumber(rs.getInt("jerseynumber"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching player data: " + e.getMessage());
            e.printStackTrace();
        }
        
        return player;
    }
    
    public boolean updatePlayerInfo(Player player) {
        String query = "UPDATE Players SET user_name = ?, age = ?, healthstatus = ?, position = ?, nationality = ?, jerseynumber = ? WHERE player_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, player.getUsername());
            pstmt.setInt(2, player.getAge());
            pstmt.setString(3, player.getHealthStatus());
            pstmt.setString(4, player.getPosition());
            pstmt.setString(5, player.getNationality());
            pstmt.setInt(6, player.getJerseyNumber());
            pstmt.setInt(7, player.getPlayerId());
            
            int rowsAffected = pstmt.executeUpdate();
            return (rowsAffected > 0);
        } catch (SQLException e) {
            System.err.println("Error updating player data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Training Session Operations
    public List<TrainingSession> getTrainingSessions() {
        List<TrainingSession> sessions = new ArrayList<>();
        String query = "SELECT session_id, session_type, session_date FROM trainingsessions";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                TrainingSession session = new TrainingSession();
                session.setSessionId(rs.getInt("session_id"));
                session.setSessionType(rs.getString("session_type"));
                session.setSessionDate(rs.getTimestamp("session_date"));
                
                sessions.add(session);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching training sessions: " + e.getMessage());
            e.printStackTrace();
        }
        
        return sessions;
    }
    
    public boolean addTrainingSession(TrainingSession session) {
        String query = "INSERT INTO trainingsessions (session_type, session_date) VALUES (?::session_enum, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, session.getSessionType());
            pstmt.setTimestamp(2, session.getSessionDate());
            
            int rowsInserted = pstmt.executeUpdate();
            return (rowsInserted > 0);
        } catch (SQLException e) {
            System.err.println("Error adding training session: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateTrainingSession(TrainingSession session) {
        String query = "UPDATE trainingsessions SET session_type = ?::session_enum, session_date = ? WHERE session_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, session.getSessionType());
            pstmt.setTimestamp(2, session.getSessionDate());
            pstmt.setInt(3, session.getSessionId());
            
            int rowsUpdated = pstmt.executeUpdate();
            return (rowsUpdated > 0);
        } catch (SQLException e) {
            System.err.println("Error updating training session: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteTrainingSessionById(int sessionId) {
        String query = "DELETE FROM trainingsessions WHERE session_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, sessionId);
            int rowsDeleted = pstmt.executeUpdate();
            return (rowsDeleted > 0);
        } catch (SQLException e) {
            System.err.println("Error deleting training session: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public int getSessionId(String sessionType, Timestamp sessionDate) {
        int sessionId = -1;
        String query = "SELECT session_id FROM trainingsessions WHERE session_type::text = ? AND session_date = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, sessionType);
            pstmt.setTimestamp(2, sessionDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    sessionId = rs.getInt("session_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting session ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return sessionId;
    }
    
    // Achievement Operations
    public List<Achievementclass> getAchievements() {
        List<Achievementclass> achievements = new ArrayList<>();
        String query = "SELECT title, description, achievement_date FROM achievements";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Achievementclass achievement = new Achievementclass();
                achievement.setTitle(rs.getString("title"));
                achievement.setDescription(rs.getString("description"));
                achievement.setDate(rs.getString("achievement_date"));
                
                achievements.add(achievement);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching achievements: " + e.getMessage());
            e.printStackTrace();
        }
        
        return achievements;
    }
    public static void loadAchievementsToTable(DefaultTableModel model) {
    model.setRowCount(0); // Clear existing data
    
    String query = "SELECT achievement_id, title, description, achievement_date FROM achievements ORDER BY achievement_date DESC";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery()) {
        
        while (rs.next()) {
            Object[] row = new Object[4]; // Create a proper array with exact size
            row[0] = rs.getInt("achievement_id");
            row[1] = rs.getString("title");
            row[2] = rs.getString("description");
            row[3] = rs.getTimestamp("achievement_date");
            
            // Add debug info
            System.out.println("Loading row: " + row[0] + ", " + row[1] + ", " + row[3]);
            
            model.addRow(row);
        }
        System.out.println("Total rows loaded: " + model.getRowCount());
    } catch (SQLException e) {
        System.err.println("Error loading achievements: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    // Method to add a new achievement
    public static boolean addAchievement(String title, String description, Timestamp date) {
        String query = "INSERT INTO achievements (title, description, achievement_date) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setTimestamp(3, date);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding achievement: " + e.getMessage());
            return false;
        }
    }
    
    // Method to update an existing achievement
    public static boolean updateAchievement(int id, String title, String description, Timestamp date) {
        String query = "UPDATE achievements SET title = ?, description = ?, achievement_date = ? WHERE achievement_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setTimestamp(3, date);
            pstmt.setInt(4, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating achievement: " + e.getMessage());
            return false;
        }
    }
    
    // Method to delete an achievement
    public static boolean deleteAchievement(int id) {
        String query = "DELETE FROM achievements WHERE achievement_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting achievement: " + e.getMessage());
            return false;
        }
    }
    
    // Method to get a specific achievement by ID
    public static Object[] getAchievementById(int id) {
        String query = "SELECT title, description, achievement_date FROM achievements WHERE achievement_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[] {
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getTimestamp("achievement_date")
                    };
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving achievement: " + e.getMessage());
        }
        
        return null;
    }
    public static void loadTournamentsToTable(DefaultTableModel model) {
    if (model == null) {
        System.err.println("Error: Table model is null!");
        return;
    }
    
    model.setRowCount(0); // Clear existing data
    
    String query = "SELECT tournament_id, name, start_date, end_date, location, status FROM tournaments ORDER BY start_date DESC";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery()) {
        
        while (rs.next()) {
            Object[] row = new Object[6]; // Create array of exact size
            row[0] = rs.getInt("tournament_id");
            row[1] = rs.getString("name");
            row[2] = rs.getDate("start_date");
            row[3] = rs.getDate("end_date");
            row[4] = rs.getString("location");
            row[5] = rs.getString("status");
            
            model.addRow(row);
        }
        
        System.out.println("Total tournaments loaded: " + model.getRowCount());
    } catch (SQLException e) {
        System.err.println("Error loading tournaments: " + e.getMessage());
        e.printStackTrace();
    }
}
    public static boolean addTournament(String name, Date startDate, Date endDate, String location, String status) {
        String query = "INSERT INTO tournaments (name, start_date, end_date, location, status) VALUES (?, ?, ?, ?, ?::tournament_status)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, name);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            pstmt.setString(4, location);
            pstmt.setObject(5, status); // Using setObject for enum type
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding tournament: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
     public static boolean updateTournament(int id, String name, Date startDate, Date endDate, String location, String status) {
        String query = "UPDATE tournaments SET name = ?, start_date = ?, end_date = ?, location = ?, status = ?::tournament_status WHERE tournament_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, name);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            pstmt.setString(4, location);
            pstmt.setObject(5, status); // Using setObject for enum type
            pstmt.setInt(6, id);
            
            int rowsAffected = pstmt.executeUpdate();
            
            System.out.println("Update query executed. Rows affected: " + rowsAffected);
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating tournament: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
     public static boolean deleteTournament(int id) {
        String query = "DELETE FROM tournaments WHERE tournament_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting tournament: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
     public static void loadUsersToComboBox(JComboBox<String> comboBox) {
        userIdMap.clear(); // Clear the previous mapping
        ArrayList<String> userNames = new ArrayList<>();
        
        String query = "SELECT user_id, user_name FROM users ORDER BY user_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String userName = rs.getString("user_name");
                
                userNames.add(userName);
                userIdMap.put(userName, userId);
            }
            
            // Create a new combo box model and set it
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            for (String userName : userNames) {
                model.addElement(userName);
            }
            
            comboBox.setModel(model);
            
            System.out.println("Loaded " + userNames.size() + " users into combo box");
        } catch (SQLException e) {
            System.err.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
    }
     public static int getUserIdByName(String userName) {
        return userIdMap.getOrDefault(userName, -1);
    }
     
     public static Map<String, Object> getContractByUserId(int userId) {
        Map<String, Object> contract = new HashMap<>();
        
        String query = "SELECT contract_id, start_date, end_date, salary FROM contract WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    contract.put("contract_id", rs.getInt("contract_id"));
                    contract.put("start_date", rs.getDate("start_date"));
                    contract.put("end_date", rs.getDate("end_date"));
                    contract.put("salary", rs.getBigDecimal("salary"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting contract: " + e.getMessage());
            e.printStackTrace();
        }
        
        return contract;
    }
     public static boolean addContract(int userId, Date startDate, Date endDate, BigDecimal salary) {
        String query = "INSERT INTO contract (user_id, start_date, end_date, salary) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            pstmt.setBigDecimal(4, salary);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding contract: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to update an existing contract
    public static boolean updateContract(int contractId, Date startDate, Date endDate, BigDecimal salary) {
        String query = "UPDATE contract SET start_date = ?, end_date = ?, salary = ? WHERE contract_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            pstmt.setBigDecimal(3, salary);
            pstmt.setInt(4, contractId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            System.out.println("Update query executed. Rows affected: " + rowsAffected);
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating contract: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to delete a contract
    public static boolean deleteContract(int contractId) {
        String query = "DELETE FROM contract WHERE contract_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, contractId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting contract: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to check if a user already has a contract
    public static boolean userHasContract(int userId) {
        String query = "SELECT COUNT(*) FROM contract WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Error checking if user has contract: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
}