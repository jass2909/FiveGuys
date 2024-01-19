package at.ac.fhcampuswien.fiveguysproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.InputStream;

import static at.ac.fhcampuswien.fiveguysproject.SpaceInvadersController.gamePaused;
import static at.ac.fhcampuswien.fiveguysproject.SpaceInvadersController.level;


/**
 * Enemy Klasse zur Erstellung der Gegner. Handhabung von Kollisionen und Erstellen von Feind-Objekten.
 */
public class Enemy extends ImageView {

    private static final int INITIAL_LIFE = 3;
    private static int enemyCount = 0;
    private int id;

    /**
     * neues Feind Objekt initialisiert.
     *
     * @param x Bewegung x-Achse
     * @param y Bewegung y-Achse
     */
    public Enemy(double x, double y) {
        super(getEnemyImageImage());
        setFitWidth(50);
        setFitHeight(50);
        setTranslateX(x);
        setTranslateY(y);
        setUserData(INITIAL_LIFE);
        id = enemyCount++;
    }

    /**
     * Liefert das Bild des Feinder ab. Dieses ist abhängig vom aktuellen Level.
     *
     * @return Das Image-Objekt des Feindes.
     */
    private static Image getEnemyImageImage() {
        if (level == 1) {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("Python.png");
            return new Image(inputStream);
        } else if (level == 2) {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("Enemy.png");
            return new Image(inputStream);
        }

        return null;
    }

    /**
     * Methode zum erkennen der Gegner. Jeder Gegener hat eine eigene Zahl zugeorndet bekommen.
     */
    public int getEnemyId() {
        return id;
    }

    /**
     * Bewegt den Feind um die angegebene Geschwindigkeit nach unten, wenn das Spiel nicht pausiert ist.
     * speed ist die Geschw. mit der der Feind sich bewegt.
     */
    public void move(double speed) {
        if (gamePaused) {
            return;
        } else {
            setTranslateY(getTranslateY() + speed);
        }
    }

    /**
     * Handhabung der Kollisionen mit Feinden. Reduzierung der Lebenspkt. und Entfernung bei Bedarf.
     *
     * @param projectile     Das Projekttill mit dem der Feind kolidiert.
     * @param enemyPane      Die Ansicht, die den Feind enthält.
     * @param projectilePane Die Ansicht, die das Projektil enthält.
     */
    public void handleCollision(Projectile projectile, Pane enemyPane, Pane projectilePane) {
        int currentLife = (int) getUserData();
        SpaceInvadersController.decreaseEnemyCount();

        if (currentLife > 1) {
            currentLife--;
        } else if (currentLife == 1) {
            enemyPane.getChildren().remove(this);
        }
        projectilePane.getChildren().remove(projectile);
        setTranslateX(getTranslateX() + 1000);
    }
}