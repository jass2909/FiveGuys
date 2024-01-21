package at.ac.fhcampuswien.fiveguysproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.Random;

public class Boss extends ImageView {
    private static final double BOSS_WIDTH = 50;
    private static final double BOSS_HEIGHT = 50;
    private static final double PROJECTILE_SPEED = 4.0;

    private final Pane projectilePane;
    private final SpaceInvadersController spaceInvadersController;
    private final int enemyLimit;

    public Boss(double x, double y, Pane projectilePane, SpaceInvadersController spaceInvadersController, int enemyLimit) {
        super(getBossImage());
        setFitHeight(BOSS_HEIGHT);
        setFitWidth(BOSS_WIDTH);
        setTranslateX(x);
        setTranslateY(y);

        this.projectilePane = projectilePane;
        this.spaceInvadersController = spaceInvadersController;
        this.enemyLimit = enemyLimit;

        initializeBossTimeline();
    }

    private static Image getBossImage() {
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("Boss.png");
            if (inputStream != null) {
                return new Image(inputStream);
            } else {
                throw new RuntimeException("Boss image not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading boss image", e);
        }
    }

    private void initializeBossTimeline() {
        Timeline bossTimeline = new Timeline(
                new KeyFrame(Duration.seconds(4), e -> {
                    // Check if the enemy limit is reached before spawning boss
                    if (isEnemyLimitReached()) {
                        spawnBossProjectiles();
                    }
                })
        );
        bossTimeline.setCycleCount(1); // Only spawn once
        bossTimeline.play();
    }

    private boolean isEnemyLimitReached() {
        // Implement logic to check if the enemy limit is reached
        return spaceInvadersController.getEnemyCount() >= enemyLimit;
    }

    private void spawnBossProjectiles() {
        // Boss spawning logic
        Random random = new Random();

        Timeline shootingTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    double projectileX = getTranslateX() + BOSS_WIDTH / 2 - 5; // Adjust for projectile width
                    double projectileY = getTranslateY() + BOSS_HEIGHT;
                    Projectile projectile = new Projectile(projectileX, projectileY, false); // Boss projectiles
                    projectilePane.getChildren().add(projectile);

                    Timeline projectileTimeline = new Timeline(
                            new KeyFrame(Duration.millis(16), event -> {
                                projectile.moveDown();
                                checkPlayerCollision(projectile);
                            })
                    );
                    projectileTimeline.setCycleCount(Timeline.INDEFINITE);
                    projectileTimeline.play();
                })
        );
        shootingTimeline.setCycleCount(Timeline.INDEFINITE);
        shootingTimeline.play();
    }

    private void checkPlayerCollision(Projectile projectile) {
        // Implement collision logic with the player if needed
        // You can access the player's position using its getX() and getY() methods
    }
}
