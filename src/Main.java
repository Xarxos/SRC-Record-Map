import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        GameParser gameParser = new GameParser();
        //gameParser.parseGame("m1zjje26");
        //database.addGame("m1zjje26");
        InfoParser infoParser = new InfoParser();
        ArrayList<Achievement> achievements = null;
        try {
            achievements = new ArrayList<>(infoParser.parseAchievements().values());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Achievement achievement : achievements) {
            achievement.print();
        }
    }
}
