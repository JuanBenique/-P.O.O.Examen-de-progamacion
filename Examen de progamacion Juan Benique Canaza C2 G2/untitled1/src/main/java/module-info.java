module pe.edu.upeu {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires static lombok;
    requires org.kordamp.bootstrapfx.core;

    opens pe.edu.upeu.controller to javafx.fxml;

    exports pe.edu.upeu;
}