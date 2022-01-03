import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class GameParser {
    private String baseURL = "https://www.speedrun.com/api/v1/";
    private String gameURL = "https://www.speedrun.com/api/v1/games/";
    private String categoryURL = "https://www.speedrun.com/api/v1/categories/";
    private String runURL = "https://www.speedrun.com/api/v1/runs/";
    private String userURL = "https://www.speedrun.com/api/v1/users/";
    private String variableURL = "https://www.speedrun.com/api/v1/variables/";

    private Database database;
    private final int P_MAX = 200;


    public GameParser(Database database) {
        this.database = database;
    }

    public void parseGame(String gameId) {
        parse("games", gameId, null);

        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader("C:\\Users\\ludvi\\IdeaProjects\\PdxSaveMove\\src\\RawData\\games\\" + gameId + ".txt"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        JSONObject jo = (JSONObject) obj;
        JSONObject data = (JSONObject) jo.get("data");

        Map mods = (Map)data.get("moderators");
        for (Object modId : mods.keySet()) {
            parse("users", (String)modId, null);
        }

        parseRunsForGame(gameId);
        parse("games", gameId, "levels");
        parse("games", gameId, "categories");
        parse("games", gameId, "variables");
    }

    //public void parseUser()

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

            //writeToFile(con, fileName, urlNoOffset, 0, 0);
            writeToFile2(con, fileName);

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

                //writeToFile(con, fileName, urlNoOffset, 0, 0);

                nextURL = writeToFile2(con, fileName);
                page++;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        con.disconnect();
    }

    private String writeToFile2(HttpsURLConnection con, String fileName) throws IOException, ParseException {
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
        //System.out.println(str);
        //ArrayList<Character> charArray = new ArrayList<>();
        String formatStr = "";
        int tabCount = 0;
        for(int i = 0; i < str.length()-1; i++) {
            //System.out.println(str.substring(i,i+1));
            formatStr = formatStr.concat(str.substring(i,i+1));
            //System.out.println(formatStr);
            //charArray.add(str.charAt(i));
            if(str.charAt(i+1) == '}') {
                //charArray.add('\n');
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
        //System.out.println(formatStr);
        return formatStr;
    }

    private void writeToFile(HttpsURLConnection con, String fileName, String urlNoOffset, int urlOffset, int depth) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        File gameFile = new File("src/RawData/" + fileName + ".txt");
        gameFile.createNewFile();
        FileWriter writer = new FileWriter("src/RawData/" + fileName + ".txt", true);

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            JSONObject page = (JSONObject) new JSONParser().parse(inputLine);
            //JSONObject data = (JSONObject)page.get("data");
            //System.out.println(page.get("data").toString());
            if(inputLine.contains("\"data\":[")) {
                JSONArray data = (JSONArray) page.get("data");
                writer.write(((JSONObject)data.get(0)).toJSONString());
            }
            else {
                JSONObject data = (JSONObject) page.get("data");
                writer.write(data.toJSONString());
            }

            writer.close();
            JSONObject pagination = (JSONObject)page.get("pagination");

            if(pagination != null && (long)pagination.get("size") == P_MAX) {
                JSONArray links = (JSONArray) pagination.get("links");

                URL url = new URL(urlNoOffset + (urlOffset + P_MAX));
                HttpsURLConnection newCon = (HttpsURLConnection) url.openConnection();
                newCon.setRequestMethod("GET");

                writeToFile(newCon, fileName, urlNoOffset, urlOffset + P_MAX, depth+1);

                newCon.disconnect();
            }

            /*
            int endIndex = inputLine.length()-1;
            int paginationIndex = inputLine.indexOf("pagination");

            if(paginationIndex != -1) {
                endIndex = paginationIndex - 3;
                writer.write(inputLine.substring(8, endIndex));


                Object obj = null;
                try {
                    obj = new JSONParser().parse(inputLine.substring(inputLine.indexOf('{',paginationIndex), inputLine.length()-1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                JSONObject jo = (JSONObject) obj;




            }
            else {
                writer.write(inputLine.substring(8, endIndex));
                writer.close();
            }

             */
        }
        in.close();
    }


}
