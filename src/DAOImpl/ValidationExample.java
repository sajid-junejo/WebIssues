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

public class ValidationExample extends JFrame {
    private JTextField textField;
    private JLabel resultLabel;

    public ValidationExample() {
        setTitle("Validation Example");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        textField = new JTextField(10);
        add(textField);

        JButton validateButton = new JButton("Validate");
        add(validateButton);

        resultLabel = new JLabel("");
        add(resultLabel);

        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textField.getText();
                try {
                    int number = Integer.parseInt(input);
                    if (number >= 1 && number <= 100) {
                        resultLabel.setText("Valid Number");
                    } else {
                        resultLabel.setText("Number out of range (1-100)");
                    }
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid input (not a number)");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ValidationExample().setVisible(true);
            }
        });
    }
}
