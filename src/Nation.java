import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Nation {
    private BufferedImage onMapImage;
    private int dataColor;
    private Point mapPos;
    private Database database;

    private Category category;

    public Nation(String name, int r, int g, int b, int x, int y, Database database, String categoryId) throws IOException {
        this.database = database;
        this.onMapImage = ImageIO.read(getClass().getResource("/images/" + name + ".png"));

        r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        b = b & 0x000000FF; //Mask out anything not blue.
        this.dataColor = 0xFF000000 | r | g | b;

        this.mapPos = new Point(x,y);

        this.category = database.getCategory(categoryId);
    }

    public void setScale(double scaleFactor) {
        this.mapPos = new Point((int)(this.mapPos.x * scaleFactor), (int)(this.mapPos.y * scaleFactor));
    }

    public BufferedImage getOnMapImage() {
        return onMapImage;
    }

    public void setOnMapImage(BufferedImage onMapImage) {
        this.onMapImage = onMapImage;
    }

    public int getDataColor() {
        return dataColor;
    }

    public Point getMapPos() {
        return mapPos;
    }

    public Category getCategory() {
        return category;
    }
}
