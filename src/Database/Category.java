import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Category {
    private Database database;

    private String id;
    private String name;
    private Game game;

    private String rules;
    private boolean misc;

    private Map<String, Map<Run.TimingMethod, ArrayList<Run>>> runs = new HashMap<>();
    private Map<String, ArrayList<Integer>> timingIndexes = new HashMap<>();
    private Map<String, ArrayList<Variable.Value>> subcategories = new HashMap<>();

    public Category(String id, Database database, Game game) {
        this.id = id;
        this.database = database;
        this.game = game;
        this.subcategories.put(" ", new ArrayList<>());
        this.subcategories.get(" ").add(null);
        this.runs.put(" ", new HashMap<>());
        this.runs.get(" ").put(Run.TimingMethod.RTA_NS5, new ArrayList<>());
        this.runs.get(" ").put(Run.TimingMethod.RTA_WS5, new ArrayList<>());
        this.runs.get(" ").put(Run.TimingMethod.IGT, new ArrayList<>());
        this.runs.get(" ").put(Run.TimingMethod.IGT_WSS, new ArrayList<>());
    }

    private void storeTimingIndexes(ArrayList<Run> subCatRuns, String subCat) {
        int t = -1;
        for(int i = 0; i < subCatRuns.size(); i++) {
            if(subCatRuns.get(i).getTimingMethod() != null) {
                if(subCatRuns.get(i).getTimingMethod().ordinal() != t) {
                    t = subCatRuns.get(i).getTimingMethod().ordinal();
                    this.timingIndexes.get(subCat).set(t, i);
                }
            }
        }
    }

    public void printAll(String prefixTabs) {
        System.out.println(prefixTabs + "id: " + id);
        System.out.println(prefixTabs + "name: " + name);
        System.out.println(prefixTabs + "rules: " + rules);
        System.out.print(prefixTabs + "misc: ");
        if (misc) {
            System.out.println("true");
        }
        else {
            System.out.println("false");
        }

        for(ArrayList<Variable.Value> subCats : subcategories.values()) {
            if(subCats.get(0) != null) {
                for(Variable.Value subCat : subCats) {
                    System.out.println(prefixTabs + "Subcategory: " + subCat.getLabel());
                    for(ArrayList<Run> timingMethod : runs.get(subCat.getId()).values()) {
                        for (Run run : timingMethod) {
                            System.out.println(prefixTabs + "\tRun:\n" + prefixTabs + "\t----");
                            run.printAll(prefixTabs + "\t\t");
                        }
                    }
                }
            }
            else {
                for(ArrayList<Run> timingMethod : runs.get(" ").values()) {
                    for (Run run : timingMethod) {
                        System.out.println(prefixTabs + "\tRun:\n" + prefixTabs + "\t----");
                        run.printAll(prefixTabs + "\t\t");
                    }
                }
            }
        }
        System.out.println("");
    }

    public void printNameAndID() {
        System.out.println(name + ": " + id);
    }

    public void printRuns(String prefixTabs) {
        for(ArrayList<Variable.Value> subCats : subcategories.values()) {
            for(Variable.Value subCat : subCats) {
                System.out.println(prefixTabs + "Subcategory: " + subCat.getLabel());
                for(ArrayList<Run> timingMethod : runs.get(subCat.getId()).values()) {
                    for (Run run : timingMethod) {
                        System.out.println(prefixTabs + "\tRun:\n" + prefixTabs + "\t----");
                        run.printAll(prefixTabs + "\t\t");
                    }
                }
            }
        }
    }

    public void addSubcategory(Variable subCatVariable) {
        if(subcategories.containsKey(" ")) {
            subcategories.remove(" ");
            runs.remove(" ");
        }
        ArrayList<Variable.Value> subCats = new ArrayList(subCatVariable.getValues().values());
        subcategories.put(subCatVariable.getId(), subCats);
        for(Variable.Value subCat : subCats) {
            runs.put(subCat.getId(), new HashMap<>());
            runs.get(subCat.getId()).put(Run.TimingMethod.RTA_NS5, new ArrayList<>());
            runs.get(subCat.getId()).put(Run.TimingMethod.RTA_WS5, new ArrayList<>());
            runs.get(subCat.getId()).put(Run.TimingMethod.IGT, new ArrayList<>());
            runs.get(subCat.getId()).put(Run.TimingMethod.IGT_WSS, new ArrayList<>());
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Variable.Value> getSubcategories() {
        for(ArrayList<Variable.Value> subCat : subcategories.values()) {
            return subCat;
        }
        return null;
    }

    public boolean isSubCatVariable(String varId) {
        return subcategories.containsKey(varId);
    }

    public boolean hasSubCategories() {
        return !subcategories.containsKey(" ");
    }

    public String getRules() {
        return rules;
    }

    public boolean isMisc() {
        return misc;
    }
/*
    public Run getWR(String subCat, int timingMethod) {
        int timingIndex = timingIndexes.get(subCat).get(timingMethod);

        if(timingIndex != -1) {
            if(this.id.equals("vdoo9wyd") && timingMethod == 1) {
                return null;
            }
            return runs.get(subCat).get(timingIndex);
        }
        else {
            //Run noWR = new Run("-1", database);
            //noWR.setTimingMethod(Run.TimingMethod.values()[timingMethod]);
            return null;
        }
        //return null;
    }

 */

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public void setMisc(boolean misc) {
        this.misc = misc;
    }

    public void addRun(Run run, String subCatId) {
        this.runs.get(subCatId).get(run.getTimingMethod()).add(run);
    }

    @Override
    public boolean equals(Object o) {
        return ((Category)o).id.equals(this.id);
    }
}
