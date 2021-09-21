import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MapMouse implements MouseMotionListener {
    private RecordMap map;
    private GUI gui;

    public MapMouse(RecordMap map, GUI gui) {
        this.map = map;
        this.gui = gui;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.println("X: " + e.getX() + ", Y: " + e.getY());
        //map.printPixelColor(e.getX(), e.getY());
        map.hover(e.getX(), e.getY());
    }
}
