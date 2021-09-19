import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GUI {
    private final Panel panel;

    public GUI() {
        JFrame frame = new JFrame("Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new Panel();
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        RecordMap map = null;

        try {
            map = new RecordMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        panel.setRecordMap(map);
        panel.addMouseMotionListener(new MapMouse(map));
        Loop loop = new Loop(panel);

    }
}
