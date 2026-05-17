/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.dbproject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Abd Alrahman
 */
public class Trainingsessionforcoach extends javax.swing.JPanel {

    private javax.swing.table.DefaultTableModel tableModel;
    private Services services;
    private TrainingSession selectedSession;
    private int selectedSessionId;
    /**
     * Creates new form Trainingsessionforcoach
     */
    public Trainingsessionforcoach() {
        initComponents();
        services = new Services();
        tableModel = (javax.swing.table.DefaultTableModel) Sessiontable.getModel();
        loadData();
    }
    
    private void addNewSession() {
        try {
            // Get values from form
            String sessionType = typefield.getSelectedItem().toString();
            String sessionDateStr = datefeild.getText().trim();
            
            // Validate input
            if (sessionType.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a session type.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (sessionDateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a session date.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse date - modify this format if needed
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            java.util.Date parsedDate;
            
            try {
                parsedDate = dateFormat.parse(sessionDateStr);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid date format. Please use format: YYYY-MM-DD HH:MM", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Convert to SQL Timestamp
            Timestamp sessionDate = new Timestamp(parsedDate.getTime());
            
            // Create TrainingSession object
            TrainingSession session = new TrainingSession();
            session.setSessionType(sessionType);
            session.setSessionDate(sessionDate);
            
            // Add to database
            boolean success = services.addTrainingSession(session);
            
            if (success) {
                // Show success message
                JOptionPane.showMessageDialog(this, 
                    "Training session added successfully.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear input fields
                typefield.setSelectedIndex(0);
                datefeild.setText("");
                
                // Refresh data
               // loadData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to add training session.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            System.err.println("Error adding training session: " + ex.getMessage());
            ex.printStackTrace();
            
            JOptionPane.showMessageDialog(this, 
                "Error adding training session: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refreshData() {
        loadData();
    }
    
     public void loadData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try {
            // Get training sessions from the database
            List<TrainingSession> sessions = services.getTrainingSessions();
            
            // Add sessions to the table
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            
            for (TrainingSession session : sessions) {
                String formattedDate = (session.getSessionDate() != null) ? 
                    dateFormat.format(session.getSessionDate()) : "";
                    
                Object[] rowData = {
                    session.getSessionType(),
                    formattedDate
                };
                
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            System.err.println("Error loading training sessions: " + e.getMessage());
            e.printStackTrace();
        }
    }
     
     private void updateSession() {
    try {
        // Get values from form
        String sessionType = typefield.getSelectedItem().toString();
        String sessionDateStr = datefeild.getText().trim();
        
        // Validate input
        if (sessionType.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a session type.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (sessionDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a session date.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Parse date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date parsedDate;
        
        try {
            parsedDate = dateFormat.parse(sessionDateStr);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, 
                "Invalid date format. Please use format: YYYY-MM-DD HH:MM", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Convert to SQL Timestamp
        Timestamp sessionDate = new Timestamp(parsedDate.getTime());
        
        // Update the TrainingSession object
        selectedSession.setSessionType(sessionType);
        selectedSession.setSessionDate(sessionDate);
        
        // Update in database
        boolean success = services.updateTrainingSession(selectedSession);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Training session updated successfully.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear input fields and selection
            typefield.setSelectedIndex(0);
            datefeild.setText("");
            selectedSession = null;
            
            // Refresh data
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to update training session.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
    } catch (Exception ex) {
        System.err.println("Error updating training session: " + ex.getMessage());
        ex.printStackTrace();
        
        JOptionPane.showMessageDialog(this, 
            "Error updating training session: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
     private void sessionTableRowSelected() {
    int selectedRow = Sessiontable.getSelectedRow();
    if (selectedRow != -1) {
        try {
            // Get session ID (assuming it's displayed in the table or stored in the table model)
            List<TrainingSession> sessions = services.getTrainingSessions();
            selectedSession = sessions.get(selectedRow);
            selectedSessionId = selectedSession.getSessionId();
            
            // Populate form fields
            typefield.setSelectedItem(selectedSession.getSessionType());
            
            // Format the date for display
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            datefeild.setText(dateFormat.format(selectedSession.getSessionDate()));
            
        } catch (Exception e) {
            System.err.println("Error selecting session: " + e.getMessage());
            e.printStackTrace();
        }
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Sessiontable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        typefield = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        datefeild = new javax.swing.JTextField();
        addsession = new javax.swing.JButton();
        editsession = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(245, 245, 220));

        Sessiontable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Session Type", "Session Date"
            }
        ));
        Sessiontable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SessiontableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Sessiontable);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Session Type:");

        typefield.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recovery", "Rest", "Physical" }));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Session Date:");

        addsession.setBackground(new java.awt.Color(95, 99, 104));
        addsession.setForeground(new java.awt.Color(242, 242, 242));
        addsession.setText("Add new one");
        addsession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addsessionActionPerformed(evt);
            }
        });

        editsession.setBackground(new java.awt.Color(95, 99, 104));
        editsession.setForeground(new java.awt.Color(242, 242, 242));
        editsession.setText("Edit selected");
        editsession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editsessionActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(95, 99, 104));
        jButton1.setForeground(new java.awt.Color(242, 242, 242));
        jButton1.setText("Delete Session");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(typefield, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(addsession, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(77, 77, 77)
                                .addComponent(jButton1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(datefeild, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(editsession, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)))))
                .addGap(35, 35, 35))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(typefield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(datefeild, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addsession)
                    .addComponent(editsession)
                    .addComponent(jButton1))
                .addGap(54, 54, 54))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 609, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addsessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addsessionActionPerformed
            addNewSession();
            refreshData();
    }//GEN-LAST:event_addsessionActionPerformed

    private void editsessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editsessionActionPerformed
        if (selectedSession == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a session from the table first.",
            "No Selection",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    updateSession();
    }//GEN-LAST:event_editsessionActionPerformed

    private void SessiontableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SessiontableMouseClicked
        Sessiontable.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting() && Sessiontable.getSelectedRow() != -1) {
            sessionTableRowSelected();
        }
    }
});
    }//GEN-LAST:event_SessiontableMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      int selectedRow = Sessiontable.getSelectedRow();
    
    // Check if a row is selected
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Please select a session to delete.",
            "No Selection",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Confirm deletion
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete this training session?",
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION);
    
    if (confirm != JOptionPane.YES_OPTION) {
        return; // User canceled the operation
    }
    
    try {
        // If your table has a hidden column for session_id
        int sessionId = -1;
        
        // Option 1: If the session_id is directly stored in the table model
        if (Sessiontable.getColumnCount() > 2) { // Assuming there's a hidden column for ID
            sessionId = Integer.parseInt(Sessiontable.getValueAt(selectedRow, 2).toString());
        } 
        // Option 2: If you need to look up the session ID based on type and date
        else {
            String sessionType = (String) Sessiontable.getValueAt(selectedRow, 0);
            String sessionDateStr = (String) Sessiontable.getValueAt(selectedRow, 1);
            
            // Parse the date
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            java.util.Date parsedDate = displayFormat.parse(sessionDateStr);
            Timestamp sessionDate = new Timestamp(parsedDate.getTime());
            
            // Find session ID based on type and date
            sessionId = services.getSessionId(sessionType, sessionDate);
        }
        
        // Check if we have a valid session ID
        if (sessionId <= 0) {
            JOptionPane.showMessageDialog(this,
                "Could not identify the session to delete.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the table model
        javax.swing.table.DefaultTableModel tableModel = (javax.swing.table.DefaultTableModel) Sessiontable.getModel();
        
        // Delete the session from the database
        boolean success = services.deleteTrainingSessionById(sessionId);
        
        if (success) {
            // Remove the row from the table model
            tableModel.removeRow(selectedRow);
            
            JOptionPane.showMessageDialog(this,
                "Training session deleted successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to delete training session.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        System.err.println("Error deleting training session: " + ex.getMessage());
        ex.printStackTrace();
        
        JOptionPane.showMessageDialog(this,
            "Error deleting training session: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Sessiontable;
    private javax.swing.JButton addsession;
    private javax.swing.JTextField datefeild;
    private javax.swing.JButton editsession;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> typefield;
    // End of variables declaration//GEN-END:variables
}
