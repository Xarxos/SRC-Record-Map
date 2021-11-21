import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private GameParser parser;
    private boolean parseAPI = false;

    private String baseFilePath = "C:\\Users\\ludvi\\IdeaProjects\\PdxSaveMove\\src\\RawData\\";

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

    public Database() {
    }

    public void parseAPI(boolean yes) {
        this.parseAPI = yes;
    }

    public void addGame(String gameId) {
        if (!games.containsKey(gameId)) {
            Game game = new Game(gameId, this);

            JSONObject gameObject = null;
            JSONArray categoryArray = null;
            JSONArray levelArray = null;
            JSONArray variableArray = null;
            JSONArray runArray = null;
            try {
                String gameFilePath = baseFilePath + "games\\" + gameId;
                gameObject = (JSONObject) (new JSONParser().parse(new FileReader(gameFilePath + ".txt")));
                categoryArray = (JSONArray) (new JSONParser().parse(new FileReader(gameFilePath + "_categories.txt")));
                levelArray = (JSONArray) (new JSONParser().parse(new FileReader(gameFilePath + "_levels.txt")));
                variableArray = (JSONArray) (new JSONParser().parse(new FileReader(gameFilePath + "_variables.txt")));
                runArray = (JSONArray) (new JSONParser().parse(new FileReader(gameFilePath + "_runs.txt")));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Map names = (Map)gameObject.get("names");
            game.setName((String)names.get("international"));
            game.setAbbreviation((String)gameObject.get("abbreviation"));

            Map mods = (Map)gameObject.get("moderators");
            for(Object modId : mods.keySet()) {
                addUser((String)modId);
                game.addModerator(getUser((String)modId));
            }

            for (Object cat : categoryArray) {
                addCategory((JSONObject) cat, game);
                game.addCategory(categories.get((String)(((JSONObject)cat).get("id"))));
            }

            for (Object level : levelArray) {
                addCategory((JSONObject) level, game);
                game.addCategory(categories.get((String)(((JSONObject)level).get("id"))));
            }

            for (Object var : variableArray) {
                addVariable((JSONObject) var);
            }

            for (Object run : runArray) {
                addRun((JSONObject) run);
            }

            //game.storeData();
            games.put(gameId, game);
            game.printAll();
        }
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    public void addUser(String userId) {
        if (!users.containsKey(userId)) {
            User user = new User(userId);

            JSONObject userObject = null;
            try {
                userObject = (JSONObject) (new JSONParser().parse(new FileReader("C:\\Users\\ludvi\\IdeaProjects\\PdxSaveMove\\src\\RawData\\users\\" + userId + ".txt")));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Map names = (Map)userObject.get("names");
            user.setName((String)names.get("international"));
            user.setRole((String)userObject.get("role"));

            //user.storeData();
            users.put(userId, user);
        }
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public void addCategory(JSONObject categoryObject, Game game) {
        String categoryId = (String)categoryObject.get("id");
        if (!categories.containsKey(categoryId)) {
            Category category = new Category(categoryId, this, game);

            category.setName((String)categoryObject.get("name"));
            category.setRules((String)categoryObject.get("rules"));
            category.setMisc((boolean)categoryObject.get("miscellaneous"));

            //category.storeData(categoryObject);
            categories.put(categoryId, category);
        }
    }

    public Category getCategory(String categoryId) {
        return categories.get(categoryId);
    }

    public void addRun(JSONObject runObject) {
        String runId = (String)runObject.get("id");
        if (!runs.containsKey(runId)) {
            Run run = new Run(runId, this);

            run.setGame(games.get((String)runObject.get("game")));
            run.setComment((String)runObject.get("comment"));
            run.setRunner(users.get((String)((JSONObject)runObject.get("players")).get("id")));
            run.storeTime((Double)((JSONObject)runObject.get("times")).get("primary_t"));

            JSONObject status = (JSONObject)runObject.get("status");
            run.setVerified(((String)status.get("status")).equals("verified"));
            run.setVerifier(users.get((String)runObject.get("examiner")));

            String categoryId = (String)runObject.get("category");
            if(((String)runObject.get("level") != null)) {
                if(categoryId.compareTo("jdrogxld") == 0) {
                    run.setTimingMethod(Run.TimingMethod.IGT);
                }
                else if(categoryId.compareTo("q25e0qg2") == 0) {
                    run.setTimingMethod(Run.TimingMethod.RTA_NS5);
                }
                else if(categoryId.compareTo("rkl3oqwk") == 0) {
                    run.setTimingMethod(Run.TimingMethod.IGT_WSS);
                }
                categoryId = (String)runObject.get("level");
            }

            Category category = categories.get(categoryId);
            run.setCategory(category);

            Map values = (Map)runObject.get("values");
            for(Object varId : values.keySet()) {
                String valueId = (String)values.get(varId);
                if(category.isSubCatVariable((String)varId)) {
                    category.addRun(run, valueId);
                }
                if(variables.get((String)varId).getName().contains("Timing Method")) {
                    String valueLabel = variables.get((String)varId).getValue(valueId).getLabel();
                    if(valueLabel.equals("RTA NS5")) { run.setTimingMethod(Run.TimingMethod.RTA_NS5); }
                    if(valueLabel.contains("RTA W/S5")) { run.setTimingMethod(Run.TimingMethod.RTA_WS5); }
                    if(valueLabel.equals("IGT No Save Scum")) { run.setTimingMethod(Run.TimingMethod.IGT); }
                    if(valueLabel.equals("IGT W/Save Scum")) { run.setTimingMethod(Run.TimingMethod.IGT_WSS); }
                }
                else {
                    run.addVariableValue((String)varId, valueId);
                }
            }

            if(!category.hasSubCategories()) {
                category.addRun(run, " ");
            }

            //run.storeData();
            runs.put(runId, run);
        }
    }

    public Run getRun(String runId) {
        return runs.get(runId);
    }

    public void addVariable(JSONObject variableObject) {
        String varId = (String)variableObject.get("id");
        if (!variables.containsKey(varId)) {
            Variable var = new Variable(varId, this);

            var.setName((String)variableObject.get("name"));

            String categoryId = (String)variableObject.get("category");
            Category category = null;
            if(categoryId != null) {
                category = categories.get(categoryId);
            }
            var.setCategory(category);

            var.setSubcategory((boolean)variableObject.get("is-subcategory"));

            Map valuesOuter = (Map)variableObject.get("values");
            Map values = (Map)valuesOuter.get("values");
            var.addValues(values);

            if(var.isSubcategory()) {
                category.addSubcategory(var);
            }



            //var.storeData(cleanedDataArray);
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
                //category.storeData(cleanedDataArray);

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
