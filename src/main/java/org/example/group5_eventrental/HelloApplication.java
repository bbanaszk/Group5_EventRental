package org.example.group5_eventrental;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {

    protected static Connection connection = null;
    protected static Statement statement = null;
    protected static ResultSet resultSet = null;
    protected static String url, username, password, driver;
    protected static PreparedStatement preparedStatement = null;

    @Override
    public void start(Stage stage) throws IOException {
        driver = "com.mysql.cj.jdbc.Driver";

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("update-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 1000);

        UpdateController connectionController = fxmlLoader.getController();
        connectionController.setStage(stage);

        stage.setTitle("Event Rental");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

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