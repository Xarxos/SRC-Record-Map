import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecordBox extends JPanel {
    public class SubcategoryPanel extends JPanel {
        private JTable table;
        private RecordTableModel recordTableModel = new RecordTableModel();
    }

    private GUI gui;
    private Category category;

    private JTabbedPane recordPanel = new JTabbedPane();
    private JPanel categoryPanel;

    private ArrayList<SubcategoryPanel> subcategoryPanels = new ArrayList<>();


    private JLabel nation = new JLabel("Nation");
    private JLabel achievement = new JLabel("Achievement");

    public RecordBox(GUI gui) {
        this.gui = gui;
        this.setLayout(new BorderLayout());

        nation.setFont(new Font("Serif", Font.BOLD, 34));
        achievement.setFont(new Font("Serif", Font.BOLD, 34));

        categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(2,1));
        categoryPanel.add(nation);
        categoryPanel.add(achievement);

        this.add(categoryPanel, BorderLayout.EAST);
        this.add(recordPanel, BorderLayout.WEST);

        try {
            clearCategory();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Border border = BorderFactory.createLineBorder(Color.BLACK);
        nation.setBorder(border);
        achievement.setBorder(border);

        this.setVisible(true);
    }

    private SubcategoryPanel setupSubcategoryPanel() {
        SubcategoryPanel panel = new SubcategoryPanel();

        panel.table = new JTable(panel.recordTableModel);
        panel.table.setDefaultRenderer(String[].class, new RecordTableRenderer());
        panel.table.setRowHeight(65);

        panel.setLayout(new BorderLayout());

        panel.add(panel.table.getTableHeader(), BorderLayout.NORTH);
        panel.add(panel.table, BorderLayout.CENTER);

        return panel;
    }

    public void setCategory(Category category) throws IOException {
        clearCategory();
        this.category = category;
        if(category.getSubcategories().isEmpty()) {
            subcategoryPanels.add(setupSubcategoryPanel());
            addRunsToTable(0, null);
        }
        else {
            for(int i = 0; i < category.getSubcategories().size(); i++) {
                subcategoryPanels.add(setupSubcategoryPanel());
                addRunsToTable(i, category.getSubcategories().get(i));
            }
        }

    }

    public void clearCategory() throws IOException {
        this.category = null;
        subcategoryPanels.clear();
        recordPanel.removeAll();

        SubcategoryPanel panel1 = setupSubcategoryPanel();
        for(int i = 0; i < 3; i+=2) {
            Run noWR = new Run("-1", null);
            noWR.setTimingMethod(Run.TimingMethod.values()[i]);
            panel1.recordTableModel.addRun(noWR);
        }
        panel1.recordTableModel.setRowCount(2);
        panel1.recordTableModel.fireTableDataChanged();
        subcategoryPanels.add(panel1);

        recordPanel.addTab(" ", panel1);

        BufferedImage image = ImageIO.read(getClass().getResource("/images/flags/Blank.png"));
        nation.setIcon(new ImageIcon(image));

        image = ImageIO.read(getClass().getResource("/images/achievements/Blank.png"));
        achievement.setIcon(new ImageIcon(image));

        nation.setText("                              ");
        achievement.setText("                              ");
        pack();
    }

    private void addRunsToTable(int tabIndex, Variable.Value subCat) {
        SubcategoryPanel tab = subcategoryPanels.get(tabIndex);

        tab.recordTableModel.clearRuns();
        int numWRs = 0;

        if(subCat == null) {
            recordPanel.addTab(" ", tab);
        }
        else {
            recordPanel.addTab(subCat.getLabel(), tab);
        }

        for(int i = 0; i < 4; i++) {
            Run WR_Run = null;
            if(subCat == null) {
                WR_Run = category.getWR(" ", i);
            }
            else {
                WR_Run = category.getWR(subCat.getId(), i);
            }

            if(WR_Run != null) {
                numWRs++;
                tab.recordTableModel.addRun(WR_Run);
            }
        }

        tab.recordTableModel.setRowCount(numWRs);
        tab.recordTableModel.fireTableDataChanged();

        this.pack();
    }

    public void setNation(String name, String flagFilePath) throws IOException {
        BufferedImage image = ImageIO.read(getClass().getResource(flagFilePath));
        nation.setIcon(new ImageIcon(image));
        nation.setText(name);
        pack();
    }

    public void setAchievement(String name, String iconFilePath) throws IOException {
        if(name != null) {
            BufferedImage image = ImageIO.read(getClass().getResource(iconFilePath));
            achievement.setIcon(new ImageIcon(image));
            achievement.setText(name);
            pack();
        }
    }

    public void pack() {
        int biggerX = nation.getPreferredSize().width > achievement.getPreferredSize().width ? nation.getPreferredSize().width : achievement.getPreferredSize().width;
        int biggerY = nation.getPreferredSize().height + achievement.getPreferredSize().height;
        //categoryPanel.setPreferredSize(new Dimension(biggerX, biggerY));
        categoryPanel.setMaximumSize(new Dimension(biggerX, biggerY));


        //table.setPreferredSize(new Dimension(table.getPreferredSize().width, table.getRowCount() * table.getRowHeight()));

        for(SubcategoryPanel subCatPanel : subcategoryPanels) {
            int tableWidth = subCatPanel.table.getPreferredSize().width;
            biggerX = biggerX < tableWidth ? tableWidth : biggerX;
            int tableHeight = subCatPanel.table.getRowCount() * subCatPanel.table.getRowHeight();
            biggerY = biggerY < tableHeight ? tableHeight : biggerY;
        }
        recordPanel.setMaximumSize(new Dimension(biggerX, biggerY));

        biggerX = categoryPanel.getMaximumSize().width + recordPanel.getMaximumSize().width;
        biggerY = categoryPanel.getMaximumSize().height > recordPanel.getMaximumSize().height ? categoryPanel.getMaximumSize().width : recordPanel.getMaximumSize().width;
        this.setMaximumSize(new Dimension(biggerX, biggerY));
        gui.getFrame().pack();
        //gui.getFrame().setPreferredSize(new Dimension(gui.getFrame().getPreferredSize().width, gui.getFrame().getPreferredSize().height + 20));
    }

    private BufferedImage scaleImage(BufferedImage image, double scaleFactor) {
        int w = image.getWidth();
        int h = image.getHeight();
        //System.out.println(w + " - " + h);
        BufferedImage scaledSprite = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scaleFactor, scaleFactor);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        scaledSprite = scaleOp.filter(image, scaledSprite);
        //System.out.println(scaledSprite.getWidth() + " - " + scaledSprite.getHeight());

        return scaledSprite;
    }

    public JLabel getNation() {
        return nation;
    }

    public JLabel getAchievement() {
        return achievement;
    }
}
