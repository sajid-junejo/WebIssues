package webissuesFrame;
 
import DAOImpl.ConnectionDAOImpl;
import DAOImpl.GlobalDAOImpl;
import DAOImpl.IssuesDAOImpl;
import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Image;
import java.io.BufferedReader;  
import java.net.HttpURLConnection; 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXDatePicker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.Issues;
import pojos.SessionManager; 

public class EditIssues extends javax.swing.JFrame {

    JXDatePicker datetime = new JXDatePicker();
    HomeFrame home = new HomeFrame();
    private JLabel[] labels;
    private JTextField[] textFields;
    private JComboBox[] combobox;
    public static String oldName = null;
    GlobalDAOImpl global = new GlobalDAOImpl();
    IssuesDAOImpl issueDao = new IssuesDAOImpl();
    private Map<Integer, String> userMap = new HashMap<>();
    boolean error = false;
    List<JSONObject> types = issueDao.typesList;

    public EditIssues() {
        initComponents();
        global.fetchData();
        issueDao.getIssueResult(id);
        this.setLocationRelativeTo(null);
        Image icon = new ImageIcon(this.getClass().getResource("/img/webissueslogo.png")).getImage();
        this.setIconImage(icon);
        this.setTitle("Edit Attributes");
        labels = new JLabel[0];
        textFields = new JTextField[0];
        combobox = new JComboBox[0]; 
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/img/information.png"));
        info.setIcon(icon1);
        for (JSONObject user : usersList) {
            int id = user.optInt("id");
            String name = user.optString("name");
            userMap.put(id, name);
        }
    }
    List<JSONObject> usersList = global.getUsersList();
    String csrfToken = SessionManager.getInstance().getCsrfToken();
    int userID = SessionManager.getInstance().getUserId();
    Issues issue = new Issues();
    int id = HomeFrame.IssueID;
    Map<Integer, Object> attributeValues = issueDao.printAttributes(id);
    Map<Integer, Object> getAttributeValues = new HashMap<>();
    Map<Integer, Object> filteredValues = new HashMap<>();
    private Map<Component, Integer> componentIdMap = new HashMap<>();
    public static Map<Integer, Object> getAPIValues = new HashMap<>();
    public static String modifiedName = null;
    ConnectionDAOImpl connectionDao = new ConnectionDAOImpl();

