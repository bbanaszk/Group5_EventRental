package org.example.group5_eventrental;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class QueryController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void switchToSelectView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("select-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1320, 1220);

        SelectController selectController = fxmlLoader.getController();
        selectController.setStage(stage);

        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public void switchToUpdateView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("update-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 1000);

        UpdateController updateController = fxmlLoader.getController();
        updateController.setStage(stage);

        stage.setScene(scene);
        stage.centerOnScreen();
    }
}
