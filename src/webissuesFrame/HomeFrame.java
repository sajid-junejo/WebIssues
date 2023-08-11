package webissuesFrame;

import dbConnection.DbConnection;
import java.awt.Image;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import DAOImpl.IssuesDAOImpl;
import DAOImpl.ProjectsDAOImpl;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import pojos.Folder;
import pojos.Project;
import pojos.SessionManager;

public class HomeFrame extends javax.swing.JFrame {

    AddNewIssue issue = new AddNewIssue();
    DefaultTreeModel model;
    private IssuesDAOImpl issuesDAO = new IssuesDAOImpl();
    private ProjectsDAOImpl projectsDAO = new ProjectsDAOImpl();
    //SessionManager 
    AddViewFrame view;
    private JLabel[] labels;
    int issueId = 0;
    JLabel attributeLabel = null;
    int selectedRowIndex = 0;
    String typeName = null;
    int typeId = 0;
    int attributeY = 0;
    String issueName = "";
    int userID = SessionManager.getInstance().getUserId();
    int userAccess = SessionManager.getInstance().getUserAccess();
    String folderName = null;

    public HomeFrame() {
        initComponents();
        // Get the default screen device
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        int screenWidth = gd.getDisplayMode().getWidth();
        int screenHeight = gd.getDisplayMode().getHeight();
        System.out.println("width "+screenWidth + "Height" +  screenHeight);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setExtendedState(HomeFrame.MAXIMIZED_BOTH);
        Load();
        jLabel12.setEnabled(false);
        labels = new JLabel[0];
        Image icon = new ImageIcon(this.getClass().getResource("/img/webissueslogo.png")).getImage();
        this.setIconImage(icon);
        this.setTitle("Genetech WI - WebIssues Desktop Client");
        jPanel3.setVisible(false);
    }

    DefaultMutableTreeNode courses = new DefaultMutableTreeNode("Projects");

