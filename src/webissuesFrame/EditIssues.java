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
import javax.swing.JComboBox;
import javax.swing.JLabel;
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

    public EditIssues() {
        initComponents();
        this.setLocationRelativeTo(null);
        Image icon = new ImageIcon(this.getClass().getResource("/img/webissueslogo.png")).getImage();
        this.setIconImage(icon);
        this.setTitle("Edit Attributes");
        labels = new JLabel[0];

        textFields = new JTextField[0];
        combobox = new JComboBox[0];
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/img/information.png"));
        info.setIcon(icon1);
    }
    String csrfToken = SessionManager.getInstance().getCsrfToken();
    int userID = SessionManager.getInstance().getUserId();
    GlobalDAOImpl global = new GlobalDAOImpl();
    Issues issue = new Issues();
    IssuesDAOImpl issueDao = new IssuesDAOImpl();
    int id = HomeFrame.IssueID;
    Map<Integer, Object> attributeValues = issueDao.printAttributes(id);
    Map<Integer, Object> getAttributeValues = new HashMap<>();
    Map<Integer, Object> filteredValues = new HashMap<>();
    private Map<Component, Integer> componentIdMap = new HashMap<>();
    public static Map<Integer, Object> getAPIValues = new HashMap<>();
    public static String modifiedName = null;

    public void EditForm() {
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
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
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
                                    int attributeId = attributeObject.getInt("id");
                                    boolean required = false;
                                    if (attributeObject.has("required")) {
                                        required = true;
                                    }
                                    // Create a label for the attribute using the "name" from the API response
                                    JLabel label = new JLabel(attributeName + ":");
                                    label.setBounds(tx, y, labelWidth, height);
                                    jPanel3.add(label);
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
                                        for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                            System.out.println("Entry key " + entry.getKey());
                                            System.out.println("Attribute values " + entry.getValue());
                                            if (entry.getKey() == attributeId) {
                                                Object attributeValue = entry.getValue();
                                                System.out.println("Entry Value " + attributeValue);

                                                // Check if attributeValue is null or an empty string
                                                if (attributeValue == null || attributeValue.toString().isEmpty()) {
                                                    comboBox.setSelectedItem(null); // Deselect the combo box
                                                } else {
                                                    // Find the index of attributeValue in the combo box model
                                                    int index = comboBoxModel.getIndexOf(attributeValue.toString());
                                                    if (index != -1) {
                                                        comboBox.setSelectedIndex(index);
                                                    } else {
                                                        // Handle the case where attributeValue doesn't exist in the model
                                                        comboBox.setSelectedItem(null); // or choose a default selection
                                                    }
                                                }
                                            }
                                        }

                                        getAttributeValues.put(attributeId, comboBox.getSelectedItem());
                                        jPanel3.add(comboBox);
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
                                                System.out.println("Elements : " + itemsArray.getString(k));
                                            }
                                        }
                                        for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                            System.out.println("Value " + entry.getValue());
                                            Integer keyValue = entry.getKey();
                                            String value = (String) entry.getValue();

                                            if (keyValue.equals(attributeId)) {
                                                if (value != null && (value.length() > 1 || !value.isEmpty())) {
                                                    System.out.println("Value length: " + value.length()); // Print the length of the value
                                                    if (!value.isEmpty()) {
                                                        System.out.println("-------checkingcccccccccccccccc : " + value);
                                                        comboBox.setSelectedItem(value.toString());
                                                        break;
                                                    }
                                                } else {
                                                    if (attributeObject.has("default")) {
                                                        System.out.println("-------checkingcccccccccccccccc -> " + value);
                                                        String defaultValue = attributeObject.getString("default");
                                                        comboBox.setSelectedItem(defaultValue);
                                                        break;
                                                    } else {
                                                        System.out.println("-------checkingcccccccccccccccc = " + value);
                                                        comboBox.setSelectedItem(null);
                                                        break;
                                                    }
                                                }

                                            }
                                        }

                                        System.out.println("Attribute Id " + attributeId);
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

                                        System.out.println("Attribute Id " + attributeId);

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
                                                System.out.println("Attribute with type NUMERIC (max-value > 10) found: " + attributeObject.toString());
                                            } else {
                                                DefaultComboBoxModel<Integer> comboBoxModel = new DefaultComboBoxModel<>();
                                                JComboBox<Integer> comboBox = new JComboBox<>(comboBoxModel);
                                                comboBox.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                                y += height + spacing;
                                                componentIdMap.put(comboBox, attributeId);
                                                for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                                    if (entry.getKey() == attributeId) {
                                                        if (entry.getValue() != null) {
                                                            comboBox.setSelectedItem(entry.getValue());
                                                            break;
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
                                                for (int num = (int) minValue; num <= (int) maxValue; num++) {
                                                    comboBoxModel.addElement(num);
                                                }
                                                getAttributeValues.put(attributeId, comboBox.getSelectedItem());
                                                jPanel3.add(comboBox);
                                                System.out.println("Attribute with type NUMERIC (max-value <= 10) found: " + attributeObject.toString());
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
                                                                // Try to parse the defaultValue as an integer
                                                                int intValue = Integer.parseInt(defaultValue);
                                                                textField.setText(Integer.toString(intValue)); // Store as an integer and set as text
                                                            } catch (NumberFormatException e) {
                                                                // Handle the case where defaultValue is not a valid integer
                                                                e.printStackTrace(); // Print an error message or handle as needed
                                                            }
                                                        } else {
                                                            textField.setText(null);
                                                        }
                                                    }
                                                }
                                            }
                                            getAttributeValues.put(attributeId, textField.getText());
                                            jPanel3.add(textField);
                                            System.out.println("Attribute with type NUMERIC (max-value not present) found: " + attributeObject.toString());
                                        }
                                    } else if ("TEXT".equals(attributeType)) {
                                        JTextField textField = new JTextField();
                                        textField.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                        y += height + spacing;
                                        componentIdMap.put(textField, attributeId);
                                        System.out.println("Attribute Id " + attributeId);
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
                                        System.out.println("Attribute with type TEXT found: " + attributeObject.toString());
                                    } else if ("DATETIME".equals(attributeType)) {
                                        JDateChooser dateChooser = new JDateChooser();
                                        dateChooser.setBounds(tx + labelWidth + spacing, y, componentWidth, height);
                                        y += height + spacing;
                                        componentIdMap.put(dateChooser, attributeId);

                                        System.out.println("Attribute Id " + attributeId);

                                        for (Map.Entry<Integer, Object> entry : attributeValues.entrySet()) {
                                            if (entry.getKey() == attributeId) {
                                                String dateValue = entry.getValue() != null ? entry.getValue().toString().trim() : null;
                                                System.out.println("Date " + dateValue);
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
                                        System.out.println("Attribute with type DATETIME found: " + attributeObject.toString());
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
        jPanel3.revalidate();
        jPanel3.repaint();
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

    }//GEN-LAST:event_nametextActionPerformed
    public void printComboBoxNames() {
        Component[] components = jPanel3.getComponents();
        for (Component component : components) {
            if (component instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) component;
                Integer comboBoxId = componentIdMap.get(comboBox);
                Object comboBoxValue = comboBox.getSelectedItem();
                filteredValues.put(comboBoxId, comboBoxValue);
                //System.out.println("Updated Key : " + comboBoxId + " Updated Value :" + comboBoxValue);
            } else if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                Integer textFieldId = componentIdMap.get(textField);
                String textFieldValue = textField.getText();
                filteredValues.put(textFieldId, textFieldValue);
                //System.out.println("Updated Key : " + textFieldId + "Updated Value :" + textFieldValue);
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
                    // Handle the case where dateChooserValue is null (e.g., show an error message or set a default value)
                    // Example: filteredValues.put(dateChooserId, "default_value");
                    System.err.println("Date is null.");
                }
            }

        }
    }
    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        SwingUtilities.invokeLater(() -> {
            printComboBoxNames();
            home.refreshJTable();
            for (Map.Entry<Integer, Object> entry : getAttributeValues.entrySet()) {
                Integer key = entry.getKey();
                Object value = entry.getValue();
                if (value == null && filteredValues.containsKey(key)) {
                    Object filteredValue = filteredValues.get(key);
                    if (filteredValue != null) {
                        System.out.println("Key: " + key + ", Value from filteredValues: " + filteredValue);
                        getAPIValues.put(key, filteredValue);
                    }
                }
                if (value != null && filteredValues.containsKey(key)) {
                    Object filteredValue = filteredValues.get(key);
                    if (!value.equals(filteredValue)) {
                        System.out.println("Key: " + key + ", Value from filteredValues: " + filteredValue);
                        getAPIValues.put(key, filteredValue);
                    }
                }
            }
            //printComboBoxNames();
            oldName = nametext.getText();
            issueDao.editIssue();
            getAPIValues.clear();
            home.refreshJTable();
            dispose();
        });
    }//GEN-LAST:event_okActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        dispose();
        System.out.println("project id : " + HomeFrame.projectId);
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
