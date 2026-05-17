/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.dbproject;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.sql.*;
import java.util.Vector;

/**
 *
 * @author Abd Alrahman
 */
public class users extends javax.swing.JPanel {
    private DatabaseConnection dbConnection;
    private Services services;
    /**
     * Creates new form users
     */
    public users() {
        initComponents();
        dbConnection = new DatabaseConnection();
        services = new Services();
        setupTable();
        loadUsersData();
        setupTableSelectionListener();
        
    }
    private void setupTable() {
        String[] columnNames = {"User ID", "Username", "Role", "Age", "Additional Info"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        jTable1.setModel(model);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void loadUsersData() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing data
        
        try {
            Connection conn = dbConnection.getConnection();
                            String query = """
                SELECT u.user_id, u.user_name, u.user_role,
                       COALESCE(p.age, d.age, h.age) as age,
                       CASE 
                           WHEN u.user_role = 'Player' THEN CONCAT(p.position, ' - ', p.nationality)
                           WHEN u.user_role = 'Doctor' THEN d.specialization
                           WHEN u.user_role = 'HeadCoach' THEN h.nationality
                           WHEN u.user_role = 'Manager' THEN 'Manager'
                           ELSE ''
                       END as additional_info
                FROM users u
                LEFT JOIN players p ON u.user_name = p.user_name
                LEFT JOIN doctors d ON u.user_id = d.doctor_id
                LEFT JOIN headcoaches h ON u.user_id = h.coach_id
                ORDER BY u.user_id
            """;
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("user_id"));
                row.add(rs.getString("user_name"));
                row.add(rs.getString("user_role"));
                row.add(rs.getInt("age"));
                row.add(rs.getString("additional_info"));
                model.addRow(row);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error message to user
        }
    }
    
