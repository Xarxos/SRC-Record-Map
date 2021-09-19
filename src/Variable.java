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

    public void storeData(ArrayList<String> cleanedDataArray) {
        int valueStartIndex = 0;
        int valueEndIndex = 0;

        for (int i = 0; i < cleanedDataArray.size(); i++) {
            if (cleanedDataArray.get(i).compareTo("name") == 0) {
                this.name = cleanedDataArray.get(i+1);
            }
            else if (cleanedDataArray.get(i).compareTo("category") == 0) {
                if (cleanedDataArray.get(i+1).contains("null")) {
                    category = null;
                }
                else {
                    this.category = database.getCategory(cleanedDataArray.get(i+1));
                }
            }
            else if (cleanedDataArray.get(i).compareTo("is-subcategory") == 0) {
                this.subcategory = cleanedDataArray.get(i+1).contains("true");
                valueEndIndex = i;
            }
            else if (cleanedDataArray.get(i).compareTo("values") == 0) {
                valueStartIndex = i;
            }
        }

        storeValuesData(cleanedDataArray, valueStartIndex, valueEndIndex);
    }

    public void storeValuesData(ArrayList<String> dataArray, int start, int end) {
        for (int i = start; i < end; i++) {
            if (dataArray.get(i).compareTo("label") == 0) {
                Value value = new Value();
                value.id = dataArray.get(i-1);
                value.label = dataArray.get(i+1);
                value.rules = dataArray.get(i+3);
                value.misc = dataArray.get(i+6).contains("true");

                values.put(value.id, value);
            }
        }
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
}