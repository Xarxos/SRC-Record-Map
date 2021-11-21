import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class User {
    enum Role {
        User,
        Mod,
        SuperMod
    }

    private String id;

    private String name = "N/A";
    private String role = "N/A";

    public User(String id) {
        this.id = id;
    }

    public void storeData() {
        JSONObject userObject = null;
        try {
            userObject = (JSONObject) (new JSONParser().parse(new FileReader("C:\\Users\\ludvi\\IdeaProjects\\PdxSaveMove\\src\\RawData\\users\\" + id + ".txt")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map names = (Map)userObject.get("names");
        this.name = (String)names.get("international");
        this.role = (String)userObject.get("role");
    }
/*
    public void storeData(ArrayList<String> cleanedDataArray) {
        for (int i = 0; i < cleanedDataArray.size(); i++) {
            if (cleanedDataArray.get(i).compareTo("international") == 0
                && cleanedDataArray.get(i-3).compareTo("code") != 0
            ) {
                this.name = cleanedDataArray.get(i+1);
            }
            else if (cleanedDataArray.get(i).compareTo("role") == 0) {
                if (cleanedDataArray.get(i+1).compareTo("moderator") == 0) {
                    this.role = Role.Mod;
                }
                else {
                    this.role = Role.User;
                }
            }
        }
    }

 */

    public void printAll() {
        System.out.println("id: " + id);
        System.out.println("name: " + name);

        System.out.print("role: " + role);
        /*
        if (role == Role.Mod) {
            System.out.println("moderator");
        }
        else {
            System.out.println("user");
        }

         */
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
