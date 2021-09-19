import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Game {
    private String gameURL = "https://www.speedrun.com/api/v1/games/";
    //private String allData = "";
    private Database database;

    private String id;
    private String name;
    private String abbreviation;
    private ArrayList<User> moderators = new ArrayList<>();
    private ArrayList<String> superModerators = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();

    public Game(String id, Database database) {
        this.id = id;
        this.database = database;
        gameURL = gameURL.concat(id);
    }
/*
    public void parseGame() {
        String allData = fetchApiData(gameURL);

        ArrayList<String> cleanedDataArray = cleanData(allData);

        storeGameData(cleanedDataArray);

        printAll();
    }
*/
    private String fetchApiData(String urlStr) {
        URL url = null;
        HttpsURLConnection con = null;
        String allData = null;

        try {
            url = new URL(urlStr);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            allData = getAllData(con);

        } catch (IOException e) {
            e.printStackTrace();
        }
        con.disconnect();

        return allData;
    }

    private ArrayList<String> cleanData(String allData) {
        allData = allData.replaceAll("\\[|\\]|\\{|\\}", "");
        String[] baseDataArray = allData.split(",|\"");
        ArrayList<String> cleanedDataArray = new ArrayList<>();

        for (String str: baseDataArray) {
            //System.out.println(str);
            if (!str.isBlank() && str.compareTo(":") != 0 ) {
                cleanedDataArray.add(str);
            }
        }

        return cleanedDataArray;
    }

    public void storeData(ArrayList<String> cleanedDataArray) {
        for (int i = 0; i < cleanedDataArray.size(); i++) {
            String thisLine = cleanedDataArray.get(i);
            String nextLine = i < cleanedDataArray.size()-1 ? cleanedDataArray.get(i+1) : null;
            String prevLine = i > 0 ? cleanedDataArray.get(i-1) : null;
            //System.out.println(cleanedDataArray.get(i));
            if (thisLine.compareTo("international") == 0) {
                this.name = nextLine;
            }
            else if (thisLine.compareTo("abbreviation") == 0) {
                this.abbreviation = nextLine;
            }
            else if (thisLine.compareTo("moderator") == 0) {
                database.addUser(prevLine);
                moderators.add(database.getUser(prevLine));
            }
            else if (thisLine.compareTo("super-moderator") == 0) {
                superModerators.add(prevLine);
                database.addUser(prevLine);
                moderators.add(database.getUser(prevLine));
            }
            else if (thisLine.compareTo("categories") == 0 || thisLine.compareTo("levels") == 0 ) {
                String catUrl = cleanedDataArray.get(i+2);
                categories.addAll(database.parseCategories(catUrl, this));
                //categories = database.parseCategories(catUrl, this);
            }
        }
    }

    public String getAllData(HttpsURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        String allData = "";
        while ((inputLine = in.readLine()) != null) {
            allData = allData.concat(inputLine);
        }
        //System.out.println(this.allData);
        in.close();
        return allData;
    }

    public void printAll() {
        System.out.println("id: " + id);
        System.out.println("name: " + name);
        System.out.println("abbreviation: " + abbreviation);

        for (User mod : moderators) {
            System.out.println("moderator:\n----------");
            mod.printAll();
        }
        for (String supMod : superModerators) {
            System.out.println("super-moderator: " + supMod);
        }
        for (Category cat : categories) {
            System.out.println("Category:\n---------");
            cat.printAll();
        }
    }
}
