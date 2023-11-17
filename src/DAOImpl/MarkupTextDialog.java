/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOImpl;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.StyledEditorKit.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MarkupTextDialog extends JDialog {
    private JTextPane textPane;

    public MarkupTextDialog(Frame parent) {
        super(parent, "Markup Text Editor", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        textPane = createMarkupTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);

        JToolBar toolBar = createToolBar();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            // Perform OK action here (you can get the content of the textPane)
            dispose(); // Close the dialog after OK
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            // Perform Cancel action here
            dispose(); // Close the dialog after Cancel
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 300);
        setLocationRelativeTo(parent);
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
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton openDialogButton = new JButton("Open Dialog");
            openDialogButton.addActionListener(e -> new MarkupTextDialog(frame));

            frame.getContentPane().setLayout(new FlowLayout());
            frame.getContentPane().add(openDialogButton);

            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
