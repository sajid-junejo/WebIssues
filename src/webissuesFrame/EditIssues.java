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
import java.util.Objects;
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
    Map<Integer, Object> attrIdToSelectedItemMap = new HashMap<>();
    Map<String, String> attrValues = new HashMap<>();
    Map<Integer, Object> attrIdToOldValueMap = new HashMap<>();

    public void setRowData(Object[] rowData, String[] columnNames) {
        int numFields = columnNames.length;
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
        System.out.println("Old value " + nametext.getText());

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
                    resultSet = statement.executeQuery("SELECT attr_id, attr_name, attr_def FROM attr_types WHERE type_id = " + typeId);
                    while (resultSet.next()) {
                        String attrName = resultSet.getString("attr_name");
                        String attrDef = resultSet.getString("attr_def");
                        int attrId = resultSet.getInt("attr_id");

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
                                    System.out.println(attrId+" line number 138");
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
//                                    Object selectedItem = comboBox.getSelectedItem();
//                                    attrIdToSelectedItemMap.put(attrId, selectedItem);
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
                                    System.out.println(attrId+" line number 175");
                                    comboBoxModel.addElement(null);
                                    //System.out.println("row data " + rowData[i]);
                                    if (rowData[i] != null) {
                                        String selectedValue = rowData[i] != null ? rowData[i].toString() : null;
                                        if (selectedValue != null) {
                                            comboBox.setSelectedItem(selectedValue);
                                        }
                                    } else {
                                        comboBox.setSelectedItem(null);
                                    }
//                                    Object selectedItem = comboBox.getSelectedItem();
//                                    attrIdToSelectedItemMap.put(attrId, selectedItem);
                                    jPanel3.add(comboBox);
                                    attrResultSet.close();
                                    attrStatement.close();
                                } else if (attrDef.startsWith("TEXT")) {
                                    JTextField textField = new JTextField(rowData[i] != null ? rowData[i].toString() : "");
                                    textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                    jPanel3.add(textField);
                                    System.out.println(attrId+" line number 195");
                                }

                                JLabel label = new JLabel(attrName);
                                label.setBounds(x, y, labelWidth, height);
                                jPanel3.add(label);

                                y += height + spacing;
                                break;
                            }
                        }

                        if (!matchFound) {
                             if (attrDef.startsWith("USER")) {
                                PreparedStatement stmt = null;
                                Connection conn = null;
                                ResultSet rst = null;
                                System.out.println(attrId + " attr id in the match not found user");
                                DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
                                JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);
                                comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                Statement attrStatement = con.createStatement();
                                ResultSet attrResultSet = attrStatement.executeQuery("SELECT user_name FROM users");
                                while (attrResultSet.next()) {
                                    String userName = attrResultSet.getString("user_name");
                                    comboBoxModel.addElement(userName);
                                }
                                conn = DbConnection.getConnection();
                                String query = "SELECT attr_value FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                                stmt = conn.prepareStatement(query);
                                stmt.setInt(1, attrId);
                                stmt.setInt(2, issueId);

                                rst = stmt.executeQuery();
                                if (rst.next()) {

                                    String selectedValue = rst.getString("attr_value");
                                    System.out.println(selectedValue);
                                    comboBox.setSelectedItem(selectedValue);
                                } else {
                                    comboBox.setSelectedItem(null);
                                }
                                jPanel3.add(comboBox);
                            }
                            else if (attrDef.startsWith("DATETIME")) {
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
                                    System.out.println(attrId+" line number 285");
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
//                                    Object selectedItem = datetime.getDate();
//                                    attrIdToSelectedItemMap.put(attrId, selectedItem);
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
                                System.out.println(attrId+" line number 318");
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
//                                Object selectedItem = textField.getText();
//                                attrIdToSelectedItemMap.put(attrId, selectedItem);
                            } else if (attrDef.startsWith("NUMERIC")) {
                                int atr_id = 0;
                                String attr_value = null;
                                Connection conn = null;
                                PreparedStatement stmt = null;
                                ResultSet rst = null;
                                System.out.println(attrId+" line number 351");
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
                                System.out.println(attrId+" line number 383");
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
                            } else if (attrDef.startsWith("ENUM")) {
                                if( attrName.equalsIgnoreCase("Reason") || attrName.equalsIgnoreCase("Status") || attrName.equalsIgnoreCase("Types")){
                                String enumItems = attrDef.substring(attrDef.indexOf("items={") + 7, attrDef.lastIndexOf("}"));
                                String[] values = enumItems.split(",");
                                DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
                                JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);
                                comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                System.out.println(attrId +" line number 417");
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
                                    System.out.println("attr name line 429"+attrName);
                                    rst = stmt.executeQuery();
                                    if (rst.next()) {
                                        atr_id = rst.getInt("attr_id");
                                    }
                                    rst.close();
                                    stmt.close();
                                    System.out.println(atr_id+" line number 438 "+attrId+" on same line");
                                    stmt = conn.prepareStatement("SELECT attr_value FROM attr_values WHERE attr_id = ? AND issue_id = ?");
                                    stmt.setInt(1, attrId);
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
//                                Object selectedItem = comboBox.getSelectedItem();
//                                attrIdToSelectedItemMap.put(attrId, selectedItem);
                                jPanel3.add(comboBox);
                            }
                            }
                            else {
                                 System.out.println("else block in types attrname");
                                if(attrName.equalsIgnoreCase("Types"))
                                {
                                    if (attrDef.startsWith("ENUM")) {
                                String enumItems = attrDef.substring(attrDef.indexOf("items={") + 7, attrDef.lastIndexOf("}"));
                                String[] values = enumItems.split(",");
                                DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
                                JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);
                                comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                System.out.println("ENUM");
                                System.out.println(attrName);
                                System.out.println(attrId + " attr id in the match not found enum");

                                for (String value : values) {
                                    String trimmedValue = value.trim().replaceAll("\"", "");
                                    comboBoxModel.addElement(trimmedValue);
                                }

                                String query = "SELECT attr_value FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                                PreparedStatement state = con.prepareStatement(query);

                                state.setInt(1, attrId);
                                state.setInt(2, issueId);

                                resultSet = state.executeQuery();

                                if (resultSet.next()) {
                                    String selectedValue = resultSet.getString("attr_value");
                                    comboBox.setSelectedItem(selectedValue);
                                } else {
                                    comboBox.setSelectedItem(null);
                                }

                                jPanel3.add(comboBox);
                            }
                                }
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
        int stampId = 0;
        int attr_change_id = 0;
        String valueNew = "";
        String valueOld = "";
        try {
            connection = DbConnection.getConnection();
            statement = connection.createStatement();
            String getTypeQuery = "SELECT type_id FROM folders WHERE folder_name = '" + locationValue + "'";
            ResultSet typeResultSet = statement.executeQuery(getTypeQuery);
            int typeId = 0;
            if (typeResultSet.next()) {
                typeId = typeResultSet.getInt("type_id");
            }

            String valueQuery = "SELECT attr_id, value_new, value_old FROM changes WHERE issue_id = ? AND attr_id IS NULL ORDER BY change_id DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(valueQuery);
            preparedStatement.setInt(1, issueId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                attr_change_id = resultSet.getInt("attr_id");
                valueNew = resultSet.getString("value_new");
                valueOld = resultSet.getString("value_old");
            } else {
                valueNew = null;
                valueOld = null;
            }
            System.out.println("value old " + valueOld + " value new " + valueNew);
            System.out.println("attttttrrrrribute id" + attr_change_id);
            String getSessionQuery = "SELECT user_id FROM sessions";
            PreparedStatement getSessionStatement = connection.prepareStatement(getSessionQuery);
            ResultSet sessionResultSet = getSessionStatement.executeQuery();
            int userId = 0;
            if (sessionResultSet.next()) {
                userId = sessionResultSet.getInt("user_id");
                System.out.println("user session id " + userId);
            }
//            String insertStampsQuery = "INSERT INTO stamps (user_id, stamp_time) VALUES (?, ?)";
//            PreparedStatement insertStampsStatement = connection.prepareStatement(insertStampsQuery, Statement.RETURN_GENERATED_KEYS);
//            insertStampsStatement.setInt(1, userId);
//            insertStampsStatement.setInt(2, (int) (System.currentTimeMillis() / 1000));
//            insertStampsStatement.executeUpdate();
//            resultSet = insertStampsStatement.getGeneratedKeys();
            int StampsIssueId = 0;
//            if (resultSet.next()) {
//                StampsIssueId = resultSet.getInt(1);
//                System.out.println("Issue ID " + StampsIssueId);
//            }
//            System.out.println("Inserted into stamps table. issueId: " + StampsIssueId);
//
//            String changeIssueName = "UPDATE issues SET issue_name = ?, stamp_id = ? WHERE issue_id = ?";
//            preparedStatement = connection.prepareStatement(changeIssueName);

            String nameTextValue = nametext.getText();
            System.out.println("New value " + nameTextValue);
            if (nameTextValue == null || nameTextValue.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Issue name cannot be null or empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                if (valueNew == null) {
                    valueNew = ""; // Treat null valueNew as an empty string
                }
                if (!valueNew.equals(nameTextValue)) {
                    String insertStampsQuery = "INSERT INTO stamps (user_id, stamp_time) VALUES (?, ?)";
                    PreparedStatement insertStampsStatement = connection.prepareStatement(insertStampsQuery, Statement.RETURN_GENERATED_KEYS);
                    insertStampsStatement.setInt(1, userId);
                    insertStampsStatement.setInt(2, (int) (System.currentTimeMillis() / 1000));
                    insertStampsStatement.executeUpdate();
                    resultSet = insertStampsStatement.getGeneratedKeys();
                    if (resultSet.next()) {
                        StampsIssueId = resultSet.getInt(1);
                        System.out.println("Issue ID " + StampsIssueId);
                    }
                    System.out.println("Inserted into stamps table. issueId: " + StampsIssueId);

                    String changeIssueName = "UPDATE issues SET issue_name = ?, stamp_id = ? WHERE issue_id = ?";
                    preparedStatement = connection.prepareStatement(changeIssueName);
                    System.out.println("value new -> " + valueNew + "value input " + nameTextValue);
                    preparedStatement.setString(1, nameTextValue);
                    preparedStatement.setInt(2, StampsIssueId);
                    preparedStatement.setInt(3, issueId);
                    preparedStatement.executeUpdate();
                    String updateName = "INSERT INTO changes (change_id, issue_id, change_type, stamp_id, value_old, value_new) VALUES(?,?,?,?,?,?)";
                    PreparedStatement updateIssueStatement = connection.prepareStatement(updateName);
                    updateIssueStatement.setInt(1, StampsIssueId);
                    updateIssueStatement.setInt(2, issueId);
                    if (valueNew == null || valueNew.isEmpty()) {
                        updateIssueStatement.setInt(3, 0);
                    } else {
                        updateIssueStatement.setInt(3, 1);
                    }
                    updateIssueStatement.setInt(4, StampsIssueId);
                    updateIssueStatement.setString(5, valueNew);
                    updateIssueStatement.setString(6, nameTextValue);
                    updateIssueStatement.executeUpdate();
                }
            }
            int attrId = 0;
            String getAttrIdQuery = "SELECT attr_id FROM attr_types WHERE type_id = " + typeId + " LIMIT 1";
            ResultSet attrIdResultSet = statement.executeQuery(getAttrIdQuery);
            if (attrIdResultSet.next()) {
                attrId = attrIdResultSet.getInt("attr_id");
            }
            for (Component component : components) {
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;
                    String labelName = label.getText();
                    String query = "SELECT attr_id FROM attr_types WHERE attr_name = '" + labelName + "' AND type_id = " + typeId;
                    resultSet = statement.executeQuery(query);
                    if (resultSet.next()) {
                        attrId = resultSet.getInt("attr_id") + 1;
                    }
                } else if (component instanceof JTextField) {
                    JTextField textField = (JTextField) component;
                    String textFieldValue = textField.getText();
                    String attrDefQuery = "SELECT attr_def FROM attr_types WHERE attr_id = ?";
                    PreparedStatement attrDefStatement = connection.prepareStatement(attrDefQuery);
                    attrDefStatement.setInt(1, attrId);
                    ResultSet attrDefResult = attrDefStatement.executeQuery();
                    attrDefResult.next();
                    String attrDef = attrDefResult.getString("attr_def");
                    if (attrDef.startsWith("NUMERIC")) {
                        double minValue = Double.NEGATIVE_INFINITY;
                        double maxValue = Double.POSITIVE_INFINITY;
                        boolean isRequired = false;
                        int decimalPlaces = 0;
                        System.out.println("attr id " + attrId+" line number 450");
                        String[] attrDefParts = attrDef.split(" ");
                        for (String attrDefPart : attrDefParts) {
                            if (attrDefPart.contains("required")) {
                                isRequired = true;
                            } else if (attrDefPart.startsWith("min-value")) {
                                String minValueString = attrDefPart.split("=")[1].replace("\"", "");
                                minValue = Double.parseDouble(minValueString);
                            } else if (attrDefPart.startsWith("max-value")) {
                                String maxValueString = attrDefPart.split("=")[1].replace("\"", "");
                                maxValue = Double.parseDouble(maxValueString);
                            } else if (attrDefPart.startsWith("decimal")) {
                                String decimalString = attrDefPart.split("=")[1].replace("\"", "");
                                decimalPlaces = Integer.parseInt(decimalString);
                            }
                        }

                        if (isRequired && textFieldValue.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Invalid input! Numeric value is required.", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                        if (textFieldValue != null && !textFieldValue.isEmpty() && !textFieldValue.matches("-?\\d+(\\.\\d+)?")) {
                            JOptionPane.showMessageDialog(null, "Invalid input! Numeric value expected.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        if (textFieldValue != null && !textFieldValue.isEmpty()) {
                            double numericValue = Double.parseDouble(textFieldValue);
                            if (numericValue < minValue || numericValue > maxValue) {
                                JOptionPane.showMessageDialog(null, "Invalid input! Numeric value should be between " + minValue + " and " + maxValue + ".", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        if (decimalPlaces > 0) {
                            String[] valueParts = textFieldValue.split("\\.");
                            if (valueParts.length > 1 && valueParts[1].length() > decimalPlaces) {
                                JOptionPane.showMessageDialog(null, "Invalid input! Numeric value should have a maximum of " + decimalPlaces + " decimal places.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else if (attrDef.startsWith("TEXT")) {
                        if (textFieldValue != null && !textFieldValue.isEmpty() && !textFieldValue.matches("[A-Za-z ]+")) {
                            JOptionPane.showMessageDialog(null, "Invalid input! Text value expected.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    String checkQuery = "SELECT COUNT(*) AS count FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                    checkStatement.setInt(1, attrId);
                    checkStatement.setInt(2, issueId);
                    ResultSet checkResult = checkStatement.executeQuery();
                    checkResult.next();
                    int rowCount = checkResult.getInt("count");
                    //System.out.println("attribute id" + attrDef);
                    if (rowCount > 0) {
                        if (textFieldValue != null && !textFieldValue.isEmpty()) {
                            String updateQuery = "UPDATE attr_values SET attr_value = ? WHERE attr_id = ? AND issue_id = ?";
                            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.setString(1, textFieldValue);
                            updateStatement.setInt(2, attrId);
                            updateStatement.setInt(3, issueId);
                            updateStatement.executeUpdate();
                            Object selectedItem = textFieldValue;
                            attrIdToSelectedItemMap.put(attrId, selectedItem);
                            
                        } else {
                            String deleteQuery = "DELETE FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                            deleteStatement.setInt(1, attrId);
                            deleteStatement.setInt(2, issueId);
                            deleteStatement.executeUpdate();
                        }
                    } else {
                        if (attrId == 11 && textFieldValue == null) {
                            JOptionPane.showMessageDialog(null, "Invalid input! 'Name' value is required.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (textFieldValue != null) {
                                 
                                String insertQuery = "INSERT INTO attr_values (attr_id, issue_id, attr_value) VALUES (?, ?, ?)";
                                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                                insertStatement.setInt(1, attrId);
                                insertStatement.setInt(2, issueId);
                                insertStatement.setString(3, textFieldValue);
                                insertStatement.executeUpdate();
                                Object selectedItem = textFieldValue;
                                attrIdToSelectedItemMap.put(attrId, selectedItem);
                                
                            } else {

                            }
                        }
                    }
                } else if (component instanceof JComboBox) {
                    JComboBox<?> comboBox = (JComboBox<?>) component;
                    Object comboBoxValue = comboBox.getSelectedItem();
                    System.out.println("attr id " + attrId);
                    String checkQuery = "SELECT COUNT(*) AS count FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                    checkStatement.setInt(1, attrId);
                    checkStatement.setInt(2, issueId);
                    ResultSet checkResult = checkStatement.executeQuery();
                    checkResult.next();
                    int rowCount = checkResult.getInt("count");

                    if (rowCount > 0) {
                        if (comboBoxValue != null) {
                            String updateQuery = "UPDATE attr_values SET attr_value = ? WHERE attr_id = ? AND issue_id = ?";
                            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.setString(1, comboBoxValue.toString());
                            updateStatement.setInt(2, attrId);
                            updateStatement.setInt(3, issueId);
                            updateStatement.executeUpdate();
                            Object selectedItem = comboBoxValue.toString();
                            attrIdToSelectedItemMap.put(attrId, selectedItem);

                        } else {
                            String deleteQuery = "DELETE FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                            deleteStatement.setInt(1, attrId);
                            deleteStatement.setInt(2, issueId);
                            deleteStatement.executeUpdate();
                        }
                    } else {
                        if (comboBoxValue != null) {
                            String insertQuery = "INSERT INTO attr_values (attr_id, issue_id, attr_value) VALUES (?, ?, ?)";
                            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                            insertStatement.setInt(1, attrId);
                            insertStatement.setInt(2, issueId);
                            insertStatement.setString(3, comboBoxValue.toString());
                            insertStatement.executeUpdate();
                            Object selectedItem = comboBoxValue.toString();
                            attrIdToSelectedItemMap.put(attrId, selectedItem);
                        } else {

                        }
                    }
                } else if (component instanceof JXDatePicker) {
                    JXDatePicker datePicker = (JXDatePicker) component;
                    Date date = datePicker.getDate();
                    System.out.println("attr id " + attrId);

                    // Check if attrId and issueId exist in the table
                    String checkQuery = "SELECT COUNT(*) AS count FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                    checkStatement.setInt(1, attrId);
                    checkStatement.setInt(2, issueId);
                    ResultSet checkResult = checkStatement.executeQuery();
                    checkResult.next();
                    int rowCount = checkResult.getInt("count");

                    if (rowCount > 0) {
                        if (date != null) {
                            String updateQuery = "UPDATE attr_values SET attr_value = ? WHERE attr_id = ? AND issue_id = ?";
                            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.setDate(1, new java.sql.Date(date.getTime()));
                            updateStatement.setInt(2, attrId);
                            updateStatement.setInt(3, issueId);
                            updateStatement.executeUpdate();
                            Object selectedItem = new java.sql.Date(date.getTime());
                            attrIdToSelectedItemMap.put(attrId, selectedItem);
                        } else {
                            // Remove the existing row
                            String deleteQuery = "DELETE FROM attr_values WHERE attr_id = ? AND issue_id = ?";
                            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                            deleteStatement.setInt(1, attrId);
                            deleteStatement.setInt(2, issueId);
                            deleteStatement.executeUpdate();
                        }
                    } else {
                        if (date != null) {
                            String insertQuery = "INSERT INTO attr_values (attr_id, issue_id, attr_value) VALUES (?, ?, ?)";
                            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                            insertStatement.setInt(1, attrId);
                            insertStatement.setInt(2, issueId);
                            insertStatement.setDate(3, new java.sql.Date(date.getTime()));
                            insertStatement.executeUpdate();
                            Object selectedItem = new java.sql.Date(date.getTime());
                            attrIdToSelectedItemMap.put(attrId, selectedItem);
                            System.out.println("Inserted value for attr_id " + attrId + ": " + date);
                        }
                    }
                }
            }

            String fetchOldValuesQuery = "SELECT c.attr_id, c.value_new "
                    + "FROM changes c "
                    + "JOIN ("
                    + "    SELECT attr_id, MAX(stamp_id) AS max_stamp_id "
                    + "    FROM changes "
                    + "    WHERE issue_id = ? "
                    + "    GROUP BY attr_id "
                    + ") AS max_changes "
                    + "ON c.attr_id = max_changes.attr_id AND c.stamp_id = max_changes.max_stamp_id "
                    + "WHERE c.issue_id = ?";
            PreparedStatement fetch = connection.prepareStatement(fetchOldValuesQuery);
            fetch.setInt(1, issueId);
            fetch.setInt(2, issueId);
            ResultSet oldValuesResult = fetch.executeQuery();

            while (oldValuesResult.next()) {
                int attributeId = oldValuesResult.getInt("attr_id");
                Object oldValue = oldValuesResult.getObject("value_new");
                attrIdToOldValueMap.put(attributeId, oldValue);
                System.out.println("key " + attributeId + "  value new " + oldValue);
            }

            System.out.println("Database updated successfully.");
            String newChangesQuery = "INSERT INTO changes (change_id, issue_id, change_type, stamp_id, attr_id, value_old, value_new) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement changeStatement = connection.prepareStatement(newChangesQuery);
            for (Map.Entry<Integer, Object> entry : attrIdToSelectedItemMap.entrySet()) {
                int attributeId = entry.getKey();
                Object selectedItem = entry.getValue();
                Object oldValue = attrIdToOldValueMap.get(attributeId);
                if (!Objects.equals(selectedItem, oldValue)) {
                    String newInsertStampsQuery = "INSERT INTO stamps (user_id, stamp_time) VALUES (?, ?)";
                    PreparedStatement newInsertStampsStatement = connection.prepareStatement(newInsertStampsQuery, Statement.RETURN_GENERATED_KEYS);
                    newInsertStampsStatement.setInt(1, userId);
                    newInsertStampsStatement.setInt(2, (int) (System.currentTimeMillis() / 1000));
                    newInsertStampsStatement.executeUpdate();

                    ResultSet stampResult = newInsertStampsStatement.getGeneratedKeys();
                    //int stampId = 0;
                    if (stampResult.next()) {
                        stampId = stampResult.getInt(1);
                    }
                    String updateIssuesStat = "UPDATE issues SET stamp_id = ? WHERE issue_id = ?";
                    PreparedStatement newUpdateIssue = connection.prepareStatement(updateIssuesStat);
                    newUpdateIssue.setInt(1, stampId);
                    newUpdateIssue.setInt(2, issueId);
                    newUpdateIssue.executeUpdate();
                    
                    changeStatement.setInt(1, stampId);
                    changeStatement.setInt(2, issueId);
                    changeStatement.setInt(3, 0);
                    changeStatement.setInt(4, stampId);
                    changeStatement.setInt(5, attributeId);
                    if (selectedItem instanceof Date) {
                        
                        java.sql.Date selectedDate = (java.sql.Date) selectedItem;
                        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
                        changeStatement.setString(6, (String) oldValue);
                        changeStatement.setString(7, formattedDate);
                    } else {
                        if (oldValue == null || oldValue.equals(null)) {
                            changeStatement.setString(6, null);
                        } else {
                            changeStatement.setString(6, oldValue.toString());
                        }
                        if (selectedItem.toString() == null) {
                            changeStatement.setString(7, null);
                        } else {
                            changeStatement.setString(7, selectedItem.toString());
                        }
                    }
                    changeStatement.executeUpdate();
                }
                System.out.println("updated stamps id" + stampId);
            }
            //System.out.println("stamps id Map for loop " + stampId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private void handleOK() {
//        attrValues = new HashMap<>();
//        for (Component component : jPanel3.getComponents()) {
//            if (component instanceof JComboBox) {
//                JComboBox<?> comboBox = (JComboBox<?>) component;
//                int labelIndex = jPanel3.getComponentZOrder(component) - 1;
//                if (labelIndex >= 0 && jPanel3.getComponent(labelIndex) instanceof JLabel) {
//                    JLabel label = (JLabel) jPanel3.getComponent(labelIndex);
//                    String attrName = label.getText();
//                    attrValues.put(attrName, comboBox.getSelectedItem().toString());
//                }
//            } else if (component instanceof JTextField) {
//                JTextField textField = (JTextField) component;
//                int labelIndex = jPanel3.getComponentZOrder(component) - 1; // Get the index of the label (one step back)
//                if (labelIndex >= 0 && jPanel3.getComponent(labelIndex) instanceof JLabel) {
//                    JLabel label = (JLabel) jPanel3.getComponent(labelIndex);
//                    String attrName = label.getText();
//                    attrValues.put(attrName, textField.getText());
//                }
//            } else if (component instanceof JXDatePicker) {
//                JXDatePicker datePicker = (JXDatePicker) component;
//                Date date = datePicker.getDate();
//                int labelIndex = jPanel3.getComponentZOrder(component) - 1; // Get the index of the label (one step back)
//                if (labelIndex >= 0 && jPanel3.getComponent(labelIndex) instanceof JLabel) {
//                    JLabel label = (JLabel) jPanel3.getComponent(labelIndex);
//                    String attrName = label.getText();
//                    if (datePicker.getDate() != null) {
//                        Object getdate = new java.sql.Date(date.getTime());
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        String dateString = dateFormat.format(getdate);
//                        attrValues.put(attrName, dateString);
//                    }
//                }
//            }
//        }
////        for (Map.Entry<String, String> entry : attrValues.entrySet()) {
////            String attrName = entry.getKey();
////            String attrValue = entry.getValue();
////            System.out.println(attrName + ": " + attrValue);
////        }
//    }
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
                    .addComponent(datetime, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
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
                            .addComponent(clmname, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                            .addGap(91, 91, 91)
                            .addComponent(value, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clmname, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(value, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(typename, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(info, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                        .addGap(392, 392, 392)
                        .addComponent(ok, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancel, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                        .addGap(27, 27, 27))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(issuename, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nametext, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(typename, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(issuename, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addComponent(nametext))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2)
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(info))
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
        //handleOK();
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
