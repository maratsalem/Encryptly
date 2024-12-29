module com.example.ispcourseproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.ispcourseproject to javafx.fxml;
    exports com.example.ispcourseproject;
}