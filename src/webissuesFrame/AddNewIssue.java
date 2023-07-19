package webissuesFrame;

import DAO.ProjectsDAO;
import DAOImpl.ProjectsDAOImpl;
import dbConnection.DbConnection;
import java.awt.Component;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import static javax.swing.GroupLayout.Alignment.values;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;

public class AddNewIssue extends javax.swing.JFrame {

    public AddNewIssue() {
        initComponents();
        Image icon = new ImageIcon(this.getClass().getResource("/webissueslogo.png")).getImage();
        this.setIconImage(icon);
        this.setTitle("Add Issue");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setIconImage(icon);
    }
    String locationValue = "";
    int issueId = 0;
    int folderId = 0;
    private Map<String, String> attrValues = new HashMap<>();

    public void setRowData(Object[] rowData, String[] columnNames) {
        int numFields = columnNames.length;
        jPanel5.removeAll();
        int x = 30;
        int tx = 40;
        int y = 10;
        int labelWidth = 100;
        int componentWidth = 500;
        int height = 28;
        int spacing = 7;
        attrValues = new HashMap<>();

        for (int i = 0; i < numFields; i++) {
            if (columnNames[i].equalsIgnoreCase("LOCATION") && rowData[i] != null) {
                String locationString = rowData[i].toString();
                int lastDashIndex = locationString.lastIndexOf("-");
                if (lastDashIndex != -1 && lastDashIndex < locationString.length() - 1) {
                    locationValue = locationString.substring(lastDashIndex + 1).trim();
                }
            }
        }

        if (locationValue != null) {
            Connection con = null;
            Statement statement = null;

            try {
                con = DbConnection.getConnection();
                statement = con.createStatement();

                String getTypeQuery = "SELECT type_id FROM folders WHERE folder_name = '" + locationValue + "'";
                ResultSet typeResultSet = statement.executeQuery(getTypeQuery);
                int typeId = 0;
                if (typeResultSet.next()) {
                    typeId = typeResultSet.getInt("type_id");
                }

                String getTypeName = "SELECT type_name FROM issue_types WHERE type_id = " + typeId;
                ResultSet typeNameResultSet = statement.executeQuery(getTypeName);

                if (typeNameResultSet.next()) {
                    String typeName = typeNameResultSet.getString("type_name");
                    typename.setText(typeName);
                    System.out.println("type name " + typeName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        int issueId = 0;
        for (int i = 0; i < numFields; i++) {
            if (columnNames[i].equalsIgnoreCase("Issue") && columnNames[i + 1].equalsIgnoreCase("ID")) {
                issueId = rowData[i + 1] != null ? Integer.parseInt(rowData[i + 1].toString()) : 0;
                break;
            }
        }

        try {
            Connection con = DbConnection.getConnection();
            Statement statement = con.createStatement();

            if (locationValue != null) {
                ResultSet resultSet = statement.executeQuery("SELECT type_id FROM folders WHERE folder_name = '" + locationValue + "'");
                if (resultSet.next()) {
                    int typeId = resultSet.getInt("type_id");

                    resultSet = statement.executeQuery("SELECT attr_name, attr_def FROM attr_types WHERE type_id = " + typeId);
                    while (resultSet.next()) {
                        String attrName = resultSet.getString("attr_name");
                        String attrDef = resultSet.getString("attr_def");

                        JLabel label = new JLabel(attrName);
                        label.setBounds(x, y, labelWidth, height);
                        jPanel5.add(label);

                        if (attrDef.startsWith("ENUM")) {
                            String enumItems = attrDef.substring(attrDef.indexOf("items={") + 7, attrDef.lastIndexOf("}"));
                            String[] values = enumItems.split(",");
                            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
                            JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);
                            comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);

                            for (String value : values) {
                                String trimmedValue = value.trim().replaceAll("\"", "");
                                comboBoxModel.addElement(trimmedValue);
                            }

                            jPanel5.add(comboBox);
                            attrValues.put(attrName, comboBoxModel.getSelectedItem().toString());
                        } else if (attrDef.startsWith("NUMERIC")) {
                            double minValue = 0.0;
                            double maxValue = 0.0;
                            String[] parts = attrDef.split(" ");
                            for (String part : parts) {
                                if (part.startsWith("min-value=")) {
                                    String value = part.substring(part.indexOf("\"") + 1, part.lastIndexOf("\""));
                                    minValue = Double.parseDouble(value);
                                } else if (part.startsWith("max-value=")) {
                                    String value = part.substring(part.indexOf("\"") + 1, part.lastIndexOf("\""));
                                    maxValue = Double.parseDouble(value);
                                }
                            }

                            if (maxValue > 3.0) {
                                JTextField textField = new JTextField();
                                textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                jPanel5.add(textField);
                                attrValues.put(attrName, textField.getText());
                            } else {
                                DefaultComboBoxModel<Integer> comboBoxModel = new DefaultComboBoxModel<>();
                                JComboBox<Integer> comboBox = new JComboBox<>(comboBoxModel);
                                comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                for (int value = (int) minValue; value <= maxValue; value++) {
                                    comboBoxModel.addElement(value);
                                }
                                jPanel5.add(comboBox);
                                attrValues.put(attrName, comboBoxModel.getSelectedItem().toString());
                            }
                        } else if (attrDef.startsWith("USER")) {
                            DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
                            JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);
                            comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                            Statement attrStatement = con.createStatement();
                            ResultSet attrResultSet = attrStatement.executeQuery("SELECT user_name FROM users");
                            while (attrResultSet.next()) {
                                String userName = attrResultSet.getString("user_name");
                                comboBoxModel.addElement(userName);
                            }
                            jPanel5.add(comboBox);
                            attrValues.put(attrName, comboBoxModel.getSelectedItem().toString());

                            attrResultSet.close();
                            attrStatement.close();
                        } else if (attrDef.startsWith("TEXT")) {
                            JTextField textField = new JTextField();
                            textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                            jPanel5.add(textField);
                            attrValues.put(attrName, textField.getText());
                        } else if (attrDef.startsWith("DATETIME")) {
                            JXDatePicker datePicker = new JXDatePicker();
                            datePicker.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                            jPanel5.add(datePicker);
                            if (datePicker.getDate() != null) {
                                attrValues.put(attrName, datePicker.getDate().toString());
                            }
                        }

                        y += height + spacing;
                    }
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jPanel5.revalidate();
        jPanel5.repaint();
    }

    private void handleOkButton() {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DbConnection.getConnection();
            con.setAutoCommit(false);
            System.out.println("issue name on line number 192 " + newissue.getText());
            String getSessionQuery = "SELECT user_id FROM sessions";
            PreparedStatement getSessionStatement = con.prepareStatement(getSessionQuery);
            ResultSet sessionResultSet = getSessionStatement.executeQuery();
            int userId = 0;
            if (sessionResultSet.next()) {
                userId = sessionResultSet.getInt("user_id");
                System.out.println("user session id " + userId);
            }

            // Insert user_id into stamps table
            String insertStampsQuery = "INSERT INTO stamps (user_id, stamp_time) VALUES (?, ?)";
            PreparedStatement insertStampsStatement = con.prepareStatement(insertStampsQuery, Statement.RETURN_GENERATED_KEYS);
            insertStampsStatement.setInt(1, userId);
            insertStampsStatement.setInt(2, (int) (System.currentTimeMillis() / 1000));
            insertStampsStatement.executeUpdate();
            ResultSet resultSet = insertStampsStatement.getGeneratedKeys();
            int issueId = 0;
            if (resultSet.next()) {
                issueId = resultSet.getInt(1);
                System.out.println("Issue ID " + issueId);
            }
            System.out.println("Inserted into stamps table. issueId: " + issueId);

            String getFolderId = "SELECT folder_id from folders where folder_name = '" + locationValue + "'";
            System.out.println("query " + getFolderId);
            PreparedStatement folderid = con.prepareStatement(getFolderId);
            System.out.println("query on line 205 " + getFolderId);
            ResultSet get = folderid.executeQuery();
            if (get.next()) {
                System.out.println("inside if ");
                folderId = get.getInt("folder_id");
                System.out.println("Folder ID " + folderId);
            }

            String query = "INSERT INTO issues (issue_id, folder_id, issue_name, stamp_id) VALUES (?, ?, ?, ?)";
            statement = con.prepareStatement(query);
            statement.setInt(1, issueId);
            statement.setInt(2, folderId);
            statement.setString(3, newissue.getText());
            statement.setInt(4, issueId);
            statement.executeUpdate();

            System.out.println("Inserted into issues table.");

            String changesQuery = "INSERT INTO changes (change_id, issue_id, change_type, stamp_id, value_new) VALUES (?, ?, ?, ?, ?)";
            statement = con.prepareStatement(changesQuery);
            statement.setInt(1, issueId);
            statement.setInt(2, issueId);
            statement.setInt(3, 0);
            statement.setInt(4, issueId);
            statement.setString(5, newissue.getText());
            statement.executeUpdate();

//            String attrValuesQuery = "INSERT INTO attr_values (issue_id, attr_id, attr_value) VALUES (?, ?, ?)";
//            statement = con.prepareStatement(attrValuesQuery);
//
//            for (Map.Entry<String, String> entry : attrValues.entrySet()) {
//                String attrName = entry.getKey();
//                String attrValue = entry.getValue();
//
//                // Retrieve the attr_id from attr_types based on attr_name
//                String attrIdQuery = "SELECT attr_id FROM attr_types WHERE attr_name = ?";
//                PreparedStatement attrIdStatement = con.prepareStatement(attrIdQuery);
//                attrIdStatement.setString(1, attrName);
//                ResultSet attrIdResult = attrIdStatement.executeQuery();
//
//                if (attrIdResult.next()) {
//                    int attrId = attrIdResult.getInt("attr_id");
//                    String attrType = attrIdResult.getString("attr_type");
//
//                    statement.setInt(1, issueId);
//                    statement.setInt(2, attrId);
//
//                    // Handle different data types for attr_value based on attr_type
//                    if ("INT".equals(attrType)) {
//                        // Convert the attrValue to an integer and set it as an int parameter
//                        int intValue = Integer.parseInt(attrValue);
//                        statement.setInt(3, intValue);
//                    } else {
//                        // Treat the attrValue as a string and set it as a string parameter
//                        statement.setString(3, attrValue);
//                    }
//
//                    statement.executeUpdate();
//                }
//            }
            String updateQuery = "UPDATE folders SET stamp_id = ? WHERE folder_id = ? AND COALESCE(stamp_id, 0) < ?";
            statement = con.prepareStatement(updateQuery);
            statement.setInt(1, issueId);
            statement.setInt(2, folderId);
            statement.setInt(3, issueId);
            statement.executeUpdate();

            if (description.getText() != null && !description.getText().isEmpty()) {
                String stampQuery = "INSERT INTO stamps (user_id, stamp_time) VALUES (?, ?)";
                PreparedStatement stampStatement = con.prepareStatement(stampQuery, Statement.RETURN_GENERATED_KEYS);
                stampStatement.setInt(1, userId);
                stampStatement.setLong(2, System.currentTimeMillis() / 1000);
                stampStatement.executeUpdate();

                ResultSet stampResult = stampStatement.getGeneratedKeys();
                int stampId = 0;
                if (stampResult.next()) {
                    stampId = stampResult.getInt(1);
                }

                String descriptionQuery = "INSERT INTO issue_descriptions (issue_id, descr_text, descr_format) VALUES (?, ?, ?)";
                PreparedStatement descriptionStatement = con.prepareStatement(descriptionQuery);
                descriptionStatement.setInt(1, issueId);
                descriptionStatement.setString(2, description.getText());
                descriptionStatement.setInt(3, 1);
                descriptionStatement.executeUpdate();

                String updateIssuesQuery = "UPDATE issues SET descr_id = ? WHERE issue_id = ? AND COALESCE(descr_id, descr_stub_id, 0) < ?";
                PreparedStatement updateIssuesStatement = con.prepareStatement(updateIssuesQuery);
                updateIssuesStatement.setInt(1, stampId);
                updateIssuesStatement.setInt(2, issueId);
                updateIssuesStatement.setInt(3, stampId);
                updateIssuesStatement.executeUpdate();

                String updateStampQuery = "UPDATE issues SET stamp_id = ? WHERE issue_id = ? AND stamp_id < ?";
                PreparedStatement updateStampStatement = con.prepareStatement(updateStampQuery);
                updateStampStatement.setInt(1, stampId);
                updateStampStatement.setInt(2, issueId);
                updateStampStatement.setInt(3, stampId);
                updateStampStatement.executeUpdate();

                String updateFoldersQuery = "UPDATE folders SET stamp_id = ? WHERE folder_id = ? AND COALESCE(stamp_id, 0) < ?";
                PreparedStatement updateFoldersStatement = con.prepareStatement(updateFoldersQuery);
                updateFoldersStatement.setInt(1, stampId);
                updateFoldersStatement.setInt(2, folderId);
                updateFoldersStatement.setInt(3, stampId);
                updateFoldersStatement.executeUpdate();

                con.commit(); // Commit the transaction
            }

            con.commit(); // Commit the transaction

            System.out.println("Transaction committed successfully.");
        } catch (SQLException ex) {
            if (con != null) {
                try {
                    con.rollback(); // Rollback the transaction
                    System.out.println("Transaction rolled back.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                    System.out.println("Statement closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        clmname = new javax.swing.JLabel();
        value11 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextPane();
        jSeparator1 = new javax.swing.JSeparator();
        info = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        issuename = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        newissue = new javax.swing.JTextField();
        typename = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(741, 517));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\addissue.jpg")); // NOI18N
        jLabel1.setText("Create a new Issue in folder:");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 654, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 238, Short.MAX_VALUE)
        );

        clmname.setText("Name");
        clmname.setAlignmentX(20.0F);

        value11.setText("Value");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addComponent(clmname, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(76, 76, 76)
                            .addComponent(value11, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(214, 214, 214))
                        .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clmname, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(value11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jTabbedPane1.addTab("Attributes ", jPanel2);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setViewportView(description);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Description", jPanel4);

        info.setIcon(new javax.swing.ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\info.png")); // NOI18N
        info.setText("please enter values.");

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("CANCEL");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        issuename.setText("Name:");

        jLabel2.setText("Name:");

        typename.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        typename.setText("jLabel3");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newissue)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTabbedPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(typename, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(info)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(25, 25, 25))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(337, 337, 337)
                    .addComponent(issuename, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                    .addGap(338, 338, 338)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(typename, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newissue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(60, 60, 60))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(249, 249, 249)
                    .addComponent(issuename, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addGap(250, 250, 250)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        handleOkButton();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        dispose();

    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddNewIssue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewIssue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewIssue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewIssue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddNewIssue().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clmname;
    private javax.swing.JTextPane description;
    private javax.swing.JLabel info;
    private javax.swing.JLabel issuename;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField newissue;
    private javax.swing.JLabel typename;
    private javax.swing.JLabel value11;
    // End of variables declaration//GEN-END:variables
}
