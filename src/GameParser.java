import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class GameParser {
    private String gameURL = "https://www.speedrun.com/api/v1/games/";
    private String categoryURL = "https://www.speedrun.com/api/v1/categories/";
    private String runURL = "https://www.speedrun.com/api/v1/runs/";
    private String userURL = "https://www.speedrun.com/api/v1/users/";
    private String variableURL = "https://www.speedrun.com/api/v1/variables/";

    private Database database;


    public GameParser(Database database) {
        this.database = database;
    }

    public void parseGame(String gameId) {
        parse(gameId, null);
        parse(gameId, "levels");
        parse(gameId, "categories");
        parse(gameId, "variables");
        //parse(gameId, "");
    }

    private void parse(String gameId, String link) {
        URL url = null;
        HttpsURLConnection con = null;

        try {
            String fileName;
            if(link != null) {
                url = new URL(gameURL + gameId + "/" + link);
                fileName = gameId + "_" + link;
            }
            else {
                url = new URL(gameURL + gameId);
                fileName = gameId;
            }

            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            writeToFile(con, fileName);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        con.disconnect();
    }

    private void writeToFile(HttpsURLConnection con, String fileName) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        File gameFile = new File("src/RawData/" + fileName + ".txt");
        gameFile.createNewFile();
        FileWriter writer = new FileWriter("src/RawData/" + fileName + ".txt");

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            inputLine = inputLine.substring(8);
            writer.write(inputLine);
        }
        in.close();
        writer.close();
    }


}
