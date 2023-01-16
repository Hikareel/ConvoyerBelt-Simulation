module com.example.projektpw {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.projektpw to javafx.fxml;
    exports com.example.projektpw;
}