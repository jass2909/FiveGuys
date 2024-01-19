package at.ac.fhcampuswien.fiveguysproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import java.util.*;

public class SpaceInvadersController implements GameController {

    public ImageView player;
    private Map<Integer, Enemy> enemiesMap = new HashMap<>();
    public void addEnemy(Enemy enemy){
        enemiesMap.put(enemy.getEnemyId(), enemy);
    }
    @FXML
    private Button startButton;
    public Button pauseButton;
    @FXML
    private Pane starPane;
    private MapController mapController;
    @FXML
    public Pane enemyPane;
    public Pane projectilePane;
    private Timeline projectileTimeline;
    private ImageView playerImageView;
    @FXML
    private ImageView pauseButtonImage;
    private Timeline timeline;
    public static int enemyCount = 0;
    private int enemyLimit = 10;
    public static int level = 1;
    private double playerX = 0;
    private double leftBorder = -380;
    private double rightBorder = 410;
    private double playerY = 300; // Adjustable fire rate (projectiles per second)
    private double fireRate = 5; // Two projectiles per second
    private double timeBetweenShots = 1 / fireRate;
    public static boolean gamePaused = false;
    private at.ac.fhcampuswien.fiveguysproject.SpaceInvadersController SpaceInvadersController;

    @FXML
    public void initialize() {
        startButton.setStyle("-fx-background-color: lightblue;");

        loadImages();
        mapController = new MapController(starPane);
        initializeTimeline();
    }

