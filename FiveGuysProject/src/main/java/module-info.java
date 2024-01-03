module at.ac.fhcampuswien.fiveguysproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires com.almasb.fxgl.all;

    opens at.ac.fhcampuswien.fiveguysproject to javafx.fxml;
    exports at.ac.fhcampuswien.fiveguysproject;
}