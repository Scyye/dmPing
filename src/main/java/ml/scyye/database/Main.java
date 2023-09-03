package ml.scyye.database;

public class Main {
    public static void main(String[] args) {
        SQLiteUtils utils = new SQLiteUtils();
        // utils.insertEntry("1141124795686195242", "553652308295155723", "lets go");
        System.out.println(SQLiteUtils.findByMessageId("1141124785305288784"));
    }
}
