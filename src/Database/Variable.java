import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Variable {
    public class Value {
        public String id;
        public String label;
        public String rules = null;
        public boolean misc = false;

        public void printAll() {
            System.out.println("id: " + id);
            System.out.println("label: " + label);

            if (rules != null) {
                System.out.println("rules: " + rules);
            }

            System.out.print("misc: ");
            if (misc) {
                System.out.println("yes");
            }
            else {
                System.out.println("no");
            }
            System.out.println("");
        }

        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }
    }

    private String id;
    private Database database;

    private String name;
    private Category category;
    private boolean subcategory = false;
    private Map<String, Value> values = new HashMap<>();

    public Variable(String id, Database database) {
        this.id = id;
        this.database = database;
    }

    public void printAll() {
        System.out.println("id: " + id);
        System.out.println("name: " + name);

        System.out.print("subcategory: ");
        if (subcategory) {
            System.out.println("yes");
        }
        else {
            System.out.println("no");
        }

        for (Value val : values.values()) {
            val.printAll();
        }
        System.out.println("");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Value getValue(String valueId) {
        return values.get(valueId);
    }

    public Map<String, Value> getValues() {
        return values;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isSubcategory() {
        return subcategory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setSubcategory(boolean subcategory) {
        this.subcategory = subcategory;
    }

    public void addValues(Map valueObjects) {
        for(Object valueIdObj : valueObjects.keySet()) {
            String valueId = (String) valueIdObj;
            Value val = new Value();
            val.id = valueId;
            val.label = (String) ((JSONObject)valueObjects.get(valueId)).get("label");
            val.rules = (String) ((JSONObject)valueObjects.get(valueId)).get("rules");

            JSONObject flags = (JSONObject) ((JSONObject)valueObjects.get(valueId)).get("flags");
            if (flags != null) {
                Object misc = flags.get("miscellaneous");
                if (misc != null) {
                    val.misc = (boolean)misc;
                }
            }

            values.put((String) valueId, val);
        }
    }
}