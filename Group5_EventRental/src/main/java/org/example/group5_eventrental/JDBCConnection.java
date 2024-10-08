package org.example.group5_eventrental;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCConnection {
    public static void establishConnection(String JDBCDriverName, String url, String user, String password) {
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;

        try {
            try {
                Class.forName(JDBCDriverName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Driver Not Found");
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
