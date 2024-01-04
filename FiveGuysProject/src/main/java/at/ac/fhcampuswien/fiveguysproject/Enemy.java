package at.ac.fhcampuswien.fiveguysproject;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;

import static at.ac.fhcampuswien.fiveguysproject.SpaceInvadersController.gamePaused;

public class Enemy extends Circle {

    private static final int INITIAL_LIFE = 3;
    private static int enemyCount = 0;
    private int id;
;


    public Enemy(double x, double y) {
        super(10, Color.RED);
        setTranslateX(x);
        setTranslateY(y);
        setUserData(INITIAL_LIFE);
        id = enemyCount++;
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

            if (currentLife >1) {

                currentLife--;
            } else if (currentLife ==1) {
                enemyPane.getChildren().remove(this);

            }
        projectilePane.getChildren().remove(projectile);
        setTranslateX(getTranslateX()+ 1000);

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
