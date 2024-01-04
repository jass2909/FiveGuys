package at.ac.fhcampuswien.fiveguysproject;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Projectile extends Circle {
    private static final int PROJECTILE_RADIUS = 5;
    private static final Color PROJECTILE_COLOR = Color.BLUE;
    private static final double PROJECTILE_SPEED = 3.0;

    public Projectile(double x, double y) {
        super(PROJECTILE_RADIUS, PROJECTILE_COLOR);
        setTranslateX(x + 20);
        setTranslateY(y + 300);
    }

    public void move() {
        setTranslateY(getTranslateY() - PROJECTILE_SPEED);
    }
        // Existing code...

    public boolean checkCollision(Enemy enemy, Pane projectilePane) {
        if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
            projectilePane.getChildren().remove(this);
            setTranslateX(getTranslateX()+1000);
            return true;// Kollision erfolgt

        }
        return false; // Keine Kollision
    }
}
