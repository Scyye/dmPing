package ml.scyye.dmping.utils;

import ml.scyye.dmping.listeners.Antidelete;

import java.sql.*;
import java.util.*;

public class SQLiteUtils {

    private static Connection connect() {
        Connection conn = null;
        try {
            String cacheDatabaseUrl = "jdbc:sqlite:C:/sqlite/dmping_caching.db";
            conn = DriverManager.getConnection(cacheDatabaseUrl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void insertEntry(String messageId, String userId, String messageContent) {
        String sql = """
                INSERT INTO cache(message_id, user_id, message_content) VALUES(?,?,?)
                """;

        try {
            Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, messageId);
            statement.setString(2, userId);
            statement.setString(3, messageContent);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Antidelete.CachedMessage findByMessageId(String messageId) {
        String sql = "SELECT * FROM cache";

        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                if (set.getString(1).equals(messageId))
                    return new Antidelete.CachedMessage(set.getString(1), set.getString(2), set.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Antidelete.CachedMessage();
    }

    public static List<Antidelete.CachedMessage> findAllCachedMessages() {
        String sql = "SELECT * FROM cache";
        List<Antidelete.CachedMessage> messages = new ArrayList<>();

        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                messages.add(new Antidelete.CachedMessage(set.getString(1), set.getString(2), set.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public String formatCacheMessage(Antidelete.CachedMessage message) throws SQLException {
        return String.format("""
                message_id: %s
                user_id: %s
                message_content: %s
                """, message.messageId, message.authorId, message.content);
    }

    /*
    public static void createNewTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS cache (
                 message_id text PRIMARY KEY,
                 user_id text NOT NULL,
                 message_content text NOT NULL
                );
                """;

        try{
            Connection conn = DriverManager.getConnection(cacheDatabaseUrl);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     */
}
