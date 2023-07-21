package DAOImpl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResizableContainerExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Draggable and Resizable Container Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create two custom resizable panels
        ResizablePanel resizablePanel1 = new ResizablePanel();
        ResizablePanel resizablePanel2 = new ResizablePanel();

        // Set their preferred sizes
        resizablePanel1.setPreferredSize(new Dimension(300, 200));
        resizablePanel2.setPreferredSize(new Dimension(300, 200));

        // Create components for the first resizable panel
        JButton button1 = new JButton("Click Me!");
        button1.setBounds(10, 10, 100, 30);

        JLabel label1 = new JLabel("Enter your name:");
        label1.setBounds(10, 50, 100, 30);

        JTextField textField1 = new JTextField(20);
        textField1.setBounds(120, 50, 150, 30);

        // Add components to the first resizable panel
        resizablePanel1.add(button1);
        resizablePanel1.add(label1);
        resizablePanel1.add(textField1);

        // Create components for the second resizable panel
        JButton button2 = new JButton("Button 2");
        button2.setBounds(10, 10, 100, 30);

        JLabel label2 = new JLabel("Label 2");
        label2.setBounds(10, 50, 100, 30);

        JTextField textField2 = new JTextField(20);
        textField2.setBounds(120, 50, 150, 30);

        // Add components to the second resizable panel
        resizablePanel2.add(button2);
        resizablePanel2.add(label2);
        resizablePanel2.add(textField2);

        // Create a container panel to hold the two resizable panels
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new FlowLayout());
        containerPanel.add(resizablePanel1);
        containerPanel.add(resizablePanel2);

        frame.add(containerPanel);
        frame.pack();
        frame.setVisible(true);
    }
}

class ResizablePanel extends JPanel {
    private Point initialClick;
    private boolean isResizing;

    public ResizablePanel() {
        super();
        setLayout(null); // Custom layout manager to control component placement

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                isResizing = contains(e.getX(), e.getY(), getWidth(), getHeight(), 8); // 8-pixel resize margin
            }

            public void mouseReleased(MouseEvent e) {
                isResizing = false;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getX() - initialClick.x;
                int deltaY = e.getY() - initialClick.y;

                if (isResizing) {
                    int newWidth = Math.max(getWidth() + deltaX, getMinimumSize().width);
                    int newHeight = Math.max(getHeight() + deltaY, getMinimumSize().height);
                    setSize(newWidth, newHeight);
                    revalidate();
                    repaint();
                } else {
                    setLocation(getLocation().x + deltaX, getLocation().y + deltaY);
                }
            }
        });
    }

    private boolean contains(int x, int y, int width, int height, int margin) {
        return x >= width - margin && x <= width + margin && y >= height - margin && y <= height + margin;
    }
}
