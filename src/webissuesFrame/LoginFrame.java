package webissuesFrame;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.json.JSONException;
import org.json.JSONObject;
import pojos.Principal;
import pojos.SessionManager;

public class LoginFrame extends javax.swing.JFrame {

    JCheckBox rememberPasswordCheckBox;
    JTextField emailField;
    JPasswordField passwordField;
    public static String apiUrl;
    public static String hostname;
    String user = null;
    char[] passwordChars = null;
    private Preferences prefs;

    public LoginFrame() {
        prefs = Preferences.userNodeForPackage(getClass());
        initComponents();
        jTable1.setDefaultEditor(Object.class, null);
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
        jTable1.getTableHeader().setDefaultRenderer(headerRenderer);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Image icon = new ImageIcon(this.getClass().getResource("/img/webissueslogo.png")).getImage();
        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/img/play.png"));
        submit.setIcon(icon1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setIconImage(icon);
        this.setTitle("Webissues 2.0.2");
        //displayLoginForm();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jDialog1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        address = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        submit = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1396, 758));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText(" Enter Address of the Webissues Server");

        address.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addressKeyPressed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {" Genetech WI", "https://192.168.85.130/webissues/"},
                {" Genetech WI", "https://webissues-new.genetechz.com/"}
            },
            new String [] {
                "Name", "Addres"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText(" Recent Connections");

        submit.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        submit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                submitMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("  WebIssues Desktop Client 2.02");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addGap(927, 927, 927))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(168, 168, 168)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(805, 805, 805))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                        .addGap(807, 807, 807))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(address)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(175, 175, 175))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(156, 156, 156)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(address, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addGap(171, 171, 171))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void disposeFrame() {
        this.dispose();
    }
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int selectedRow = jTable1.getSelectedRow();
        address.setText(model.getValueAt(selectedRow, 1).toString());
        if (evt.getClickCount() == 2) {
            makeConnection();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void submitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitMouseClicked
        makeConnection();

    }//GEN-LAST:event_submitMouseClicked
    public void makeConnection() {
        String inputAddress = address.getText();
        if (inputAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "The address you entered is not valid", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        hostname = extractHostname(inputAddress);
        System.out.println("Host name " + hostname);

        int maxRetries = 3;
        int retryCount = 0;
        boolean errorOccurred = false;

        apiUrl = "https://" + hostname + "/server/api/login.php";
        while (retryCount < maxRetries) {
            try {
                InetAddress[] resolvedAddresses = InetAddress.getAllByName(hostname);
                System.out.println("IP Addresses for " + hostname + ":");
                for (InetAddress addr : resolvedAddresses) {
                    System.out.println("  " + addr.getHostAddress());
                }
                URL url = new URL(apiUrl);
                System.out.println("URL " + url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection instanceof HttpsURLConnection) {
                    HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
                    httpsConnection.setHostnameVerifier((host, session) -> true);
                }

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(1000);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST
                        || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                        || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                    // Show the authentication panel
                    showAuthenticationPanel();
                    break;
                } else {
                    retryCount++;
                    errorOccurred = true;
                }
                connection.disconnect();

            } catch (UnknownHostException e) {
                JOptionPane.showMessageDialog(this, "Failed to resolve hostname: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                break;
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(this, "Invalid URL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                break;
            } catch (IOException e) {
                if (retryCount >= 1) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error while connecting to the URL", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                } else {
                    apiUrl = "http://" + hostname + "/webissues/server/api/login.php";
                    retryCount++;
                    errorOccurred = true;
                }
            }
        }

        if (errorOccurred && retryCount >= maxRetries) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "Error while connecting to the URL", "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    public void displayLoginForm() {
        String username = prefs.get("username", "");
        String password = prefs.get("password", "");
        System.out.println("USEr  " + username + " Password : " + password);
        if (!username.isEmpty() && !password.isEmpty()) {
            emailField.setText(username);
            passwordField.setText(password);
            rememberPasswordCheckBox.setSelected(true);
        }

        this.setVisible(true);
    }

    private void showAuthenticationPanel() {
        JPanel authenticationPanel = new JPanel(new GridBagLayout());
        JLabel urlLabel = new JLabel("Hostname: " + hostname);
        JLabel hostnameLabel = new JLabel("Server name: Genetech WI");
        JLabel loginLabel = new JLabel("Login");
        JLabel passwordLabel = new JLabel("Password");
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField.requestFocusInWindow();

        rememberPasswordCheckBox = new JCheckBox("Remember Password");

        String username = prefs.get("username", "");
        String password = prefs.get("password", "");
        System.out.println("USEr  " + username + " Password : " + password);
        if (!username.isEmpty() && !password.isEmpty()) {
            emailField.setText(username);
            passwordField.setText(password);
            rememberPasswordCheckBox.setSelected(true);
            
            SwingUtilities.invokeLater(() -> {
                JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(authenticationPanel);
                Component[] components = dialog.getComponents();
                for (Component component : components) {
                    if (component instanceof JButton) {
                        JButton button = (JButton) component;
                        if (button.getText().equals("OK")) {
                            button.doClick();
                            break;
                        }
                    }
                }
            });
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        authenticationPanel.add(urlLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        authenticationPanel.add(hostnameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        authenticationPanel.add(loginLabel, gbc);

        gbc.gridx = 1;
        authenticationPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        authenticationPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        authenticationPanel.add(passwordField, gbc);

        JPanel checkBoxPanel = new JPanel(new GridBagLayout());
        GridBagConstraints checkBoxGbc = new GridBagConstraints();
        checkBoxGbc.gridx = 0;
        checkBoxGbc.gridy = 0;
        checkBoxGbc.anchor = GridBagConstraints.WEST;
        checkBoxPanel.add(rememberPasswordCheckBox, checkBoxGbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        authenticationPanel.add(checkBoxPanel, gbc);

        user = emailField.getText();
        passwordChars = passwordField.getPassword();
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logger logger = Logger.getLogger(getClass().getName());
                long startTime = System.currentTimeMillis();

                user = emailField.getText();
                passwordChars = passwordField.getPassword();
                doLogin();
                if (rememberPasswordCheckBox.isSelected()) {
                    prefs.put("username", emailField.getText());
                    prefs.put("password", String.valueOf(passwordField.getPassword()));
                } else {
                    prefs.remove("username");
                    prefs.remove("password");
                }
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                logger.log(Level.INFO, "Execution time: " + executionTime + " milliseconds");
            }
        });

        int results = JOptionPane.showConfirmDialog(this, authenticationPanel, "Login", JOptionPane.OK_CANCEL_OPTION);

        if (results == JOptionPane.OK_OPTION) {
            user = emailField.getText();
            passwordChars = passwordField.getPassword();
            doLogin();
            if (rememberPasswordCheckBox.isSelected()) {
                prefs.put("username", emailField.getText());
                prefs.put("password", String.valueOf(passwordField.getPassword()));
            } else {
                prefs.remove("username");
                prefs.remove("password");
            }
        }
    }

    public void doLogin() { 
        String pass = new String(passwordChars); 
        try { 
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
                httpsConnection.setHostnameVerifier((hostname, session) -> true);
            }
            connection.setRequestProperty("Content-Type", "application/json");
            String jsonInputString = "{\"login\":\"" + user + "\",\"password\":\"" + pass + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
                if (responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
                    String newUrl = connection.getHeaderField("Location");
                    if (newUrl != null) {
                        URL redirectedUrl = new URL(newUrl);
                        connection.disconnect();
                        connection = (HttpURLConnection) redirectedUrl.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "application/json");

                        try (OutputStream os = connection.getOutputStream()) {
                            byte[] input = jsonInputString.getBytes("utf-8");
                            os.write(input, 0, input.length);
                        }

                        responseCode = connection.getResponseCode();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error: Redirected without a new URL", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONObject result = jsonResponse.getJSONObject("result");
                    SessionManager.getInstance().setUserId(result.getInt("userId"));
                    SessionManager.getInstance().setCsrfToken(result.getString("csrfToken"));
                    SessionManager.getInstance().setUserName(result.getString("userName"));
                    SessionManager.getInstance().setUserAccess(result.getInt("userAccess"));
                    String userName = SessionManager.getInstance().getUserName();
                    String sessionCookie = connection.getHeaderField("Set-Cookie");
                    if (sessionCookie != null) {
                        SessionManager.getInstance().setCookie(sessionCookie);
                    }
                    connection.disconnect();
                    HomeFrame home = new HomeFrame();
                    home.setTitle("Welcome");
                    home.setVisible(true);
                    dispose();
                    JOptionPane.showMessageDialog(this, "Successfully Logged in as " + userName);
                    //global.getAttributeName();
                    initializePrincipal();
                } catch (JSONException e) {
                    JOptionPane.showMessageDialog(this, "Username or Password Wrong", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error, Username or Password Wrong!", "Sajid", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void initializePrincipal() {
        Principal principal = new Principal(SessionManager.getInstance().getUserId());
        Principal.setCurrent(principal);
    }
    private void addressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addressKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            makeConnection();
        }
    }//GEN-LAST:event_addressKeyPressed
    private String extractHostname(String fullAddress) {
        int protocolIndex = fullAddress.indexOf("//");
        String remaining;
        if (protocolIndex != -1) {
            remaining = fullAddress.substring(protocolIndex + 2);
        } else {
            remaining = fullAddress;
        }
        int pathIndex = remaining.indexOf('/');
        if (pathIndex != -1) {
            return remaining.substring(0, pathIndex);
        } else {
            return remaining;
        }
    }

    public static void main(String args[]) {
        FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField address;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel submit;
    // End of variables declaration//GEN-END:variables

}
