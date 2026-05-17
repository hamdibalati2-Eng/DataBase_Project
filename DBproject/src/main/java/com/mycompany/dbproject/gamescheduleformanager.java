/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.dbproject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Abd Alrahman
 */
public class gamescheduleformanager extends javax.swing.JPanel {
        private DefaultTableModel tableModel;
        private Services services;
    public gamescheduleformanager() {
        initComponents();
        
        services = new Services();
        
        // Get the table model from your existing table
        tableModel = (DefaultTableModel) gameschedule.getModel();
        
        // Load data from database
        loadData();
        populateTournamentsComboBox();
        addTableSelectionListener();
        
    }
    private void addTableSelectionListener() {
    gameschedule.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            populateFormFromSelectedRow();
        }
    });
}
    private void populateFormFromSelectedRow() {
    int selectedRow = gameschedule.getSelectedRow();
    
    if (selectedRow != -1) {
        // Get data from the selected row
        String tournament = (String) tableModel.getValueAt(selectedRow, 0);
        String date = (String) tableModel.getValueAt(selectedRow, 1);
        String tactics = (String) tableModel.getValueAt(selectedRow, 2);
        String stadium = (String) tableModel.getValueAt(selectedRow, 3);
        String jerseyType = (String) tableModel.getValueAt(selectedRow, 4);
        
        // Populate the form fields
        jComboBox2.setSelectedItem(tournament);  // Tournament combo box
        dateField.setText(date);
        tacticsField.setText(tactics);
        staduimField.setText(stadium);
        jComboBox1.setSelectedItem(jerseyType);  // Jersey type combo box
    }
}
    public void refreshData() {
        loadData();
    }
    public void loadData() {
    // Clear existing data
    tableModel.setRowCount(0);
    
    // Get game schedules from the database
    List<GamesSchedule> schedules = services.getGameSchedules();
    
    // Add schedules to the table
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    
    for (GamesSchedule schedule : schedules) {
        String formattedDate = (schedule.getGameDate() != null) ? 
            dateFormat.format(schedule.getGameDate()) : "";
            
        Object[] rowData = {
            schedule.getTournamentName(),
            formattedDate,
            schedule.getTactics(),
            schedule.getStadium(),
            schedule.getJerseyType(),
            schedule.getGameId()  // Store the ID in a hidden column (index 5)
        };
        
        tableModel.addRow(rowData);
    }
}
   private void populateTournamentsComboBox() {
    try {
        jComboBox2.removeAllItems();
        
        // Add default item first
        jComboBox2.addItem("Select Tournament");
        
        // Get tournaments from database
        List<Tournamentsforediting> tournaments = services.getAllTournaments();
        
        // Debug: Check if we got any tournaments
        System.out.println("Number of tournaments retrieved: " + tournaments.size());
        
        // Add each tournament to combo box
        for (Tournamentsforediting tournament : tournaments) {
            String tournamentName = tournament.getName();
            System.out.println("Adding tournament: " + tournamentName); // Debug line
            jComboBox2.addItem(tournamentName);
        }
        
        // Force refresh the combo box
        jComboBox2.revalidate();
        jComboBox2.repaint();
        
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading tournaments: " + e.getMessage(), 
                                    "Error", JOptionPane.ERROR_MESSAGE);
    }
}
   
   
    private void addNewGame() {
    // Get values from input fields
    String tournament = jComboBox2.getSelectedItem().toString();
    String tactics = tacticsField.getText().trim();
    String date = dateField.getText().trim();
    String stadium = staduimField.getText().trim();
    String jerseyType = (String) jComboBox1.getSelectedItem();
    
    // Validate input
    if (tournament.isEmpty() || tactics.isEmpty() || date.isEmpty() || stadium.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Create new game object
    Game newGame = new Game(0, tournament, date, tactics, stadium, jerseyType);
    
    // Add to database
    if (services.addGame(newGame)) {
        JOptionPane.showMessageDialog(this, "Game added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        loadData(); // Refresh table
        clearInputFields();
    } else {
        JOptionPane.showMessageDialog(this, "Failed to add game", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// Delete selected game functionality
private void deleteSelectedGame() {
    int selectedRow = gameschedule.getSelectedRow();
    
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a game to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this game?", 
                                               "Confirm Delete", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        // Get the game ID from the hidden column (index 5)
        int gameId = (Integer) tableModel.getValueAt(selectedRow, 5);
        
        if (services.deleteGame(gameId)) {
            JOptionPane.showMessageDialog(this, "Game deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData(); // Refresh table
            clearInputFields(); // Clear form fields
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete game", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Edit selected game functionality
private void editSelectedGame() {
    int selectedRow = gameschedule.getSelectedRow();
    
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a game to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Get the game ID from the hidden column (index 5)
    int gameId = (Integer) tableModel.getValueAt(selectedRow, 5);
    
    // Get current values from form fields
    String selectedTournament = (String) jComboBox2.getSelectedItem();
    String tactics = tacticsField.getText().trim();
    String date = dateField.getText().trim();
    String stadium = staduimField.getText().trim();
    String jerseyType = (String) jComboBox1.getSelectedItem();
    
    // Validate input
    if (selectedTournament.equals("Select Tournament") || tactics.isEmpty() || 
        date.isEmpty() || stadium.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Create updated game object with the ID
    Game updatedGame = new Game(gameId, selectedTournament, date, tactics, stadium, jerseyType);
    
    // Update the game in database
    if (services.updateGame(updatedGame)) {
        JOptionPane.showMessageDialog(this, "Game updated successfully!", 
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
        loadData(); // Refresh table
        clearInputFields(); // Clear form fields
    } else {
        JOptionPane.showMessageDialog(this, "Failed to update game", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void clearInputFields() {
    jComboBox2.setSelectedIndex(0); // Tournament combo box
    dateField.setText(""); // Date
    tacticsField.setText(""); // Tactics
    staduimField.setText(""); // Stadium
    jComboBox1.setSelectedIndex(0); // Jersey Type
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        gameschedule = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        tacticsField = new javax.swing.JTextField();
        staduimField = new javax.swing.JTextField();
        dateField = new javax.swing.JTextField();
        addNewGameButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();

        jPanel1.setBackground(new java.awt.Color(245, 245, 220));

        gameschedule.setBackground(new java.awt.Color(248, 248, 248));
        gameschedule.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(153, 204, 0))); // NOI18N
        gameschedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Tournaments", "Date", "Tactics", "Staduim", "Jersey Type", "ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(gameschedule);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Tournaments:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Tactics:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Date:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Staduim:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Jersey Type:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Home", "Away" }));

        tacticsField.setText("jTextField1");
        tacticsField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tacticsFieldActionPerformed(evt);
            }
        });

        staduimField.setText("jTextField2");

        dateField.setText("jTextField4");

        addNewGameButton.setBackground(new java.awt.Color(95, 99, 104));
        addNewGameButton.setForeground(new java.awt.Color(242, 242, 242));
        addNewGameButton.setText("Add new Game");
        addNewGameButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(105, 105, 105), 1, true));
        addNewGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewGameButtonActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(95, 99, 104));
        jButton2.setForeground(new java.awt.Color(242, 242, 242));
        jButton2.setText("Delete Selected");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(105, 105, 105), 1, true));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(95, 99, 104));
        jButton3.setForeground(new java.awt.Color(242, 242, 242));
        jButton3.setText("Edit Selected");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(105, 105, 105), 1, true));
        jButton3.setBorderPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(addNewGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tacticsField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(staduimField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(tacticsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(staduimField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewGameButton)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addNewGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewGameButtonActionPerformed
   String selectedTournament = (String) jComboBox2.getSelectedItem();

// Validate tournament selection
if (selectedTournament == null || selectedTournament.equals("Select Tournament")) {
    JOptionPane.showMessageDialog(this, "Please select a tournament", "Error", JOptionPane.ERROR_MESSAGE);
    return;
}

// Get other field values
String tactics = tacticsField.getText().trim();
String dateString = dateField.getText().trim();
String stadium = staduimField.getText().trim();
String jerseyType = (String) jComboBox1.getSelectedItem();

// Validate other fields
if (tactics.isEmpty() || dateString.isEmpty() || stadium.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
    return;
}

// Validate date format in GUI (optional - you can remove this if you want)
try {
    // Just validate the format, don't convert yet
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime.parse(dateString, formatter);
} catch (DateTimeParseException e) {
    try {
        // Try date-only format
        DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime.parse(dateString, dateOnlyFormatter);
        dateString = dateString + " 00:00:00"; // Add default time
    } catch (DateTimeParseException e2) {
        JOptionPane.showMessageDialog(this, 
            "Invalid date format. Please use: YYYY-MM-DD HH:MM:SS or YYYY-MM-DD", 
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
}

// Create new game object with String date (unchanged Game class)
Game newGame = new Game(0, selectedTournament, dateString, tactics, stadium, jerseyType);

// Add to database
if (services.addGame(newGame)) {
    JOptionPane.showMessageDialog(this, "Game added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    loadData(); // Refresh table
    clearInputFields();
} else {
    JOptionPane.showMessageDialog(this, "Failed to add game", "Error", JOptionPane.ERROR_MESSAGE);
}
    
    }//GEN-LAST:event_addNewGameButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         int selectedRow = gameschedule.getSelectedRow();
    
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a game to delete", 
                                    "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to delete this game?", 
        "Confirm Delete", 
        JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        // Get the game ID from the first column
        int gameId = (Integer) tableModel.getValueAt(selectedRow, 5);
        
        if (services.deleteGame(gameId)) {
            JOptionPane.showMessageDialog(this, "Game deleted successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData(); // Refresh table
            clearInputFields(); // Clear form fields
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete game", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = gameschedule.getSelectedRow();
    
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a game to edit", 
                                    "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Get the game ID from the selected row
    int gameId = (Integer) tableModel.getValueAt(selectedRow, 0);
    
    // Get current values from form fields
    String selectedTournament = (String) jComboBox2.getSelectedItem();
    String tactics = tacticsField.getText().trim();
    String date = dateField.getText().trim();
    String stadium = staduimField.getText().trim();
    String jerseyType = (String) jComboBox1.getSelectedItem();
    
    // Validate input
    if (selectedTournament.equals("Select Tournament") || tactics.isEmpty() || 
        date.isEmpty() || stadium.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Create updated game object with the ID
    Game updatedGame = new Game(gameId, selectedTournament, date, tactics, stadium, jerseyType);
    
    // Update the game in database
    if (services.updateGame(updatedGame)) {
        JOptionPane.showMessageDialog(this, "Game updated successfully!", 
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
        loadData(); // Refresh table
        clearInputFields(); // Clear form fields
    } else {
        JOptionPane.showMessageDialog(this, "Failed to update game", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tacticsFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tacticsFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tacticsFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewGameButton;
    private javax.swing.JTextField dateField;
    private javax.swing.JTable gameschedule;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField staduimField;
    private javax.swing.JTextField tacticsField;
    // End of variables declaration//GEN-END:variables
}
