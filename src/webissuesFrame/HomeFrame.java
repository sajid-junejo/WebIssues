package webissuesFrame;

import DAOImpl.FoldersDAOImpl;
import dbConnection.DbConnection;
import java.awt.Image;
import java.sql.*; 
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import DAOImpl.IssuesDAOImpl;
import DAOImpl.ProjectsDAOImpl;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap; 
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set; 
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import pojos.Folder;
import pojos.Project;
import pojos.SessionManager;

public class HomeFrame extends javax.swing.JFrame {
    FoldersDAOImpl folders = new FoldersDAOImpl();
    Folder folder = new Folder();
    private final Map<String, Integer> projectIds = new HashMap<>();
    DefaultTreeModel model;
    private final IssuesDAOImpl issuesDAO = new IssuesDAOImpl();
    private final ProjectsDAOImpl projectsDAO = new ProjectsDAOImpl(); 
    AddViewFrame view;
    private JLabel[] labels;
    JLabel attributeLabel = null;
    int selectedRowIndex = 0;
    String typeName = null;
    AddNewIssue issue;
    public static int typeId = 0;
    
    //public static int projectId = 0;
    ImageIcon add ;
    ImageIcon edit;
    int attributeY = 0;
    String issueName = "";
    int userID = SessionManager.getInstance().getUserId();
    int userAccess = SessionManager.getInstance().getUserAccess();
    public static String folderName = null;
    static String projectName = "";
    static String ProjectNamess = null;
    static int projectId = 0;
    public static String PATH = null;
    public static int FoldersID = 0;
    public static int IssueID = 0;

    public HomeFrame() {
        initComponents();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        LoadImages();
        setUSer();
        jPanel2.setWrapStyleWord(true);
        jPanel2.setEditable(false);
        jPanel2.setLineWrap(true);
        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        System.out.println("width " + screenWidth + "Height" + screenHeight);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setExtendedState(HomeFrame.MAXIMIZED_BOTH);
        Load();
        jLabel12.setEnabled(false);
        labels = new JLabel[0];
        Image icon = new ImageIcon(this.getClass().getResource("/img/webissueslogo.png")).getImage();
        this.setIconImage(icon);
        this.setTitle("Genetech WI - WebIssues Desktop Client");
        jPanel3.setVisible(false);
        jTable1.setEditingRow(ERROR);
    }

    DefaultMutableTreeNode courses = new DefaultMutableTreeNode("<html><b>Projects</b></html>");
    public void setUSer() {
    String name = SessionManager.getInstance().getUserName();
        System.out.println("Name length "+name.length());
    if (name.length() > 10) {
        int width = (name.length() + 2) * 4; // Calculate the desired width
        System.out.println("width "+width);
        int height = username.getPreferredSize().height; // Keep the default height
        System.out.println("Heghit "+height);
        Dimension preferredSize = new Dimension(width, height);
        username.setPreferredSize(preferredSize);
        username.setText(name);
        username.setToolTipText(name);
    } else {
        username.setText(name);
        username.setToolTipText(SessionManager.getInstance().getUserName());
    }
}

    public void LoadImages() {
        ImageIcon addissue = new ImageIcon(this.getClass().getResource("/img/addissue.jpg"));
        jLabel12.setIcon(addissue);

        ImageIcon update = new ImageIcon(this.getClass().getResource("/img/update.png"));
        jLabel3.setIcon(update);

        ImageIcon icon1 = new ImageIcon(this.getClass().getResource("/img/alerts-icon.jpg"));
        jLabel4.setIcon(icon1);

        ImageIcon icon2 = new ImageIcon(this.getClass().getResource("/img/goto.png"));
        jLabel7.setIcon(icon2);

        ImageIcon icon3 = new ImageIcon(this.getClass().getResource("/img/keys.png"));
        jLabel8.setIcon(icon3);

        ImageIcon icon4 = new ImageIcon(this.getClass().getResource("/img/info.png"));
        jLabel9.setIcon(icon4);

        ImageIcon icon5 = new ImageIcon(this.getClass().getResource("/img/information.png"));
        jLabel5.setIcon(icon5);

        ImageIcon user = new ImageIcon(this.getClass().getResource("/img/user.png"));
        username.setIcon(user);
        
        add = new ImageIcon(this.getClass().getResource("/img/mail.png"));
        
        edit = new ImageIcon(this.getClass().getResource("/img/edit.png"));
        
    }

