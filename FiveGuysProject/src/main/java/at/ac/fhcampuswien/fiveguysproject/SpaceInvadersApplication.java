package at.ac.fhcampuswien.fiveguysproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Die SpaceInvadersApplication-Klasse ist die Hauptklasse der Anwendung.
 * Sie erweitert die JavaFX Application-Klasse und initialisiert das Hauptfenster der Anwendung.
 */
public class SpaceInvadersApplication extends Application {
    SoundController soundController = new SoundController("/BgMusic.mp3");

    @Override
    public void start(Stage stage) throws Exception {

        // Aufruf der Hintergrundmusik in Hauptmenü
        soundController.playBackgroundMusic();
        soundController.setBackgroundMusicVolume(0.5);

        // Aufruf des Spielfensters in gewünschtem Format.
        FXMLLoader intermediateLoader = new FXMLLoader(getClass().getResource("IntermediateScene.fxml"));
        Scene intermediateScene = new Scene(intermediateLoader.load(), 800, 748, Color.WHITE);

        // Fügt Eventhandler hinzu, der auf Mausclick auf die Intermediate Scene reagiert und die Load Screen Methode aufruft, um Startbildschirm zu laden.
        intermediateScene.setOnMouseClicked((MouseEvent event) -> {
            try {
                loadStartScreen(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        stage.setScene(intermediateScene); // Setzt die Szene für das Hauptfenster Stage auf die erstellte Intermediate Scene.
        stage.setResizable(false); // Fenster kann nicht vergrößert werden.
        stage.show(); // Zeigt Intermediate Scene an.
    }

    private void loadStartScreen(Stage stage) throws IOException {
        FXMLLoader startScreenLoader = new FXMLLoader(SpaceInvadersApplication.class.getResource("start-screen.fxml"));
        Scene startScreenScene = new Scene(startScreenLoader.load(), 800, 748, Color.BLACK);
        startScreenScene.getStylesheets().add(getClass().getResource("css.css").toExternalForm());

        StartScreenController startScreenController = startScreenLoader.getController();
        startScreenController.setSoundController(soundController);


        startScreenController.playGameButton.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(SpaceInvadersApplication.class.getResource("hello-view.fxml")); // Laden der FXML-Datei für die Benutzeroberfläche
            Scene gameScene = null; // Erstellen der Szene mit der geladenen Benutzeroberfläche und Hintergrundfarbe
            try {
                gameScene = new Scene(fxmlLoader.load(), 800, 748, Color.BLACK);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            SpaceInvadersController spaceInvadersController = fxmlLoader.getController(); // Zugriff auf den SpaceInvadersController, um Tastatureingaben zu behandeln
            gameScene.setOnKeyPressed(spaceInvadersController); // Setzen des Event Handlers für Tastatureingaben auf die Szene

            soundController.stopBackgroundMusic();
            soundController.setBackgroundMusic("/GameMusic.mp3");
            soundController.playBackgroundMusic();
            startScreenController.playGame();
            stage.setScene(gameScene);
            gameScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        });

        stage.setScene(startScreenScene);

    }
}