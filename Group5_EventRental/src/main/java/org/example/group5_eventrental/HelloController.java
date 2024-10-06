package org.example.group5_eventrental;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.*;

public class HelloController {
    @FXML private TextField inputTextField;
    @FXML private Label welcomeText;
    @FXML private Label SQLstatus;
    @FXML private Label errorLabel;
    @FXML private ObservableList<ObservableList> data;
    @FXML private TableView tableView;

    @FXML protected void fetch(String query) {
        /*
        Below is code for testing. Query is read in from input box and then converted to string in the onExecuteButtonClick()
        which then passes that string to this method to display
        */

        String url = "jdbc:mysql://localhost:3306/EventRental";
        String user = "root";
        String password = "rootROOT!";
        String driver = "com.mysql.cj.jdbc.Driver";

        data = FXCollections.observableArrayList();
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);

            stmt = conn.createStatement();

            rs = stmt.executeQuery(query);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                        new SimpleStringProperty(param.getValue().get(j).toString()));

                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }

            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);
            }

            //FINALLY ADDED TO TableView
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
                SQLstatus.setText(se.getSQLState());
                errorLabel.setText(se.toString());
            }
        }
    }

    @FXML protected void onAddButtonClick() {
        welcomeText.setText("Add new entry");
    }

    @FXML protected void onUpdateButtonClick() {
        welcomeText.setText("Update existing entry");
    }

    @FXML protected void onDeleteButtonClick() {
        welcomeText.setText("Delete existing entry");
    }

    @FXML protected void onExecuteButtonClick() {
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
        
        String input = inputTextField.getText();
        fetch(input);
    }
}