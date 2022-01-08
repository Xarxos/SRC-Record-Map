import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Nation {
    private String tag;
    private String name;
    private String achievement;
    private BufferedImage onMapImage;
    private String flagFilePath;
    private String achievementIconFilePath;
    private int dataColor;
    private int[] rgb = new int[3];
    private Point mapPos;
    private Point origin;
    private Database database;

    private Category category;

    public Nation(String tag) {
        this.tag = tag;
    }

    public Nation(String name, int r, int g, int b, int x, int y, Database database, String categoryId, String achievement) throws IOException {
        this.name = name;
        this.achievement = achievement;
        this.database = database;
        this.onMapImage = ImageIO.read(getClass().getResource("/images/nations/" + name + ".png"));
        this.flagFilePath = "/images/flags/" + name + ".png";
        if(achievement != null) {
            this.achievementIconFilePath = "/images/achievements/" + achievement + ".png";
        }

        r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        b = b & 0x000000FF; //Mask out anything not blue.
        this.dataColor = 0xFF000000 | r | g | b;

        this.mapPos = new Point(x,y);
        this.origin = new Point(mapPos.x + onMapImage.getWidth()/2, mapPos.y + onMapImage.getHeight()/2);

        this.category = database.getCategory(categoryId);
    }

    public void setScale(double scaleFactor) {
        this.mapPos = new Point((int)(this.mapPos.x * scaleFactor), (int)(this.mapPos.y * scaleFactor));
        this.origin = new Point((int)(this.origin.x * scaleFactor), (int)(this.origin.y * scaleFactor));
    }

    public BufferedImage getOnMapImage() {
        return onMapImage;
    }

    public void setOnMapImage(BufferedImage onMapImage) {
        this.onMapImage = onMapImage;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDataColor(int r, int g, int b) {
        this.rgb[0] = r;
        this.rgb[1] = g;
        this.rgb[2] = b;

        r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        b = b & 0x000000FF; //Mask out anything not blue.
        this.dataColor = 0xFF000000 | r | g | b;
    }

    public int getDataColor() {
        return dataColor;
    }

    public Point getMapPos() {
        return mapPos;
    }

    public Point getOrigin() {
        return origin;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getAchievement() {
        return achievement;
    }

    public String getFlagFilePath() {
        return flagFilePath;
    }

    public String getAchievementIconFilePath() {
        return achievementIconFilePath;
    }

    public void print() {
        System.out.println("Tag: " + tag);
        System.out.println("Name: " + name);
        System.out.println("RGB: " + rgb[0] + "-" + rgb[1] + "-" + rgb[2]);
    }
}
