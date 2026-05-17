/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.dbproject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Abd Alrahman
 */
public class playershealth extends javax.swing.JPanel {
    private DatabaseConnection dbConnection;
    private Services services;
    private HashMap<String, Integer> playerNameToIdMap;
    /**
     * Creates new form playershealth
     */
    public playershealth() {
        initComponents();
        initComponents();
        dbConnection = new DatabaseConnection();
        services = new Services();
        playerNameToIdMap = new HashMap<>();
        setupTable();
        loadPlayersIntoComboBox();
        loadInjuriesData();
        setupTableSelectionListener();
    }
     private void clearAllFields() {
        jComboBox1.setSelectedIndex(0);
        jTextField1.setText(""); // injury_date
        jTextField2.setText(""); // status
        jTextField3.setText(""); // recovery_date
        jTextField4.setText(""); // description
    }
    private void setupTable() {
        String[] columnNames = {"Injury ID", "Player Name", "Injury Date", "Status", "Recovery Date", "Description"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        injuriesTable.setModel(model);
        injuriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Hide the Injury ID column (but keep it for reference)
        injuriesTable.getColumnModel().getColumn(0).setMinWidth(0);
        injuriesTable.getColumnModel().getColumn(0).setMaxWidth(0);
        injuriesTable.getColumnModel().getColumn(0).setWidth(0);
    }
     private void loadPlayersIntoComboBox() {
        try {
            Connection conn = dbConnection.getConnection();
            String query = "SELECT player_id, user_name FROM players ORDER BY user_name";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            jComboBox1.removeAllItems();
            playerNameToIdMap.clear();
            
            while (rs.next()) {
                String playerName = rs.getString("user_name");
                int playerId = rs.getInt("player_id");
                
                jComboBox1.addItem(playerName);
                playerNameToIdMap.put(playerName, playerId);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error loading players: " + e.getMessage());
        }
    }
     private void loadInjuriesData() {
        DefaultTableModel model = (DefaultTableModel) injuriesTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        try {
            Connection conn = dbConnection.getConnection();
            String query = """
                SELECT i.injury_id, i.player_id, p.user_name as player_name, 
                       i.injury_date, i.health_status, i.recovery_date, i.injury_description
                FROM injuries i
                INNER JOIN players p ON i.player_id = p.player_id
                ORDER BY i.injury_date DESC
            """;
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("injury_id")); // Hidden column
                row.add(rs.getString("player_name"));
                row.add(rs.getDate("injury_date"));
                row.add(rs.getString("health_status"));
                row.add(rs.getDate("recovery_date")); // Can be null
                row.add(rs.getString("injury_description"));
                model.addRow(row);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error loading injuries data: " + e.getMessage());
        }
    }
     private void setupTableSelectionListener() {
        injuriesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
           // @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = injuriesTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        loadSelectedInjuryData(selectedRow);
                    }
                }
            }
        });
    }
     private void loadSelectedInjuryData(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) injuriesTable.getModel();
        
        try {
            String playerName = (String) model.getValueAt(selectedRow, 1);
            Date injuryDate = (Date) model.getValueAt(selectedRow, 2);
            String status = (String) model.getValueAt(selectedRow, 3);
            Date recoveryDate = (Date) model.getValueAt(selectedRow, 4);
            String description = (String) model.getValueAt(selectedRow, 5);
            
            // Set form fields
            jComboBox1.setSelectedItem(playerName);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            jTextField1.setText(injuryDate != null ? dateFormat.format(injuryDate) : ""); // injury_date
            jTextField2.setText(status != null ? status : ""); // status
            jTextField3.setText(recoveryDate != null ? dateFormat.format(recoveryDate) : ""); // recovery_date
            jTextField4.setText(description != null ? description : ""); // description
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        injuriesTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 245, 220));

        injuriesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Player Name", "Injury Date", "Status", "Recovery Date", "Description"
            }
        ));
        jScrollPane1.setViewportView(injuriesTable);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Player:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Injury Date:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Recovery Date:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Description:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Status:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextField1.setText("jTextField1");

        jTextField2.setText("jTextField2");

        jTextField3.setText("jTextField3");

        jTextField4.setText("jTextField4");

        jButton1.setText("Add New Injury");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Delete Selected");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Update Selected");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jButton1)
                        .addGap(72, 72, 72)
                        .addComponent(jButton2)
                        .addGap(51, 51, 51)
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(70, 70, 70)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(64, 64, 64)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(22, 22, 22))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String selectedPlayer = (String) jComboBox1.getSelectedItem();
        String injuryDateStr = jTextField1.getText().trim();
        String status = jTextField2.getText().trim();
        String recoveryDateStr = jTextField3.getText().trim();
        String description = jTextField4.getText().trim();
        
        if (selectedPlayer == null || injuryDateStr.isEmpty() || status.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill all required fields (Player, Injury Date, Status)!");
            return;
        }
        
        try {
            Connection conn = dbConnection.getConnection();
            
            Integer playerId = playerNameToIdMap.get(selectedPlayer);
            if (playerId == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Invalid player selected!");
                return;
            }
            
            String insertQuery = "INSERT INTO injuries (player_id, injury_date, health_status, recovery_date, injury_description) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            
            pstmt.setInt(1, playerId);
            pstmt.setDate(2, Date.valueOf(injuryDateStr)); // Expected format: yyyy-MM-dd
            pstmt.setString(3, status);
            
            if (!recoveryDateStr.isEmpty()) {
                pstmt.setDate(4, Date.valueOf(recoveryDateStr));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            
            if (!description.isEmpty()) {
                pstmt.setString(5, description);
            } else {
                pstmt.setNull(5, Types.VARCHAR);
            }
            
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            
            // Refresh table and clear fields
            loadInjuriesData();
            clearAllFields();
            javax.swing.JOptionPane.showMessageDialog(this, "Injury added successfully!");
            
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error adding injury: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = injuriesTable.getSelectedRow();
        if (selectedRow < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an injury to delete!");
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) injuriesTable.getModel();
        int injuryId = (int) model.getValueAt(selectedRow, 0);
        String playerName = (String) model.getValueAt(selectedRow, 1);
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete this injury record for " + playerName + "?",
            "Confirm Delete",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                Connection conn = dbConnection.getConnection();
                
                String deleteQuery = "DELETE FROM injuries WHERE injury_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                pstmt.setInt(1, injuryId);
                pstmt.executeUpdate();
                
                pstmt.close();
                conn.close();
                
                loadInjuriesData();
                clearAllFields();
                javax.swing.JOptionPane.showMessageDialog(this, "Injury deleted successfully!");
                
            } catch (SQLException e) {
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(this, "Error deleting injury: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
         int selectedRow = injuriesTable.getSelectedRow();
        if (selectedRow < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an injury to update!");
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) injuriesTable.getModel();
        int injuryId = (int) model.getValueAt(selectedRow, 0);
        
        String selectedPlayer = (String) jComboBox1.getSelectedItem();
        String injuryDateStr = jTextField1.getText().trim();
        String status = jTextField2.getText().trim();
        String recoveryDateStr = jTextField3.getText().trim();
        String description = jTextField4.getText().trim();
        
        if (selectedPlayer == null || injuryDateStr.isEmpty() || status.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill all required fields!");
            return;
        }
        
        try {
            Connection conn = dbConnection.getConnection();
            
            Integer playerId = playerNameToIdMap.get(selectedPlayer);
            if (playerId == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Invalid player selected!");
                return;
            }
            
            String updateQuery = "UPDATE injuries SET player_id = ?, injury_date = ?, health_status = ?, recovery_date = ?, injury_description = ? WHERE injury_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
            
            pstmt.setInt(1, playerId);
            pstmt.setDate(2, Date.valueOf(injuryDateStr));
            pstmt.setString(3, status);
            
            if (!recoveryDateStr.isEmpty()) {
                pstmt.setDate(4, Date.valueOf(recoveryDateStr));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            
            if (!description.isEmpty()) {
                pstmt.setString(5, description);
            } else {
                pstmt.setNull(5, Types.VARCHAR);
            }
            
            pstmt.setInt(6, injuryId);
            
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            
            loadInjuriesData();
            clearAllFields();
            javax.swing.JOptionPane.showMessageDialog(this, "Injury updated successfully!");
            
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error updating injury: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable injuriesTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
