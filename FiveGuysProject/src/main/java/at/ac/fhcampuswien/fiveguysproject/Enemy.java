package at.ac.fhcampuswien.fiveguysproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static at.ac.fhcampuswien.fiveguysproject.SpaceInvadersController.gamePaused;
import static at.ac.fhcampuswien.fiveguysproject.SpaceInvadersController.level;

public class Enemy extends ImageView {

    private static final int INITIAL_LIFE = 3;
    private static int enemyCount = 0;
    private int id;

    public Enemy(double x, double y) {
        super(getEnemyImageImage());
        setFitWidth(50);
        setFitHeight(50);
        setTranslateX(x);
        setTranslateY(y);
        setUserData(INITIAL_LIFE);
        id = enemyCount++;
    }

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

    public int getEnemyId() {
        return id;
    }

    public void move(double speed) {
        if (gamePaused) {
            return;
        } else {
            setTranslateY(getTranslateY() + speed);
        }
    }

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

    public static List<Enemy> createEnemyRow(double startX, double startY, int count) {
        List<Enemy> enemies = new ArrayList<>();

        for (int col = 0; col < count; col++) {
            Enemy enemy = new Enemy(startX + col * 50, startY);
            enemies.add(enemy);
        }

        return enemies;
    }
}
