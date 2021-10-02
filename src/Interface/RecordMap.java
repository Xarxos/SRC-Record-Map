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

        defineNation(new Nation("Algiers", 230, 225, 110, 2741, 779, database, "5d71eqvd", null));
        defineNation(new Nation("Andalusia", 240, 230, 155, 2664, 725, database, "r9g7p2k9", null));
        defineNation(new Nation("Aotearoa", 120, 16, 0, 5420, 1803, database, "5wkxn4q9", null));
        defineNation(new Nation("Bavaria", 17, 116, 193, 2977, 529, database, "owoje4y9", null));
        defineNation(new Nation("Bukhara", 255, 134, 0, 3727, 396, database, "owojeoj9", null));
        defineNation(new Nation("Dai Viet", 136, 117, 90, 4415, 1018, database, "kwjg1v7w", null));
        defineNation(new Nation("Delhi", 157, 200, 42, 3956, 883, database, "kwjg467w", null));
        defineNation(new Nation("England", 193, 26, 14, 2713, 403, database, "owoj37v9", null));
        defineNation(new Nation("Franconia", 89, 128, 68, 2941, 512, database, "rw61mrr9", null));
        defineNation(new Nation("Georgia", 190, 35, 37, 3424, 652, database, "o9xg361d", null));
        defineNation(new Nation("Golden Horde", 250, 182, 3, 3297, 329, database, "02q4le7d", "Gold Rush"));
        defineNation(new Nation("Hawai'i", 180, 215, 145, 265, 986, database, "r9glj6kw", null));
        defineNation(new Nation("Holy Roman Empire", 150, 177, 161, 2885, 412, database, "vdoo9wyd", "A Kaiser Not Just In Name"));
        defineNation(new Nation("Inca", 125, 92, 110, 1535, 1152, database, "o9xx5509", null));
        defineNation(new Nation("Iroquois", 87, 182, 80, 1508, 422, database, "4952rpj9", null));
        defineNation(new Nation("Japan", 194, 53, 60, 4812, 693, database, "n93mr0ed", null));
        defineNation(new Nation("Jerusalem", 211, 230, 211, 3314, 802, database, "n931m22d", null));
        defineNation(new Nation("Lanfang", 43, 165, 157, 4490, 1333, database, "5d712ged", null));
        defineNation(new Nation("Lan Xang", 210, 46, 28, 4367, 1027, database, "5d71z26d", null));
        defineNation(new Nation("Lotharingia", 95, 106, 207, 2826, 462, database, "xd41p1rd", null));
        defineNation(new Nation("Mamluks", 188, 166, 93, 3178, 867, database, "kwjgv21w", null));
        defineNation(new Nation("Malaya", 103, 14, 211, 4291, 1282, database, "ewpnge49", "The Spice Must Flow"));
        defineNation(new Nation("Maya", 94, 136, 191, 1155, 770, database, "5wk03rx9", null));
        defineNation(new Nation("Mongol Empire", 130, 180, 3593, 370, 403, database, "o9x6pl19", null));
        defineNation(new Nation("Morocco", 191, 110, 62, 2597, 797, database, "n931m8nd", null));
        defineNation(new Nation("Mughals", 33, 96, 48, 3885, 784, database, "jdzlvrr2", null));
        defineNation(new Nation("Nepal", 215, 12, 64, 4096, 910, database, "rw6mzv79", null));
        defineNation(new Nation("Orissa", 210, 106, 47, 4075, 1026, database, "z983megd", null));
        defineNation(new Nation("Persia", 62, 129, 20, 3495, 729, database, "ldyqzrkw", null));
        defineNation(new Nation("Pomerania", 61, 136, 66, 3000, 428, database, "z9844llw", null));
        defineNation(new Nation("Poland", 197, 92, 106, 3049, 451, database, "rw61lzp9", null));
        defineNation(new Nation("Qing", 237, 152, 18, 4537, 404, database, "jdrr7wnd", "A Manchurian Candidate"));
        defineNation(new Nation("Roman Empire", 167, 10, 100, 2656, 528, database, "wdmwyg52", null));
        defineNation(new Nation("Romania", 21, 96, 178, 3135, 546, database, "ywe0evy9", null));
        defineNation(new Nation("Rûm", 30, 160, 203, 3195, 677, database, "ewp3oqld", null));
        defineNation(new Nation("Russia", 96, 131, 80, 3232, 42, database, "ewpoekld", "All Belongs To Mother Russia"));
        defineNation(new Nation("Saxony", 155, 147, 180, 2963, 485, database, "rdn7qrq9", null));
        defineNation(new Nation("Scotland", 199, 175, 12, 2687, 341, database, "69z5neo9", null));
        defineNation(new Nation("Shan", 250, 240, 215, 4264, 946, database, "z983zkrd", null));
        defineNation(new Nation("Silesia", 84, 166, 93, 3041, 483, database, "4952rxj9", null));
        defineNation(new Nation("Switzerland", 153, 122, 108, 2896, 576, database, "owoj3r39", null));
        defineNation(new Nation("Tripoli", 25, 105, 90, 2965, 851, database, "kwjg84zw", null));
        defineNation(new Nation("Viti", 130, 180, 215, 5538, 1646, database, "rdn7l159", null));
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
