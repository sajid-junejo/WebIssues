package DAOImpl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ResizablePanel extends JPanel implements MouseListener, MouseMotionListener {

    private static final int RESIZE_MARGIN = 10; // Margin size for resizing area

    private boolean resizing = false;
    private int resizeDirection = 0; // 1 for increase, -1 for decrease
    private Point dragStart;

    public ResizablePanel() {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStart = e.getPoint();
        resizing = isResizingArea(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        resizing = false;
        resizeDirection = 0;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (resizing) {
            int dy = e.getPoint().y - dragStart.y;
            int newHeight = getHeight() + (resizeDirection * dy);

            if (newHeight >= getMinimumSize().height) {
                setPreferredSize(new Dimension(getWidth(), newHeight));
                revalidate();
                repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isResizingArea(e.getPoint())) {
            Cursor resizeCursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
            setCursor(resizeCursor);
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private boolean isResizingArea(Point point) {
        return point.y >= getHeight() - RESIZE_MARGIN;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // No need to define mouseDragged(MouseEvent) again, as it's already implemented above

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ResizablePanel panel1 = new ResizablePanel();
            panel1.setPreferredSize(new Dimension(300, 200));
            panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            ResizablePanel panel2 = new ResizablePanel();
            panel2.setPreferredSize(new Dimension(300, 200));
            panel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Create a container to hold the two resizable panels
            JPanel container = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 0.5; // Both panels get equal initial width
            gbc.weighty = 1.0;

            container.add(panel1, gbc);
            gbc.weightx = 0.5; // Reset weightx for the second panel
            container.add(panel2, gbc);

            JFrame frame = new JFrame("Resizable Panels Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(container);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
