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
        Scene scene = new Scene(fxmlLoader.load(), 1220, 1040);

        SelectController selectController = fxmlLoader.getController();
        selectController.setStage(stage);

        stage.setScene(scene);
    }

    public void switchToInsertView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("insert-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 640);

        InsertController insertController = fxmlLoader.getController();
        insertController.setStage(stage);

        stage.setScene(scene);
    }

    public void switchToUpdateView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("update-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 640);

        UpdateController updateController = fxmlLoader.getController();
        updateController.setStage(stage);

        stage.setScene(scene);
    }

    public void switchToDeleteView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("delete-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 820, 640);

        DeleteController deleteController = fxmlLoader.getController();
        deleteController.setStage(stage);

        stage.setScene(scene);
    }
}
