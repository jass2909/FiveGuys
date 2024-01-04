package at.ac.fhcampuswien.fiveguysproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Random;

public class MapController {



    @FXML
    private Pane starPane;

    public MapController(Pane starPane) {
        this.starPane = starPane;
        initializeStars();
    }

    private void moveStars() {
        Random random = new Random();
        for (var node : starPane.getChildren()) {
            node.setTranslateY(node.getTranslateY() + random.nextInt(3) + 1);
            if (node.getTranslateY() > starPane.getPrefHeight()) {
                node.setTranslateY(0);
            }
        }
    }

    private void initializeStars() {
        Random random = new Random();
        Timeline starsTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> moveStars()));
        starsTimeline.setCycleCount(Timeline.INDEFINITE);
        starsTimeline.play();
        for (int i = 0; i < 100; i++) {
            Circle star = new Circle(1, Color.WHITE);
            star.setTranslateX(random.nextDouble() * starPane.getPrefWidth());
            star.setTranslateY(random.nextDouble() * starPane.getPrefHeight());
            starPane.getChildren().add(star);
        }
    }
}
