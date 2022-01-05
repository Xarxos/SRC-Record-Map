import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
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

    public void printAll() {
        System.out.println("id: " + id);
        System.out.println("name: " + name);
        System.out.println("abbreviation: " + abbreviation);

        for (User mod : moderators) {
            System.out.println("moderator:\n----------");
            mod.printAll();
            System.out.println("----------");
        }
        for (String supMod : superModerators) {
            System.out.println("super-moderator: " + supMod);
        }
        for (Category cat : categories) {
            System.out.println("Category:\n---------");
            cat.printAll("\t");
        }
        System.out.println("");
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
