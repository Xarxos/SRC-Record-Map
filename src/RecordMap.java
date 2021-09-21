import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecordMap {
    private BufferedImage dataMap;
    private BufferedImage prettyMap;
    private Map<Integer, Nation> nations = new HashMap<>();
    private Nation drawNation;

    public RecordMap() throws IOException {
        dataMap = ImageIO.read(getClass().getResource("/dataMap.png"));
        prettyMap = ImageIO.read(getClass().getResource("/prettyMap.png"));

        dataMap = scaleImage(dataMap);
        prettyMap = scaleImage(prettyMap);

        Nation england = new Nation("/England.png", 193, 26, 14, 2715, 405);
        england.setOnMapImage(scaleImage(england.getOnMapImage()));
        nations.put(england.getDataColor(), england);
    }

    private BufferedImage scaleImage(BufferedImage image) {
        double scaleFactorY = 0.25;
        double scaleFactorX = 0.25;

        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage scaledSprite = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scaleFactorX, scaleFactorY);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        scaledSprite = scaleOp.filter(image, scaledSprite);

        return scaledSprite;
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
            if (rgb != -16777216) {
                Nation hoverNation = nations.get(rgb);
                if (hoverNation != drawNation) {
                    drawNation = hoverNation;
                    //System.out.println("ENGLAND!");
                }
            }
            else {
                drawNation = null;
                //System.out.println("ENGLANDN'T!");
            }
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

    private void traceOutline() {

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
            g.drawImage(drawNation.getOnMapImage(), (int)(drawNation.getMapPos().x * 0.25), (int)(drawNation.getMapPos().y * 0.25), panel);
        }
        //System.out.println("Not null");
    }
}