    public void Load() {
        //getUser();
        try {
            List<Project> projects = projectsDAO.getProjects();
            for (Project project : projects) {
                String projectName = project.getProjectName();
                DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(projectName);
                courses.add(rootNode);

                int projectId = project.getProjectId();
                List<Folder> folders = projectsDAO.getFoldersForProject(projectId);
                for (Folder folder : folders) {
                    String folderName = folder.getFolderName();
                    String typeName = folder.getTypeName();
                    String nodeValue = folderName + "      [" + typeName + "]";
                    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(folderName);
                    childNode.setUserObject(nodeValue);
                    rootNode.add(childNode);
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
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jLabel3.setIcon(new javax.swing.ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\img\\update.png")); // NOI18N
        jLabel3.setText("Update");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel4.setIcon(new javax.swing.ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\img\\alerts-icon.jpg")); // NOI18N
        jLabel4.setText("Alerts");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\img\\goto.png")); // NOI18N
        jLabel7.setText("Go To Items");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setIcon(new javax.swing.ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\img\\keys.png")); // NOI18N
        jLabel8.setText("Password");
        jLabel8.setIconTextGap(-12);
        jLabel8.setPreferredSize(new java.awt.Dimension(107, 20));

        jSeparator2.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setIcon(new javax.swing.ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\img\\info.png")); // NOI18N
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
        jLabel12.setIcon(new javax.swing.ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\img\\addissue.jpg")); // NOI18N
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

        jLabel5.setIcon(new javax.swing.ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\img\\information.png")); // NOI18N
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
        jScrollPane3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
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
        jTable1.setSelectionBackground(new java.awt.Color(0, 0, 0));
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
        jScrollPane2.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane2MouseWheelMoved(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setAutoscrolls(true);
        jPanel2.setPreferredSize(new java.awt.Dimension(950, 215));
        jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanel2MouseMoved(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1176, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 227, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel2);

        jSplitPane1.setRightComponent(jScrollPane2);

        jSplitPane2.setRightComponent(jSplitPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSplitPane2))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1520, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public static int getTypeIdFromNode(DefaultMutableTreeNode node) {
        Object userObject = node.getUserObject();
        if (userObject instanceof String) {
            String nodeValue = (String) userObject;
            String typeName = null;
            int openingBracketIndex = nodeValue.lastIndexOf('[');
            int closingBracketIndex = nodeValue.lastIndexOf(']');
            if (openingBracketIndex != -1 && closingBracketIndex != -1) {
                typeName = nodeValue.substring(openingBracketIndex + 1, closingBracketIndex).trim();
            }

            if (typeName != null && !typeName.isEmpty()) {
                return getTypeIDFromDB(typeName);
            }
        }
        return -1;
    }

    private static int getTypeIDFromDB(String typeName) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int typeId = -1;

        try {
            con = DbConnection.getConnection();
            String query = "SELECT type_id FROM issue_types WHERE type_name = ?";
            statement = con.prepareStatement(query);
            statement.setString(1, typeName);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                typeId = resultSet.getInt("type_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return typeId;
    }

    public void issueAttributes() {
        int cx = 30;
        int cy = 40;
        int clabelWidth = 400;
        int cheight = 16;
        int clabelSpacing = 5;
        try {
            Connection connection = DbConnection.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT type_id FROM issue_types WHERE type_name = '" + typeName + "'";
            ResultSet getTypeId = statement.executeQuery(query);
            if (getTypeId.next()) {
                typeId = getTypeId.getInt("type_id");
            }
            String attrQuery = "SELECT attr_name, attr_value "
                    + "FROM attr_types AS a "
                    + "LEFT JOIN attr_values AS v ON v.attr_id = a.attr_id AND v.issue_id = " + issueId
                    + " WHERE a.type_id = " + typeId;
            ResultSet attrValues = statement.executeQuery(attrQuery);

            attributeY = cy;
            while (attrValues.next()) {
                String attrName = attrValues.getString("attr_name");
                String attrValue = attrValues.getString("attr_value");
                if (attrValue == null) {
                    attrValue = "";
                }
                attributeLabel = new JLabel(attrName + "    :    " + attrValue);
                attributeLabel.setBounds(cx + clabelWidth, attributeY, clabelWidth, cheight);
                attributeLabel.setHorizontalAlignment(SwingConstants.LEFT);
                jPanel2.add(attributeLabel);
                attributeY += cheight + clabelSpacing;
            }
            statement.close();
            connection.close();
        } catch (Exception e) {

        }
    }

    public void buttonsCreation() {
        int bx = 440;
        int by = attributeY + 5;
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
            int hy = 160;
            int hlabelWidth = 400;
            int hheight = 16;
            int hlabelSpacing = 5;
            int labelY = hy;
            JLabel label = new JLabel("<html><b>Issue History</b></html>");
            label.setBounds(hx, labelY, hlabelWidth, hheight);
            labelY += hheight + hlabelSpacing;
            jPanel2.add(label);
            int issueID = issueId;
//hktyirtju frtu
            String sqlQuery = "SELECT fl.file_id, fl.file_name, fl.file_size, fl.file_descr, fl.file_storage, i.issue_id, i.folder_id, sc.user_id, sc.stamp_time, u.user_login "
                    + "FROM files AS fl "
                    + "JOIN changes AS ch ON ch.change_id = fl.file_id "
                    + "JOIN issues AS i ON i.issue_id = ch.issue_id "
                    + "JOIN stamps AS sc ON sc.stamp_id = ch.change_id "
                    + "JOIN folders AS f ON f.folder_id = i.folder_id "
                    + "JOIN projects AS p ON p.project_id = f.project_id "
                    + "JOIN users AS u ON u.user_id = sc.user_id "
                    + "WHERE i.issue_id = ?"
                    + " ORDER BY sc.stamp_time DESC";

            statement = con.prepareStatement(sqlQuery);
            statement.setInt(1, issueID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                //int fileId = resultSet.getInt("file_id");
                fileName = resultSet.getString("file_name");
                //long fileSize = resultSet.getLong("file_size");
                fileDescr = resultSet.getString("file_descr");
                //String fileStorage = resultSet.getString("file_storage");
                //int issueIdResult = resultSet.getInt("issue_id");
                //int folderId = resultSet.getInt("folder_id");
                //int userId = resultSet.getInt("user_id");
                long stampTime = resultSet.getLong("stamp_time") * 1000L;
                Date createdDate = new Date(stampTime);
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy hh:mm a");
                String formattedCreatedDate = sdf.format(createdDate);
                String name = resultSet.getString("user_login");

                if (fileName == null || fileName.isEmpty()) {
                    changeLabel = new JLabel("There are no files");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                } else {
                    changeLabel = new JLabel("<html><b>" + formattedCreatedDate + " --- " + name + "</b></html>");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                    labelY += hheight + hlabelSpacing;
                    attribute = new JLabel("<html><li>" + fileName + "---" + fileDescr + "</li></html>");
                    attribute.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(attribute);
                }
                labelY += hheight + hlabelSpacing + 5;
            }
            sqlQuery = "SELECT c.comment_id, c.comment_text, c.comment_format, i.issue_id, i.folder_id, sc.user_id, sc.stamp_time, u.user_login "
                    + "FROM comments AS c "
                    + "JOIN changes AS ch ON ch.change_id = c.comment_id "
                    + "JOIN issues AS i ON i.issue_id = ch.issue_id "
                    + "JOIN stamps AS sc ON sc.stamp_id = ch.change_id "
                    + "JOIN folders AS f ON f.folder_id = i.folder_id "
                    + "JOIN projects AS p ON p.project_id = f.project_id "
                    + "JOIN users AS u ON u.user_id = sc.user_id "
                    + "WHERE i.issue_id = ?";

            statement = con.prepareStatement(sqlQuery);
            statement.setInt(1, issueID);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                //int commentId = resultSet.getInt("comment_id");
                commentText = resultSet.getString("comment_text");
                long stampTime = resultSet.getLong("stamp_time") * 1000L;
                Date createdDate = new Date(stampTime);
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy hh:mm a");
                String formattedCreatedDate = sdf.format(createdDate);
                String name = resultSet.getString("user_login");
                //String commentFormat = resultSet.getString("comment_format");
                //int retrievedIssue = resultSet.getInt("issue_id"); // Update issueId with the retrieved value
                //int folderId = resultSet.getInt("folder_id");
                //int userId = resultSet.getInt("user_id");
                if (commentText == null || commentText.isEmpty()) {

                    changeLabel = new JLabel("There are no comments");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                } else {
                    attribute = new JLabel("<html><b>" + formattedCreatedDate + " --- " + name + "</b></html>");
                    attribute.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(attribute);
                    labelY += hheight + hlabelSpacing;
                    changeLabel = new JLabel("<html><li>" + commentText + "</li></html>");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                }

                labelY += hheight + hlabelSpacing + 5;
            }

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
            int hy = 160;
            int hlabelWidth = 400;
            int hheight = 16;
            int hlabelSpacing = 5;
            int labelY = hy;
            JLabel label = new JLabel("<html><b>Issue History</b></html>");
            label.setBounds(hx, labelY, hlabelWidth, hheight);
            labelY += hheight + hlabelSpacing;
            jPanel2.add(label);
            int issueID = issueId;

            String sqlQuery = "SELECT fl.file_id, fl.file_name, fl.file_size, fl.file_descr, fl.file_storage, i.issue_id, i.folder_id, sc.user_id, sc.stamp_time, u.user_login "
                    + "FROM files AS fl "
                    + "JOIN changes AS ch ON ch.change_id = fl.file_id "
                    + "JOIN issues AS i ON i.issue_id = ch.issue_id "
                    + "JOIN stamps AS sc ON sc.stamp_id = ch.change_id "
                    + "JOIN folders AS f ON f.folder_id = i.folder_id "
                    + "JOIN projects AS p ON p.project_id = f.project_id "
                    + "JOIN users AS u ON u.user_id = sc.user_id "
                    + "WHERE i.issue_id = ?"
                    + " ORDER BY sc.stamp_time DESC";

            statement = con.prepareStatement(sqlQuery);
            statement.setInt(1, issueID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                fileName = resultSet.getString("file_name");
                fileDescr = resultSet.getString("file_descr");
                long stampTime = resultSet.getLong("stamp_time") * 1000L;
                Date createdDate = new Date(stampTime);
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy hh:mm a");
                String formattedCreatedDate = sdf.format(createdDate);
                String name = resultSet.getString("user_login");

                if (fileName == null || fileName.isEmpty()) {
                    changeLabel = new JLabel("There are no files");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                } else {
                    changeLabel = new JLabel("<html><b>" + formattedCreatedDate + " --- " + name + "</b></html>");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                    labelY += hheight + hlabelSpacing;
                    attribute = new JLabel("<html><li>" + fileName + "---" + fileDescr + "</li></html>");
                    attribute.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(attribute);
                }
                labelY += hheight + hlabelSpacing + 5;
            }

            resultSet.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
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
            int hy = 160;
            int hlabelWidth = 400;
            int hheight = 16;
            int hlabelSpacing = 5;
            int labelY = hy;
            JLabel label = new JLabel("<html><b>Issue History</b></html>");
            label.setBounds(hx, labelY, hlabelWidth, hheight);
            labelY += hheight + hlabelSpacing;
            jPanel2.add(label);
            int issueID = issueId;

            String sqlQuery = "SELECT c.comment_id, c.comment_text, c.comment_format, i.issue_id, i.folder_id, sc.user_id, sc.stamp_time, u.user_login "
                    + "FROM comments AS c "
                    + "JOIN changes AS ch ON ch.change_id = c.comment_id "
                    + "JOIN issues AS i ON i.issue_id = ch.issue_id "
                    + "JOIN stamps AS sc ON sc.stamp_id = ch.change_id "
                    + "JOIN folders AS f ON f.folder_id = i.folder_id "
                    + "JOIN projects AS p ON p.project_id = f.project_id "
                    + "JOIN users AS u ON u.user_id = sc.user_id "
                    + "WHERE i.issue_id = ?"
                    + " ORDER BY sc.stamp_time DESC";

            statement = con.prepareStatement(sqlQuery);
            statement.setInt(1, issueID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // int commentId = resultSet.getInt("comment_id");
                commentText = resultSet.getString("comment_text");
                long stampTime = resultSet.getLong("stamp_time") * 1000L;
                Date createdDate = new Date(stampTime);
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy hh:mm a");
                String formattedCreatedDate = sdf.format(createdDate);
                String name = resultSet.getString("user_login");
                if (commentText == null || commentText.isEmpty()) {
                    // If commentText is null or empty, display "There are no comments" label
                    changeLabel = new JLabel("There are no comments");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                } else {
                    // If commentText is not empty, display the comment details
                    attribute = new JLabel("<html><b>" + formattedCreatedDate + " --- " + name + "</b></html>");
                    attribute.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(attribute);
                    labelY += hheight + hlabelSpacing;
                    changeLabel = new JLabel("<html><li>" + commentText + "</li></html>");
                    changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                    jPanel2.add(changeLabel);
                }

                labelY += hheight + hlabelSpacing + 5;
            }

            resultSet.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void issueDetails() {
        int x = 30;
        int y = 35;
        int labelWidth = 400;
        int height = 20;
        int labelSpacing = 1;
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        Object[] rowData = new Object[tableModel.getColumnCount()];
        String description = "";
        JLabel descrLabel = null;
        boolean flag = false;
        String modified_name = null;

        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            rowData[i] = tableModel.getValueAt(selectedRowIndex, i);
            String columnName = tableModel.getColumnName(i);
            Connection connection = null;
            Statement statement = null;
            if (columnName.equalsIgnoreCase("ID")) {
                JLabel idLabel = new JLabel(columnName + ": " + rowData[i]);
                idLabel.setBounds(x, y, labelWidth, height);
                idLabel.setHorizontalAlignment(SwingConstants.LEFT);
                jPanel2.add(idLabel);
                y += height + labelSpacing;
                issueId = (int) rowData[i];

                try {
                    connection = DbConnection.getConnection();
                    statement = connection.createStatement();
                    String desc = "Select descr_text from issue_descriptions where issue_id = " + issueId;
                    ResultSet getDesc = statement.executeQuery(desc);

                    if (getDesc.next()) {
                        description = getDesc.getString("descr_text");
                        flag = true;
                    } else {
                        System.out.println("descriptions not found");
                    }
                    String getTypeQuery = "SELECT it.type_name "
                            + "FROM issues i "
                            + "JOIN folders f ON i.folder_id = f.folder_id "
                            + "JOIN issue_types it ON f.type_id = it.type_id "
                            + "WHERE i.issue_id = " + rowData[i];

                    ResultSet typeResultSet = statement.executeQuery(getTypeQuery);
                    if (typeResultSet.next()) {
                        typeName = typeResultSet.getString("type_name");
                        JLabel typeLabel = new JLabel("TYPE: " + typeName);
                        typeLabel.setBounds(x, y, labelWidth, height);
                        typeLabel.setHorizontalAlignment(SwingConstants.LEFT);
                        jPanel2.add(typeLabel);
                        y += height + labelSpacing;
                    } else {
                        JLabel nullLabel = new JLabel("TYPE: Null");
                        nullLabel.setBounds(x, y, labelWidth, height);
                        nullLabel.setHorizontalAlignment(SwingConstants.LEFT);
                        jPanel2.add(nullLabel);
                        y += height + labelSpacing;
                    }
                } catch (Exception e) {

                }
                continue;
            }
            if (columnName.equalsIgnoreCase("modified by")) {
                modified_name = (String) rowData[i];
                break;
            }

            if (columnName.equalsIgnoreCase("issue_name")) {
                issueName = rowData[i].toString();
                JLabel issue = new JLabel("<html><b>" + rowData[i] + "</b></html>");
                issue.setBounds(x, 10, labelWidth, height);
                issue.setHorizontalAlignment(SwingConstants.LEFT);
                jPanel2.add(issue);
            } else {
                if (columnName.equalsIgnoreCase("location")) {
                    JLabel rowLabel = new JLabel(columnName + ": " + rowData[i]);
                    rowLabel.setBounds(x, y, labelWidth, height);
                    rowLabel.setHorizontalAlignment(SwingConstants.LEFT);
                    jPanel2.add(rowLabel);
                    y += height + labelSpacing;
                }
            }
        }
        try {
            Connection connection = DbConnection.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT "
                    + "sc.stamp_time AS created_date, "
                    + "uc.user_id AS created_user, "
                    + "uc.user_name AS created_by, "
                    + "sm.stamp_time AS modified_date, "
                    + "um.user_id AS modified_user, "
                    + "um.user_name AS modified_by "
                    + "FROM "
                    + "issues AS i "
                    + "JOIN stamps AS sc ON sc.stamp_id = i.issue_id "
                    + "JOIN users AS uc ON uc.user_id = sc.user_id "
                    + "JOIN stamps AS sm ON sm.stamp_id = i.stamp_id "
                    + "JOIN users AS um ON um.user_id = sm.user_id "
                    + "WHERE i.issue_id = " + issueId;

            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
            } else {
                do {
                    long modifiedDateMillis = resultSet.getLong("modified_date") * 1000L;
                    long createdDateMillis = resultSet.getLong("created_date") * 1000L;
                    Date createdDate = new Date(createdDateMillis);
                    Date modifiedDate = new Date(modifiedDateMillis);
                    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy hh:mm a");
                    String formattedCreatedDate = sdf.format(createdDate);
                    String formattedModifiedDate = sdf.format(modifiedDate);
                    String createdBy = resultSet.getString("created_by");
                    String modifiedBy = resultSet.getString("modified_by");
                    //String createdLabelText = "Created Date: " + formattedCreatedDate + ", Created By: " + createdBy;
                    JLabel createdLabel = new JLabel("Created : " + formattedCreatedDate + " --- " + createdBy);
                    createdLabel.setBounds(x, y, labelWidth, height);
                    createdLabel.setHorizontalAlignment(SwingConstants.LEFT);
                    jPanel2.add(createdLabel);
                    y += height + labelSpacing;
                    JLabel modifiedLabel = new JLabel("Modified : " + formattedModifiedDate + " --- " + modifiedBy);
                    modifiedLabel.setBounds(x, y, labelWidth, height);
                    modifiedLabel.setHorizontalAlignment(SwingConstants.LEFT);
                    jPanel2.add(modifiedLabel);
                    y += height + labelSpacing;
                } while (resultSet.next());
            }
        } catch (Exception e) {
        }
        if (flag == true) {
            descrLabel = new JLabel("Description " + description);
            descrLabel.setBounds(x, y, labelWidth, height);
            descrLabel.setHorizontalAlignment(SwingConstants.LEFT);
            jPanel2.add(descrLabel);
            y += height + labelSpacing;
        }
    }

    public void history() {
        Connection con = null;
        PreparedStatement statement = null;
        JLabel changeLabel = null;
        JLabel attribute = null;

        try {
            con = DbConnection.getConnection();
            int hx = 30;
            int hy = 175;
            int hlabelWidth = 500;
            int hheight = 16;
            int hlabelSpacing = 5;
            int labelY = hy;

            JLabel label = new JLabel("<html><b>Issue History</b></html>");
            label.setBounds(hx, labelY, hlabelWidth, hheight);
            labelY += hheight + hlabelSpacing;
            jPanel2.add(label);

            String sqlQuery = "SELECT ch.change_id, ch.change_type, ch.stamp_id, "
                    + "sc.stamp_time AS created_date, "
                    + "cu.user_login AS created_user_login, "
                    + "sm.stamp_time AS modified_date, sm.user_id AS modified_user, "
                    + "ch.attr_id, ch.value_old, ch.value_new, ch.from_folder_id, ch.to_folder_id "
                    + "FROM changes AS ch "
                    + "JOIN stamps AS sc ON sc.stamp_id = ch.change_id "
                    + "JOIN stamps AS sm ON sm.stamp_id = ch.stamp_id "
                    + "JOIN users AS cu ON sc.user_id = cu.user_id "
                    + "WHERE ch.issue_id = ?"
                    + " ORDER BY created_date DESC";

            statement = con.prepareStatement(sqlQuery);
            statement.setInt(1, issueId);

            ResultSet resultSet = statement.executeQuery();
            Map<String, List<String>> changesByDate = new LinkedHashMap<>();
            Map<Integer, String> attrIdToNameMap = new HashMap<>();

            try {
                String sqlQueryAttributes = "SELECT attr_id, attr_name FROM attr_types";
                PreparedStatement attrStatement = con.prepareStatement(sqlQueryAttributes);
                ResultSet attrResultSet = attrStatement.executeQuery();
                while (attrResultSet.next()) {
                    int attrId = attrResultSet.getInt("attr_id");
                    String attrName = attrResultSet.getString("attr_name");
                    attrIdToNameMap.put(attrId, attrName);
                }
            } catch (SQLException c) {
                c.printStackTrace();
            }

            while (resultSet.next()) {
                int attrId = resultSet.getInt("attr_id");
                String attrName = attrIdToNameMap.get(attrId);

                long createdDateMillis = resultSet.getLong("created_date") * 1000L;
                Date createdDate = new Date(createdDateMillis);
                String createdUserLogin = resultSet.getString("created_user_login"); // Use the new column name
                long modifiedDateMillis = resultSet.getLong("modified_date") * 1000L;
                Date modifiedDate = new Date(modifiedDateMillis);
                //String modifiedUser = resultSet.getString("modified_user");
                String valueOld = resultSet.getString("value_old");
                String valueNew = resultSet.getString("value_new");
                //int fromFolderId = resultSet.getInt("from_folder_id");
                //int toFolderId = resultSet.getInt("to_folder_id");
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy hh:mm a");
                String formattedCreatedDate = sdf.format(createdDate);
                String formattedModifiedDate = sdf.format(modifiedDate);

                String changeLabelString = formattedCreatedDate + " --- " + createdUserLogin;
                String attributeString = "";

                if (attrName == null && valueOld == null) {
                    attributeString = "Name -> " + valueNew;
                } else if (attrName == null) {
                    attributeString = "Name -> " + " -> " + valueOld + " -> " + valueNew;
                } else {
                    if (valueOld == null || valueOld.isEmpty()) {
                        attributeString = attrName + " -> " + valueNew;
                    } else if (valueNew == null || valueNew.isEmpty()) {
                        attributeString = attrName + " -> " + valueOld + " -> empty";
                    } else {
                        attributeString = attrName + " -> " + valueOld + " -> " + valueNew;
                    }
                }

                List<String> changesList = changesByDate.getOrDefault(changeLabelString, new ArrayList<>());
                changesList.add(attributeString);
                changesByDate.put(changeLabelString, changesList);
            }

            resultSet.close();
            statement.close();
            con.close();

            for (Map.Entry<String, List<String>> entry : changesByDate.entrySet()) {
                String changeLabelString = entry.getKey();
                List<String> changesList = entry.getValue();

                changeLabel = new JLabel("<html><b>" + changeLabelString + "</b></html>");
                changeLabel.setBounds(hx, labelY, hlabelWidth, hheight);
                jPanel2.add(changeLabel);

                // Update the labelY to avoid overlapping of changeLabel and attribute labels
                labelY += (hheight + hlabelSpacing);

                StringBuilder bulletList = new StringBuilder("<html><ul>");

                for (String attributeString : changesList) {
                    bulletList.append("<li>").append(attributeString).append("</li>");
                }

                bulletList.append("</ul></html>");

                attribute = new JLabel(bulletList.toString());
                attribute.setBounds(hx, labelY, hlabelWidth, hheight * changesList.size());
                jPanel2.add(attribute);

                labelY += (hheight + hlabelSpacing) * changesList.size();

                labelY += hlabelSpacing + 5;
            }
            if (labelY > 215) {
                jPanel2.setPreferredSize(new Dimension(jPanel2.getWidth(), labelY));

            }
        } catch (Exception v) {
            v.printStackTrace();
        }
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
        System.out.println("Refresh table is called in the edit issues class");
        try {
            DefaultTableModel tableModel = issuesDAO.getIssuesByTypeId(typeId, folderName);
            jTable1.setModel(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jTable1.revalidate();
        jTable1.repaint();
    }
    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
//        AddViewFrame view = new AddViewFrame();
//        view.setVisible(true);
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        // TODO add your handling code here:
        // AddNewIssue issue = new AddNewIssue();
        AddNewFolder folder = new AddNewFolder();
        if (jLabel12.getText() == "Add Issue") {
            issue.setVisible(true);
        } else if (jLabel12.getText() == "Add Folder") {
            folder.setVisible(true);
        }
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        jLabel12.setText("Add Folder");
        jLabel12.setEnabled(true);
        jPanel3.setVisible(false);
        jPanel2.setEnabled(false);
        //getUser();
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
                    String valueBetweenCommas = pathString.substring(firstCommaIndex + 1, lastCommaIndex).trim();
                }
            }
            if (path != null) {
                System.out.println("Path name "+path);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                int typeId = getTypeIdFromNode(node);

                if (typeId != -1) {

                    Object userObject = node.getUserObject();
                    if (userObject instanceof String) {
                        String nodeValue = (String) userObject;
                        int openingBracketIndex = nodeValue.lastIndexOf('[');
                        int closingBracketIndex = nodeValue.lastIndexOf(']');
                        if (openingBracketIndex != -1 && closingBracketIndex != -1) {
                            folderName = nodeValue.substring(0, openingBracketIndex).trim();
                        } else {
                            folderName = nodeValue.trim();
                            projectsDAO.setFolderName(folderName);
                        }
                        
                        try {
                            DefaultTableModel tableModel = null;
                            tableModel = issuesDAO.getIssuesByTypeId(typeId, folderName);
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
        //getUser();
        if (SwingUtilities.isRightMouseButton(evt)) {
            selectedRowIndex = jTable1.getSelectedRow();
            jTable1.setSelectionBackground(Color.YELLOW);
            jTable1.setSelectionForeground(Color.BLACK);
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem updateItem = new JMenuItem("Edit Attributes");
            JMenuItem insertItem = new JMenuItem("Add Issue");
            //JMenuItem editItem = new JMenuItem("Edit");
            ImageIcon insertIcon = new ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\img\\edit.png");
            ImageIcon addIssue = new ImageIcon("C:\\Users\\sajid.ali\\Desktop\\Webissues\\src\\img\\mail.png");
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
                    edit.setRowData(rowData, getColumnNames(tableModel));
                    edit.setDefaultCloseOperation(edit.DISPOSE_ON_CLOSE);
                    issue.setRowData(rowData, getColumnNames(tableModel));
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

//            editItem.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//
//                    System.out.println("Edit option selected");
//                }
//            });
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
                            issuesDAO.deleteIssue(issueId); 
                            refreshJTable();
                            jPanel2.removeAll();
                            jPanel2.repaint();
                            jPanel2.revalidate();
                        } else if (option == JOptionPane.NO_OPTION) {
                            System.out.println("User clicked NO.");
                        }
                        System.out.println("Delete option selected");
                    }
                });
                popupMenu.add(deleteItem);
            }
      
           // popupMenu.add(editItem);

            popupMenu.show(jTable1, evt.getX(), evt.getY());
        } else if (SwingUtilities.isLeftMouseButton(evt)) {
            jPanel2.removeAll();
            selectedRowIndex = jTable1.getSelectedRow();
            int rowCount = jTable1.getRowCount();
            JLabel totalRows = new JLabel(rowCount + " issues");
            totalRows.setBounds(1130, 687, 30, 8);
            totalRows.setHorizontalAlignment(SwingConstants.LEFT);
            jPanel1.add(totalRows);
            history();
            issueDetails();
            issueAttributes();
            buttonsCreation();
        }
        int selectedRowIndex = jTable1.getSelectedRow();
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        Object[] rowData = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            rowData[i] = tableModel.getValueAt(selectedRowIndex, i);

        }
        issue.setRowData(rowData, getColumnNames(tableModel));
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
        if (jLabel12.getText() == "Add Issue") {
        } else if (jLabel12.getText() == "Add Folder") {
        }

    }//GEN-LAST:event_jScrollPane3MouseClicked

    private void jScrollPane3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jScrollPane3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane3KeyPressed

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged
        // Calculate the change in mouseY (vertical movement) during dragging
  
    }//GEN-LAST:event_jPanel2MouseDragged

    private void jPanel2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseMoved
        // Save the initial heights of jScrollPane2 and jScrollPane3
        
    }//GEN-LAST:event_jPanel2MouseMoved

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
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
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
    private javax.swing.JPanel jPanel2;
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
    // End of variables declaration//GEN-END:variables
}
