package at.ac.fhcampuswien.fiveguysproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.InputStream;

import static at.ac.fhcampuswien.fiveguysproject.SpaceInvadersController.gamePaused;

/**
 * Die Projectile-Klasse repräsentiert ein Projektil im Space Invaders-Spiel.
 * Sie erweitert die ImageView-Klasse und enthält Methoden zum Bewegen
 * und Überprüfen von Kollisionen mit Feinden.
 */
public class Projectile extends ImageView {
    private static final double PROJECTILE_SPEED = 3.0;

    /**
     * Konstruktor für die Projectile-Klasse. Initialisiert ein neues Projektil
     * mit der übergebenen Position und einem Bild.
     *
     * @param x Die X-Position des Projektils.
     * @param y Die Y-Position des Projektils.
     */

    public Projectile(double x, double y) {
        super(getProjectileImage());
        setFitHeight(30);
        setFitWidth(30);
        setTranslateX(x - 5);
        setTranslateY(y + 240);
    }

    /**
     * Lädt das Bild für das Projektil aus einer Ressourcendatei.
     *
     * @return Das Image-Objekt für das Projektil.
     */
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

    /**
     * Bewegt das Projektil nach oben, wenn das Spiel nicht pausiert ist.
     */
    public void move() {
        if (gamePaused) {
            return;

        } else setTranslateY(getTranslateY() - PROJECTILE_SPEED);

    }

    /**
     * Überprüft, ob das Projektil mit einem Feind kollidiert ist.
     * Entfernt das Projektil und verschiebt es außerhalb der Ansicht, wenn eine Kollision erfolgt ist.
     *
     * @param enemy          Der Feind, mit dem die Kollision überprüft werden soll.
     * @param projectilePane Die Ansicht, die das Projektil enthält.
     * @return true, wenn eine Kollision erfolgt ist, andernfalls false.
     */
    public boolean checkCollision(Enemy enemy, Pane projectilePane) {
        if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
            projectilePane.getChildren().remove(this);
            setTranslateX(getTranslateX() + 1000);  // Verschiebt das Projektil außerhalb der Ansicht.
            return true;    // Kollision erfolgt
        }
        return false;   // Keine Kollision
    }
}
