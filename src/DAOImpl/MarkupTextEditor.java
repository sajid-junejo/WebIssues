/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOImpl;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.StyledEditorKit.*;
import java.awt.*;

public class MarkupTextEditor extends JFrame {
    private JTextPane textPane;

    public MarkupTextEditor() {
        setTitle("Markup Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textPane = createMarkupTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);

        JToolBar toolBar = createToolBar();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JTextPane createMarkupTextPane() {
        JTextPane textPane = new JTextPane();
        StyledDocument styledDoc = textPane.getStyledDocument();

        // Set default style
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        styledDoc.setLogicalStyle(0, defaultStyle);

        return textPane;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();

        addActionToToolBar(toolBar, "Bold", new BoldAction());
        addActionToToolBar(toolBar, "Italic", new ItalicAction());
        // Add more buttons as needed...

        return toolBar;
    }

    private void addActionToToolBar(JToolBar toolBar, String text, Action action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        toolBar.add(button);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MarkupTextEditor());
    }
}
