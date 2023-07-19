/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOImpl;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomLinkButtonExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Button Color Change Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();

        JButton button1 = createButton("Button 1", panel);
        JButton button2 = createButton("Button 2", panel);
        JButton button3 = createButton("Button 3", panel);
        JButton button4 = createButton("Button 4", panel);
        JButton button5 = createButton("Button 5", panel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static JButton createButton(String buttonText, JPanel panel) {
        JButton button = new JButton(buttonText);
        panel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton clickedButton = (JButton) e.getSource();
                String buttonName = clickedButton.getText();
                System.out.println("Clicked button: " + buttonName);
                clickedButton.setBackground(Color.RED);

                // Reset background color of other buttons
                for (Component component : panel.getComponents()) {
                    if (component instanceof JButton && component != clickedButton) {
                        JButton otherButton = (JButton) component;
                        otherButton.setBackground(null);
                    }
                }
            }
        });

        return button;
    }
}

