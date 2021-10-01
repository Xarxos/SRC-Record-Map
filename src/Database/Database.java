import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private String gameURL = "https://www.speedrun.com/api/v1/games/";
    private String categoryURL = "https://www.speedrun.com/api/v1/categories/";
    private String runURL = "https://www.speedrun.com/api/v1/runs/";
    private String userURL = "https://www.speedrun.com/api/v1/users/";
    private String variableURL = "https://www.speedrun.com/api/v1/variables/";

    private Map<String, Game> games = new HashMap<>();
    private Map<String, Category> categories = new HashMap<>();
    private Map<String, Run> runs = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    private Map<String, Variable> variables = new HashMap<>();

    public Database() {}

    public void addGame(String gameId) {
        if (!games.containsKey(gameId)) {
            Game game = new Game(gameId, this);
            ArrayList<String> cleanedDataArray = parseItem(gameURL + gameId);
            game.storeData(cleanedDataArray);
            games.put(gameId, game);
            //game.printAll();
            //printCategories();
        }
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    public void addUser(String userId) {
        if (!users.containsKey(userId)) {
            User user = new User(userId);
            ArrayList<String> cleanedDataArray = parseItem(userURL + userId);
            user.storeData(cleanedDataArray);
            users.put(userId, user);
        }
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public void addCategory(String categoryId, Game game) {
        if (!categories.containsKey(categoryId)) {
            Category category = new Category(categoryId, this, game);
            ArrayList<String> cleanedDataArray = parseItem(categoryURL + categoryId);
            category.storeData(cleanedDataArray);
            categories.put(categoryId, category);
        }
    }

    public Category getCategory(String categoryId) {
        return categories.get(categoryId);
    }

    public void addRun(String runId) {
        if (!runs.containsKey(runId)) {
            Run run = new Run(runId, this);
            ArrayList<String> cleanedDataArray = parseItem(runURL + runId);
            run.storeData(cleanedDataArray);
            runs.put(runId, run);
        }
    }

    public Run getRun(String runId) {
        return runs.get(runId);
    }

    public void addVariable(String varId) {
        /*
        System.out.println(varId + " | " + variables.containsKey(varId));
        for(String varsId : variables.keySet()) {
            System.out.println(varsId);
        }

         */
        //System.out.println("\n");
        if (!variables.containsKey(varId)) {
            Variable var = new Variable(varId, this);
            ArrayList<String> cleanedDataArray = parseItem(variableURL + varId);
            var.storeData(cleanedDataArray);
            variables.put(varId, var);
        }
    }

    public Variable getVariable(String varId) {
        return variables.get(varId);
    }

    public ArrayList<String> parseItem(String url) {
        if(url.compareTo("https://www.speedrun.com/api/v1/users/jn95n12x") == 0) {
            //fetchApiErrorData(url);
        }

        String allData = fetchApiData(url);

        ArrayList<String> cleanedDataArray = cleanData(allData);

        return cleanedDataArray;
    }

    public ArrayList<Category> parseCategories(String url, Game game) {
        String allData = fetchApiData(url);
        ArrayList<Category> cats = new ArrayList<>();

        String[] categoryDatas = allData.split("\"id\"");
        for (int i = 1; i < categoryDatas.length; i++) {
            ArrayList<String> cleanedDataArray = cleanData(categoryDatas[i]);
            if(cleanedDataArray.get(0).compareTo("jdrogxld") == 0
                || cleanedDataArray.get(0).compareTo("q25e0qg2") == 0
                || cleanedDataArray.get(0).compareTo("rkl3oqwk") == 0
            ) {

            }
            else {
                Category category = new Category(cleanedDataArray.get(0), this, game);
                categories.put(cleanedDataArray.get(0), category);
                category.storeData(cleanedDataArray);

                cats.add(category);
            }
        }

        return cats;
    }

    public ArrayList<Run> parseRuns(String url) {
        String allData = fetchApiData(url);
        ArrayList<Run> parsedRuns = new ArrayList<>();

        String[] runDatas = allData.split("\\{\"id\"");
        for (int i = 1; i < runDatas.length; i++) {
            ArrayList<String> cleanedDataArray = cleanData(runDatas[i]);
            Run run = new Run(cleanedDataArray.get(0), this);
            run.storeData(cleanedDataArray);
            runs.put(cleanedDataArray.get(0), run);
            parsedRuns.add(run);
        }

        return parsedRuns;
    }

    public ArrayList<Variable> parseVariables(String url) {
        String allData = fetchApiData(url);
        ArrayList<Variable> parsedVars = new ArrayList<>();

        String[] variableDatas = allData.split("\\{\"id\"");
        for (int i = 1; i < variableDatas.length; i++) {
            ArrayList<String> cleanedDataArray = cleanData(variableDatas[i]);

            Variable var = new Variable(cleanedDataArray.get(0), this);
            var.storeData(cleanedDataArray);
            variables.put(cleanedDataArray.get(0), var);
            parsedVars.add(var);
        }

        return parsedVars;
    }

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

    public void printGames() {

    }

    public static void printErrorStream(HttpsURLConnection con) throws IOException {
        BufferedReader in;
        try {
            con.getInputStream();
        } catch (IOException e) {
            in = new BufferedReader(
                    //new InputStreamReader(con.getInputStream()));
                    new InputStreamReader(con.getErrorStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
        }


    }

    private String fetchApiErrorData(String urlStr) {
        URL url = null;
        HttpsURLConnection con = null;
        String allData = null;

        try {
            url = new URL(urlStr);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            printErrorStream(con);
            //allData = getAllData(con);

        } catch (IOException e) {
            e.printStackTrace();
        }
        con.disconnect();

        return allData;
    }

    public void printCategories() {
        for (Category cat : categories.values() ) {
            if(cat.getName().equals("Form Roman Empire")) {
                cat.printAll("");
            }

            //cat.printNameAndID();
            //cat.printRuns();
        }
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }
}
