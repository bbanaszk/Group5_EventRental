package org.example.group5_eventrental;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
        Scene scene = new Scene(fxmlLoader.load(), 300, 300);

        QueryController queryController = fxmlLoader.getController();
        queryController.setStage(stage);

        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML private RadioButton client;
    @FXML private RadioButton employee;
    @FXML private RadioButton equipment;
    @FXML private RadioButton event;
    @FXML private RadioButton storageroom;
    @FXML private Label button;
    @FXML private Label errorLabel;
    @FXML private VBox attributes;
    private ToggleGroup group;
    private List<String> tableAttributes = new ArrayList<>();
    private List<String> columnTypes = new ArrayList<>();
    @FXML private ObservableList<ObservableList> data;
    @FXML private TableView tableView;
    @FXML private Button saveButtonAdd;
    @FXML private Button saveButtonUpdate;
    @FXML private Button discardButton;
    @FXML private Label confirmationMessage;
    @FXML private Button delete;
    @FXML private Button discard;
    private ObservableList<String> selectedRow;
    @FXML private VBox inputFieldsContainer;
    private List<TextField> inputFields = new ArrayList<>();
    private List<String> columnNames = new ArrayList<>();
    private String tableName;
    private List<String> primaryKeys = new ArrayList<>();

    public void initialize() {
        group = new ToggleGroup();
        client.setToggleGroup(group);
        employee.setToggleGroup(group);
        equipment.setToggleGroup(group);
        event.setToggleGroup(group);
        storageroom.setToggleGroup(group);
    }

    private void resetLabels() {
        saveButtonAdd.setVisible(false);
        saveButtonUpdate.setVisible(false);
        discardButton.setVisible(false);
        delete.setVisible(false);
        discard.setVisible(false);
        confirmationMessage.setText("");
        errorLabel.setText("");
        inputFieldsContainer.setVisible(false);
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    @FXML private void checkSelectedButton() throws SQLException {
        resetLabels();

        if (tableView != null) {
            tableView.getItems().clear();
            tableView.getColumns().clear();
        }
        if (data != null) {
            data.clear();
        }

        inputFieldsContainer.getChildren().clear();
        inputFields.clear();

        columnTypes = new ArrayList<>();

        RadioButton selectedButton = (RadioButton) group.getSelectedToggle();

        if (selectedButton != null) {
            String option = selectedButton.getText();
            tableName = option;

            data = FXCollections.observableArrayList();
            ResultSet rs = null;
            Statement stmt = null;
            Connection conn = null;
            String url = "jdbc:mysql://localhost:3306/EventRental";
            String user = "root";
            String password = "rootROOT!";
            // String url = HelloApplication.url;
            // String user = HelloApplication.username;
            // String password = HelloApplication.password;
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

                    int columnSize = rs.getMetaData().getPrecision(i + 1);
                    String columnType = rs.getMetaData().getColumnTypeName(i + 1);

                    String column = columnType + "(" + columnSize + ")";
                    columnTypes.add(column);
                    tableAttributes.add(rs.getMetaData().getColumnName(i + 1));
                }
                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        row.add(rs.getString(i));
                    }
                    data.add(row);
                }
                tableView.setItems(data);

                System.out.println(columnTypes);
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

    @FXML private void updateSelectedRow() {
        resetLabels();

        selectedRow = (ObservableList<String>) tableView.getSelectionModel().getSelectedItem();

        if (selectedRow == null) {
            errorLabel.setText("No row selected.");
            return;
        } else {
            errorLabel.setText("");
        }

        inputFieldsContainer.getChildren().clear();
        inputFields.clear();
        inputFieldsContainer.setVisible(true);

        for (int i = 0; i < selectedRow.size(); i++) {
            String column = columnTypes.get(i);

            Label name = new Label(tableAttributes.get(i));
            name.setPrefWidth(100.0);
            name.setStyle("-fx-text-fill: #030303; -fx-opacity: 0.5;");

            Label label = new Label(column);
            label.setPrefWidth(100.0);
            label.setStyle("-fx-text-fill: #4A90E2; -fx-opacity: 0.5;");

            TextField textField = new TextField();
            textField.setText(selectedRow.get(i));

            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER_LEFT);
            box.getChildren().addAll(name, textField, label);

            inputFieldsContainer.getChildren().add(box);
            inputFields.add(textField);
        }

        List<String> str = new ArrayList<>();
        for (TextField textField : inputFields) {
            str.add(textField.getText());
        }

        saveButtonUpdate.setVisible(true);
        discardButton.setVisible(true);
    }

    @FXML private void addNewRow() {
        resetLabels();

        List<TableColumn> tableColumns = tableView.getColumns();
        columnNames = new ArrayList<>();
        for (TableColumn col : tableColumns) {
            String columnName = col.getText();
            columnNames.add(columnName);
        }

        // System.out.println(str);
        inputFieldsContainer.getChildren().clear();
        inputFields.clear();
        inputFieldsContainer.setVisible(true);

        for (int i = 0; i < columnNames.size(); i++) {
            String columnType = columnTypes.get(i);
            String columnName = columnNames.get(i);

            Label label = new Label(columnType);
            label.setPrefWidth(100.0);
            label.setStyle("-fx-text-fill: #4A90E2; -fx-opacity: 0.5;");

            TextField textField = new TextField();
            textField.setPromptText(columnName);

            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER_LEFT);
            box.getChildren().addAll(textField, label);

            inputFieldsContainer.getChildren().add(box);
            inputFields.add(textField);
        }

        List<String> str = new ArrayList<>();
        for (TextField textField : inputFields) {
            str.add(textField.getText());
        }

        saveButtonAdd.setVisible(true);
        discardButton.setVisible(true);
    }


    @FXML private void deleteSelectedRow() {
        resetLabels();

        selectedRow = (ObservableList<String>) tableView.getSelectionModel().getSelectedItem();

        if (selectedRow == null) {
            errorLabel.setText("No row selected.");
            return;
        } else {
            errorLabel.setText("");
        }

        confirmationMessage.setText("Are you sure that you want to delete the selected row?");

        delete.setVisible(true);
        discard.setVisible(true);
    }

    @FXML private void confirmDelete() {
        confirmationMessage.setText("");

        List<String> attributeNames = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        for (int i = 0; i < selectedRow.size(); i++) {
            String attributeName = tableAttributes.get(i);
            String value = selectedRow.get(i);
            attributeNames.add(attributeName);
            columnNames.add(value);
        }

        deleteSQL(tableName, attributeNames, columnNames);

        inputFieldsContainer.getChildren().clear();
        inputFields.clear();

        delete.setVisible(false);
        discard.setVisible(false);
    }

    @FXML private void discardDelete() {
        resetLabels();

        confirmationMessage.setText("");

        delete.setVisible(false);
        discard.setVisible(false);
    }

    @FXML private void saveChangesUpdate() {
        resetLabels();

        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/EventRental";
        String user = "root";
        String password = "rootROOT!";
        // String url = HelloApplication.url;
        // String user = HelloApplication.username;
        // String password = HelloApplication.password;
        // String driver = HelloApplication.driver;
        String driver = "com.mysql.cj.jdbc.Driver";

        primaryKeys = new ArrayList<>();

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet pkResultSet = meta.getPrimaryKeys(conn.getCatalog(), "EventRental", tableName);

            while (pkResultSet.next()) {
                String primaryKey = pkResultSet.getString("COLUMN_NAME");
                primaryKeys.add(primaryKey);
            }

            pkResultSet.close();

            List<String> primaryKeyValues = new ArrayList<>();

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                        new SimpleStringProperty(param.getValue().get(j).toString()));

                String name = rs.getMetaData().getColumnName(i + 1);

                if (primaryKeys.contains(name)) {
                    primaryKeyValues.add(selectedRow.get(i).toString());
                }
            }

            List<String> updatedFields = new ArrayList<>();
            List<String> columns = new ArrayList<>();

            for (Node node : inputFieldsContainer.getChildren()) {
                if (node instanceof HBox) {
                    HBox box = (HBox) node;
                    TextField textField = (TextField) box.getChildren().get(1);
                    Label label = (Label) box.getChildren().get(0);
                    updatedFields.add(textField.getText());
                    columns.add(label.getText());
                }
            }

            System.out.println("DEBUG: Primary Keys = " + primaryKeys);
            System.out.println("DEBUG: Primary Key Values = " + primaryKeyValues);
            // updateSQL(tableName, primaryKeys, primaryKeyValues);

            updateSQL(tableName, columns, updatedFields, primaryKeys, primaryKeyValues);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                errorLabel.setText(se.getMessage());
            }
        }

        inputFieldsContainer.getChildren().clear();
        inputFields.clear();

        saveButtonUpdate.setVisible(false);
        discardButton.setVisible(false);
    }

    @FXML private void discardChangesUpdate() {
        resetLabels();

        inputFieldsContainer.getChildren().clear();
        inputFields.clear();
    }

    @FXML private void saveChangesInsert() {
        resetLabels();

        List<String> updatedValues = new ArrayList<>();
        for (TextField textField : inputFields) {
            updatedValues.add(textField.getText());
        }

        for (String value : updatedValues) {
            System.out.println(value);
        }

        List<String> insertFields = new ArrayList<>();

        for (Node node : inputFieldsContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox box = (HBox) node;

                TextField textField = (TextField) box.getChildren().get(0);
                insertFields.add(textField.getText());
            }
        }

        insertSQL(tableName, insertFields);

        inputFieldsContainer.getChildren().clear();
        inputFields.clear();

        saveButtonAdd.setVisible(false);
        discardButton.setVisible(false);
    }

    private void updateSQL(String tableName, List<String> columns, List<String> updatedFields, List<String> primaryKey, List<String> pkValue) {
        PreparedStatement stmt = null;
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/EventRental";
        String user = "root";
        String password = "rootROOT!";
        // String url = HelloApplication.url;
        // String user = HelloApplication.username;
        // String password = HelloApplication.password;
        // String driver = HelloApplication.driver;
        String driver = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);

            StringBuilder str = new StringBuilder("UPDATE " + tableName + " SET ");

            for (int i = 0; i < updatedFields.size(); i++) {
                if (i < updatedFields.size() - 1) {
                    str.append(columns.get(i)).append(" = ").append("'").append(updatedFields.get(i)).append("'").append(", ");
                } else {
                    str.append(columns.get(i)).append(" = ").append("'").append(updatedFields.get(i)).append("'");
                }
            }

            str.append(" WHERE ").append(primaryKey.get(0)).append(" = ").append("'").append(pkValue.get(0)).append("'");

            stmt = conn.prepareStatement(str.toString());
            try {
                stmt.executeUpdate();
                errorLabel.setStyle("-fx-text-fill: black;");
                errorLabel.setText("Successfully added new entry");
                errorLabel.setVisible(true);
            } catch (SQLException se) {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(se.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                errorLabel.setText(se.getMessage());
            }
        }
    }

    private void insertSQL(String tableName, List<String> insertFields) {
        PreparedStatement stmt = null;
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/EventRental";
        String user = "root";
        String password = "rootROOT!";
        // String url = HelloApplication.url;
        // String user = HelloApplication.username;
        // String password = HelloApplication.password;
        // String driver = HelloApplication.driver;
        String driver = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);

            StringBuilder str = new StringBuilder("INSERT INTO " + tableName + " VALUES(");

            for (int i = 0; i < insertFields.size(); i++) {
                if (i < insertFields.size() - 1) {
                    str.append("?, ");
                } else {
                    str.append("?);");
                }
            }

            stmt = conn.prepareStatement(str.toString());

            for (int i = 1; i <= insertFields.size(); i++) {
                stmt.setString(i, insertFields.get(i - 1));
            }

            try {
                stmt.executeUpdate();
                errorLabel.setStyle("-fx-text-fill: black;");
                errorLabel.setText("Successfully added new entry");
                errorLabel.setVisible(true);
            } catch (SQLException se) {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(se.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                errorLabel.setText(se.getMessage());
            }
        }
    }

    private void deleteSQL(String tableName, List<String> columnNames, List<String> entryFields) {
        Statement stmt = null;
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/EventRental";
        String user = "root";
        String password = "rootROOT!";
        // String url = HelloApplication.url;
        // String user = HelloApplication.username;
        // String password = HelloApplication.password;
        // String driver = HelloApplication.driver;
        String driver = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);

            StringBuilder attr = new StringBuilder();

            for (int i = 0; i < entryFields.size(); i++) {
                String columnName = columnNames.get(i);
                if (i < (entryFields.size() - 1)) {
                    attr.append(columnName).append("=").append("'").append(entryFields.get(i)).append("'").append(" AND ");
                } else {
                    attr.append(columnName).append("=").append("'").append(entryFields.get(i)).append("'").append(";");
                }
            }

            String sqlQuery = "DELETE FROM " + tableName + " WHERE " + attr;
            System.out.println(sqlQuery);

            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(sqlQuery);
                errorLabel.setStyle("-fx-text-fill: black;");
                errorLabel.setText("Successfully deleted entry");
                errorLabel.setVisible(true);
            } catch (SQLException se) {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(se.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                errorLabel.setText(se.getMessage());
            }
        }
    }
}
