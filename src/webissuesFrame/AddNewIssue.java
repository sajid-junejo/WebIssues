package webissuesFrame;
 
import DAOImpl.GlobalDAOImpl;
import DAOImpl.IssuesDAOImpl;
import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Image;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map; 
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject; 

public class AddNewIssue extends javax.swing.JFrame {
 
    IssuesDAOImpl issueDao = new IssuesDAOImpl();
    GlobalDAOImpl global = new GlobalDAOImpl();
    private Map<Integer, String> userMap = new HashMap<>();
    List<JSONObject> types = issueDao.typesList;

    public AddNewIssue() {
        initComponents();
        global.fetchData();
        typename.setText(HomeFrame.folderName);
        Image icon = new ImageIcon(this.getClass().getResource("/img/webissueslogo.png")).getImage();
        this.setIconImage(icon);
        this.setTitle("Add Issue");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setIconImage(icon);
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/img/information.png"));
        info.setIcon(icon1);
        for (JSONObject user : usersList) {
            int id = user.optInt("id");
            String name = user.optString("name");
            userMap.put(id, name);
        }
        AddForm();
    }
    List<JSONObject> usersList = global.getUsersList();  
    boolean error = false; 
    int id = HomeFrame.IssueID; 
    Map<Integer, Object> getAttributeValues = new HashMap<>();
    public static Map<Integer, Object> filteredValues = new HashMap<>();
    private Map<Component, Integer> componentIdMap = new HashMap<>();
    public static String name = null;
    public static String getDescription = null;

