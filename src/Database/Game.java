import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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
}