    public void Load() {
        try {
            List<Project> projects = projectsDAO.getProjects();
            for (Project project : projects) {
                projectName = project.getProjectName();
                DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(projectName);
                courses.add(rootNode);
                int ProjectId = project.getProjectId();
                List<Folder> folders = projectsDAO.getFoldersForProject(ProjectId);
                for(Folder folder : folders) {
                    String folderName = folder.getFolderName();
                    String typeName = projectsDAO.getTypeName(folder.getTypeId());
                    //System.out.println("type name "+ typeName);
                    String nodeValue = folderName + "      [" + typeName + "]";
                    projectIds.put(projectName, ProjectId);
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(folderName);
                    childNode.setUserObject(nodeValue);
                    rootNode.add(childNode);
                    //System.out.println("Type id "+folder.getTypeId());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model = new DefaultTreeModel(courses);
        jTree1.setModel(model);
    }

    private String[] getColumnNames(DefaultTableModel tableModel) {
        int columnCount = tableModel.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = tableModel.getColumnName(i);
        }
        return columnNames;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JTextArea();
        username = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1368, 680));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jLabel1.setText("View:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All Issues" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 102, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Projects");

        jLabel3.setText("Update");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel4.setText("Alerts");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Go To Items");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Password");
        jLabel8.setIconTextGap(5);
        jLabel8.setPreferredSize(new java.awt.Dimension(107, 20));

        jSeparator2.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Preferences");
        jLabel9.setIconTextGap(8);

        jSeparator3.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel10.setForeground(new java.awt.Color(0, 102, 204));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Tools");

        jLabel11.setForeground(new java.awt.Color(0, 102, 204));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Tools");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setText("Manage Views");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Add View" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 383, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 58, Short.MAX_VALUE))
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSeparator4.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Add Issue");
        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        jSeparator5.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setText("View was updated Succesfully");

        jSplitPane2.setBackground(new java.awt.Color(255, 255, 255));
        jSplitPane2.setDividerLocation(320);
        jSplitPane2.setDividerSize(3);
        jSplitPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTree1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setPreferredSize(new java.awt.Dimension(53, 16));
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jSplitPane2.setLeftComponent(jScrollPane1);

        jSplitPane1.setBackground(new java.awt.Color(255, 255, 255));
        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(4);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jScrollPane3.setPreferredSize(new java.awt.Dimension(754, 421));
        jScrollPane3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseMoved(evt);
            }
        });
        jScrollPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane3MouseClicked(evt);
            }
        });
        jScrollPane3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jScrollPane3KeyPressed(evt);
            }
        });

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTable1MouseMoved(evt);
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTable1);

        jSplitPane1.setLeftComponent(jScrollPane3);

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane2MouseWheelMoved(evt);
            }
        });

        jPanel2.setColumns(20);
        jPanel2.setRows(5);
        jScrollPane2.setViewportView(jPanel2);

        jSplitPane1.setRightComponent(jScrollPane2);

        jSplitPane2.setRightComponent(jSplitPane1);

        username.setText("username");
        username.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        username.setPreferredSize(new java.awt.Dimension(57, 14));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 464, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel7))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(jLabel2)))
                        .addGap(35, 35, 35))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)))
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1520, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void issueAttributes() {
        int cx = 60;
        int cy = 40;
        int clabelWidth = 400;
        int cheight = 16;
        int clabelSpacing = 5;

        try {
            Map<String, Object> attributesMap = issuesDAO.getAttributes(IssueID);
            attributeY = cy;
            for (Map.Entry<String, Object> entry : attributesMap.entrySet()) {
                String attrName = entry.getKey();
                Object attrValueObj = entry.getValue();
                String attrValue = (attrValueObj != null) ? attrValueObj.toString() : "";

                attributeLabel = new JLabel(attrName + "    :    " + attrValue);
                attributeLabel.setBounds(cx + clabelWidth, attributeY, clabelWidth, cheight);
                attributeLabel.setHorizontalAlignment(SwingConstants.LEFT);
                jPanel2.add(attributeLabel);
                attributeY += cheight + clabelSpacing;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jPanel2.revalidate();
        jPanel2.repaint();

    }

    public void buttonsCreation() {
        int bx = 440;
        int by = attributeY + 13;
        int blabelWidth = 100;
        int bheight = 20;
        int blabelSpacing = 5;
        JButton button1 = new JButton("All History");
        button1.setBounds(bx, by, blabelWidth, bheight);
        button1.setHorizontalAlignment(SwingConstants.LEADING);
        button1.addActionListener(new ButtonClickListener());

        JButton button2 = new JButton("Comments");
        button2.setBounds(bx + (blabelWidth + blabelSpacing), by, blabelWidth, bheight);
        button2.setHorizontalAlignment(SwingConstants.LEADING);
        button2.addActionListener(new ButtonClickListener());

        JButton button3 = new JButton("Attachements");
        button3.setBounds(bx + 2 * (blabelWidth + blabelSpacing), by, blabelWidth, bheight);
        button3.setHorizontalAlignment(SwingConstants.LEADING);
        button3.addActionListener(new ButtonClickListener());

        JButton button4 = new JButton("Comments & Attachements");
        button4.setBounds(bx + 3 * (blabelWidth + blabelSpacing), by, blabelWidth + 50, bheight);
        button4.setHorizontalAlignment(SwingConstants.LEADING);
        button4.addActionListener(new ButtonClickListener());
        jPanel2.add(button1);
        jPanel2.add(button2);
        jPanel2.add(button3);
        jPanel2.add(button4);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button1.setBackground(Color.GREEN);
                jPanel2.removeAll();
                issueDetails();
                buttonsCreation();
                issueAttributes();
                history();
                buttonsCreation();
                button2.setBackground(null);
                button3.setBackground(null);
                button4.setBackground(null);
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jPanel2.removeAll();
                issueDetails();
                issueAttributes();
                buttonsCreation();
                getComment();
                button1.setBackground(null);
                button4.setBackground(null);
                button3.setBackground(null);
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jPanel2.removeAll();
                issueDetails();
                issueAttributes();
                buttonsCreation();
                getFiles();
                button1.setBackground(null);
                button2.setBackground(null);
                button4.setBackground(null);
            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jPanel2.removeAll();
                issueDetails();
                issueAttributes();
                buttonsCreation();
                getCommentsAndFiles();
                button1.setBackground(null);
                button2.setBackground(null);
                button3.setBackground(null);
            }
        });
        jPanel2.revalidate();
        jPanel2.repaint();
        jScrollPane2.revalidate();
        jScrollPane2.repaint();
    }

    public void getCommentsAndFiles() {
        Connection con = null;
        PreparedStatement statement = null;
        JLabel changeLabel = null;
        JLabel attribute = null;
        String commentText = null;
        String fileName = null;
        String fileDescr = null;
        try {
            con = DbConnection.getConnection();
            int hx = 30;
            int hy = 183;
            int hlabelWidth = 400;
            int hheight = 16;
            int hlabelSpacing = 5;
            int labelY = hy;
            JLabel label = new JLabel("<html><b>Issue History</b></html>");
            label.setBounds(hx, labelY, hlabelWidth, hheight);
            labelY += hheight + hlabelSpacing;
            jPanel2.add(label);
//hktyirtju frtu
          

         

                if (fileName == null || fileName.isEmpty()) {
                    changeLabel = new JLabel("There are no files");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                } else {
                    //changeLabel = new JLabel("<html><b>" + formattedCreatedDate + " --- " + name + "</b></html>");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                    labelY += hheight + hlabelSpacing;
                    attribute = new JLabel("<html><li>" + fileName + "---" + fileDescr + "</li></html>");
                    attribute.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(attribute);
                }
                labelY += hheight + hlabelSpacing + 5;
            
            

           
                if (commentText == null || commentText.isEmpty()) {

                    changeLabel = new JLabel("There are no comments");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                } else {
                    //attribute = new JLabel("<html><b>" + formattedCreatedDate + " --- " + name + "</b></html>");
                    attribute.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(attribute);
                    labelY += hheight + hlabelSpacing;
                    changeLabel = new JLabel("<html><li>" + commentText + "</li></html>");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                }

                labelY += hheight + hlabelSpacing + 5;
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFiles() {
        Connection con = null;
        PreparedStatement statement = null;
        JLabel changeLabel = null;
        JLabel attribute = null;
        String fileName = null;
        String fileDescr = null;
        try {
            con = DbConnection.getConnection();
            int hx = 30;
            int hy = 183;
            int hlabelWidth = 400;
            int hheight = 16;
            int hlabelSpacing = 5;
            int labelY = hy;
            JLabel label = new JLabel("<html><b>Issue History</b></html>");
            label.setBounds(hx, labelY, hlabelWidth, hheight);
            labelY += hheight + hlabelSpacing;
            jPanel2.add(label);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getComment() {
        Connection con = null;
        PreparedStatement statement = null;
        JLabel changeLabel = null;
        JLabel attribute = null;
        String commentText = null;
        try {
            con = DbConnection.getConnection();
            int hx = 30;
            int hy = 183;
            int hlabelWidth = 400;
            int hheight = 16;
            int hlabelSpacing = 5;
            int labelY = hy;
            JLabel label = new JLabel("<html><b>Issue History</b></html>");
            label.setBounds(hx, labelY, hlabelWidth, hheight);
            labelY += hheight + hlabelSpacing;
            jPanel2.add(label);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void issueDetails() {
    int x = 30;
    int y = 15;
    int labelWidth = 900;
    int height = 20;
    int labelSpacing = 5;
    DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
    Object[] rowData = new Object[tableModel.getColumnCount()];
    String description = issuesDAO.getDescription(IssueID);
    JLabel descrLabel = null;
    boolean flag = false;
    String modified_name = null;

    try {
        Map<String, Object> issueDetailsMap = issuesDAO.getIssueDetails(IssueID);
        attributeY = y;

        // Define a list of keys you want to include in labels
        Set<String> keysToInclude = new LinkedHashSet<>(Arrays.asList("NAME", "ID", "TYPE", "LOCATION", "CREATED", "LAST MODIFIED"));
        for (String attrName : keysToInclude) {
            Object attrValueObj = issueDetailsMap.get(attrName);

            // Check if the key is in the list of keys to include
            if (attrValueObj != null) {
                // Create a single JLabel for attribute name and value
                String labelText = attrName.equals("NAME") ? ("<html><b>" + attrValueObj.toString() + "</b></html>") : (attrName + " : " + attrValueObj.toString());

                JLabel attrLabel = new JLabel(labelText);
                attrLabel.setBounds(x, attributeY, labelWidth, height);
                jPanel2.add(attrLabel);
                attributeY += height + labelSpacing;
            }
        }
        attributeY += 15;
        if (description != null && !description.isEmpty()) {
    descrLabel = new JLabel("Description:");
    descrLabel.setBounds(x, attributeY, labelWidth, height);
    jPanel2.add(descrLabel);
    attributeY += height + labelSpacing; 

    // Replace \n with <br> to ensure proper line breaks in HTML
    description = description.replace("\n", "<br>");

    // Use a JEditorPane to render HTML content
    JEditorPane descEditorPane = new JEditorPane();
    descEditorPane.setContentType("text/html"); // Set content type to HTML
    descEditorPane.setText("<html>" + description + "</html>"); // Wrap with <html> tag
    descEditorPane.setEditable(false);

    // Add a HyperlinkListener to make links clickable
    descEditorPane.addHyperlinkListener(new HyperlinkListener() {
        @Override
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException | java.net.URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    });
    descEditorPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    JScrollPane scrollPane = new JScrollPane(descEditorPane);
    scrollPane.setBounds(x, attributeY, labelWidth, height * 4);
    jPanel2.add(scrollPane);

    attributeY += height * 3 + labelSpacing;

    // Create the button
    JButton menuButton = new JButton("<span aria-hidden=\"true\" class=\"fa fa-ellipsis-v\"></span>");
    menuButton.setToolTipText("Menu");
    menuButton.setFocusable(false);
    menuButton.setBounds(x + labelWidth - 30, 10, 30, 30); // Adjust the position as needed
    menuButton.setBorderPainted(false);
    menuButton.setContentAreaFilled(false);
    menuButton.setOpaque(false);
    jPanel2.add(menuButton);
}

    } catch (Exception e) {
        e.printStackTrace();
    }
    jPanel2.revalidate();
    jPanel2.repaint();
}

    public void history() {
        int hx = 30;
        int hy = attributeY + 25;
        int hlabelWidth = 500;
        int hheight = 16;
        int hlabelSpacing = 5;
        int labelY = hy;

        JLabel label = new JLabel("<html><b>Issue History</b></html>");
        label.setBounds(hx, labelY, hlabelWidth, hheight);
        labelY += hheight + hlabelSpacing;
        jPanel2.add(label);
        issuesDAO.printAttributes(IssueID);
        Map<String, String> history = issuesDAO.getHistory(IssueID);
        for (Map.Entry<String, String> entry : history.entrySet()) {
            String keyValue = entry.getKey();
            String formattedEntry = entry.getValue();
            String[] lines = formattedEntry.split("\n");
            JLabel KeyLabel = new JLabel("<html><b>" + keyValue + "</b></html>");
            KeyLabel.setBounds(hx, labelY, hlabelWidth, hheight);
            jPanel2.add(KeyLabel);
            labelY += hheight + hlabelSpacing;

            JTextArea descTextArea = new JTextArea();
            descTextArea.setLineWrap(true);
            descTextArea.setWrapStyleWord(true);
            descTextArea.setEditable(false);

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    if (line.startsWith("Comment: →")) {
                        descTextArea.append(line.replace("Comment: →", "") + "");
                    } else {
                        JLabel historyLabel = new JLabel("<html><li>" + line + "</li></html>");
                        historyLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                        jPanel2.add(historyLabel);
                        labelY += hheight + hlabelSpacing;
                    }
                }
            }

            if (!descTextArea.getText().isEmpty()) {
                JScrollPane scrollPane = new JScrollPane(descTextArea);
                scrollPane.setBounds(hx, labelY, hlabelWidth, hheight * 3);
                jPanel2.add(scrollPane);
                labelY += hheight * 3 + hlabelSpacing;
            }
        }

        int currentHeight = jPanel2.getHeight();
        if (labelY > currentHeight) {
            jPanel2.setPreferredSize(new Dimension(jPanel2.getWidth(), labelY));
        }

        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private class ButtonClickListener implements ActionListener {

        private boolean labelsAdded = false;
        private JButton greenButton = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton currentButton = (JButton) e.getSource();
            if (currentButton.getText().equals("All History")) {
            } else if (currentButton.getText().equals("Only Comments")) {
                if (labelsAdded) {
                    jPanel2.revalidate();
                    jPanel2.repaint();
                    labelsAdded = false; // Reset the flag after removing the labels
                }
            } else if (currentButton.getText().equals("Only Attachements")) {
                System.out.println("Button 3 is clicked!");
            } else if (currentButton.getText().equals("Comments & Attachements")) {
                System.out.println("Button 4 is clicked!");
            }
            if (greenButton == currentButton) {
                currentButton.setBackground(UIManager.getColor("Button.background"));
                greenButton = null;
            } else {
                if (greenButton != null) {
                    greenButton.setBackground(UIManager.getColor("Button.background"));
                }
                currentButton.setBackground(Color.BLUE);
                greenButton = currentButton;
            }
        }
    }

    public void refreshJTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        try {
            DefaultTableModel tableModel = issuesDAO.getTableData(FoldersID);
            jTable1.setModel(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jTable1.revalidate();
        jTable1.repaint();
    }
    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed

    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        AddNewFolder viewFolder = new AddNewFolder();
        if (jLabel12.getText().equals("Add Issue")) {
            issue.setVisible(true);
            issue.AddForm();
        } else if (jLabel12.getText().equals("Add Folder")) {
            viewFolder.setVisible(true);
        }
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        jLabel12.setText("Add Folder");
        String valueBetweenCommas = null;
        jLabel12.setEnabled(true);
        jPanel3.setVisible(false);
        jPanel2.setEnabled(false); 
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
        jTable1.getTableHeader().setDefaultRenderer(headerRenderer);
        if (evt.getClickCount() != -1) {
            jPanel2.removeAll();
            jPanel2.repaint();
            jPanel2.revalidate();
            int x = evt.getX();
            int y = evt.getY();
            TreePath path = jTree1.getPathForLocation(x, y);
            if (path != null) {
                String pathString = path.toString();
                int firstCommaIndex = pathString.indexOf(",");
                int lastCommaIndex = pathString.lastIndexOf(",");
                if (firstCommaIndex != -1 && lastCommaIndex != -1 && lastCommaIndex > firstCommaIndex) {
                    valueBetweenCommas = pathString.substring(firstCommaIndex + 1, lastCommaIndex).trim(); 
                }
            }
            if (path != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                //int typeId = getTypeIdFromNode(node);
                projectName = node.toString(); 
                if (typeId != -1) {
                    Object userObject = node.getUserObject();
                    if (userObject instanceof String) {
                        String nodeValue = (String) userObject;
                        int openingBracketIndex = nodeValue.lastIndexOf('[');
                        int closingBracketIndex = nodeValue.lastIndexOf(']');
                        if (openingBracketIndex != -1 && closingBracketIndex != -1) {
                            folderName = nodeValue.substring(0, openingBracketIndex).trim();
                            projectId = projectIds.get(valueBetweenCommas);
                            System.out.println("Folder Name "+folderName); 
                            List<Folder> folders = projectsDAO.getFoldersForProject(projectId);
                            PATH = valueBetweenCommas + " - " + folderName; 
                            for (Folder folder : folders) {
                                if (folder.getFolderName().equals(folderName)) {
                                    FoldersID = folder.getFolderId();
                                    System.out.println("Folder Id "+FoldersID);
                                    typeId = folder.getTypeId();
                                    break;
                                }
                            }
                            System.out.println("Type id in JTree "+typeId);
                        } else {
                            folderName = nodeValue.trim();
                            projectsDAO.setFolderName(folderName);
                        }
                        try {
                            DefaultTableModel tableModel = null;
                            tableModel = issuesDAO.getTableData(FoldersID);
                            jTable1.setModel(tableModel);
                        } catch (Exception e) {
                        }
                    }

                }
            }
        }
    }//GEN-LAST:event_jTree1MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        jLabel12.setEnabled(true);
        jLabel12.setText("Add Issue");
        //System.out.println("folder name in the table mouse clicked "+folderName);
        if (SwingUtilities.isRightMouseButton(evt)) {
            selectedRowIndex = jTable1.getSelectedRow();
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem updateItem = new JMenuItem("Edit Attributes");
            JMenuItem insertItem = new JMenuItem("Add Issue");
            issue = new AddNewIssue();
            ImageIcon insertIcon = edit;
            ImageIcon addIssue = add;
            updateItem.setIcon(insertIcon);
            insertItem.setIcon(addIssue);
            updateItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //int selectedRowIndex = jTable1.getSelectedRow();
                    DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
                    Object[] rowData = new Object[tableModel.getColumnCount()];
                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
                        rowData[i] = tableModel.getValueAt(selectedRowIndex, i);
                    }
                    EditIssues edit = new EditIssues();
                    edit.setVisible(true);
                    edit.EditForm();
                    edit.setDefaultCloseOperation(edit.DISPOSE_ON_CLOSE);
                    
                    issue.AddForm();
                    //frame.setr
                }
            });

            insertItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    issue.setVisible(true);
                    issue.setDefaultCloseOperation(issue.DISPOSE_ON_CLOSE);
                }
            });
            popupMenu.add(insertItem);
            popupMenu.add(updateItem);
            if (userAccess == 2) {
                JMenuItem deleteItem = new JMenuItem("Delete Issue");
                ImageIcon deletedIssue = new ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\img\\delete.png");
                deleteItem.setIcon(deletedIssue);
                deleteItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int option = JOptionPane.showConfirmDialog(
                                null,
                                "Are you sure you want to delete " + issueName,
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION);

                        if (option == JOptionPane.YES_OPTION) {
                            issuesDAO.deleteIssue(IssueID);
                            refreshJTable();
                            jPanel2.removeAll();
                            jPanel2.repaint();
                            jPanel2.revalidate();
                        } else if (option == JOptionPane.NO_OPTION) {
                            System.out.println("User clicked NO.");
                        } 
                    }
                });
                popupMenu.add(deleteItem);
            }
            popupMenu.show(jTable1, evt.getX(), evt.getY());
        } else if (SwingUtilities.isLeftMouseButton(evt)) {
            jPanel2.removeAll();
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow != -1) {
                int idColumnIndex = jTable1.getColumnModel().getColumnIndex("ID");
                Object idValue = jTable1.getValueAt(selectedRow, idColumnIndex);
                if (idValue != null) {
                    String idString = idValue.toString();
                    idString = idString.replaceAll("\\<.*?\\>|[^0-9]", "");
                    System.out.println("ISSUE ID "+IssueID);
                    IssueID = Integer.parseInt(idString);
                    issueAttributes();
                    //buttonsCreation();
                    issueDetails();
                    history();
                }
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1KeyPressed

    private void jScrollPane3MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane3MouseDragged

    private void jScrollPane3MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane3MouseMoved

    private void jScrollPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane3MouseClicked
        // TODO add your handling code here:
        jLabel12.setEnabled(true);
        jLabel12.setText("Add Issue");
        if(jLabel12.getText().equals("Add Issue")) {
        } else if (jLabel12.getText().equals("Add Folder")) {
        }
    }//GEN-LAST:event_jScrollPane3MouseClicked

    private void jScrollPane3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane3KeyPressed

    private void jScrollPane2MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jScrollPane2MouseWheelMoved
        // TODO add your handling code here:
        int notches = evt.getWheelRotation();
        evt.consume(); // Consume the original event to prevent default scrolling
        JScrollBar verticalScrollBar = jScrollPane2.getVerticalScrollBar();
        int newValue = verticalScrollBar.getValue() + (notches * verticalScrollBar.getUnitIncrement() * 20);
        verticalScrollBar.setValue(newValue);
    }//GEN-LAST:event_jScrollPane2MouseWheelMoved

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked

        int x = evt.getX();
        int y = evt.getY();
        System.out.println(" this is x " + x);
        System.out.println(" this is y " + y);

    }//GEN-LAST:event_jPanel1MouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

    }//GEN-LAST:event_formWindowClosing

    private void jTable1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseMoved

    public static void main(String args[]) {
        FlatLightLaf.registerCustomDefaultsSource("style");
        FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextArea jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTree jTree1;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
