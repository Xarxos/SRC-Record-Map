import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
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

    private Map<String, ArrayList<Run>> runs = new HashMap<>();
    private Map<String, ArrayList<Integer>> timingIndexes = new HashMap<>();
    private Map<String, ArrayList<Variable.Value>> subcategories = new HashMap<>();

    public Category(String id, Database database, Game game) {
        this.id = id;
        this.database = database;
        this.game = game;
        this.subcategories.put(" ", new ArrayList<>());
    }

    public void storeData(JSONObject categoryObject) {
        this.name = (String)categoryObject.get("name");
        this.rules = (String)categoryObject.get("rules");
        this.misc = (boolean)categoryObject.get("miscellaneous");
    }

/*
    public void storeData(ArrayList<String> cleanedDataArray) {
        ArrayList<Run> allRuns = null;
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
                allRuns = database.parseRuns(cleanedDataArray.get(i+2));
            }
        }
        for(int i = 0; i < allRuns.size(); i++) {
            if(!allRuns.get(i).isVerified()) {
                allRuns.remove(i);
                i--;
            }
        }
        for(Variable var : database.getVariables().values()) {
            if(this.name.equals("Form Roman Empire")) {
                //System.out.println(this.name + " | " + var.getName() + " | " + var.getId() + " | " + var.getCategory().getName());
            }
        }

        for(Variable var : database.getVariables().values()) {
            if(var.getCategory() != null && var.getCategory().equals(this) && var.getName().contains("Subcategory")) {
                subcategories.put(var.getId(), new ArrayList<>());
                for(Variable.Value value : var.getValues().values()) {
                    subcategories.get(var.getId()).add(value);
                    runs.put(value.id, new ArrayList<Run>());
                    timingIndexes.put(value.id, new ArrayList<>());
                    for(int i = 0; i < 4; i++) {
                        timingIndexes.get(value.id).add(-1);
                    }
                }
            }
        }
        if(subcategories.isEmpty()) {
            subcategories.put(" ", new ArrayList<>());
            //subcategories.get(" ").add(null);
            runs.put(" ", allRuns);
            Collections.sort(runs.get(" "));
            timingIndexes.put(" ", new ArrayList<>());
            for(int i = 0; i < 4; i++) {
                timingIndexes.get(" ").add(-1);
            }
            storeTimingIndexes(runs.get(" "), " ");
        }
        else {
            divideRunsBySubCat(allRuns);
        }
    }

 */

    private void divideRunsBySubCat(ArrayList<Run> allRuns) {
        for(Run run : allRuns) {
            for(String varId : run.getVariableValues().keySet()) {
                if(subcategories.containsKey(varId)) {
                    runs.get(run.getVariableValues().get(varId)).add(run);
                }
            }
        }
        for(String subCat : runs.keySet()) {
            Collections.sort(runs.get(subCat));
            storeTimingIndexes(runs.get(subCat), subCat);
        }
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
                    for (Run run : runs.get(subCat.getId())) {
                        System.out.println(prefixTabs + "\tRun:\n----");
                        run.printAll(prefixTabs + "\t\t");
                    }
                }
            }
            else {
                for (Run run : runs.get(" ")) {
                    System.out.println(prefixTabs + "Run:\n----");
                    run.printAll(prefixTabs + "\t");
                }
            }

        }



    }

    public void printNameAndID() {
        System.out.println(name + ": " + id);
    }

    public void printRuns(String prefixTabs) {
        for(ArrayList<Variable.Value> subCats : subcategories.values()) {
            for(Variable.Value subCat : subCats) {
                System.out.println(prefixTabs + "Subcategory: " + subCat.getLabel());
                for (Run run : runs.get(subCat.getId())) {
                    System.out.println(prefixTabs + "\tRun:\n----");
                    run.printAll(prefixTabs + "\t\t");
                }
            }
        }
    }

    public void addSubcategory(Variable subCatVariable) {
        if(subcategories.containsKey(" ")) {
            subcategories.remove(" ");
        }
        subcategories.put(subCatVariable.getId(), (ArrayList)subCatVariable.getValues().values());
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

    public void addRun(Run run, String subCatId) {
        this.runs.get(subCatId).add(run);
    }

    @Override
    public boolean equals(Object o) {
        return ((Category)o).id.equals(this.id);
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
        return subcategories.size() > 1;
    }
}
