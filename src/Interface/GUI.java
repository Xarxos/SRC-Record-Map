import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GUI {
    private final Panel panel;
    private RecordBox recordBox;
    private JLabel nationLabel;

    public GUI(Database database) {
        JFrame frame = new JFrame("Map");
        this.recordBox = new RecordBox();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new Panel();
        nationLabel = new JLabel();
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        RecordMap map = null;

        try {
            map = new RecordMap(this, database);
        } catch (IOException e) {
            e.printStackTrace();
        }
        panel.setRecordMap(map);
        panel.addMouseMotionListener(new MapMouse(map, this));
        panel.addMouseListener(new MapMouseClicker(map, this));
        Loop loop = new Loop(panel);
        loop.execute();
    }

    public Panel getPanel() {
        return panel;
    }

    public void showRecordBox(Category category) {
        this.recordBox = new RecordBox();
    }

    public void hideRecordBox() {
        //this.recordBox.setVisible(false);
        //recordBox.cl
    }

    public RecordBox getRecordBox() {
        return recordBox;
    }

    public JLabel getNationLabel() {
        return nationLabel;
    }
}
