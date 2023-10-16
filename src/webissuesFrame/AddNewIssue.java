package webissuesFrame;

import DAOImpl.GlobalDAOImpl;
import DAOImpl.IssuesDAOImpl;
import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.SessionManager;

public class AddNewIssue extends javax.swing.JFrame {

    public AddNewIssue() {
        initComponents();
        AddForm();
        typename.setText(HomeFrame.folderName);
        Image icon = new ImageIcon(this.getClass().getResource("/img/webissueslogo.png")).getImage();
        this.setIconImage(icon);
        this.setTitle("Add Issue");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setIconImage(icon);
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/img/information.png"));
        info.setIcon(icon1);
    }
    HomeFrame home = new HomeFrame();
    String locationValue = "";
    int issueId = 0;
    int folderId = 0;
    int typeId = 0;
    int attrId = 0;
    String attrDef = null;
    int userID = SessionManager.getInstance().getUserId();
    int userAccess = SessionManager.getInstance().getUserAccess();
    private Map<String, String> attrValues = new HashMap<>();
    IssuesDAOImpl issueDao = new IssuesDAOImpl();
    GlobalDAOImpl global = new GlobalDAOImpl();
    int id = HomeFrame.IssueID;
    Map<Integer, Object> attributeValues = issueDao.printAttributes(id);
    Map<Integer, Object> getAttributeValues = new HashMap<>();
    public static Map<Integer, Object> filteredValues = new HashMap<>();
    private Map<Component, Integer> componentIdMap = new HashMap<>();
    public static String name = null;
    public static String getDescription = null;

