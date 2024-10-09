package org.example.group5_eventrental;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloApplication extends Application {

    protected static Connection connection = null;
    protected static Statement statement = null;
    protected static ResultSet resultSet = null;
    protected static String url, username, password;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 300);

        ConnectionController connectionController = fxmlLoader.getController();
        connectionController.setStage(stage);

        stage.setTitle("Event Rental");
        stage.setScene(scene);
        stage.show();

        try {
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
            if (connection != null)
                connection.close();
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}