    private void initializeTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(16), this::update));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void loadImages() {
        Image playImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/play.png")));
        Image pauseImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pause.png")));
        pauseButtonImage.setImage(pauseImage); // Set the initial state to play
        pauseButtonImage.setOnMouseClicked(event -> pauseGame());
    }

    public void startGame(){
        startButton.setDisable(true);
        startButton.setVisible(false);

        player.setTranslateX(playerX);
        player.setTranslateY(playerY);
        player.setOpacity(1);


        timeline = new Timeline(new KeyFrame(Duration.millis(16), this::update));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        Timeline enemiesTimeline = new Timeline(new KeyFrame(Duration.millis(50), this::moveEnemies));
        enemiesTimeline.setCycleCount(Timeline.INDEFINITE);
        enemiesTimeline.play();

        Timeline borderCheck = new Timeline(new KeyFrame(Duration.millis(50), e -> checkBorder()));
        borderCheck.setCycleCount(Timeline.INDEFINITE);
        borderCheck.play();

        spawnEnemiesAtInterval();

        projectileTimeline = new Timeline();
        Timeline shootingTimeline = new Timeline(new KeyFrame(Duration.seconds(timeBetweenShots), e -> shoot()));
        shootingTimeline.setCycleCount(Timeline.INDEFINITE);
        shootingTimeline.play();
    }

    public void pauseGame() {
        if (gamePaused) {
            resumeGame();
            gamePaused = false;

        } else {
            pauseButtonImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/play.png"))));
            timeline.pause();
            gamePaused = true;
        }
    }

    private void resumeGame() {
        pauseButtonImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pause.png"))));
        timeline.play();
        projectileTimeline.play();

    }
    private void update(ActionEvent actionEvent) {

    }
    private void checkBorder() {

        player.setTranslateX(player.getTranslateX());
        player.setTranslateY(player.getTranslateY());

        if (playerX <= leftBorder) {
            playerX = leftBorder;
            moveRight();
        } else if (playerX >= rightBorder) {
            playerX = rightBorder;
            moveLeft();
        }
    }

    public void checkCollision(Projectile projectile) {
        List<Node> enemiesToRemove = new ArrayList<>();

        for (var entry : enemiesMap.entrySet()) {
            Enemy enemy = entry.getValue();
            if (projectile.checkCollision(enemy, projectilePane)) {
                enemy.handleCollision(projectile, enemyPane, projectilePane);

                enemiesToRemove.add(enemy);
                break;
            }
        }
        enemyPane.getChildren().removeAll(enemiesToRemove);
        enemiesMap.values().removeAll(enemiesToRemove);
    }

    private void spawnSingleEnemy(double startY) {

        if (!gamePaused){
            if (level == 1){
                if (enemyCount<enemyLimit){
                    Random random = new Random();
                    double randomX;  // Random x coordinate within the range [-380, 380]

                    double minDistance = 10.0;

                    do {
                        randomX = random.nextDouble() * (350 * 2) - 0;
                    } while (!isDistanceSafe(randomX, startY, minDistance));

                    Enemy enemy = new Enemy(randomX, startY);
                    addEnemy(enemy);
                    enemyPane.getChildren().add(enemy);
                    enemyCount++;
                    System.out.println(enemyCount);
                } else {
                    enemyCount = 0;
                    level = 2;
                }
            }}
        if (level==2){
            if (enemyCount<enemyLimit){
                Random random = new Random();
                double randomX;  // Random x coordinate within the range [-380, 380]

                double minDistance = 10.0;

                do {
                    randomX = random.nextDouble() * (350 * 2) - 0;
                } while (!isDistanceSafe(randomX, startY, minDistance));

                Enemy enemy = new Enemy(randomX, startY);
                addEnemy(enemy);
                enemyPane.getChildren().add(enemy);
                enemyCount++;
                System.out.println(enemyCount);
            }
        }
    }

    private void spawnEnemiesAtInterval() {
        double startY = -700;
        Timeline spawnTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> spawnSingleEnemy(startY))
        );
        spawnTimeline.setCycleCount(Timeline.INDEFINITE);
        spawnTimeline.play();

        spawnTimeline.setOnFinished(event -> {
            if (level == 2) {
                // Pause enemy spawning for 5 seconds
                pauseEnemySpawning();
            }
        });
    }

    private void pauseEnemySpawning() {
        Timeline pauseTimeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {
                    // Resume enemy spawning after the pause
                    spawnEnemiesAtInterval();
                })
        );
        pauseTimeline.play();
    }

    public static void decreaseEnemyCount(){
        if (enemyCount == 1){
            System.out.println("All enemies dead!!");
        }
    }

    private void moveEnemies(ActionEvent event) {
        Random random = new Random();

        for (var node : enemyPane.getChildren()) {
            if (node instanceof Enemy) {
                Enemy enemy = (Enemy) node;
                double speed = random.nextDouble() * 1 + 0.5;
                enemy.move(speed);

                if (enemy.getTranslateY() > enemyPane.getPrefHeight()) {
                    enemy.setTranslateY(0);
                }
            }
        }
    }

    @FXML
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case A:
                if (!gamePaused){
                    moveLeft();
                    break;
                }
            case D:
                if (!gamePaused){

                    moveRight();
                    break;
                }
            case S:
                if (!gamePaused){
                    timeline.pause();
                }
        }
    }
    private void moveLeft() {
        timeline.stop(); // Stop the previous movement
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.millis(16), e -> {
                    playerX -= 3.5; // Adjust the speed as needed
                    player.setTranslateX(playerX);

                })
        );
        timeline.play();
    }

    private void moveRight() {
        timeline.stop(); // Stop the previous movement
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.millis(16), e -> {
                    playerX += 5; // Adjust the speed as needed
                    player.setTranslateX(playerX);

                })
        );
        timeline.play();
    }

    public void shoot() {
        if (!gamePaused){
            Projectile projectile = new Projectile(player.getTranslateX() + 380, player.getTranslateY() - 715);
            projectilePane.getChildren().add(projectile);

            Timeline projectileTimeline = new Timeline(
                    new KeyFrame(Duration.millis(16), e -> {
                        projectile.move();
                        checkCollision(projectile);
                    })
            );
            projectileTimeline.setCycleCount(Timeline.INDEFINITE);
            projectileTimeline.play();
        }
    }

    private boolean isDistanceSafe(double x, double y, double minDistance) {
        for (var entry : enemiesMap.entrySet()) {
            Enemy existingEnemy = entry.getValue();
            double distance = calculateDistance(x, y, existingEnemy.getTranslateX(), existingEnemy.getTranslateY());

            if (distance < minDistance) {
                return false; // Distance is not safe
            }
        }

        return true; // Distance is safe
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    @Override
    public void update() {

    }
}

