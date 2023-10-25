module com.example.whatsup_client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.whatsup_client to javafx.fxml;
    exports com.example.whatsup_client;
}