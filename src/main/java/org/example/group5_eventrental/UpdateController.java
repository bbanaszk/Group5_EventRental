package org.example.group5_eventrental;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateController {
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

    @FXML private RadioButton client;
    @FXML private RadioButton employee;
    @FXML private RadioButton equipment;
    @FXML private RadioButton event;
    @FXML private RadioButton storageroom;
    @FXML private Label button;
    @FXML private VBox attributes;
    private ToggleGroup group;
    private List<String> tableAttributes = new ArrayList<>();
    @FXML private ObservableList<ObservableList> data;
    @FXML private TableView tableView;
    @FXML private Button saveButton;
    @FXML private Button discardButton;

    public void initialize() {
        group = new ToggleGroup();
        client.setToggleGroup(group);
        employee.setToggleGroup(group);
        equipment.setToggleGroup(group);
        event.setToggleGroup(group);
        storageroom.setToggleGroup(group);
    }

    @FXML private void checkSelectedButton() throws SQLException {
        if (tableView != null) {
            tableView.getItems().clear();
            tableView.getColumns().clear();
        }
        if (data != null) {
            data.clear();
        }
        RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
        if (selectedButton != null) {
            String option = selectedButton.getText();

            data = FXCollections.observableArrayList();
            ResultSet rs = null;
            Statement stmt = null;
            Connection conn = null;
            String url = "jdbc:mysql://localhost:3306/EventRental";
            String user = "root";
            String password = "rootROOT!";
            String driver = "com.mysql.cj.jdbc.Driver";

            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url, user, password);
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM " + option);
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                            new SimpleStringProperty(param.getValue().get(j).toString()));
                    tableView.getColumns().addAll(col);
                }
                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        row.add(rs.getString(i));
                    }
                    data.add(row);
                }
                tableView.setItems(data);
            } catch (Exception se) {
                System.out.println(se.getMessage());
            } finally {
                try {
                    if (stmt != null)
                        stmt.close();
                    if (rs != null)
                        rs.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    System.out.println(se.getMessage());
                }
            }
        }
    }

    @FXML private VBox inputFieldsContainer;
    private List<TextField> inputFields = new ArrayList<>();

    @FXML private void updateSelectedRow() {
        ObservableList<String> selectedRow = (ObservableList<String>) tableView.getSelectionModel().getSelectedItem();

        if (selectedRow == null) {
            System.out.println("No row selected.");
            return;
        }

        inputFieldsContainer.getChildren().clear();
        inputFields.clear();

        for (int i = 0; i < selectedRow.size(); i++) {
            TextField textField = new TextField();
            textField.setText(selectedRow.get(i));

            inputFieldsContainer.getChildren().add(textField);
            inputFields.add(textField);
        }

        List<String> str = new ArrayList<>();
        for (TextField textField : inputFields) {
            str.add(textField.getText());
        }

        saveButton.setVisible(true);
        discardButton.setVisible(true);
    }

    @FXML private void addNewRow() {
        inputFieldsContainer.getChildren().clear();
        inputFields.clear();

        saveButton.setVisible(false);
        discardButton.setVisible(false);
    }

    @FXML private void deleteSelectedRow() {
        inputFieldsContainer.getChildren().clear();
        inputFields.clear();

        saveButton.setVisible(false);
        discardButton.setVisible(false);
    }

    @FXML private void saveChanges() {
        inputFieldsContainer.getChildren().clear();
        inputFields.clear();

        saveButton.setVisible(false);
        discardButton.setVisible(false);
    }

    @FXML private void discardChanges() {
        inputFieldsContainer.getChildren().clear();
        inputFields.clear();

        saveButton.setVisible(false);
        discardButton.setVisible(false);
    }
}
