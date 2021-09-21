import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.util.ArrayList;

public class RecordMap {
    private BufferedImage mapImage;
    private BufferedImage topMap;
    private ArrayList<Point> england = new ArrayList<>();
    private boolean hoverEngland = false;
    private int lastRGB = 0;

    public RecordMap() throws IOException {
        String img = "/testMap2.png";
        mapImage = ImageIO.read(getClass().getResource(img));
        topMap = ImageIO.read(getClass().getResource(img));

        double scaleFactorY = 1;
        double scaleFactorX = 1;

        int w = mapImage.getWidth();
        int h = mapImage.getHeight();
        BufferedImage scaledSprite = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scaleFactorX, scaleFactorY);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        scaledSprite = scaleOp.filter(mapImage, scaledSprite);
        mapImage = scaledSprite;

        storeEngland();
        //highlightEngland(true);
    }

    public void printPixelColor(int x, int y) {
        if (x < mapImage.getWidth() && y < mapImage.getHeight()) {
            int rgb = mapImage.getRGB(x,y);
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;
            System.out.println("RGB: " + rgb);
            System.out.print("R: " + red);
            System.out.print(", G: " + green);
            System.out.println(", B: " + blue);
        }
    }

    private void storeEngland() {
        for (int x = 0; x < mapImage.getWidth(); x++) {
            for (int y = 0; y < mapImage.getHeight(); y++) {
                if (mapImage.getRGB(x,y) == -4122098) {
                    england.add(new Point(x,y));
                }
            }
        }
    }

    public void hover(int x, int y) {
        if (x < mapImage.getWidth() && y < mapImage.getHeight()) {
            int rgb = mapImage.getRGB(x,y);
            if (rgb != lastRGB) {
                if (rgb == -4122098) {
                    System.out.println("ENGLAND!");
                    highlightEngland(true);
                } else if (lastRGB == -4122098) {
                    System.out.println("ENGLANDN'T!");
                    //highlightEngland(false);
                }
                lastRGB = rgb;
            }
        }
    }

    public void highlightEngland(boolean highlight) {
        System.out.println("1!");
        int rgb = -4122098;
        if (highlight) {
            System.out.println("2!");
            rgb = -3690740;
        }
        for (Point p : england) {
            topMap.setRGB(p.x, p.y, rgb);
        }
        System.out.println("3!");
    }

    private void makePolygon(int startX, int startY) {
        for (int x = 0; x < mapImage.getWidth(); x++) {
            for (int y = 0; y < mapImage.getHeight(); y++) {
                if (mapImage.getRGB(x,y) == -4122098) {

                }
            }
        }
    }

    private void traceOutline() {

    }

    private void checkNeighbors(int currentX, int currentY, int targetColor, ArrayList<Point> vertices) {
        for (int x = currentX-1; x <= currentX+1; x++) {
            for (int y = currentY-1; y <= currentY+1; y++) {
                if (mapImage.getRGB(x,y) != targetColor) {
                    vertices.
                }
            }
        }
    }

    public void draw(Graphics2D g, Panel panel) {
        g.drawImage(topMap, 0, 0, panel);
    }
}
