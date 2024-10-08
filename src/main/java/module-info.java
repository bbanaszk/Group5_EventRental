module org.example.group5_eventrental {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
//    requires mysql.connector.j;

    opens org.example.group5_eventrental to javafx.fxml;
    exports org.example.group5_eventrental;
}