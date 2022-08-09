import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
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

    public Database() { }

    public void addGame(String gameId) {
        if (!games.containsKey(gameId)) {
            Game game = new Game(gameId, this);

            JSONObject gameObject = null;
            JSONArray categoryArray = null;
            JSONArray levelArray = null;
            JSONArray variableArray = null;
            ArrayList<JSONArray> runArrays = new ArrayList<>();

            try {
                String gameFilePath = baseFilePath + "games\\" + gameId;

                gameObject = (JSONObject) ((JSONObject) (new JSONParser().parse(new FileReader(gameFilePath + ".txt")))).get("data");
                categoryArray = (JSONArray) ((JSONObject) (new JSONParser().parse(new FileReader(gameFilePath + "_categories.txt")))).get("data");
                levelArray = (JSONArray) ((JSONObject) (new JSONParser().parse(new FileReader(gameFilePath + "_levels.txt")))).get("data");
                variableArray = (JSONArray) ((JSONObject) (new JSONParser().parse(new FileReader(gameFilePath + "_variables.txt")))).get("data");

                Integer i = 0;
                File runFile = null;
                while((runFile = new File(gameFilePath + "_runs_" + i.toString() + ".txt")).exists()) {
                    runArrays.add((JSONArray) ((JSONObject) (new JSONParser().parse(new FileReader(gameFilePath + "_runs_" + i.toString() + ".txt")))).get("data"));
                    i++;
                }
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

            for (JSONArray runArray : runArrays) {
                for (Object run : runArray) {
                    addRun((JSONObject) run);
                }
            }

            games.put(gameId, game);
            game.printAll();
        }
    }

    public void addUser(String userId) {
        if (!users.containsKey(userId)) {
            User user = new User(userId);

            JSONObject userObject = null;
            try {
                userObject = (JSONObject) ((JSONObject) (new JSONParser().parse(new FileReader("C:\\Users\\ludvi\\IdeaProjects\\PdxSaveMove\\src\\RawData\\users\\" + userId + ".txt")))).get("data");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Map names = (Map)userObject.get("names");
            user.setName((String)names.get("international"));
            user.setRole((String)userObject.get("role"));

            users.put(userId, user);
        }
    }

    public void addCategory(JSONObject categoryObject, Game game) {
        String categoryId = (String)categoryObject.get("id");
        if (!categories.containsKey(categoryId)) {
            Category category = new Category(categoryId, this, game);

            category.setName((String)categoryObject.get("name"));
            category.setRules((String)categoryObject.get("rules"));
            Object misc = categoryObject.get("miscellaneous");
            if (misc != null) {
                category.setMisc((boolean)misc);
            }

            categories.put(categoryId, category);
        }
    }

    public void addRun(JSONObject runObject) {
        String runId = (String)runObject.get("id");
        if (!runs.containsKey(runId)) {
            Run run = new Run(runId, this);

            run.setGame(games.get((String)runObject.get("game")));
            run.setComment((String)runObject.get("comment"));

            String runnerId = (String)((JSONObject)((JSONArray)runObject.get("players")).get(0)).get("id");
            if(!users.containsKey(runnerId)) {
                addUser(runnerId);
            }
            run.setRunner(users.get(runnerId));

            Object primaryT = ((JSONObject)runObject.get("times")).get("primary_t");
            Long l = 1l;
            if(primaryT.getClass() == l.getClass()) {
                run.storeTime(((Long) primaryT).doubleValue());
            }
            else {
                run.storeTime((Double)primaryT);
            }

            JSONObject status = (JSONObject)runObject.get("status");
            run.setVerified(((String)status.get("status")).equals("verified"));
            if(!((String)status.get("status")).equals("new")) {
                String verifierId = (String)status.get("examiner");
                if(!users.containsKey(verifierId)) {
                    addUser(verifierId);
                }
                run.setVerifier(users.get(verifierId));
            }

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
            if(category != null) {
                //System.out.println("Category: " + category.getName());
                run.setCategory(category);

                Map values = (Map)runObject.get("values");
                if (categoryId.equals("7kjp39xk") || categoryId.equals("wk6emvp2")) {
                    run.setTimingMethod(Run.TimingMethod.RTA_NS5);
                }
                for(Object varId : values.keySet()) {
                    String valueId = (String)values.get(varId);
                    //System.out.println("Varname: " + variables.get((String)varId).getName());
                    if(variables.get((String)varId).getName().contains("Timing Method")
                    || variables.get((String)varId).getName().contains("timing method")) {
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

                for(Object varId : values.keySet()) {
                    String valueId = (String)values.get(varId);
                    if(category.isSubCatVariable((String)varId)) {
                        String valueLabel = variables.get((String)varId).getValue(valueId).getLabel();
                        //System.out.println("Valuelabel: " + valueLabel);
                        category.addRun(run, valueId);
                    }
                }

                if(!category.hasSubCategories()) {
                    category.addRun(run, " ");
                }

                runs.put(runId, run);
            }
        }
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

            variables.put(varId, var);
        }
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

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    public Variable getVariable(String varId) {
        return variables.get(varId);
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }

    public Run getRun(String runId) {
        return runs.get(runId);
    }

    public Category getCategory(String categoryId) {
        return categories.get(categoryId);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }
}
