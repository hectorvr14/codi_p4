module com.example.p4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.rmi;

    opens com.example.p4 to javafx.fxml;
    exports com.example.p4;
}