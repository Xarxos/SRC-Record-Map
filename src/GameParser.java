import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class GameParser {
    private String baseFilePath = "C:\\Users\\ludvi\\IdeaProjects\\PdxSaveMove\\src\\RawData\\";

    private String baseURL = "https://www.speedrun.com/api/v1/";
    private String gameURL = "https://www.speedrun.com/api/v1/games/";
    private String categoryURL = "https://www.speedrun.com/api/v1/categories/";
    private String runURL = "https://www.speedrun.com/api/v1/runs/";
    private String userURL = "https://www.speedrun.com/api/v1/users/";
    private String variableURL = "https://www.speedrun.com/api/v1/variables/";

    private final int P_MAX = 200;

    private ArrayList<String> userIds = new ArrayList<>();

    public GameParser() {

    }

    public void parseGame(String gameId) {
        parse("games", gameId, null);

        parseRunsForGame(gameId);
        parseUsersFromRuns(gameId);
        parse("games", gameId, "levels");
        parse("games", gameId, "categories");
        parse("games", gameId, "variables");
    }

    public void parse(String type, String id, String link) {
        URL url = null;
        HttpsURLConnection con = null;

        try {
            String fileName;
            String urlNoOffset;
            if(link != null) {
                urlNoOffset = baseURL + type + "/" + id + "/" + link + "?max=" + Integer.toString(P_MAX) + "&offset=";
                url = new URL(urlNoOffset + 0);
                fileName = type + "/" + id + "_" + link;
            }
            else {
                urlNoOffset = baseURL + type + "/" + id + "?max=" + Integer.toString(P_MAX) + "&offset=";
                url = new URL( urlNoOffset+ 0);
                fileName = type + "/" + id;
            }

            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            writeToFile(con, fileName);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        con.disconnect();
    }

    public void parseRunsForGame(String gameId) {
        URL url = null;
        HttpsURLConnection con = null;

        try {
            String nextURL = baseURL + "runs?max=" + Integer.toString(P_MAX) + "&game=" + gameId;
            int page = 0;
            while(nextURL != null) {
                url = new URL(nextURL);
                String fileName = "games/" + gameId + "_runs_" + page;

                con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                nextURL = writeToFile(con, fileName);
                page++;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        con.disconnect();
    }

    private void parseUsersFromRuns(String gameId) {
        ArrayList<JSONArray> runArrays = new ArrayList<>();
        Integer i = 0;
        File runFile = null;
        while((runFile = new File(baseFilePath + "games\\" + gameId + "_runs_" + i.toString() + ".txt")).exists()) {
            try {
                runArrays.add((JSONArray) ((JSONObject) (new JSONParser().parse(new FileReader(baseFilePath + "games\\" + gameId + "_runs_" + i.toString() + ".txt")))).get("data"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            i++;
        }

        for(JSONArray runArray : runArrays) {
            for(Object run : runArray) {
                String runnerId = (String)((JSONObject)((JSONArray)((JSONObject)run).get("players")).get(0)).get("id");
                if(!userIds.contains(runnerId)) {
                    userIds.add(runnerId);
                    parse("users", runnerId, null);
                }

                String status = (String)((JSONObject)((JSONObject)run).get("status")).get("status");
                if(!status.equals("new")) {
                    String verifierId = (String)((JSONObject)((JSONObject)run).get("status")).get("examiner");
                    if(!userIds.contains(verifierId)) {
                        userIds.add(verifierId);
                        parse("users", verifierId, null);
                    }
                }
            }
        }
    }

    private String writeToFile(HttpsURLConnection con, String fileName) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        File gameFile = new File("src/RawData/" + fileName + ".txt");
        gameFile.createNewFile();
        FileWriter writer = new FileWriter("src/RawData/" + fileName + ".txt", true);

        File gameFile2 = new File("src/RawData/" + fileName + "_formatted.txt");
        gameFile2.createNewFile();
        FileWriter writer2 = new FileWriter("src/RawData/" + fileName + "_formatted.txt", true);

        String inputLine = in.readLine();

        writer.write(inputLine);
        writer2.write(formatString(inputLine));
        writer.close();
        writer2.close();

        JSONObject page = (JSONObject) new JSONParser().parse(inputLine);
        JSONObject pagination = (JSONObject)page.get("pagination");

        String nextURL = null;
        if(pagination != null && (long)pagination.get("size") == P_MAX) {
            JSONArray links = (JSONArray) pagination.get("links");
            nextURL = (String) ((JSONObject)links.get(links.size()-1)).get("uri");
        }

        return nextURL;
    }

    private String formatString(String str) {
        String formatStr = "";
        int tabCount = 0;
        for(int i = 0; i < str.length()-1; i++) {
            formatStr = formatStr.concat(str.substring(i,i+1));

            if(str.charAt(i+1) == '}') {
                formatStr = formatStr.concat("\n");
                tabCount--;
                for(int t = 0; t < tabCount; t++) {
                    formatStr = formatStr.concat("\t");
                }
            }
            else if(str.charAt(i) == ',') {
                formatStr = formatStr.concat("\n");
                for(int t = 0; t < tabCount; t++) {
                    formatStr = formatStr.concat("\t");
                }
            }
            else if(str.charAt(i) == '{') {
                formatStr = formatStr.concat("\n");
                tabCount++;
                for(int t = 0; t < tabCount; t++) {
                    formatStr = formatStr.concat("\t");
                }
            }
        }
        formatStr = formatStr.concat("}");
        return formatStr;
    }
}
