public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        GameParser gameParser = new GameParser();
        gameParser.parseGame("m1zjje26");
        database.addGame("m1zjje26");
    }
}
