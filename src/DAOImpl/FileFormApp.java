/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOImpl;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileFormApp {
    private JFrame frame;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JLabel filePromptLabel;
    private JButton submitButton;
    private JButton cancelButton;
    private JFileChooser fileChooser;

    private String mode;
    private int issueId;
    private String issueName;
    private int fileId;
    private String initialName;
    private String initialDescription;

    public FileFormApp(String mode, int issueId, String issueName, int fileId, String initialName, String initialDescription) {
        this.mode = mode;
        this.issueId = issueId;
        this.issueName = issueName;
        this.fileId = fileId;
        this.initialName = initialName;
        this.initialDescription = initialDescription;

        frame = new JFrame();
        frame.setTitle(mode.equals("add") ? "Attach File" : "Edit File");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("File Name:");
        nameField = new JTextField(initialName);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(initialDescription);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);

        JLabel fileLabel = new JLabel("File:");
        filePromptLabel = new JLabel("Drag file here");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(descriptionLabel);
        formPanel.add(descriptionScrollPane);

        if (mode.equals("add")) {
            formPanel.add(fileLabel);
            formPanel.add(filePromptLabel);
        }

        frame.add(formPanel, BorderLayout.CENTER);

        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (mode.equals("add")) {
            filePromptLabel.setTransferHandler(new FileTransferHandler());
        }

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String description = descriptionArea.getText(); 
                returnToDetails();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle cancel button click
                returnToDetails();
            }
        });

        frame.setVisible(true);
    }

    private void returnToDetails() {
        // Implement navigation logic to return to issue details
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileFormApp("add", 1, "Issue 1", 0, "", "");
            }
        });
    }

    // Implement a custom TransferHandler for handling file drops
    private class FileTransferHandler extends TransferHandler {
        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            Transferable transferable = support.getTransferable();
            try {
                java.util.List<File> files = (java.util.List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                if (!files.isEmpty()) {
                    File selectedFile = files.get(0);
                    fileChooser.setSelectedFile(selectedFile);
                    filePromptLabel.setText(selectedFile.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
    }
    
}

