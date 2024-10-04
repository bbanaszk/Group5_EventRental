module org.example.group5_eventrental {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.group5_eventrental to javafx.fxml;
    exports org.example.group5_eventrental;
}