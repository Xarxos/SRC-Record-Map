import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecordBox extends JPanel {
    private GUI gui;
    private RecordMap map;
    private Category category;

    private JTable table;
    private RecordTableModel recordTableModel = new RecordTableModel();

    private JPanel recordPanel;
    private JPanel categoryPanel;

    private JLabel nation = new JLabel("Nation");
    private JLabel achievement = new JLabel("Achievement");

    public RecordBox(GUI gui) {
        this.gui = gui;
        //this.map = map;
        //this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        table = new JTable(recordTableModel);
        table.setDefaultRenderer(String[].class, new RecordTableRenderer());
        table.setRowHeight(65);

        //this.setPreferredSize(new Dimension(1,1));
        //this.setLayout(new WrapLayout());
        //panel = new JPanel();
        /*
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

         */
        //scrollPane = new JScrollPane(table);
        //scrollPane.setPreferredSize(table.getPreferredSize());

        this.setLayout(new BorderLayout());

        recordPanel = new JPanel();
        recordPanel.setLayout(new BorderLayout());
        recordPanel.add(table.getTableHeader(), BorderLayout.NORTH);
        recordPanel.add(table, BorderLayout.CENTER);
/*
        JPanel flagPanel = new JPanel();
        JPanel nationPanel = new JPanel();
        nationPanel.setLayout(new BorderLayout());
        nationPanel.add(flagPanel, BorderLayout.WEST);
        nationPanel.add(nation, BorderLayout.EAST);

        JPanel iconPanel = new JPanel();
        JPanel achievementPanel = new JPanel();
        achievementPanel.setLayout(new BorderLayout());
        achievementPanel.add(iconPanel, BorderLayout.WEST);
        achievementPanel.add(achievement, BorderLayout.EAST);

 */
        nation.setFont(new Font("Serif", Font.BOLD, 34));
        achievement.setFont(new Font("Serif", Font.BOLD, 34));
        /*
        try {
            nation.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/England.png"))));
            achievement.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/Scotland.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

        categoryPanel = new JPanel();
        categoryPanel.setLayout(new BorderLayout());
        categoryPanel.add(nation, BorderLayout.NORTH);
        categoryPanel.add(achievement, BorderLayout.SOUTH);

        this.add(categoryPanel, BorderLayout.EAST);
        this.add(recordPanel, BorderLayout.WEST);

        //recordPanel.getPreferredSize().height + table.getTableHeader().getPreferredSize().height

        try {
            clearCategory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pack();

        //this.setMinimumSize(new Dimension(250,100));
        this.setVisible(true);
    }

    public void setCategory(Category category) {
        this.category = category;
        //this.setTitle(category.getName());
        addRunsToTable();
        //changeText();
    }

    public void clearCategory() throws IOException {
        this.category = null;
        recordTableModel.clearRuns();

        for(int i = 0; i < 4; i++) {
            Run noWR = new Run("-1", null);
            noWR.setTimingMethod(Run.TimingMethod.values()[i]);
            recordTableModel.addRun(noWR);
        }

        recordTableModel.fireTableDataChanged();

        nation.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/flags/Blank.png"))));
        achievement.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/achievements/Blank.png"))));
        nation.setText("--------------------");
        achievement.setText("--------------------");
        pack();
    }

    private void addRunsToTable() {
        recordTableModel.clearRuns();
        int numWRs = 0;

        for(int i = 0; i < 4; i++) {
            Run WR_Run = category.getWR(i);

            if(WR_Run != null) {
                numWRs++;
                recordTableModel.addRun(WR_Run);
            }
        }
        //recordTableModel.setRowCount(numWRs);

        recordTableModel.fireTableDataChanged();
        //table.setPreferredSize(new Dimension(table.getPreferredSize().width, table.getRowCount() * table.getRowHeight()));
        //scrollPane.setPreferredSize(new Dimension(table.getPreferredSize().width, table.getPreferredSize().height + table.getTableHeader().getPreferredSize().height));
        //this.setPreferredSize(new Dimension(table.getPreferredSize().width, table.getPreferredSize().height + table.getTableHeader().getPreferredSize().height));
        //this.pack();
    }

    private void changeText() {
        Run[] WR_Runs = new Run[4];
        String[] WR_Texts = new String[4];
        String finalText = "";

        for(int i = 0; i < 4; i++) {
            WR_Runs[i] = category.getWR(i);
            //System.out.println("WR: " + category.getWR(i));

            if(WR_Runs[i] != null) {
                int time[] = WR_Runs[i].getTime();
                String rta = time[0] + "h / " + time[1] + "m / " + time[2] + "s / " + time[3] + "ms";
                String igt = time[0] + "y / " + time[1] + "m / " + time[2] + "d";

                switch(i) {
                    case 0:
                        WR_Texts[i] = "RTA NS5: " + rta;
                        //System.out.println("RTA NS5: " + rta);
                        break;
                    case 1:
                        WR_Texts[i] = "RTA WS5: " + rta;
                        //System.out.println("RTA WS5: " + rta);
                        break;
                    case 2:
                        WR_Texts[i] = "IGT NSS: " + igt;
                        //System.out.println("IGT NSS: " + igt);
                        break;
                    case 3:
                        WR_Texts[i] = "IGT WSS: " + igt;
                        //System.out.println("IGT WSS: " + igt);
                }
                WR_Texts[i] += "\n" + WR_Runs[i].getRunner().getName();
                finalText += WR_Texts[i] + "\n";
            }
        }
        //System.out.println("Final Text: " + finalText);
        //textArea.setText(finalText);
        //this.pack();
    }

    public void setNation(String name, String flagFilePath) throws IOException {
        nation.setIcon(new ImageIcon(ImageIO.read(getClass().getResource(flagFilePath))));
        nation.setText(name);
        pack();
    }

    public void setAchievement(String name, String iconFilePath) throws IOException {
        if(name != null) {
            achievement.setIcon(new ImageIcon(ImageIO.read(getClass().getResource(iconFilePath))));
            achievement.setText(name);
            pack();
        }
    }

    public void pack() {
        //nation.setPreferredSize(new Dimension(nation.getPreferredSize().width, nation.getIcon().getIconHeight()));
        //achievement.setPreferredSize(new Dimension(achievement.getPreferredSize().width, achievement.getIcon().getIconHeight()));

        int biggerX = nation.getPreferredSize().width > achievement.getPreferredSize().width ? nation.getPreferredSize().width : achievement.getPreferredSize().width;
        int biggerY = nation.getPreferredSize().height + achievement.getPreferredSize().height;
        categoryPanel.setPreferredSize(new Dimension(biggerX, biggerY));


        //table.setPreferredSize(new Dimension(table.getPreferredSize().width, table.getRowCount() * table.getRowHeight()));

        biggerX = table.getPreferredSize().width;
        biggerY = table.getPreferredSize().height + table.getTableHeader().getPreferredSize().height;
        recordPanel.setPreferredSize(new Dimension(biggerX, biggerY));

        biggerX = categoryPanel.getPreferredSize().width + recordPanel.getPreferredSize().width;
        biggerY = categoryPanel.getPreferredSize().height > recordPanel.getPreferredSize().height ? categoryPanel.getPreferredSize().width : recordPanel.getPreferredSize().width;
        this.setPreferredSize(new Dimension(biggerX, biggerY));
        gui.getFrame().pack();
        //gui.getFrame().setPreferredSize(new Dimension(gui.getFrame().getPreferredSize().width, gui.getFrame().getPreferredSize().height + 20));
    }
}
