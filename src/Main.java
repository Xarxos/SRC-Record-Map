import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        /*

        database.addGame("m1zjje26");

        SwingUtilities.invokeLater(() -> {
            GUI gui=new GUI(database);
        });


        */
        Database database = new Database();
        GameParser gameParser = new GameParser(database);
        gameParser.parseGame("m1zjje26");



        // parsing file "JSONExample.json"




    }
}
