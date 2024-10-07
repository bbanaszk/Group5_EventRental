import java.sql.SQLException;

public class ModificationClasses {

    // Add
    public static void add(String table, String values) {
        String sql = "INSERT INTO " + table + " VALUES (" + values + ")";
        executeUpdate(sql);
    }

    // Insert
    public static void insert(String query) {
        executeUpdate(query);
    }

    // Delete
    public static void delete(String table, String condition) {
        String sql = "DELETE FROM " + table + " WHERE " + condition;
        executeUpdate(sql);
    }

    // Helper method to execute update queries
    private static void executeUpdate(String query) {
        try {

            JDBCConnection.stmt.executeUpdate(query);
            System.out.println("Operation successful.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to execute the operation.");
        }
    }

}