    public void AddForm() {
        SwingUtilities.invokeLater(() -> { 
        int x = 1;
        int tx = 20;
        int y = 10;
        int labelWidth = 100;
        int componentWidth = 500;
        int height = 28;
        int spacing = 7;
        issueDao.printAttributes(id);
        List<JSONObject> resultList = global.getResultList();
        try {
            for (JSONObject jsonResponse : resultList) {
                if (jsonResponse.has("result")) {
                    JSONObject result = jsonResponse.getJSONObject("result");
                    JSONArray typesArray = result.getJSONArray("types");
                    for (int i = 0; i < typesArray.length(); i++) {
                        JSONObject typeObject = typesArray.getJSONObject(i);
                        int currentProjectId = typeObject.getInt("id");
                        int typeId = HomeFrame.typeId;
                        if (currentProjectId == typeId) {
                            JSONArray attributesArray = typeObject.getJSONArray("attributes");
                            for (int j = 0; j < attributesArray.length(); j++) { 
                                JSONObject attributeObject = attributesArray.getJSONObject(j);
                                String attributeName = attributeObject.getString("name");
                                String attributeType = attributeObject.getString("type");
                                boolean required = false;
                                if (attributeObject.has("required")) {
                                    required = true;
                                }
                                int attributeId = attributeObject.getInt("id");
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
                                        comboBoxModel.addElement(userMap.get(memberId));
                                    }
                                    if (attributeObject.has("default")) {
                                        String defaultValue = attributeObject.getString("default");
                                        comboBox.setSelectedItem(defaultValue);
                                    } else {
                                        comboBox.setSelectedItem(null);
                                    }
                                    getAttributeValues.put(attributeId, comboBox.getSelectedItem());
                                    jPanel5.add(comboBox);
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
                                    if (attributeObject.has("max-value")) {
                                        double maxValue = 0.0;
                                        try {
                                            String maxValStr = attributeObject.getString("max-value");
                                            maxValue = Double.parseDouble(maxValStr);

                                            if (maxValue == (int) maxValue) {
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

                                            if (attributeObject.has("default")) {
                                                String defaultValue = attributeObject.getString("default");
                                                try {
                                                    int intValue = Integer.parseInt(defaultValue);
                                                    textField.setText(Integer.toString(intValue));
                                                } catch (NumberFormatException e) {

                                                    e.printStackTrace();
                                                }
                                            }

                                            getAttributeValues.put(attributeId, textField.getText());

                                            jPanel5.add(textField);
                                            //System.out.println("Attribute with type NUMERIC (max-value > 10) found: " + attributeObject.toString());
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
                                                Integer defaultValue = Integer.parseInt(defaultValueStr);
                                                comboBox.setSelectedItem(defaultValue);
                                            } else {
                                                comboBox.setSelectedItem(null);
                                            }
                                            getAttributeValues.put(attributeId, comboBox.getSelectedItem());
                                            jPanel5.add(comboBox);
                                            // System.out.println("Attribute with type NUMERIC (max-value <= 10) found: " + attributeObject.toString());
                                        }
                                    } else {
                                        JTextField textField = new JTextField();
                                        textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                        y += height + spacing;
                                        if (attributeObject.has("default")) {
                                            String defaultValue = attributeObject.getString("default");
                                            try {
                                                // Try to parse the defaultValue as an integer
                                                int intValue = Integer.parseInt(defaultValue);
                                                textField.setText(Integer.toString(intValue)); // Store as an integer and set as text
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace(); 
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } 
        jPanel5.revalidate();
        jPanel5.repaint(); 
        });
    }

    private boolean isNumeric(String input) {
        String alphabetRegex = ".*[a-zA-Z].*";
        return input.matches(alphabetRegex);
    }

    public void printComboBoxNames() {
        Component[] components = jPanel5.getComponents();
        for (Component component : components) {
            if (component instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) component;
                Integer comboBoxId = componentIdMap.get(comboBox);
                Object comboBoxValue = comboBox.getSelectedItem();
                String attributeName = null;
                for (JSONObject type : types) {
                    try {
                        if (type.has("attributes")) {
                            JSONArray typeAttributes = type.getJSONArray("attributes");
                            for (int j = 0; j < typeAttributes.length(); j++) {
                                JSONObject typeAttribute = typeAttributes.getJSONObject(j);
                                int typeAttrId = typeAttribute.getInt("id");
                                String typeAttrName = typeAttribute.optString("name", " ");
                                if (typeAttrId == comboBoxId) {
                                    attributeName = typeAttrName;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (global.getRequired(comboBoxId)) {
                    if (comboBoxValue != null) {
                        filteredValues.put(comboBoxId, comboBoxValue);
                    } else {
                        JOptionPane.showMessageDialog(this, attributeName + " cannot be null.", "Error", JOptionPane.ERROR_MESSAGE);
                        error = true;
                    }
                } else if (comboBoxValue == null) {

                    if (global.getDefaultValue(comboBoxId) != null) {
                        filteredValues.put(comboBoxId, comboBoxValue);
                    }
                } else {
                    filteredValues.put(comboBoxId, comboBoxValue);
                }
            } else if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                Integer textFieldId = componentIdMap.get(textField);
                String textFieldValue = textField.getText().trim();
                String attributeName = null;
                Object minValue = global.getMinValue(textFieldId);
                Object maxValue = global.getMaxValue(textFieldId);
//                if (minValue != null && maxValue != null) {
//                    if (minValue instanceof Integer && maxValue instanceof Integer) { 
//                        int min = (int) minValue;
//                        int max = (int) maxValue;
//                        int value = Integer.parseInt(textFieldValue);
//
//                        if (value >= min && value <= max) {
//                            System.out.println(" Value is in the range ");
//                        }
//                    } else if (minValue instanceof Double && maxValue instanceof Double) {
//                        // Both min and max are doubles.
//                        double min = (double) minValue;
//                        double max = (double) maxValue;
//                        double value = Double.parseDouble(textFieldValue);
//
//                        if (value >= min && value <= max) {
//                            // The value is within the specified range.
//                            attributeName = "Your Attribute Name";  
//                        }
//                    }
//                }
                System.out.println("Min : " + minValue + " Max : " + maxValue);
                for (JSONObject type : types) {
                    try {
                        if (type.has("attributes")) {
                            JSONArray typeAttributes = type.getJSONArray("attributes");
                            for (int j = 0; j < typeAttributes.length(); j++) {
                                JSONObject typeAttribute = typeAttributes.getJSONObject(j);
                                int typeAttrId = typeAttribute.getInt("id");
                                String typeAttrName = typeAttribute.optString("name", " ");
                                if (typeAttrId == textFieldId) {
                                    attributeName = typeAttrName;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (global.getType(textFieldId).equals("NUMERIC")) {
                    if (isNumeric(textFieldValue)) {
                        JOptionPane.showMessageDialog(this, attributeName + " is a Numeric Type .", "Error", JOptionPane.ERROR_MESSAGE);
                        error = true;
                    } else {
                        //JOptionPane.showMessageDialog(this, attributeName + " is a Numeric Type .", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (global.getRequired(textFieldId)) {
                    if (textFieldValue != null && !textFieldValue.isEmpty()) {
                        Integer decimalValue = global.getDecimal(textFieldId);
                        if (decimalValue != null && decimalValue > 0) {
                            textFieldValue += ".";
                            for (int i = 0; i < decimalValue; i++) {
                                textFieldValue += "0";
                            }
                        }
                        filteredValues.put(textFieldId, textFieldValue);
                    } else if (textFieldValue == null || textFieldValue.isEmpty()) {

                        if (global.getDefaultValue(textFieldId) != null) {
                            filteredValues.put(textFieldId, global.getDefaultValue(textFieldId));
                        } else {
                            JOptionPane.showMessageDialog(this, attributeName + " cannot be null.", "Error", JOptionPane.ERROR_MESSAGE);
                            error = true;
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, attributeName + " cannot be null.", "Error", JOptionPane.ERROR_MESSAGE);
                        error = true;
                    }
                } else if (textFieldValue == null && textFieldValue.isEmpty()) {

                    if (global.getDefaultValue(textFieldId) != null) {
                        filteredValues.put(textFieldId, global.getDefaultValue(textFieldId));
                    }
                } else {
                    Integer decimalValue = global.getDecimal(textFieldId);
                    if (decimalValue != null && decimalValue > 0) {
                        textFieldValue += ".";
                        for (int i = 0; i < decimalValue; i++) {
                            textFieldValue += "0";
                        }
                    }
                    if (minValue != null) {
                        System.out.println("Min Value " + minValue);
                    }
                    filteredValues.put(textFieldId, textFieldValue);
                }

            } else if (component instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) component;
                Integer dateChooserId = componentIdMap.get(dateChooser);
                Date dateChooserValue = dateChooser.getDate();
                String attributeName = null;
                for (JSONObject type : types) {
                    try {
                        if (type.has("attributes")) {
                            JSONArray typeAttributes = type.getJSONArray("attributes");
                            for (int j = 0; j < typeAttributes.length(); j++) {
                                JSONObject typeAttribute = typeAttributes.getJSONObject(j);
                                int typeAttrId = typeAttribute.getInt("id");
                                String typeAttrName = typeAttribute.optString("name", " ");
                                if (typeAttrId == dateChooserId) {
                                    attributeName = typeAttrName;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (dateChooserValue != null) {
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

                    Date parsedDate = null;

                    try {
                        parsedDate = inputDateFormat.parse(dateChooserValue.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (parsedDate != null) {
                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDateStr = outputDateFormat.format(parsedDate);

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
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
            .addGap(0, 273, Short.MAX_VALUE)
        );

        clmname.setText("Name");
        clmname.setAlignmentX(20.0F);

        value11.setText("Value");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(clmname, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(value11, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(214, 214, 214)))
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
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
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
                        .addComponent(info)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(25, 25, 25))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(newissue, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(typename, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        name = newissue.getText().trim();
        if (name == null || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be null.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            printComboBoxNames();
            if (error) {
                //dispose();
            } else {
                for (Map.Entry<Integer, Object> entry : filteredValues.entrySet()) {
                    Integer key = entry.getKey();
                    Object value = entry.getValue();
                }
                getDescription = description.getText().trim();
                issueDao.addIssue();
                filteredValues.clear(); 
                dispose();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here: 
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
