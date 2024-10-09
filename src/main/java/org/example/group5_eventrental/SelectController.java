package org.example.group5_eventrental;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.ListSelectionView;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectController {
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

    @FXML private Label errorLabel;
    @FXML private Label SQLstatus;
    @FXML private ObservableList<ObservableList> data;
    @FXML private ListSelectionView attributeNames;
    @FXML private ListSelectionView tableNames;
    @FXML private TableView tableView;
    @FXML private TextField filter;
    @FXML private CheckBox leftJoin;

    private Map<String, List<String>> tableAttributes = new HashMap<>();
    private Map<String, List<String>> tableRelationships = new HashMap<>();

    private String url = HelloApplication.url;
    private String user = HelloApplication.username;
    private String password = HelloApplication.password;
    private String driver = HelloApplication.driver;

    private static Connection conn = HelloApplication.connection;

    private String relationshipName = "";
    private List<String> relationshipAttributeList = new ArrayList<>();
    private String joinClausePrimary = "";
    private String joinClauseSecondary = "";

    @FXML protected void initialize() {
        Statement stmt = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();

            DatabaseMetaData dmbd = conn.getMetaData();
            ResultSet tables = dmbd.getTables(conn.getCatalog(), "EventRental", "%", new String[]{"TABLE"});
            stmt = conn.createStatement();

            tableAttributes.clear();

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");

                if (tableName.contains("_")) {
                    tableRelationships.put(tableName, new ArrayList<>());
                    continue;
                }

                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

                tableAttributes.put(tableName, new ArrayList<>());
                tableNames.getSourceItems().add(tableName);

                ResultSetMetaData rsmd = rs.getMetaData();
                int cols = rsmd.getColumnCount();

                for (int i = 1; i <= cols; i++) {
                    String columnName = rsmd.getColumnName(i);
                    tableAttributes.get(tableName).add(columnName);
                }
                rs.close();
            }

            updateTables();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (stmt != null) stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML protected void fetch(List<String> selectedColumns, List<String> selectedTables, String filterCondition) {
        data = FXCollections.observableArrayList();
        ResultSet rs = null;
        Statement stmt = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();

            String columns = String.join(", ", selectedColumns);
            String tables = String.join(", ", selectedTables);

            StringBuilder sql = new StringBuilder();

            if (selectedTables.size() == 2) {
                loadTableRelationships(selectedTables);

                if (leftJoin.isSelected()) {
                    sql.append("SELECT DISTINCT ").append(columns).append(" FROM ").append(selectedTables.get(0));
                    sql.append(" LEFT JOIN ").append(relationshipName).append(" ON ").append(joinClausePrimary);
                    sql.append(" LEFT JOIN ").append(selectedTables.get(1)).append(" ON ").append(joinClauseSecondary);
                    sql.append(";");
                } else {
                    sql.append("SELECT DISTINCT ").append(columns).append(" FROM ").append(tables).append(",").append(relationshipName).append(" WHERE ");
                    if (filterCondition == null || filterCondition.isEmpty()) {
                        sql.append(joinClausePrimary).append(" AND ").append(joinClauseSecondary).append(";");
                    } else {
                        sql.append(joinClausePrimary).append(" AND ").append(joinClauseSecondary).append(" AND ");
                        String attr = filterCondition.split("=")[0];
                        if (joinClausePrimary.contains(attr)) {
                            sql.append(selectedTables.get(0)).append(".").append(filterCondition).append(";");
                        } else if (joinClauseSecondary.contains(attr)) {
                            sql.append(selectedTables.get(1)).append(".").append(filterCondition).append(";");
                        }
                    }
                }
            } else {
                sql.append("SELECT DISTINCT ").append(columns).append(" FROM ").append(tables);
                if (filterCondition == null || filterCondition.isEmpty()) {
                    sql.append(";");
                } else {
                    sql.append(" WHERE ").append(filterCondition).append(";");
                }
            }

            SQLstatus.setText(sql.toString());

            rs = stmt.executeQuery(sql.toString());

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
            errorLabel.setText(se.toString());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                errorLabel.setText(se.toString());
            }
        }
    }

    protected void updateTables() {
        tableNames.getTargetItems().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {

                if (tableNames.getTargetItems().size() != 2) {
                    leftJoin.setDisable(true);
                } else {
                    leftJoin.setDisable(false);
                }

                if (change.wasAdded() || change.wasReplaced()) {
                    for (String tableName : change.getAddedSubList()) {
                        updateAttributeNames(tableName);
                    }

                    if (tableNames.getTargetItems().size() == 2) {
                        loadTableRelationships(tableNames.getTargetItems());
                        for (String attributeName : relationshipAttributeList) {
                            attributeNames.getSourceItems().add(relationshipName + "." + attributeName);
                        }
                    }

                    if (tableNames.getTargetItems().size() > 2) {
                        removeAttributeNames(relationshipName);
                    }
                }
                if (change.wasRemoved()) {
                    for (String tableName : change.getRemoved()) {
                        removeAttributeNames(tableName);
                    }

                    if (tableNames.getTargetItems().size() == 2) {
                        loadTableRelationships(tableNames.getTargetItems());
                        for (String attributeName : relationshipAttributeList) {
                            attributeNames.getSourceItems().add(relationshipName + "." + attributeName);
                        }
                    }

                    if (tableNames.getTargetItems().size() < 2 || tableNames.getTargetItems().size() > 2) {
                        removeAttributeNames(relationshipName);
                    }
                }

                leftJoin.setSelected(false);
            }
        });
    }

    private void updateAttributeNames(String tableName) {
        List<String> attributes = tableAttributes.get(tableName);
        if (attributes != null) {
            for (String attribute : attributes) {

                attributeNames.getSourceItems().add(tableName + "." + attribute);
            }
        } else {
            attributeNames.getSourceItems().clear();
        }
    }

    private void removeAttributeNames(String tableName) {
        attributeNames.getTargetItems().removeIf(attribute -> ((String) attribute).startsWith(tableName + "."));
        attributeNames.getSourceItems().removeIf(attribute -> ((String) attribute).startsWith(tableName + "."));
    }

    @FXML protected void onSelectQueryButtonClick() {
        if (tableView != null) {
            tableView.getItems().clear();
            tableView.getColumns().clear();
        }
        if (data != null) {
            data.clear();
        }
        if (errorLabel != null) {
            errorLabel.setText("");
        }
        if (SQLstatus != null) {
            SQLstatus.setText("");
        }

        List<String> selectedTables = tableNames.getTargetItems();
        List<String> selectedColumns = attributeNames.getTargetItems();
        String filterCondition = filter.getText();
        fetch(selectedColumns, selectedTables, filterCondition);
    }

    protected void loadTableRelationships(List<String> selectedTables) {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            DatabaseMetaData metaData = conn.getMetaData();
            for (String relationship : tableRelationships.keySet()) {
                ResultSet entity1 = metaData.getCrossReference(conn.getCatalog(), "EventRental", selectedTables.get(0), conn.getCatalog(), "EventRental", relationship);
                ResultSet entity2 = metaData.getCrossReference(conn.getCatalog(), "EventRental", selectedTables.get(1), conn.getCatalog(), "EventRental", relationship);
                ResultSet columns = metaData.getColumns(conn.getCatalog(), "EventRental", relationship, null);

                boolean entity1Linked = false;
                boolean entity2Linked = false;

                String pkColumnPrimary = "";
                String fkColumnPrimary = "";
                String pkColumnSecondary = "";
                String fkColumnSecondary = "";

                while (entity1.next()) {
                    String fkTable = entity1.getString("FKTABLE_NAME");
                    if (fkTable.equals(relationship)) {
                        entity1Linked = true;
                        pkColumnPrimary = entity1.getString("PKCOLUMN_NAME");
                        fkColumnPrimary = entity1.getString("FKCOLUMN_NAME");
                    }
                }

                while (entity2.next()) {
                    String fkTable = entity2.getString("FKTABLE_NAME");
                    if (fkTable.equals(relationship)) {
                        entity2Linked = true;
                        pkColumnSecondary = entity2.getString("PKCOLUMN_NAME");
                        fkColumnSecondary = entity2.getString("FKCOLUMN_NAME");
                    }
                }

                if (entity1Linked && entity2Linked) {
//                    System.out.println("Relationship table found linking " + selectedTables.get(0) + " and " + selectedTables.get(1) + ": " + relationship);
                    relationshipName = relationship;
                    relationshipAttributeList = new ArrayList<>();
                    while (columns.next()) {
                        relationshipAttributeList.add(columns.getString("COLUMN_NAME"));
                    }
                    joinClausePrimary = relationship + "." + fkColumnPrimary + "=" + selectedTables.get(0) + "." + pkColumnPrimary;
                    joinClauseSecondary = relationship + "." + fkColumnSecondary + "=" + selectedTables.get(1) + "." + pkColumnSecondary;
                    return;
                }
            }

            relationshipName = "";
            relationshipAttributeList = new ArrayList<>();
            joinClausePrimary = "";
            joinClauseSecondary = "";
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                errorLabel.setText(se.toString());
            }
        }
    }
}