    public void EditForm() {
        SwingUtilities.invokeLater(() -> {
            Logger logger = Logger.getLogger(getClass().getName());
            long startTime = System.currentTimeMillis();
            int x = 30;
            int tx = 75;
            int y = 10;
            int labelWidth = 100;
            int componentWidth = 500;
            int height = 28;
            int spacing = 7;
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String text = null;
            nametext.setText(IssuesDAOImpl.name);
            typename.setText(IssuesDAOImpl.name);
            attributeValues = issueDao.printAttributes(id);
            List<JSONObject> resultList = global.getResultList(); 
                    try {
                        System.out.println("Result List : "+global.getResultList()); 
                        for (JSONObject jsonResponse : resultList){
                            System.out.println("Hello ");
                        if (jsonResponse.has("result")) {
                            JSONObject result = jsonResponse.getJSONObject("result");
                            JSONArray typesArray = result.getJSONArray("types");
                            for (int i = 0; i < typesArray.length(); i++) {
                                JSONObject typeObject = typesArray.getJSONObject(i);
                                int currentProjectId = typeObject.getInt("id");
                                int typeId = HomeFrame.typeId;
                                if (currentProjectId == typeId) {
                                    JSONArray attributesArray = typeObject.getJSONArray("attributes");
                                    Logger logger1 = Logger.getLogger(getClass().getName());
                                    long startTime1 = System.currentTimeMillis();
                                    logger.log(Level.INFO, "Execution time: " + (startTime1 - startTime) + " milliseconds");
                                    for (int j = 0; j < attributesArray.length(); j++) {
                                        JSONObject attributeObject = attributesArray.getJSONObject(j);
                                        //System.out.println("Attributes : " + attributesArray);
                                        String attributeName = attributeObject.getString("name");
                                        String attributeType = attributeObject.getString("type");
                                        int attributeId = attributeObject.getInt("id");
                                        boolean required = false;
                                        if (attributeObject.has("required")) {
                                            required = true;
                                        }
                                        if (required) {
                                            JLabel label = new JLabel("<html>" + attributeName + "<sup>*</sup>:</html>");
                                            label.setBounds(tx, y, labelWidth, height);
                                            jPanel3.add(label);
                                        } else {
                                            JLabel label = new JLabel(attributeName + ":");
                                            label.setBounds(tx, y, labelWidth, height);
                                            jPanel3.add(label);
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
                                            for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                                if (entry.getKey() == attributeId) {
                                                    Object attributeValue = entry.getValue();

                                                    // Check if attributeValue is null or an empty string
                                                    if (attributeValue == null || attributeValue.toString().isEmpty()) {
                                                        comboBox.setSelectedItem(null); // Deselect the combo box
                                                    } else {
                                                        // Find the index of attributeValue in the combo box model
                                                        int index = comboBoxModel.getIndexOf(attributeValue.toString());
                                                        if (index != -1) {
                                                            comboBox.setSelectedIndex(index);
                                                        } else {
                                                            comboBox.setSelectedItem(null);
                                                        }
                                                    }
                                                }
                                            }

                                            getAttributeValues.put(attributeId, comboBox.getSelectedItem());
                                            jPanel3.add(comboBox);
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
                                            for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                                Integer keyValue = entry.getKey();
                                                String values = (String) entry.getValue();
                                                String value = values.trim(); 
                                                if (keyValue.equals(attributeId)) {
                                                    if (value != null && (value.length() > 1 || !value.isEmpty())) {
                                                        if (!value.isEmpty()) { 
                                                            comboBox.setSelectedItem(value.toString());
                                                            break;
                                                        }
                                                    } else {
                                                        if (attributeObject.has("default")) {
                                                            String defaultValue = attributeObject.getString("default");
                                                            comboBox.setSelectedItem(defaultValue);
                                                            break;
                                                        } else {
                                                            comboBox.setSelectedItem(null);
                                                            break;
                                                        }
                                                    }

                                                }
                                            }

                                            getAttributeValues.put(attributeId, comboBox.getSelectedItem());
                                            jPanel3.add(comboBox);
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
                                                    for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                                        if (entry.getKey() == attributeId) {
                                                            if (entry.getValue() != null) {
                                                                textField.setText(entry.getValue().toString());
                                                            } else {
                                                                if (attributeObject.has("default")) {
                                                                    String defaultValue = attributeObject.getString("default");
                                                                    textField.setText(defaultValue);
                                                                } else {
                                                                    textField.setText(null);
                                                                }
                                                            }
                                                        }
                                                    }
                                                    getAttributeValues.put(attributeId, textField.getText());
                                                    jPanel3.add(textField);

                                                } else {
                                                    DefaultComboBoxModel<Integer> comboBoxModel = new DefaultComboBoxModel<>();
                                                    JComboBox<Integer> comboBox = new JComboBox<>(comboBoxModel);
                                                    comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                                    y += height + spacing;
                                                    componentIdMap.put(comboBox, attributeId);
                                                    for (int num = (int) minValue; num <= (int) maxValue; num++) {
                                                        comboBoxModel.addElement(num);
                                                    }
                                                    for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                                        if (entry.getKey() == attributeId) {
                                                            if (entry.getValue() != null) {
                                                                try {
                                                                    int value = Integer.parseInt(entry.getValue().toString());
                                                                    comboBox.setSelectedItem(value);
                                                                    comboBox.repaint();
                                                                } catch (NumberFormatException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                break;
                                                            } else {
                                                                if (attributeObject.has("default")) {
                                                                    Object defaultValue = attributeObject.getInt("default"); // Assuming it's an integer
                                                                    comboBox.setSelectedItem(defaultValue);
                                                                    break;
                                                                } else {
                                                                    comboBox.setSelectedItem(null);
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    getAttributeValues.put(attributeId, comboBox.getSelectedItem());
                                                    jPanel3.add(comboBox);
                                                }
                                            } else {
                                                JTextField textField = new JTextField();
                                                textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                                y += height + spacing;
                                                componentIdMap.put(textField, attributeId);
                                                for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                                    if (entry.getKey() == attributeId) {
                                                        if (entry.getValue() != null) {
                                                            textField.setText(entry.getValue().toString());
                                                        } else {
                                                            if (attributeObject.has("default")) {
                                                                String defaultValue = attributeObject.getString("default");
                                                                try {
                                                                    int intValue = Integer.parseInt(defaultValue);
                                                                    textField.setText(Integer.toString(intValue)); // Store as an integer and set as text
                                                                } catch (NumberFormatException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            } else {
                                                                textField.setText(null);
                                                            }
                                                        }
                                                    }
                                                }
                                                getAttributeValues.put(attributeId, textField.getText());
                                                jPanel3.add(textField);
                                            }
                                        } else if ("TEXT".equals(attributeType)) {
                                            JTextField textField = new JTextField();
                                            textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                            y += height + spacing;
                                            componentIdMap.put(textField, attributeId);
                                            for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                                if (entry.getKey() == attributeId) {
                                                    if (entry.getValue() != null) {
                                                        textField.setText(entry.getValue().toString());
                                                    } else {
                                                        if (attributeObject.has("default")) {
                                                            String defaultValue = attributeObject.getString("default");
                                                            textField.setText(defaultValue);
                                                        } else {
                                                            textField.setText(null);
                                                        }
                                                    }
                                                }
                                            }
                                            getAttributeValues.put(attributeId, textField.getText());
                                            jPanel3.add(textField);
                                        } else if ("DATETIME".equals(attributeType)) {
                                            JDateChooser dateChooser = new JDateChooser();
                                            dateChooser.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                            y += height + spacing;
                                            componentIdMap.put(dateChooser, attributeId);

                                            for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                                if (entry.getKey() == attributeId) {
                                                    String dateValue = entry.getValue() != null ? entry.getValue().toString().trim() : null;
                                                    if (dateValue != null && !dateValue.isEmpty()) {
                                                        try {
                                                            Date selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateValue);
                                                            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
                                                            dateChooser.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate));
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else if (attributeObject.has("default")) {
                                                        String defaultValue = attributeObject.getString("default");
                                                        try {
                                                            Date defaultDate = new SimpleDateFormat("yyyy-MM-dd").parse(defaultValue);
                                                            dateChooser.setDate(defaultDate);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                            getAttributeValues.put(attributeId, dateChooser.getDate());
                                            jPanel3.add(dateChooser);
                                        }

                                    }
                                    long endTime = System.currentTimeMillis();
                                    logger.log(Level.INFO, "total Time Second : " + (endTime - startTime1) + " milliseconds");
                                    long executionTime = endTime - startTime1;
                                    logger.log(Level.INFO, "Execution time: " + executionTime + " milliseconds");
                                }
                            }
                        } else {
                            logger.log(Level.SEVERE, "Error: 'result' not found in the JSON response.");
                        }
                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                } else {
//                    System.err.println("Error: HTTP Response Code " + responseCode);
//                } 
            jPanel3.revalidate();
            jPanel3.repaint();
            long endTime = System.currentTimeMillis();
            logger.log(Level.INFO, "End Time Last time: " + endTime + " milliseconds");
            long executionTime = endTime - startTime;
            logger.log(Level.INFO, "Execution time Last: " + executionTime + " milliseconds");
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        clmname = new javax.swing.JLabel();
        value = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
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
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
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
                            .addGap(86, 86, 86)
                            .addComponent(clmname, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                            .addGap(38, 38, 38)
                            .addComponent(value, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(136, 136, 136))
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
        typename.setPreferredSize(new java.awt.Dimension(34, 26));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
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
                .addGap(22, 22, 22)
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

    }//GEN-LAST:event_nametextActionPerformed

    private boolean isNumeric(String input) {
        String alphabetRegex = ".*[a-zA-Z].*";
        return input.matches(alphabetRegex);
    }

    public void printComboBoxNames() {
        Component[] components = jPanel3.getComponents();
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
                System.out.println(" TextField Value : " + textFieldValue);
                String attributeName = null;
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
                            if (textFieldValue.contains(".")) {
                                textFieldValue = textFieldValue.substring(0, textFieldValue.indexOf(".")) + ".";
                            } else {
                                textFieldValue += ".";
                            }
                            for (int i = 0; i < decimalValue; i++) {
                                textFieldValue += "0";
                            }
                        }

                        filteredValues.put(textFieldId, textFieldValue);
                    } else if (textFieldValue == null || textFieldValue.trim().isEmpty()) {

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
                } else if (textFieldValue == null && textFieldValue.trim().isEmpty()) {

                    if (global.getDefaultValue(textFieldId) != null) {
                        filteredValues.put(textFieldId, global.getDefaultValue(textFieldId));
                    }
                } else {
                    Integer decimalValue = global.getDecimal(textFieldId);
                    if (decimalValue != null && decimalValue > 0) {
                        if (textFieldValue.contains(".")) {
                            textFieldValue = textFieldValue.substring(0, textFieldValue.indexOf(".")) + ".";
                        } else {
                            textFieldValue += ".";
                        }
                        for (int i = 0; i < decimalValue; i++) {
                            textFieldValue += "0";
                        }
                    }

                    filteredValues.put(textFieldId, textFieldValue);
                }
            } else if (component instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) component;
                Integer dateChooserId = componentIdMap.get(dateChooser);
                Date dateChooserValue = dateChooser.getDate();

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

                        filteredValues.put(dateChooserId, formattedDateStr);
                    } else {
                        System.err.println("Error parsing date.");
                    }
                } else {
                    System.err.println("Date is null.");
                }
            }

        }
    }
    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        SwingUtilities.invokeLater(() -> {
            String updatedName = nametext.getText().trim();
            if (updatedName == null || updatedName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be null.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                printComboBoxNames();
                if (error) {

                }else{
                    
                    for (Map.Entry<Integer, Object> entry : getAttributeValues.entrySet()) {
                        Integer key = entry.getKey();
                        Object value = entry.getValue();
                        if (value == null && filteredValues.containsKey(key)) {
                            Object filteredValue = filteredValues.get(key);
                            if (filteredValue != null) {
                                getAPIValues.put(key, filteredValue);
                            }
                        }
                        if (value != null && filteredValues.containsKey(key)) {
                            Object filteredValue = filteredValues.get(key);
                            if (!value.equals(filteredValue)) {
                                getAPIValues.put(key, filteredValue);
                            }
                        }
                    }
                    oldName = nametext.getText().trim();
                    issueDao.editIssue();
                    getAPIValues.clear();
                    home.refreshJTable();
                   // dispose();
                }
                dispose();
            }
        });
    }//GEN-LAST:event_okActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        dispose();
    }//GEN-LAST:event_cancelActionPerformed

    public static void main(String args[]) {
        FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditIssues().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JLabel clmname;
    private javax.swing.JLabel info;
    private javax.swing.JLabel issuename;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel name;
    private javax.swing.JTextField nametext;
    private javax.swing.JButton ok;
    private javax.swing.JLabel typename;
    private javax.swing.JLabel value;
    // End of variables declaration//GEN-END:variables
}
