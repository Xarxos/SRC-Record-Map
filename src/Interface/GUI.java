import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GUI {
    private final Panel panel;
    private RecordBox recordBox;
    private JFrame frame;

    public GUI(Database database) {
        frame = new JFrame("Map");
        this.recordBox = new RecordBox(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        panel = new Panel();

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new BorderLayout());
        mapPanel.add(panel, BorderLayout.SOUTH);
        mapPanel.add(recordBox, BorderLayout.WEST);

        tabbedPane.addTab("Map", mapPanel);

        frame.setLayout(new BorderLayout());
        frame.add(tabbedPane, BorderLayout.CENTER);
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

    public RecordBox getRecordBox() {
        return recordBox;
    }

    public JFrame getFrame() {
        return frame;
    }
}
