package at.ac.fhcampuswien.fiveguysproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

/**
 * Die MapController-Klasse steuert die Hintergrundanimation der Sterne im Spiel.
 */
public class MapController {
    @FXML
    private Pane starPane;
    private SpaceInvadersController spaceInvadersController;
    Timeline starsTimeline;

    /**
     * Konstruktor für die MapController-Klasse. Initialisiert einen neuen MapController
     * mit dem übergebenen Pane für Sterne und initialisiert die Sterne.
     *
     * @param starPane Das Pane, in dem die Sterne angezeigt werden sollen.
     */
    public MapController(Pane starPane) {
        this.starPane = starPane;
        initializeStars();
    }

    /**
     * Bewegt die Sterne nach unten und setzt ihre Position am oberen Rand des Panes zurück,
     * wenn sie den unteren Rand erreicht haben.
     */
    private void moveStars() {
        Random random = new Random();
        for (var node : starPane.getChildren()) {
            node.setTranslateY(node.getTranslateY() + random.nextInt(3) + 1);
            if (node.getTranslateY() > starPane.getPrefHeight()) {
                node.setTranslateY(0);
            }
        }
    }

    /**
     * Initialisiert die Sterne im Pane und startet die Hintergrundanimation mit einer Timeline.
     */
    private void initializeStars() {
        Random random = new Random();
        starsTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> moveStars()));
        starsTimeline.setCycleCount(Timeline.INDEFINITE); // Setzt Anzahl der Wiederholungen auf unendlich.
        starsTimeline.play();
        for (int i = 0; i < 100; i++) {
            Circle star = new Circle(1, Color.WHITE);
            star.setTranslateX(random.nextDouble() * starPane.getPrefWidth()); // Setzt den Stern auf eine zufällige Position auf der x-Achse des Star Panes.
            star.setTranslateY(random.nextDouble() * starPane.getPrefHeight()); // Setzt den Stern auf eine zufällige Position auf der y-Achse des Star Panes.
            starPane.getChildren().add(star);
        }
    }

    public Timeline getStarsTimeline() {
        return starsTimeline;
    }
}

