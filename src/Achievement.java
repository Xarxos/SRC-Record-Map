import ddsreader.DDSReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Achievement {
    private String id;
    private String name;
    private String desc;
    private BufferedImage icon;

    public Achievement(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setIcon(String iconFilePath) throws IOException {
        FileInputStream fis = new FileInputStream(iconFilePath);
        byte [] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();

        int [] pixels = DDSReader.read(buffer, DDSReader.ARGB, 0);
        int width = DDSReader.getWidth(buffer);
        int height = DDSReader.getHeight(buffer);
        this.icon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getIcon() {
        return icon;
    }

    public void print() {
        System.out.println("Id: " + this.id);
        System.out.println("Name: " + this.name);
        System.out.println("Description: " + this.desc);
        System.out.println("Icon: " + this.icon);
    }
}
