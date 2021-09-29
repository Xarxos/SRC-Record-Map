import java.util.ArrayList;

public class User {
    enum Role {
        User,
        Mod,
        SuperMod
    }

    private String id;

    private String name = "N/A";
    private Role role;

    public User(String id) {
        this.id = id;
    }

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

    public void printAll() {
        System.out.println("id: " + id);
        System.out.println("name: " + name);

        System.out.print("role: ");
        if (role == Role.Mod) {
            System.out.println("moderator");
        }
        else {
            System.out.println("user");
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
