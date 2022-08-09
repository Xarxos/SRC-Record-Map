import javax.swing.*;
import java.awt.*;

/**
 * The panel where the levels and gameplay is drawn.
 */
public class Panel extends JPanel {

    private RecordMap map;

    /**
     * Empty constructor.
     */
    public Panel() {

    }

    public void setRecordMap(RecordMap map) {
        this.map = map;
    }

    /**
     * Get the preferred size of the game panel.
     * @return  The preferred size of the game panel as a Dimension object.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1920, 698);
    }

    /**
     * Handle painting to the canvas.
     * @param g The graphics object that does the painting.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Draw everything that should be drawn from the local game data.
     * @param g The graphics object that does the drawing.
     */
    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        map.draw(g2d, this);
    }

    /**
     * Trigger a repaint of the canvas.
     */
    public void peent() {
        repaint();
    }
}
