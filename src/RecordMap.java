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

    public RecordMap() throws IOException {
        String img = "/testMap.png";
        mapImage = ImageIO.read(getClass().getResource(img));

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
    }

    public void printPixelColor(int x, int y) {
        if (x < mapImage.getWidth() && y < mapImage.getHeight()) {
            int rgb = mapImage.getRGB(x,y);
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;
            System.out.print("R: " + red);
            System.out.print(", G: " + green);
            System.out.println(", B: " + blue);
        }
    }

    public void draw(Graphics2D g, Panel panel) {
        g.drawImage(mapImage, 0, 0, panel);
    }
}
