package at.ac.fhcampuswien.fiveguysproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.InputStream;

import static at.ac.fhcampuswien.fiveguysproject.SpaceInvadersController.gamePaused;

public class Projectile extends ImageView {
    private static final int PROJECTILE_RADIUS = 5;
    private static final Color PROJECTILE_COLOR = Color.BLUE;
    private static final double PROJECTILE_SPEED = 3.0;

    public Projectile(double x, double y) {
        super(getProjectileImage());
        setFitHeight(30);
        setFitWidth(30);
        setTranslateX(x - 5);
        setTranslateY(y + 240);
    }

    private static Image getProjectileImage() {
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("Projectile.png");
            if (inputStream != null) {
                return new Image(inputStream);
            } else {
                throw new RuntimeException("Enemy image not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading enemy image", e);
        }
    }

    public void move() {
        if (gamePaused){
            return;

        } else setTranslateY(getTranslateY() - PROJECTILE_SPEED);

    }

    public boolean checkCollision(Enemy enemy, Pane projectilePane) {
        if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
            projectilePane.getChildren().remove(this);
            setTranslateX(getTranslateX()+1000);
            return true;// Kollision erfolgt

        }
        return false; // Keine Kollision
    }
}
