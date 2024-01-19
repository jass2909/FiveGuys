package at.ac.fhcampuswien.fiveguysproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Die SpaceInvadersApplication-Klasse ist die Hauptklasse der Anwendung.
 * Sie erweitert die JavaFX Application-Klasse und initialisiert das Hauptfenster der Anwendung.
 */
public class SpaceInvadersApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SpaceInvadersApplication.class.getResource("hello-view.fxml"));  // Laden der FXML-Datei für die Benutzeroberfläche
        Scene scene = new Scene(fxmlLoader.load(), 800, 748, Color.BLACK);  // Erstellen der Szene mit der geladenen Benutzeroberfläche und Hintergrundfarbe


        SpaceInvadersController spaceInvadersController = fxmlLoader.getController();   // Zugriff auf den SpaceInvadersController, um Tastatureingaben zu behandeln

        scene.setOnKeyPressed(spaceInvadersController); // Setzen des Event Handlers für Tastatureingaben auf die Szene

        // Konfigurieren des Hauptfensters
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    /**
     * Die main-Methode startet die Anwendung.
     *
     * @param args Die Argumente, die von der Befehlszeile übergeben werden (nicht verwendet).
     */
    public static void main(String[] args) {
        launch();
    }
}