    // 2. Setup table selection listener to populate form fields
    private void setupTableSelectionListener() {
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow >= 0) {
                        loadSelectedUserData(selectedRow);
                    }
                }
            }
        });
    }
    
    private void loadSelectedUserData(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int userId = (int) model.getValueAt(selectedRow, 0);
        String username = (String) model.getValueAt(selectedRow, 1);
        String role = (String) model.getValueAt(selectedRow, 2);
        
        try {
            Connection conn = dbConnection.getConnection();
            
            // Load basic user data
            String userQuery = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement userPstmt = conn.prepareStatement(userQuery);
            userPstmt.setInt(1, userId);
            ResultSet userRs = userPstmt.executeQuery();
            
            if (userRs.next()) {
                jTextField1.setText(userRs.getString("user_name")); // User Name
                jTextField2.setText(userRs.getString("user_password")); // Password
                jComboBox1.setSelectedItem(userRs.getString("user_role")); // User Role
            }
            
            // Load role-specific data
            clearRoleFields();
            
            if ("Player".equals(role)) {
                String playerQuery = "SELECT * FROM players WHERE user_name = ?";
                PreparedStatement playerPstmt = conn.prepareStatement(playerQuery);
                playerPstmt.setString(1, username);
                ResultSet playerRs = playerPstmt.executeQuery();
                
                if (playerRs.next()) {
                    jTextField3.setText(String.valueOf(playerRs.getInt("age"))); // Player Age
                    jTextField4.setText(playerRs.getString("healthstatus")); // Health Status
                    jTextField5.setText(playerRs.getString("position")); // Position
                    jTextField6.setText(playerRs.getString("nationality")); // Nationality
                    jTextField7.setText(String.valueOf(playerRs.getInt("jerseynumber"))); // Jersey Number
                }
                playerRs.close();
                playerPstmt.close();
                
            } else if ("Doctor".equals(role)) {
                String doctorQuery = "SELECT * FROM doctors WHERE doctor_id = ?";
                PreparedStatement doctorPstmt = conn.prepareStatement(doctorQuery);
                doctorPstmt.setInt(1, userId);
                ResultSet doctorRs = doctorPstmt.executeQuery();
                
                if (doctorRs.next()) {
                    jTextField8.setText(doctorRs.getString("specialization")); // Specialization
                    jTextField3.setText(String.valueOf(doctorRs.getInt("age"))); // Age
                }
                doctorRs.close();
                doctorPstmt.close();
                
            } else if ("HeadCoach".equals(role)) {
                String coachQuery = "SELECT * FROM headcoaches WHERE coach_id = ?";
                PreparedStatement coachPstmt = conn.prepareStatement(coachQuery);
                coachPstmt.setInt(1, userId);
                ResultSet coachRs = coachPstmt.executeQuery();
                
                if (coachRs.next()) {
                    jTextField3.setText(String.valueOf(coachRs.getInt("age"))); // Age
                    jTextField6.setText(coachRs.getString("nationality")); // Nationality
                }
                coachRs.close();
                coachPstmt.close();
            }
            
            userRs.close();
            userPstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void clearRoleFields() {
        jTextField3.setText(""); // Age/Player Age
        jTextField4.setText(""); // Health Status
        jTextField5.setText(""); // Position
        jTextField6.setText(""); // Nationality
        jTextField7.setText(""); // Jersey Number
        jTextField8.setText(""); // Specialization
    }
   private void deleteFromRoleTable(Connection conn, String role, int userId, String username) throws SQLException {
        if ("Player".equals(role)) {
            String deleteQuery = "DELETE FROM players WHERE user_name = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            pstmt.close();
        } else if ("Doctor".equals(role)) {
            String deleteQuery = "DELETE FROM doctors WHERE doctor_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            pstmt.close();
        } else if ("HeadCoach".equals(role)) {
            String deleteQuery = "DELETE FROM headcoaches WHERE coach_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            pstmt.close();
        }
        // Manager role doesn't have a separate table
    }
    
    private void insertIntoRoleTable(Connection conn, String role, String username) throws SQLException {
        if ("Player".equals(role)) {
            String insert = "INSERT INTO players (age, healthstatus, position, nationality, jerseynumber, user_name) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insert);
            pstmt.setInt(1, Integer.parseInt(jTextField3.getText().trim()));
            pstmt.setString(2, jTextField4.getText().trim());
            pstmt.setString(3, jTextField5.getText().trim());
            pstmt.setString(4, jTextField6.getText().trim());
            pstmt.setInt(5, Integer.parseInt(jTextField7.getText().trim()));
            pstmt.setString(6, username);
            pstmt.executeUpdate();
            pstmt.close();
        } else if ("Doctor".equals(role)) {
            // Get the user_id for the new role
            String getUserId = "SELECT user_id FROM users WHERE user_name = ?";
            PreparedStatement getUserIdPstmt = conn.prepareStatement(getUserId);
            getUserIdPstmt.setString(1, username);
            ResultSet rs = getUserIdPstmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String insert = "INSERT INTO doctors (doctor_id, specialization, age) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insert);
                pstmt.setInt(1, userId);
                pstmt.setString(2, jTextField8.getText().trim());
                pstmt.setInt(3, Integer.parseInt(jTextField3.getText().trim()));
                pstmt.executeUpdate();
                pstmt.close();
            }
            rs.close();
            getUserIdPstmt.close();
        } else if ("HeadCoach".equals(role)) {
            // Get the user_id for the new role
            String getUserId = "SELECT user_id FROM users WHERE user_name = ?";
            PreparedStatement getUserIdPstmt = conn.prepareStatement(getUserId);
            getUserIdPstmt.setString(1, username);
            ResultSet rs = getUserIdPstmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String insert = "INSERT INTO headcoaches (coach_id, age, nationality) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insert);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, Integer.parseInt(jTextField3.getText().trim()));
                pstmt.setString(3, jTextField6.getText().trim());
                pstmt.executeUpdate();
                pstmt.close();
            }
            rs.close();
            getUserIdPstmt.close();
        }
        // Manager role doesn't need insertion into a separate table
    }
    
    private void updateRoleTable(Connection conn, String role, String oldUsername, String newUsername) throws SQLException {
        if ("Player".equals(role)) {
            String update = "UPDATE players SET age = ?, healthstatus = ?, position = ?, nationality = ?, jerseynumber = ?, user_name = ? WHERE user_name = ?";
            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setInt(1, Integer.parseInt(jTextField3.getText().trim()));
            pstmt.setString(2, jTextField4.getText().trim());
            pstmt.setString(3, jTextField5.getText().trim());
            pstmt.setString(4, jTextField6.getText().trim());
            pstmt.setInt(5, Integer.parseInt(jTextField7.getText().trim()));
            pstmt.setString(6, newUsername);
            pstmt.setString(7, oldUsername);
            pstmt.executeUpdate();
            pstmt.close();
        } else if ("Doctor".equals(role)) {
            // Get the user_id for the doctor
            String getUserId = "SELECT user_id FROM users WHERE user_name = ?";
            PreparedStatement getUserIdPstmt = conn.prepareStatement(getUserId);
            getUserIdPstmt.setString(1, newUsername);
            ResultSet rs = getUserIdPstmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String update = "UPDATE doctors SET specialization = ?, age = ? WHERE doctor_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(update);
                pstmt.setString(1, jTextField8.getText().trim());
                pstmt.setInt(2, Integer.parseInt(jTextField3.getText().trim()));
                pstmt.setInt(3, userId);
                pstmt.executeUpdate();
                pstmt.close();
            }
            rs.close();
            getUserIdPstmt.close();
        } else if ("HeadCoach".equals(role)) {
            // Get the user_id for the headcoach
            String getUserId = "SELECT user_id FROM users WHERE user_name = ?";
            PreparedStatement getUserIdPstmt = conn.prepareStatement(getUserId);
            getUserIdPstmt.setString(1, newUsername);
            ResultSet rs = getUserIdPstmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String update = "UPDATE headcoaches SET age = ?, nationality = ? WHERE coach_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(update);
                pstmt.setInt(1, Integer.parseInt(jTextField3.getText().trim()));
                pstmt.setString(2, jTextField6.getText().trim());
                pstmt.setInt(3, userId);
                pstmt.executeUpdate();
                pstmt.close();
            }
            rs.close();
            getUserIdPstmt.close();
        }
        // Manager role doesn't need updates in a separate table
    }
    
    private void clearAllFields() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jComboBox1.setSelectedIndex(0);
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
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 245, 220));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("User Name:");

        jTextField1.setText("jTextField1");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Pasworrd:");

        jTextField2.setText("jTextField2");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("User Role:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Player", "HeadCoach", "Doctor", "Manager" }));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Player Age:");

        jTextField3.setText("jTextField3");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Health Status:");

        jTextField4.setText("jTextField4");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Position:");

        jTextField5.setText("jTextField5");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Nationality:");

        jTextField6.setText("jTextField6");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Jersey Number:");

        jTextField7.setText("jTextField7");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Specialization:");

        jTextField8.setText("jTextField8");

        jButton1.setBackground(new java.awt.Color(95, 99, 104));
        jButton1.setForeground(new java.awt.Color(242, 242, 242));
        jButton1.setText("Add New User");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(95, 99, 104));
        jButton2.setForeground(new java.awt.Color(242, 242, 242));
        jButton2.setText("Delete Selected");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(95, 99, 104));
        jButton3.setForeground(new java.awt.Color(242, 242, 242));
        jButton3.setText("Update Seleted");
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
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel9))
                                .addGap(35, 35, 35))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(13, 13, 13)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField8)
                                        .addComponent(jTextField1)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel4))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jButton2)
                                .addGap(33, 33, 33)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel4)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
    if (selectedRow < 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please select a user to update!");
        return;
    }
    
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    int originalUserId = (int) model.getValueAt(selectedRow, 0);
    String originalUsername = (String) model.getValueAt(selectedRow, 1);
    String role = (String) model.getValueAt(selectedRow, 2);
    
    String newUsername = jTextField1.getText().trim();
    String password = jTextField2.getText().trim();
    String newRole = (String) jComboBox1.getSelectedItem();
    
    try {
        Connection conn = dbConnection.getConnection();
        conn.setAutoCommit(false);
        
        // Update users table
        String userUpdate = "UPDATE users SET user_name = ?, user_password = ?, user_role = CAST(? AS user_role) WHERE user_id = ?";
        PreparedStatement userPstmt = conn.prepareStatement(userUpdate);
        userPstmt.setString(1, newUsername);
        userPstmt.setString(2, password);
        userPstmt.setString(3, newRole);
        userPstmt.setInt(4, originalUserId);
        userPstmt.executeUpdate();
        
        // If role changed, delete from old table and insert into new table
        if (!role.equals(newRole)) {
            // Delete from old role table
            deleteFromRoleTable(conn, role, originalUserId, originalUsername);
            // Insert into new role table
            insertIntoRoleTable(conn, newRole, newUsername);
        } else {
            // Update existing role table
            updateRoleTable(conn, role, originalUsername, newUsername);
        }
        
        conn.commit();
        userPstmt.close();
        conn.close();
        
        loadUsersData();
        clearAllFields();
        javax.swing.JOptionPane.showMessageDialog(this, "User updated successfully!");
        
    } catch (SQLException | NumberFormatException e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage());
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String username = jTextField1.getText().trim();
        String password = jTextField2.getText().trim();
        String role = (String) jComboBox1.getSelectedItem();
        
        if (username.isEmpty() || password.isEmpty() || role == null) {
            // Show validation error
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill all required fields!");
            return;
        }
        
        try {
            Connection conn = dbConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Insert into users table
            String userInsert = "INSERT INTO users (user_name, user_password, user_role) VALUES (?, ?, CAST(? AS user_role))";
            PreparedStatement userPstmt = conn.prepareStatement(userInsert);
            userPstmt.setString(1, username);
            userPstmt.setString(2, password);
            userPstmt.setString(3, role);
            userPstmt.executeUpdate();
            
            // Insert into role-specific table
            if ("Player".equals(role)) {
                String playerInsert = "INSERT INTO players (age, healthstatus, position, nationality, jerseynumber, user_name) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement playerPstmt = conn.prepareStatement(playerInsert);
                playerPstmt.setInt(1, Integer.parseInt(jTextField3.getText().trim())); // age
                playerPstmt.setString(2, jTextField4.getText().trim()); // healthstatus
                playerPstmt.setString(3, jTextField5.getText().trim()); // position
                playerPstmt.setString(4, jTextField6.getText().trim()); // nationality
                playerPstmt.setInt(5, Integer.parseInt(jTextField7.getText().trim())); // jerseynumber
                playerPstmt.setString(6, username); // user_name
                playerPstmt.executeUpdate();
                playerPstmt.close();
                
            } else if ("Doctor".equals(role)) {
                // Get the user_id that was just inserted
                String getUserId = "SELECT user_id FROM users WHERE user_name = ?";
                PreparedStatement getUserIdPstmt = conn.prepareStatement(getUserId);
                getUserIdPstmt.setString(1, username);
                ResultSet rs = getUserIdPstmt.executeQuery();
                
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String doctorInsert = "INSERT INTO doctors (doctor_id, specialization, age) VALUES (?, ?, ?)";
                    PreparedStatement doctorPstmt = conn.prepareStatement(doctorInsert);
                    doctorPstmt.setInt(1, userId); // doctor_id = user_id
                    doctorPstmt.setString(2, jTextField8.getText().trim()); // specialization
                    doctorPstmt.setInt(3, Integer.parseInt(jTextField3.getText().trim())); // age
                    doctorPstmt.executeUpdate();
                    doctorPstmt.close();
                }
                rs.close();
                getUserIdPstmt.close();
                
            } else if ("HeadCoach".equals(role)) {
                // Get the user_id that was just inserted
                String getUserId = "SELECT user_id FROM users WHERE user_name = ?";
                PreparedStatement getUserIdPstmt = conn.prepareStatement(getUserId);
                getUserIdPstmt.setString(1, username);
                ResultSet rs = getUserIdPstmt.executeQuery();
                
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String coachInsert = "INSERT INTO headcoaches (coach_id, age, nationality) VALUES (?, ?, ?)";
                    PreparedStatement coachPstmt = conn.prepareStatement(coachInsert);
                    coachPstmt.setInt(1, userId); // coach_id = user_id
                    coachPstmt.setInt(2, Integer.parseInt(jTextField3.getText().trim())); // age
                    coachPstmt.setString(3, jTextField6.getText().trim()); // nationality
                    coachPstmt.executeUpdate();
                    coachPstmt.close();
                }
                rs.close();
                getUserIdPstmt.close();
            }
            // Note: Manager role doesn't have a separate table based on the images provided
            
            conn.commit(); // Commit transaction
            userPstmt.close();
            conn.close();
            
            // Refresh table and clear fields
            loadUsersData();
            clearAllFields();
            javax.swing.JOptionPane.showMessageDialog(this, "User added successfully!");
            
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a user to delete!");
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int userId = (int) model.getValueAt(selectedRow, 0);
        String username = (String) model.getValueAt(selectedRow, 1);
        String role = (String) model.getValueAt(selectedRow, 2);
        
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete user: " + username + "?",
            "Confirm Delete",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                Connection conn = dbConnection.getConnection();
                conn.setAutoCommit(false);
                
                // Delete from role-specific table first (foreign key constraint)
                deleteFromRoleTable(conn, role, userId, username);
                
                // Delete from users table
                String userDelete = "DELETE FROM users WHERE user_id = ?";
                PreparedStatement userPstmt = conn.prepareStatement(userDelete);
                userPstmt.setInt(1, userId);
                userPstmt.executeUpdate();
                
                conn.commit();
                userPstmt.close();
                conn.close();
                
                loadUsersData();
                clearAllFields();
                javax.swing.JOptionPane.showMessageDialog(this, "User deleted successfully!");
                
            } catch (SQLException e) {
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed


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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
