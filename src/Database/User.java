import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
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

    public void printAll() {
        System.out.println("id: " + id);
        System.out.println("name: " + name);

        System.out.println("role: " + role);

        System.out.println("");
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
