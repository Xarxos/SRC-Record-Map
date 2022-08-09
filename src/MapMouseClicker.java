import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapMouseClicker implements MouseListener {
    private RecordMap map;
    private GUI gui;

    public MapMouseClicker(RecordMap map, GUI gui) {
        this.map = map;
        this.gui = gui;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        map.click(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
