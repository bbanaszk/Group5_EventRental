package org.example.group5_eventrental;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionController {
    private Stage stage;
    @FXML private TextField inputURLField;
    @FXML private TextField inputUsernameField;
    @FXML private TextField inputPasswordField;
    @FXML private Label loginText;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML protected void validateConnection() throws IOException {
        HelloApplication.url = inputURLField.getText();
        HelloApplication.username = inputUsernameField.getText();
        HelloApplication.password = inputPasswordField.getText();
        String driver = "com.mysql.jdbc.Driver";

        try {
            Class.forName(driver);
            HelloApplication.connection = DriverManager.getConnection(HelloApplication.url, HelloApplication.username, HelloApplication.password);
            switchToQuerySelectionView();
        } catch (SQLException | ClassNotFoundException e) {
            loginText.setText("Incorrect login, try again!\n");
        }
    }

    public void switchToQuerySelectionView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("query-selection.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 640);

        QueryController queryController = fxmlLoader.getController();
        queryController.setStage(stage);
        stage.setScene(scene);
    }
}
