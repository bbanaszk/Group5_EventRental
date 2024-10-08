package ser322;

import java.sql.*;

public class JDBCConnection {

    private static ResultSet rs = null;
    private static Statement stmt = null;
    private static Connection conn = null;

    private static void establishConnection(String JDBCDriverName, String url, String user, String password) {

        try {
            try {
                Class.forName(JDBCDriverName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            conn = DriverManager.getConnection(url, user, password);

            stmt = conn.createStatement();


//            rs = stmt.executeQuery("");

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    private static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            rs.close();
            stmt.close();
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("Unable to close ResultSet and Statement resources!");
        }

        try {
            conn.close();
        } catch (Throwable t2) {
            t2.printStackTrace();
            System.out.println("Unable to close connection, connection leaked!");
        }
    }
}
