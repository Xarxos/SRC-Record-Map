import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecordMap {
    private GUI gui;

    private BufferedImage dataMap;
    private BufferedImage prettyMap;
    private JPanel nationLabelPanel;
    private JLabel nationLabel;
    private Map<Integer, Nation> nations = new HashMap<>();
    private Nation drawNation;
    private final double scaleFactor = 0.341;
    private boolean nationClicked = false;

    public RecordMap(GUI gui, Database database) throws IOException {
        this.gui = gui;
        dataMap = ImageIO.read(getClass().getResource("/images/dataMap.png"));
        prettyMap = ImageIO.read(getClass().getResource("/images/prettyMap.png"));

        dataMap = scaleImage(dataMap);
        prettyMap = scaleImage(prettyMap);

        nationLabelPanel = new JPanel();
        nationLabelPanel.setLayout(new BorderLayout());
        nationLabel = new JLabel();
        nationLabelPanel.add(nationLabel, BorderLayout.CENTER);
        //nationLabelPanel.pack();
        nationLabelPanel.setVisible(false);

        defineNation(new Nation("England", 193, 26, 14, 2713, 403, database, "owoj37v9", null));
        defineNation(new Nation("Scotland", 199, 175, 12, 2687, 341, database, "69z5neo9", null));
        defineNation(new Nation("Morocco", 191, 110, 62, 2597, 797, database, "n931m8nd", null));
        defineNation(new Nation("Poland", 197, 92, 106, 3049, 451, database, "rw61lzp9", null));
        defineNation(new Nation("Saxony", 155, 147, 180, 2963, 485, database, "rdn7qrq9", null));
        defineNation(new Nation("Qing", 237, 152, 18, 4537, 404, database, "jdrr7wnd", "A Manchurian Candidate"));
    }

    private BufferedImage scaleImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage scaledSprite = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scaleFactor, scaleFactor);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        scaledSprite = scaleOp.filter(image, scaledSprite);

        return scaledSprite;
    }

    private void defineNation(Nation nation) throws IOException {
        nation.setOnMapImage(scaleImage(nation.getOnMapImage()));
        nation.setScale(scaleFactor);
        nations.put(nation.getDataColor(), nation);
    }

    public void printPixelColor(int x, int y) {
        if (x < dataMap.getWidth() && y < dataMap.getHeight()) {
            int rgb = dataMap.getRGB(x,y);
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;
            System.out.println("RGB: " + rgb);
            System.out.print("R: " + red);
            System.out.print(", G: " + green);
            System.out.println(", B: " + blue);
        }
    }

    public void hover(int x, int y) {
        if (x < dataMap.getWidth() && y < dataMap.getHeight()) {
            int rgb = dataMap.getRGB(x,y);
            if(!nationClicked) {
                if (rgb != -16777216) {
                    Nation hoverNation = nations.get(rgb);
                    if (hoverNation != drawNation && hoverNation != null) {
                        drawNation = hoverNation;
                        try {
                            gui.getRecordBox().setCategory(drawNation.getCategory());
                            gui.getRecordBox().setNation(drawNation.getName(), drawNation.getFlagFilePath());
                            gui.getRecordBox().setAchievement(drawNation.getAchievement(), drawNation.getAchievementIconFilePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    drawNation = null;
                    try {
                        gui.getRecordBox().clearCategory();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    nationLabelPanel.setVisible(false);
                }
            }

        }
    }

    public void click(int x, int y) {
        int rgb = dataMap.getRGB(x,y);
        if(!nationClicked) {
            nationClicked = true;
            if(rgb != -16777216) {
                try {
                    gui.getRecordBox().setCategory(drawNation.getCategory());
                    gui.getRecordBox().setNation(drawNation.getName(), drawNation.getFlagFilePath());
                    gui.getRecordBox().setAchievement(drawNation.getAchievement(), drawNation.getAchievementIconFilePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            try {
                gui.getRecordBox().clearCategory();
            } catch (IOException e) {
                e.printStackTrace();
            }
            nationClicked = false;
            drawNation = null;
        }
    }

    private void makePolygon(int startX, int startY) {
        for (int x = 0; x < dataMap.getWidth(); x++) {
            for (int y = 0; y < dataMap.getHeight(); y++) {
                if (dataMap.getRGB(x,y) == -4122098) {

                }
            }
        }
    }

    private void checkNeighbors(int currentX, int currentY, int targetColor, ArrayList<Point> vertices) {
        for (int x = currentX-1; x <= currentX+1; x++) {
            for (int y = currentY-1; y <= currentY+1; y++) {
                if (dataMap.getRGB(x,y) != targetColor) {

                }
            }
        }
    }

    public void draw(Graphics2D g, Panel panel) {
        g.drawImage(prettyMap, 0, 0, panel);
        if (drawNation != null) {
            //System.out.println("Not Null");
            g.drawImage(drawNation.getOnMapImage(), drawNation.getMapPos().x, drawNation.getMapPos().y, panel);
            //g.fill3DRect(drawNation.getMapPos().x, drawNation.getOrigin().y, 25, 10, false);
            //g.drawString(drawNation.getName(), drawNation.getMapPos().x, drawNation.getOrigin().y);
        }

        //System.out.println("Not null");
    }

    public boolean isNationClicked() {
        return nationClicked;
    }

    public void setNationClicked(boolean nationClicked) {
        this.nationClicked = nationClicked;
    }
}
