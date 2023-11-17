/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class WordCount {

    public static void main(String[] args) {
        
        String filePath = JOptionPane.showInputDialog("Enter the path of the file:");
        String name = "In this example, the x-coordinate (x) is calculated by subtracting half of the label width (clabelWidth / 2) from the fixed position (fixedX). This ensures that the label is centered at the fixed position.";
        // Read the file and count words
        System.out.println("Length : "+name.length());
        int wordCount = countWords(filePath);

        
        JOptionPane.showMessageDialog(null, "Word count in the file: " + wordCount);
    }

    private static int countWords(String filePath) {
        int wordCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                wordCount += words.length;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return wordCount;
    }
}

