/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImageLinkExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Image Link Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 700);

        // Create a JButton with HTML content for the clickable link
        JButton linkButton = new JButton("<html><a href='#'>Click to view image</a></html>");
        linkButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Create a separate panel to display the image
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());

        // Add an ActionListener to the linkButton to handle clicks
        linkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Load and display the image in the imagePanel
                ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/img/preferences.png"));
                JLabel imageLabel = new JLabel(imageIcon);
                imagePanel.removeAll();
                imagePanel.add(imageLabel, BorderLayout.CENTER);
                imagePanel.revalidate();
                imagePanel.repaint();
            }
        });

        frame.getContentPane().add(linkButton, BorderLayout.NORTH);
        frame.getContentPane().add(imagePanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}

