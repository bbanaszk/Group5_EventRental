package org.example.group5_eventrental;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DeleteController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void returnToQuerySelection() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("query-selection.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 640);

        QueryController queryController = fxmlLoader.getController();
        queryController.setStage(stage);

        stage.setScene(scene);
    }
}
