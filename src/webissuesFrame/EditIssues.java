package webissuesFrame;

import org.jdatepicker.JDatePicker;
import dbConnection.DbConnection;
import java.awt.Component;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.DatePicker;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import static javax.xml.bind.DatatypeConverter.parseDate;
import org.jdesktop.swingx.JXDatePicker;

public class EditIssues extends javax.swing.JFrame {

    private String[] columnNames;
    private Object[] rowData;
    private JLabel[] labels;
    private JTextField[] textFields;
    private JComboBox[] combobox;

    public EditIssues() {
        initComponents();
        this.setLocationRelativeTo(null);
        Image icon = new ImageIcon(this.getClass().getResource("/webissueslogo.png")).getImage();
        this.setIconImage(icon);
        this.setTitle("Edit Attributes");
        labels = new JLabel[0];
        textFields = new JTextField[0];
        combobox = new JComboBox[0];
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
    String locationValue = "";
    int issueId = 0;

    public void setRowData(Object[] rowData, String[] columnNames) {
        int numFields = columnNames.length;
        //  System.out.println("numfieldss " + numFields);
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;

        jPanel3.removeAll();
        int x = 30;
        int tx = 75;
        int y = 10;
        int labelWidth = 100;
        int componentWidth = 500;
        int height = 28;
        int spacing = 7;
        boolean isIssueNameSet = false;

        for (int i = 0; i < numFields; i++) {
            if (columnNames[i].equalsIgnoreCase("LOCATION") && rowData[i] != null) {
                String locationString = rowData[i].toString();
                int lastDashIndex = locationString.lastIndexOf("-");
                if (lastDashIndex != -1 && lastDashIndex < locationString.length() - 1) {
                    locationValue = locationString.substring(lastDashIndex + 1).trim();
                }
            }
            if (columnNames[i].equalsIgnoreCase("ISSUE_NAME")) {
                if (!isIssueNameSet) {
                    nametext.setText(rowData[i] != null ? rowData[i].toString() : "");
                    typename.setText(rowData[i] != null ? rowData[i].toString() : "");
                    isIssueNameSet = true;
                    continue;
                }
            }
            if (columnNames[i].equalsIgnoreCase("ID")) {
                issueId = rowData[i] != null ? Integer.parseInt(rowData[i].toString()) : 0;
            }
        }

        try {
            con = DbConnection.getConnection();
            statement = con.createStatement();
            if (locationValue != null) {
                resultSet = statement.executeQuery("SELECT type_id FROM folders WHERE folder_name = '" + locationValue + "'");
                if (resultSet.next()) {
                    int typeId = resultSet.getInt("type_id");
                    resultSet = statement.executeQuery("SELECT attr_name, attr_def FROM attr_types WHERE type_id = " + typeId);
                    while (resultSet.next()) {
                        String attrName = resultSet.getString("attr_name");
                        String attrDef = resultSet.getString("attr_def");

                        int i;
                        boolean matchFound = false;
                        for (i = 0; i < numFields; i++) {
                            if (columnNames[i].equalsIgnoreCase(attrName)) {
                                matchFound = true;
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
                                    System.out.println("row data " + rowData[i] + " component ");
                                    if (rowData[i] != null) {
                                        String selectedValue = rowData[i].toString();
                                        comboBox.setSelectedItem(selectedValue);
                                    } else {
                                        comboBox.setSelectedItem(null);
                                    }

                                    jPanel3.add(comboBox);
                                }

                                if (attrDef.startsWith("NUMERIC")) {
                                    DefaultComboBoxModel<Integer> comboBoxModel = new DefaultComboBoxModel<>();
                                    JComboBox<Integer> comboBox = new JComboBox<>(comboBoxModel);
                                    comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                    int minValue = 0;
                                    int maxValue = 0;

                                    String minStr = attrDef.substring(attrDef.indexOf("min-value=\"") + 11, attrDef.indexOf("\"", attrDef.indexOf("min-value=\"") + 11));
                                    String maxStr = attrDef.substring(attrDef.indexOf("max-value=\"") + 11, attrDef.indexOf("\"", attrDef.indexOf("max-value=\"") + 11));
                                    System.out.println("row data " + rowData[i]);
                                    try {
                                        minValue = Integer.parseInt(minStr);
                                        maxValue = Integer.parseInt(maxStr);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }

                                    for (int value = minValue; value <= maxValue; value++) {
                                        comboBoxModel.addElement(value);
                                    }

                                    String selectedValue = rowData[i] != null ? rowData[i].toString() : null;
                                    if (selectedValue != null) {
                                        try {
                                            Integer intValue = Integer.parseInt(selectedValue);
                                            comboBox.setSelectedItem(intValue);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    jPanel3.add(comboBox);
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
                                    System.out.println("row data " + rowData[i]);
                                    if (rowData[i] != null) {
                                        String selectedValue = rowData[i] != null ? rowData[i].toString() : null;
                                        if (selectedValue != null) {
                                            comboBox.setSelectedItem(selectedValue);
                                        }
                                    } else {
                                        comboBox.setSelectedItem(null);
                                    }

                                    jPanel3.add(comboBox);
                                    attrResultSet.close();
                                    attrStatement.close();
                                } else if (attrDef.startsWith("TEXT")) {
                                    JTextField textField = new JTextField(rowData[i] != null ? rowData[i].toString() : "");
                                    textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                    jPanel3.add(textField);
                                }

                                JLabel label = new JLabel(attrName);
                                label.setBounds(x, y, labelWidth, height);
                                jPanel3.add(label);

                                y += height + spacing;
                                break;
                            }
                        }

                        if (!matchFound) {
                            if (attrDef.startsWith("DATETIME")) {
                                int atr_id = 0;
                                Connection conn = null;
                                PreparedStatement stmt = null;
                                ResultSet rst = null;
                                try {
                                    conn = DbConnection.getConnection();
                                    stmt = conn.prepareStatement("SELECT attr_id FROM attr_types WHERE attr_name = ?");
                                    stmt.setString(1, attrName);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        atr_id = rst.getInt("attr_id");
                                    }
                                    rst.close();
                                    stmt.close();
                                    stmt = conn.prepareStatement("SELECT attr_value FROM attr_values WHERE attr_id = ? AND issue_id = ?");
                                    stmt.setInt(1, atr_id);
                                    stmt.setInt(2, issueId);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        String attr_value = rst.getString("attr_value");

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date = dateFormat.parse(attr_value);

                                        datetime.setDate(date);
                                        datetime.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                        jPanel3.add(datetime);
                                    } else {
                                        datetime.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                        jPanel3.add(datetime);
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else if (attrDef.equalsIgnoreCase("NUMERIC min-value=\"0\" max-value=\"100\"")) {
                                int atr_id = 0;
                                String attr_value = null;
                                Connection conn = null;
                                PreparedStatement stmt = null;
                                ResultSet rst = null;
                                try {
                                    conn = DbConnection.getConnection();
                                    stmt = conn.prepareStatement("SELECT attr_id FROM attr_types WHERE attr_name = ?");
                                    stmt.setString(1, attrName);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        atr_id = rst.getInt("attr_id");
                                    }
                                    rst.close();
                                    stmt.close();
                                    stmt = conn.prepareStatement("SELECT attr_value FROM attr_values WHERE attr_id = ? AND issue_id = ?");
                                    stmt.setInt(1, atr_id);
                                    stmt.setInt(2, issueId);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        attr_value = rst.getString("attr_value");
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();

                                }
                                JTextField textField = new JTextField(attr_value);
                                textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                jPanel3.add(textField);
                            } else if (attrDef.startsWith("NUMERIC")) {
                                int atr_id = 0;
                                String attr_value = null;
                                Connection conn = null;
                                PreparedStatement stmt = null;
                                ResultSet rst = null;
                                try {
                                    conn = DbConnection.getConnection();
                                    stmt = conn.prepareStatement("SELECT attr_id FROM attr_types WHERE attr_name = ?");
                                    stmt.setString(1, attrName);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        atr_id = rst.getInt("attr_id");
                                    }
                                    rst.close();
                                    stmt.close();
                                    stmt = conn.prepareStatement("SELECT attr_value FROM attr_values WHERE attr_id = ? AND issue_id = ?");
                                    stmt.setInt(1, atr_id);
                                    stmt.setInt(2, issueId);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        attr_value = rst.getString("attr_value");
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();

                                }

                                JTextField textField = new JTextField(attr_value);
                                textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                jPanel3.add(textField);
                            } else if (attrDef.startsWith("TEXT")) {
                                int atr_id = 0;
                                String attr_value = null;
                                Connection conn = null;
                                PreparedStatement stmt = null;
                                ResultSet rst = null;
                                try {
                                    conn = DbConnection.getConnection();
                                    stmt = conn.prepareStatement("SELECT attr_id FROM attr_types WHERE attr_name = ?");
                                    stmt.setString(1, attrName);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        atr_id = rst.getInt("attr_id");
                                    }
                                    rst.close();
                                    stmt.close();
                                    stmt = conn.prepareStatement("SELECT attr_value FROM attr_values WHERE attr_id = ? AND issue_id = ?");
                                    stmt.setInt(1, atr_id);
                                    stmt.setInt(2, issueId);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        attr_value = rst.getString("attr_value");
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();

                                }

                                JTextField textField = new JTextField(attr_value);
                                textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                jPanel3.add(textField);
                            } else if (attrDef.startsWith("ENUM") && attrName.equalsIgnoreCase("Reason") || attrName.equalsIgnoreCase("Status")) {
                                String enumItems = attrDef.substring(attrDef.indexOf("items={") + 7, attrDef.lastIndexOf("}"));
                                String[] values = enumItems.split(",");
                                DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
                                JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);
                                comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);

                                for (String value : values) {
                                    String trimmedValue = value.trim().replaceAll("\"", "");
                                    comboBoxModel.addElement(trimmedValue);
                                }

                                int atr_id = 0;
                                String attr_value = null;
                                Connection conn = null;
                                PreparedStatement stmt = null;
                                ResultSet rst = null;
                                try {
                                    conn = DbConnection.getConnection();
                                    stmt = conn.prepareStatement("SELECT attr_id FROM attr_types WHERE attr_name = ?");
                                    stmt.setString(1, attrName);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        atr_id = rst.getInt("attr_id");
                                    }
                                    rst.close();
                                    stmt.close();

                                    stmt = conn.prepareStatement("SELECT attr_value FROM attr_values WHERE attr_id = ? AND issue_id = ?");
                                    stmt.setInt(1, atr_id);
                                    stmt.setInt(2, issueId);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        attr_value = rst.getString("attr_value");
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if (attr_value != null && !attr_value.isEmpty()) {
                                    comboBox.setSelectedItem(attr_value);
                                } else {
                                    comboBox.setSelectedItem(null);
                                }

                                jPanel3.add(comboBox);
                            }
                            JLabel label = new JLabel(attrName);
                            label.setBounds(x, y, labelWidth, height);
                            jPanel3.add(label);

                            y += height + spacing;
                            i++;
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        jPanel3.revalidate();
        jPanel3.repaint();
    }

    private void handleOkButtonClick() {
        Component[] components = jPanel3.getComponents();
        Connection connection = null;
        Statement statement = null;
        System.out.println("location value in class level: " + locationValue);
        try {
            connection = DbConnection.getConnection();
            statement = connection.createStatement();

            // Get the type ID using the location value
            String getTypeQuery = "SELECT type_id FROM folders WHERE folder_name = '" + locationValue + "'";
            ResultSet typeResultSet = statement.executeQuery(getTypeQuery);
            int typeId = 0;
            if (typeResultSet.next()) {
                typeId = typeResultSet.getInt("type_id");
            }

            String changeIssueName = "UPDATE issues SET issue_name = ? WHERE issue_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(changeIssueName);
            preparedStatement.setString(1, nametext.getText());
            preparedStatement.setInt(2, issueId);
            preparedStatement.executeUpdate();

            int attrId = 0;
            System.out.println("attribute uid" + attrId);
            String getAttrIdQuery = "SELECT attr_id FROM attr_types WHERE type_id = " + typeId + " LIMIT 1";
            ResultSet attrIdResultSet = statement.executeQuery(getAttrIdQuery);
            if (attrIdResultSet.next()) {
                attrId = attrIdResultSet.getInt("attr_id");
            }
            for (Component component : components) {
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;
                    String labelName = label.getText();
                    System.out.println("Label: " + labelName);
                    String query = "SELECT attr_id FROM attr_types WHERE attr_name = '" + labelName + "' AND type_id = " + typeId;
                    ResultSet resultSet = statement.executeQuery(query);
                    if (resultSet.next()) {
                        attrId = resultSet.getInt("attr_id") + 1;
                        System.out.println("attr id: " + attrId);
                    }
                } else if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    String textFieldValue = textField.getText();
                    System.out.println("***********************************");
                    System.out.println("TextField Name: " + textField.getName());
                    System.out.println("TextField Value: " + textFieldValue);

                    // Get attr_def based on attrId
                    String attrDefQuery = "SELECT attr_def FROM attr_types WHERE attr_id = ?";
                    PreparedStatement attrDefStatement = connection.prepareStatement(attrDefQuery);
                    attrDefStatement.setInt(1, attrId);
                    ResultSet attrDefResult = attrDefStatement.executeQuery();
                    attrDefResult.next();
                    String attrDef = attrDefResult.getString("attr_def");

                    // Validate attr_def based on attribute type
                    if (attrDef.startsWith("NUMERIC")) {
                        double minValue = Double.NEGATIVE_INFINITY;
                        double maxValue = Double.POSITIVE_INFINITY;
                        boolean isRequired = false;
                        int decimalPlaces = 0;

                        String[] attrDefParts = attrDef.split(" ");
                        for (String attrDefPart : attrDefParts) {
                            if (attrDefPart.startsWith("min-value")) {
                                String minValueString = attrDefPart.split("=")[1].replace("\"", "");
                                minValue = Double.parseDouble(minValueString);
                            } else if (attrDefPart.startsWith("max-value")) {
                                String maxValueString = attrDefPart.split("=")[1].replace("\"", "");
                                maxValue = Double.parseDouble(maxValueString);
                            } else if (attrDefPart.startsWith("required")) {
                                isRequired = true;
                            } else if (attrDefPart.startsWith("decimal")) {
                                String decimalString = attrDefPart.split("=")[1].replace("\"", "");
                                decimalPlaces = Integer.parseInt(decimalString);
                            }
                        }

                        if (isRequired && textFieldValue.isEmpty()) {
                            // Display error message using JOptionPane
                            JOptionPane.showMessageDialog(null, "Invalid input! Numeric value is required.", "Error", JOptionPane.ERROR_MESSAGE);
                            return; // Exit the method or handle the error case
                        }

                        if (!textFieldValue.matches("-?\\d+(\\.\\d+)?")) {
                            // Display error message using JOptionPane
                            JOptionPane.showMessageDialog(null, "Invalid input! Numeric value expected.", "Error", JOptionPane.ERROR_MESSAGE);
                            return; // Exit the method or handle the error case
                        }

                        double numericValue = Double.parseDouble(textFieldValue);
                        if (numericValue < minValue || numericValue > maxValue) {
                            // Display error message using JOptionPane
                            JOptionPane.showMessageDialog(null, "Invalid input! Numeric value should be between " + minValue + " and " + maxValue + ".", "Error", JOptionPane.ERROR_MESSAGE);
                            return; // Exit the method or handle the error case
                        }

                        // If decimal places are specified, check the number of decimal places
                        if (decimalPlaces > 0) {
                            String[] valueParts = textFieldValue.split("\\.");
                            if (valueParts.length > 1 && valueParts[1].length() > decimalPlaces) {
                                // Display error message using JOptionPane
                                JOptionPane.showMessageDialog(null, "Invalid input! Numeric value should have a maximum of " + decimalPlaces + " decimal places.", "Error", JOptionPane.ERROR_MESSAGE);
                                return; // Exit the method or handle the error case
                            }
                        }
                    } else if (attrDef.startsWith("TEXT")) {
                        if (!textFieldValue.matches("[A-Za-z ]+")) {
                            // Display error message using JOptionPane
                            JOptionPane.showMessageDialog(null, "Invalid input! Text value expected.", "Error", JOptionPane.ERROR_MESSAGE);
                            return; // Exit the method or handle the error case
                        }
                    }
                    // Check if attrId and issueId exist in the table
                    String checkQuery = "SELECT COUNT(*) AS count FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                    checkStatement.setInt(1, attrId);
                    checkStatement.setInt(2, issueId);
                    ResultSet checkResult = checkStatement.executeQuery();
                    checkResult.next();
                    int rowCount = checkResult.getInt("count");

                    if (rowCount > 0) {
                        // Update the existing row
                        String updateQuery = "UPDATE attr_values SET attr_value = ? WHERE attr_id = ? AND issue_id = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setString(1, textFieldValue);
                        updateStatement.setInt(2, attrId);
                        updateStatement.setInt(3, issueId);
                        updateStatement.executeUpdate();
                        System.out.println("Updated value for TextField attr_id " + attrId + ": " + textFieldValue);
                    } else {
                        // Insert a new row
                        String insertQuery = "INSERT INTO attr_values (attr_id, issue_id, attr_value) VALUES (?, ?, ?)";
                        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                        insertStatement.setInt(1, attrId);
                        insertStatement.setInt(2, issueId);
                        insertStatement.setString(3, textFieldValue);
                        insertStatement.executeUpdate();
                        System.out.println("Inserted value for TextField attr_id " + attrId + ": " + textFieldValue);
                    }
                } else if (component instanceof JComboBox) {
                    JComboBox<?> comboBox = (JComboBox<?>) component;
                    Object comboBoxValue = comboBox.getSelectedItem();
                    System.out.println("***********************************");
                    System.out.println("ComboBox Name: " + comboBox.getName());
                    System.out.println("ComboBox Value: " + comboBoxValue);

                    // Check if attrId and issueId exist in the table
                    String checkQuery = "SELECT COUNT(*) AS count FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                    checkStatement.setInt(1, attrId);
                    checkStatement.setInt(2, issueId);
                    ResultSet checkResult = checkStatement.executeQuery();
                    checkResult.next();
                    int rowCount = checkResult.getInt("count");

                    if (rowCount > 0) {
                        // Update the existing row
                        String updateQuery = "UPDATE attr_values SET attr_value = ? WHERE attr_id = ? AND issue_id = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setString(1, comboBoxValue.toString());
                        updateStatement.setInt(2, attrId);
                        updateStatement.setInt(3, issueId);
                        updateStatement.executeUpdate();
                        System.out.println("Updated value for ComboBox attr_id " + attrId + ": " + comboBoxValue);
                    } else {
                        // Insert a new row
                        String insertQuery = "INSERT INTO attr_values (attr_id, issue_id, attr_value) VALUES (?, ?, ?)";
                        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                        insertStatement.setInt(1, attrId);
                        insertStatement.setInt(2, issueId);
                        insertStatement.setString(3, comboBoxValue.toString());
                        insertStatement.executeUpdate();
                        System.out.println("Inserted value for attr_id " + attrId + ": " + comboBoxValue);
                    }
                } else if (component instanceof JXDatePicker) {
                    JXDatePicker datePicker = (JXDatePicker) component;
                    Date date = datePicker.getDate();
                    System.out.println("***********************************");
                    System.out.println("DatePicker Name: " + datePicker.getName());
                    System.out.println("DatePicker Value: " + date);

                    // Check if attrId and issueId exist in the table
                    String checkQuery = "SELECT COUNT(*) AS count FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                    checkStatement.setInt(1, attrId);
                    checkStatement.setInt(2, issueId);
                    ResultSet checkResult = checkStatement.executeQuery();
                    checkResult.next();
                    int rowCount = checkResult.getInt("count");

                    if (rowCount > 0) {
                        // Update the existing row
                        String updateQuery = "UPDATE attr_values SET attr_value = ? WHERE attr_id = ? AND issue_id = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setDate(1, new java.sql.Date(date.getTime()));
                        updateStatement.setInt(2, attrId);
                        updateStatement.setInt(3, issueId);
                        updateStatement.executeUpdate();
                        System.out.println("Updated value for attr_id " + attrId + ": " + date);
                    } else {
                        // Insert a new row
                        String insertQuery = "INSERT INTO attr_values (attr_id, issue_id, attr_value) VALUES (?, ?, ?)";
                        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                        insertStatement.setInt(1, attrId);
                        insertStatement.setInt(2, issueId);
                        insertStatement.setDate(3, new java.sql.Date(date.getTime()));
                        insertStatement.executeUpdate();
                        System.out.println("Inserted value for attr_id " + attrId + ": " + date);
                    }
                }
            }

            System.out.println("Database updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        clmname = new javax.swing.JLabel();
        value = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        datetime = new org.jdesktop.swingx.JXDatePicker();
        info = new javax.swing.JLabel();
        ok = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        issuename = new javax.swing.JLabel();
        nametext = new javax.swing.JTextField();
        typename = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        name.setText("Edit Attributes of issue ");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(726, 301));

        clmname.setText("Name");
        clmname.setAlignmentX(20.0F);

        value.setText("Value");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(240, 240, 240)
                    .addComponent(datetime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(240, 240, 240)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(111, 111, 111)
                    .addComponent(datetime, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(112, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(34, 34, 34)
                            .addComponent(clmname, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(91, 91, 91)
                            .addComponent(value, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clmname, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(value, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Attributes", jPanel2);

        info.setIcon(new javax.swing.ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\info.png")); // NOI18N
        info.setText("please enter values.");

        ok.setText("OK");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        issuename.setText("Name:");

        nametext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nametextActionPerformed(evt);
            }
        });

        typename.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        typename.setText("jLabel1");
        typename.setPreferredSize(new java.awt.Dimension(34, 26));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jSeparator2)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(name)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(typename, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(info, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(issuename, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(issuename, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(info, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ok)
                    .addComponent(cancel))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nametextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nametextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nametextActionPerformed

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        handleOkButtonClick();
        dispose();
    }//GEN-LAST:event_okActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        dispose();
    }//GEN-LAST:event_cancelActionPerformed

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(EditIssues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditIssues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditIssues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditIssues.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditIssues().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JLabel clmname;
    private org.jdesktop.swingx.JXDatePicker datetime;
    private javax.swing.JLabel info;
    private javax.swing.JLabel issuename;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel name;
    private javax.swing.JTextField nametext;
    private javax.swing.JButton ok;
    private javax.swing.JLabel typename;
    private javax.swing.JLabel value;
    // End of variables declaration//GEN-END:variables
}
