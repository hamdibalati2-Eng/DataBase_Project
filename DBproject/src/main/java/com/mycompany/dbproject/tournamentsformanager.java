/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.dbproject;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Abd Alrahman
 */
public class tournamentsformanager extends javax.swing.JPanel {
        private int selectedRowId = -1;
        private DefaultTableModel tableModel ;
        private Services services;
    /**
     * Creates new form tournamentsformanager
     */
    public tournamentsformanager() {
        initComponents(); // Make sure your UI components are initialized here
    
    // Setup the table model first
    services = new Services();
    setupTableModel();
    
    // Then set up listeners
    setupRowSelectionListener();
    
    // Then load data
    loadTournaments();
    }
     private void clearFields() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jComboBox1.setSelectedIndex(0); // Reset to first item (Upcoming)
    }
     private String getCurrentDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new java.util.Date());
    }
    private void setupTableModel() {
        // Define column names
       String[] columnNames = {"ID", "Name", "Start Date", "End Date", "Location", "Status"};
    
    // Use this.tableModel or just tableModel to refer to the class field
    this.tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Integer.class;
            if (columnIndex == 2 || columnIndex == 3) return java.sql.Date.class;
            return String.class;
        }
        
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Make all cells non-editable
        }
    };
    
    // Set the model to the table
    tournamentstable.setModel(tableModel);
    
    // Hide the ID column
    tournamentstable.getColumnModel().getColumn(0).setMinWidth(0);
    tournamentstable.getColumnModel().getColumn(0).setMaxWidth(0);
    tournamentstable.getColumnModel().getColumn(0).setWidth(0);
    
    System.out.println("Tournament table model initialized");
    }
    private void setupRowSelectionListener() {
        tournamentstable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = tournamentstable.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        // Get the ID safely
                        Object idObj = tableModel.getValueAt(selectedRow, 0);
                        if (idObj != null) {
                            selectedRowId = (int)idObj;
                        } else {
                            System.err.println("Warning: ID is null for selected row");
                            return;
                        }
                        
                        // Name field
                        Object nameObj = tableModel.getValueAt(selectedRow, 1);
                        jTextField1.setText(nameObj != null ? nameObj.toString() : "");
                        
                        // Start date field
                        Object startDateObj = tableModel.getValueAt(selectedRow, 2);
                        if (startDateObj != null && startDateObj instanceof Date) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            jTextField2.setText(dateFormat.format((Date)startDateObj));
                        } else {
                            jTextField2.setText("");
                        }
                        
                        // End date field
                        Object endDateObj = tableModel.getValueAt(selectedRow, 3);
                        if (endDateObj != null && endDateObj instanceof Date) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            jTextField3.setText(dateFormat.format((Date)endDateObj));
                        } else {
                            jTextField3.setText("");
                        }
                        
                        // Location field
                        Object locationObj = tableModel.getValueAt(selectedRow, 4);
                        jTextField4.setText(locationObj != null ? locationObj.toString() : "");
                        
                        // Status combo box
                        Object statusObj = tableModel.getValueAt(selectedRow, 5);
                        if (statusObj != null) {
                            jComboBox1.setSelectedItem(statusObj.toString());
                        } else {
                            jComboBox1.setSelectedIndex(0); // Default to first item
                        }
                        
                        System.out.println("Selected tournament ID: " + selectedRowId);
                    } catch (Exception ex) {
                        System.err.println("Error selecting row: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        }
    });
    }
    private void loadTournaments() {
        if (tableModel == null) {
        System.err.println("tableModel is null in loadTournaments(). Initializing it now.");
        setupTableModel();
    }
    
    System.out.println("Loading tournaments with tableModel: " + tableModel);
    Services.loadTournamentsToTable(tableModel); 
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
        tournamentstable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 245, 220));

        tournamentstable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Name", "Start Date", "End Date", "Location", "Status"
            }
        ));
        jScrollPane1.setViewportView(tournamentstable);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Name:");

        jTextField1.setText("jTextField1");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Start Date:");

        jTextField2.setText("jTextField2");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("End Date:");

        jTextField3.setText("jTextField3");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Location:");

        jTextField4.setText("jTextField4");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Status:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Upcoming", "Ongoing", "Completed" }));

        jButton1.setBackground(new java.awt.Color(95, 99, 104));
        jButton1.setForeground(new java.awt.Color(242, 242, 242));
        jButton1.setText("Add New");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(95, 99, 104));
        jButton2.setForeground(new java.awt.Color(242, 242, 242));
        jButton2.setText("Update Selected");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(95, 99, 104));
        jButton3.setForeground(new java.awt.Color(242, 242, 242));
        jButton3.setText("Delete Selected");
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
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField3))
                                .addGap(68, 68, 68)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(47, 47, 47)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(38, 38, 38)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(36, 36, 36))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         String name = jTextField1.getText().trim();
        String startDateStr = jTextField2.getText().trim();
        String endDateStr = jTextField3.getText().trim();
        String location = jTextField4.getText().trim();
        String status = jComboBox1.getSelectedItem().toString();
        
        // Validate input
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tournament name cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Parse date strings to SQL Date objects
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            java.util.Date parsedStartDate = dateFormat.parse(startDateStr);
            java.sql.Date sqlStartDate = new java.sql.Date(parsedStartDate.getTime());
            
            java.util.Date parsedEndDate = dateFormat.parse(endDateStr);
            java.sql.Date sqlEndDate = new java.sql.Date(parsedEndDate.getTime());
            
            // Validate end date is after start date
            if (sqlEndDate.before(sqlStartDate)) {
                JOptionPane.showMessageDialog(this, "End date must be after start date", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Add tournament to database
            boolean success = Services.addTournament(name, sqlStartDate, sqlEndDate, location, status);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Tournament added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadTournaments(); // Refresh table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add tournament", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd", "Validation Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (selectedRowId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a tournament first", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String name = jTextField1.getText().trim();
        String startDateStr = jTextField2.getText().trim();
        String endDateStr = jTextField3.getText().trim();
        String location = jTextField4.getText().trim();
        String status = jComboBox1.getSelectedItem().toString();
        
        // Validate input
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tournament name cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Parse date strings to SQL Date objects
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            java.util.Date parsedStartDate = dateFormat.parse(startDateStr);
            java.sql.Date sqlStartDate = new java.sql.Date(parsedStartDate.getTime());
            
            java.util.Date parsedEndDate = dateFormat.parse(endDateStr);
            java.sql.Date sqlEndDate = new java.sql.Date(parsedEndDate.getTime());
            
            // Validate end date is after start date
            if (sqlEndDate.before(sqlStartDate)) {
                JOptionPane.showMessageDialog(this, "End date must be after start date", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Debug information
            System.out.println("Updating tournament ID: " + selectedRowId);
            System.out.println("Name: " + name);
            System.out.println("Start Date: " + sqlStartDate);
            System.out.println("End Date: " + sqlEndDate);
            System.out.println("Location: " + location);
            System.out.println("Status: " + status);
            
            // Update tournament in database
            boolean success = Services.updateTournament(selectedRowId, name, sqlStartDate, sqlEndDate, location, status);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Tournament updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTournaments(); // Refresh table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update tournament", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd", "Validation Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (selectedRowId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a tournament first", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this tournament?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = Services.deleteTournament(selectedRowId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Tournament deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadTournaments(); // Refresh table
                selectedRowId = -1; // Reset selection
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete tournament", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private javax.swing.JTable tournamentstable;
    // End of variables declaration//GEN-END:variables
}
