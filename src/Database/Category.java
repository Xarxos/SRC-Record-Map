import java.util.ArrayList;

public class Category {
    private Database database;

    private String id;
    private String name;
    private Game game;

    private String rules;
    private boolean misc;

    private ArrayList<Run> runs = new ArrayList<>();
    private ArrayList<Category> subcategories = new ArrayList<>();

    public Category(String id, Database database, Game game) {
        this.id = id;
        this.database = database;
        this.game = game;
    }

    public void storeData(ArrayList<String> cleanedDataArray) {
        for (int i = 0; i < cleanedDataArray.size(); i++) {
            //System.out.println(cleanedDataArray.get(i));
            if (cleanedDataArray.get(i).compareTo("name") == 0) {
                this.name = cleanedDataArray.get(i+1);
            }
            else if (cleanedDataArray.get(i).compareTo("rules") == 0) {
                this.rules = cleanedDataArray.get(i+1);
            }
            else if (cleanedDataArray.get(i).compareTo("miscellaneous") == 0) {
                this.misc = cleanedDataArray.get(i+1).contains("true");
            }
            else if (cleanedDataArray.get(i).compareTo("runs") == 0) {
                //System.out.println("Category: " + name);
                this.runs = database.parseRuns(cleanedDataArray.get(i+2));
            }
        }
        if(this.name.contains("Reform ")) {
            System.out.println("name: " + name);
            System.out.println("id: " + id);
        }
    }

    public void printAll() {
        System.out.println("id: " + id);
        System.out.println("name: " + name);
        System.out.println("rules: " + rules);
        System.out.print("misc: ");
        if (misc) {
            System.out.println("true");
        }
        else {
            System.out.println("false");
        }
        for (Run run : runs) {
            System.out.println("Run:\n----");
            run.printAll(1);
        }
    }

    public void printName() {
        System.out.println(name);
    }

    public void printRuns() {
        for (Run run : runs) {
            System.out.println("\tRun:\n----");
            run.printAll(1);
        }
    }

    public void addSubcategory(Category subcategory) {
        subcategories.add(subcategory);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public boolean isMisc() {
        return misc;
    }

    public void setMisc(boolean misc) {
        this.misc = misc;
    }
}
