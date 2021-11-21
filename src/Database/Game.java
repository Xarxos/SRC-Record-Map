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
import java.util.Map;

public class Game {
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
    }

    public void storeData() {
        JSONObject gameObject = null;
        JSONArray categoryArray = null;
        try {
            gameObject = (JSONObject) (new JSONParser().parse(new FileReader("C:\\Users\\ludvi\\IdeaProjects\\PdxSaveMove\\src\\RawData\\games\\" + id + ".txt")));
            categoryArray = (JSONArray) (new JSONParser().parse(new FileReader("C:\\Users\\ludvi\\IdeaProjects\\PdxSaveMove\\src\\RawData\\games\\" + id + "_categories.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map names = (Map)gameObject.get("names");
        this.name = (String)names.get("international");
        this.abbreviation = (String)gameObject.get("abbreviation");

        Map mods = (Map)gameObject.get("moderators");
        for(Object modId : mods.keySet()) {
            database.addUser((String)modId);
            moderators.add(database.getUser((String)modId));
        }

        for (Object cat : categoryArray) {

        }


    }

    /*
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
    */
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
            cat.printAll("\t");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void addModerator(User mod) {
        this.moderators.add(mod);
    }

    public void setSuperModerators(ArrayList<String> superModerators) {
        this.superModerators = superModerators;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }
}
