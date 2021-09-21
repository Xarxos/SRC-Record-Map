import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        database.addGame("m1zjje26");

        SwingUtilities.invokeLater(() -> {
            GUI gui=new GUI(database);
        });

    }
}