    public void AddForm() {
        System.out.println("Running ");
        int x = 1;
        int tx = 20;
        int y = 10;
        int labelWidth = 100;
        int componentWidth = 500;
        int height = 28;
        int spacing = 7;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String text = null;
        System.out.println("Add Issue Form");
        issueDao.printAttributes(id);
        try {
            URL url = new URL(LoginFrame.apiUrl);
            String api = url.getProtocol() + "://" + url.getHost() + "/";
            String apiUrl = api + "server/api/global.php";
            connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // Set headers
            connection.setRequestProperty("X-Csrf-Token", SessionManager.getInstance().getCsrfToken());
            connection.setRequestProperty("Cookie", SessionManager.getInstance().getCookie());
            connection.setRequestProperty("Content-Type", "application/json");
            String jsonInputString = "{}";
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.getOutputStream().write(jsonInputString.getBytes(StandardCharsets.UTF_8));
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    System.out.println("JSON response " + jsonResponse);
                    if (jsonResponse.has("result")) {
                        JSONObject result = jsonResponse.getJSONObject("result");
                        JSONArray typesArray = result.getJSONArray("types");
                        for (int i = 0; i < typesArray.length(); i++) {
                            JSONObject typeObject = typesArray.getJSONObject(i);
                            int currentProjectId = typeObject.getInt("id");
                            int typeId = HomeFrame.typeId;
                            if (currentProjectId == typeId) {
                                JSONArray attributesArray = typeObject.getJSONArray("attributes");
                                System.out.println("Attributes Array " + attributesArray);
                                for (int j = 0; j < attributesArray.length(); j++) {
                                    JSONObject attributeObject = attributesArray.getJSONObject(j);
                                    String attributeName = attributeObject.getString("name");
                                    String attributeType = attributeObject.getString("type");
                                    boolean required = false;
                                    if (attributeObject.has("required")) {
                                        required = true;
                                    }
                                    int attributeId = attributeObject.getInt("id");
                                    // Create a label for the attribute using the "name" from the API response
                                    if (required) {
                                        JLabel label = new JLabel("<html>" + attributeName + "<sup>*</sup>:</html>");
                                        label.setBounds(tx, y, labelWidth, height);
                                        jPanel5.add(label);
                                    } else {
                                        JLabel label = new JLabel(attributeName + ":");
                                        label.setBounds(tx, y, labelWidth, height);
                                        jPanel5.add(label);
                                    }

                                    if ("USER".equals(attributeType)) {
                                        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
                                        JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);
                                        comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                        y += height + spacing;
                                        componentIdMap.put(comboBox, attributeId);
                                        Integer projectId = HomeFrame.projectId;
                                        List<Integer> members = global.getMembersByProjectId(projectId);
                                        for (Integer memberId : members) {
                                            comboBoxModel.addElement(global.getUserName(memberId));
                                        }
                                        if (attributeObject.has("default")) {
                                            String defaultValue = attributeObject.getString("default");
                                            comboBox.setSelectedItem(defaultValue);
                                        } else {
                                            comboBox.setSelectedItem(null);
                                        }
                                        getAttributeValues.put(attributeId, comboBox.getSelectedItem());
                                        jPanel5.add(comboBox);
                                        System.out.println("Attribute Id " + attributeId);
                                    } else if ("ENUM".equals(attributeType)) {
                                        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
                                        JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);
                                        comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                        y += height + spacing;
                                        componentIdMap.put(comboBox, attributeId);
                                        if (attributeObject.has("items")) {
                                            JSONArray itemsArray = attributeObject.getJSONArray("items");
                                            for (int k = 0; k < itemsArray.length(); k++) {
                                                comboBoxModel.addElement(itemsArray.getString(k));
                                            }
                                        }
                                        if (attributeObject.has("default")) {
                                            String defaultValue = attributeObject.getString("default");
                                            comboBox.setSelectedItem(defaultValue);
                                        } else {
                                            comboBox.setSelectedItem(null);
                                        }
                                        System.out.println("Attribute Id " + attributeId);
                                        getAttributeValues.put(attributeId, comboBox.getSelectedItem());
                                        jPanel5.add(comboBox);
                                    } else if ("NUMERIC".equals(attributeType)) {
                                        double minValue = 0.0;
                                        if (attributeObject.has("min-value")) {
                                            try {
                                                String minValStr = attributeObject.getString("min-value");
                                                minValue = Double.parseDouble(minValStr);

                                                if (minValue == (int) minValue) {
                                                    int intValue = (int) minValue;
                                                    minValue = intValue;
                                                }
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        System.out.println("Attribute Id " + attributeId);

                                        if (attributeObject.has("max-value")) {
                                            double maxValue = 0.0; // Default value for maxValue if not specified

                                            try {
                                                // Parse the "max-value" as a double
                                                String maxValStr = attributeObject.getString("max-value");
                                                maxValue = Double.parseDouble(maxValStr);

                                                // Check if maxValue is actually an integer (e.g., 10.00)
                                                if (maxValue == (int) maxValue) {
                                                    // It's an integer, so convert it to an integer
                                                    int intValue = (int) maxValue;
                                                    maxValue = intValue;
                                                }
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }

                                            if (maxValue > 10) {
                                                JTextField textField = new JTextField();
                                                textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                                y += height + spacing;
                                                componentIdMap.put(textField, attributeId);

                                                if (required) {
                                                    textField.setInputVerifier(new InputVerifier() {
                                                        @Override
                                                        public boolean verify(JComponent input) {
                                                            JTextField textField = (JTextField) input;
                                                            String text = textField.getText();
                                                            if (text == null || text.isEmpty()) {
                                                                JOptionPane.showMessageDialog(null, "This field is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                                                                return false;
                                                            }
                                                            return true;
                                                        }
                                                    });
                                                }

                                                if (attributeObject.has("default")) {
                                                    String defaultValue = attributeObject.getString("default");
                                                    try {
                                                        // Try to parse the defaultValue as an integer
                                                        int intValue = Integer.parseInt(defaultValue);
                                                        textField.setText(Integer.toString(intValue)); // Store as an integer and set as text
                                                    } catch (NumberFormatException e) {
                                                        // Handle the case where defaultValue is not a valid integer
                                                        e.printStackTrace(); // Print an error message or handle as needed
                                                    }
                                                }
                                                if (attributeObject.has("decimal")) {
                                                    int decimalPlaces = attributeObject.getInt("decimal");

                                                    if (decimalPlaces > 0) {
                                                        String currentText = textField.getText();
                                                        int indexOfDecimal = currentText.indexOf(".");

                                                        // If there's no decimal point, append it along with the necessary number of zeros
                                                        if (indexOfDecimal == -1) {
                                                            currentText += ".";
                                                            for(int m = 0; m < decimalPlaces; m++) {
                                                                currentText += "0";
                                                            }
                                                        } else {
                                                            // If there's a decimal point, check how many decimal places are currently present
                                                            int currentDecimalCount = currentText.length() - indexOfDecimal - 1;

                                                            // If the current number of decimal places is less than required, append zeros
                                                            for (int n = currentDecimalCount; n < decimalPlaces; n++) {
                                                                currentText += "0";
                                                            }
                                                        }

                                                        // Set the modified text back to the textField
                                                        textField.setText(currentText);
                                                    }
                                                }

                                                getAttributeValues.put(attributeId, textField.getText());

                                                jPanel5.add(textField);
                                                System.out.println("Attribute with type NUMERIC (max-value > 10) found: " + attributeObject.toString());
                                            } else {
                                                DefaultComboBoxModel<Integer> comboBoxModel = new DefaultComboBoxModel<>();
                                                JComboBox<Integer> comboBox = new JComboBox<>(comboBoxModel);
                                                comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                                y += height + spacing;
                                                componentIdMap.put(comboBox, attributeId);

                                                for (int num = (int) minValue; num <= (int) maxValue; num++) {
                                                    comboBoxModel.addElement(num);
                                                }

                                                if (attributeObject.has("default")) {
                                                    String defaultValueStr = attributeObject.getString("default");
                                                    Integer defaultValue = Integer.parseInt(defaultValueStr); // Convert the default value to Integer
                                                    comboBox.setSelectedItem(defaultValue);
                                                } else {
                                                    comboBox.setSelectedItem(null);
                                                }
                                                getAttributeValues.put(attributeId, comboBox.getSelectedItem());
                                                jPanel5.add(comboBox);
                                                System.out.println("Attribute with type NUMERIC (max-value <= 10) found: " + attributeObject.toString());
                                            }
                                        } else {
                                            JTextField textField = new JTextField();
                                            textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                            y += height + spacing;

                                            if (required) {
                                                textField.setInputVerifier(new InputVerifier() {
                                                    @Override
                                                    public boolean verify(JComponent input) {
                                                        JTextField textField = (JTextField) input;
                                                        String text = textField.getText();
                                                        if (text == null || text.isEmpty()) {
                                                            JOptionPane.showMessageDialog(null, "This field is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                                                            return false;
                                                        }
                                                        return true;
                                                    }
                                                });
                                            }
                                            if (attributeObject.has("default")) {
                                                String defaultValue = attributeObject.getString("default");
                                                try {
                                                    // Try to parse the defaultValue as an integer
                                                    int intValue = Integer.parseInt(defaultValue);
                                                    textField.setText(Integer.toString(intValue)); // Store as an integer and set as text
                                                } catch (NumberFormatException e) {
                                                    // Handle the case where defaultValue is not a valid integer
                                                    e.printStackTrace(); // Print an error message or handle as needed
                                                }
                                            }
                                            componentIdMap.put(textField, attributeId);
                                            getAttributeValues.put(attributeId, textField.getText());
                                            jPanel5.add(textField);
                                        }
                                    } else if ("TEXT".equals(attributeType)) {
                                        JTextField textField = new JTextField();
                                        textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                        y += height + spacing;
                                        componentIdMap.put(textField, attributeId);
                                        getAttributeValues.put(attributeId, textField.getText());
                                        jPanel5.add(textField);
                                    } else if ("DATETIME".equals(attributeType)) {
                                        JDateChooser dateChooser = new JDateChooser();
                                        dateChooser.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                        y += height + spacing;
                                        componentIdMap.put(dateChooser, attributeId);

                                        if (attributeObject.has("default")) {
                                            String defaultDateStr = attributeObject.getString("default");
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                            if (defaultDateStr.equals("[Today]")) {
                                                dateChooser.setDate(new Date());
                                            } else {
                                                try {
                                                    Date defaultDate = dateFormat.parse(defaultDateStr);
                                                    dateChooser.setDate(defaultDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        getAttributeValues.put(attributeId, dateChooser.getDate());
                                        jPanel5.add(dateChooser);
                                    }

                                }
                            }
                        }
                    } else {
                        System.err.println("Error: 'result' not found in the JSON response.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Error: HTTP Response Code " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        jPanel5.revalidate();
        jPanel5.repaint();
    }

    public void printComboBoxNames() {
        Component[] components = jPanel5.getComponents();
        for (Component component : components) {
            if (component instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) component;
                Integer comboBoxId = componentIdMap.get(comboBox);
                Object comboBoxValue = comboBox.getSelectedItem();
                filteredValues.put(comboBoxId, comboBoxValue);
                System.out.println("Updated Key : " + comboBoxId + " Updated Value :" + comboBoxValue);
            } else if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                Integer textFieldId = componentIdMap.get(textField);
                String textFieldValue = textField.getText();
                filteredValues.put(textFieldId, textFieldValue);
                System.out.println("Updated Key : " + textFieldId + "Updated Value :" + textFieldValue);
            } else if (component instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) component;
                Integer dateChooserId = componentIdMap.get(dateChooser);
                Date dateChooserValue = dateChooser.getDate();

                if (dateChooserValue != null) {
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

                    Date parsedDate = null; // Initialize parsedDate as null

                    try {
                        parsedDate = inputDateFormat.parse(dateChooserValue.toString()); // Use toString() to get the date as a string
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (parsedDate != null) {
                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDateStr = outputDateFormat.format(parsedDate);

                        // Make sure that formattedDateStr is in the correct format
                        System.out.println("Formatted Date: " + formattedDateStr);

                        filteredValues.put(dateChooserId, formattedDateStr); // Store as a string
                    } else {
                        // Handle the case where parsing failed (e.g., show an error message)
                        System.err.println("Error parsing date.");
                    }
                } else {
                    System.err.println("Date is null.");
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
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Create a new Issue in folder:");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(716, 335));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(654, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 654, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 255, Short.MAX_VALUE)
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
                    .addComponent(clmname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(18, 40, Short.MAX_VALUE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        name = newissue.getText();
        System.out.println("Name " + name);
        if (name == null || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be null.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            printComboBoxNames();
            for (Map.Entry<Integer, Object> entry : filteredValues.entrySet()) {
                Integer key = entry.getKey();
                Object value = entry.getValue();
                System.out.println("Key Added " + key + " Value : " + value);
            }
            System.out.println(" Description : " + description.getText());
            getDescription = description.getText().trim();
            System.out.println("Description : " + getDescription);
            issueDao.addIssue();
            filteredValues.clear();
            home.refreshJTable();
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        System.out.println("window is closed ");
    }//GEN-LAST:event_formWindowClosed

    public static void main(String args[]) {
        FlatLightLaf.setup();
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
