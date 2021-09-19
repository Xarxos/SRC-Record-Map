import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MapMouse implements MouseMotionListener {
    private RecordMap map;

    public MapMouse(RecordMap map) {
        this.map = map;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.println("X: " + e.getX() + ", Y: " + e.getY());
        map.printPixelColor(e.getX(), e.getY());
    }
}
