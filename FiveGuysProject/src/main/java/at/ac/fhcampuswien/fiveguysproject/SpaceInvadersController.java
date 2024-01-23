package at.ac.fhcampuswien.fiveguysproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Die SpaceInvadersController-Klasse steuert das Hauptspielgeschehen des Space Invaders-Spiels.
 * Sie implementiert das GameController-Interface und behandelt Tastatureingaben sowie die Spiellogik.
 */
public class SpaceInvadersController implements GameController {

    public ImageView player;
    private double enemySpeedMultiplier = 1.0;
    private int health = 3;
    public HBox livesContainer;
    public Button resumeGameButton;
    public Button quitButton;
    public VBox pauseMenu;
    public Label waveCounterLabel;
    private int waveCounter = 1;
    private Stage stage;
    private Scene gameScreenScene;
    private PauseTransition levelTransition = new PauseTransition(Duration.seconds(4));
    private Map<Integer, Enemy> enemiesMap = new HashMap<>(); // Feinde werden in einer Map gespeichert
    @FXML
    public ImageView heart3;
    public ImageView heart2;
    public ImageView heart1;
    public Button pauseButton;
    @FXML
    private Pane starPane;// Hintergrund-Sterne
    private MapController mapController;
    @FXML
    public Pane enemyPane; // Panes für Feinde
    public Pane projectilePane;// Panes für Projektile
    protected Timeline projectileTimeline; // Timelines für Projektile
    protected Timeline timeline;// Timelines für Spielereignisse
    @FXML
    private ImageView pauseButtonImage;
    public static int enemyCount = 0;
    private int enemyLimit = 19;
    public static int level = 1;
    private double playerX = 25;
    private double leftBorder = -380;
    private double rightBorder = 410;
    private double playerY = 350; // Adjustable fire rate (projectiles per second)
    private double fireRate = 5; // Two projectiles per second
    private double timeBetweenShots = 1 / fireRate;
    public static boolean gamePaused = false;
    private int score = 0;
    private Timeline enemiesTimeline;
    private Timeline spawnTimeline;
    @FXML
    private Label scoreLabel;


    private at.ac.fhcampuswien.fiveguysproject.SpaceInvadersController SpaceInvadersController;

    /**
     * Initialisierungsmethode für den Controller. Bilder für play- und pause-Button geladen.
     * Controller initialisiert.
     */
    @FXML
    public void initialize() {
        loadImages();
        mapController = new MapController(starPane);
        initializeTimeline();
        startGame();
    }

    /**
     * Initialisierung der Timeline für Spielupdates.
     */
    private void initializeTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(16), this::update));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Lade Bilder für den Play- und Pause-Button
     */
    private void loadImages() {
        Image playImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/play.png")));
        Image pauseImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pause.png")));
        pauseButtonImage.setImage(pauseImage); // Set the initial state to play
        pauseButtonImage.setOnMouseClicked(event -> {
            try {
                pauseGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadGameScreen(Stage stage) throws IOException {
        // Load the game screen (replace this with your actual game screen FXML)
        restartGame(new ActionEvent());

        FXMLLoader gameScreenLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent gameScreenRoot = gameScreenLoader.load();
        SpaceInvadersController gameController = gameScreenLoader.getController();
        gameController.setStage(stage); // Pass the stage to the controller
        Scene gameScreenScene = new Scene(gameScreenRoot, 800, 748, Color.BLACK);
        gameScreenScene.setOnKeyPressed(gameController);
        gameScreenScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Set up event handling or any initialization for the game screen if needed

        stage.setScene(gameScreenScene);
        stage.setResizable(false);

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Start des Spiels. Aufruf diverser Methoden.
     * Startbutton wird deaktiviert und verborgen
     * Startpos. des Spielers ermittelt.
     * Timelines für Spiel und Feinde gestartet.
     * Überprüfung der Spielfeldgrenzen.
     * Spawnen von Feinden.
     * Timelineinitialisierung der Projektile
     */
    public void startGame() {

        player.setTranslateX(playerX);
        player.setTranslateY(playerY);
        player.setOpacity(1);
        levelTransition.setOnFinished(e -> {

            if (level < 3) {
                level++;
            } else if (level == 3) {
                level = 1;
            }
            spawnTimeline.play();
            enemyCount = 0;

        });


        timeline = new Timeline(new KeyFrame(Duration.millis(16), this::update));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        enemiesTimeline = new Timeline(new KeyFrame(Duration.millis(50), this::moveEnemies));
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

    /**
     * Pausierung des Spiels. Timelines bleiben stehen, Spieler und Gegner können nicht bewegt werden.
     */
    @FXML
    public void pauseGame() throws IOException {
        if (gamePaused) {
            resumeGame();
            gamePaused = false;

        } else {
            pauseButtonImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/play.png"))));
            pauseMenu.setOpacity(1);
            pauseMenu.setDisable(false);
            timeline.pause();
            gamePaused = true;
            mapController.getStarsTimeline().stop();
        }
    }

    /**
     * Fortsetzung des Spiels.
     */
    @FXML
    private void resumeGame() {
        pauseButtonImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pause.png"))));
        timeline.play();
        projectileTimeline.play();
        pauseMenu.setOpacity(0);
        pauseMenu.setDisable(true);
        mapController.getStarsTimeline().play();
    }

    @FXML
    private void quit(ActionEvent event) {
        // Code to quit the game
        System.exit(0);
    }

    @FXML
    private void restartGame(ActionEvent event) {

        // Spieler zurücksetzen
        playerX = 0;
        playerY = 300;
        player.setTranslateX(playerX);
        player.setTranslateY(playerY);
        player.setOpacity(1);

        // Projektile zurücksetzen
        projectilePane.getChildren().clear();

        // Timelines zurücksetzen
        timeline.stop();
        initializeTimeline();
        timeline.play(); // Timeline wieder starten

        projectileTimeline.stop();
        projectileTimeline.play(); // Projectile Timeline wieder starten

        // Feinde zurücksetzen
        enemiesMap.clear();
        enemyPane.getChildren().clear();
        enemyCount = 0;

        // Score zurücksetzen
        score = 0;
        update();

        level = 1;
    }


    /**
     * Aktualisiere die Spiellogik
     */
    private void update(ActionEvent actionEvent) {

    }

    /**
     * Überprüfe die Spielfeldgrenzen für den Spieler.
     * Wenn eine Wand erreicht wird, startet die Bewegung in die andere Richtung.
     */
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

    /**
     * Überprüft Kollisionen zwischen einem Projektil und den Feinden.
     * Entfernt die getroffenen Feinde und aktualisiert die Anzeige.
     *
     * @param projectile Das Projektil, dessen Kollision überprüft wird.
     */
    public void checkCollision(Projectile projectile) {
        List<Node> enemiesToRemove = new ArrayList<>();

        for (var entry : enemiesMap.entrySet()) {
            Enemy enemy = entry.getValue();
            if (projectile.checkCollision(enemy, projectilePane)) {
                enemy.handleCollision(projectile, enemyPane, projectilePane);
                int currentLife = (int) enemy.getUserData();
                currentLife--;
                enemy.setUserData(currentLife);

                if (currentLife <= 0) {
                    enemiesToRemove.add(enemy);
                    increaseScore(10);
                }
            }
        }

        enemyPane.getChildren().removeAll(enemiesToRemove);
        enemiesMap.values().removeAll(enemiesToRemove);
    }


    /**
     * Spawnt einen einzelnen Feind mit einer zufälligen x-Koordinate innerhalb des angegebenen Bereichs.
     * Berücksichtigt dabei den Spielstand, das aktuelle Level und den Abstand zu anderen Feinden.
     *
     * @param startY Die y-Koordinate, bei der der Feind erscheint.
     */
    private void spawnSingleEnemy(double startY) {

        if (!gamePaused) {
            if (level == 1) {
                if (enemyCount <= enemyLimit) {
                    Random random = new Random();
                    double randomX; // Zufällige x-Koordinate innerhalb des Bereichs [-380, 380]

                    double minDistance = 10.0;

                    do {
                        // Wählt eine zufällige x-Koordinate, solange der Abstand zu anderen Feinden nicht sicher ist
                        randomX = random.nextDouble() * (350 * 2) - 0;
                    } while (!isDistanceSafe(randomX, startY, minDistance));

                    Enemy enemy = new Enemy(randomX, startY); // Erstellt einen neuen Feind und fügt ihn zum Spiel hinzu

                    addEnemy(enemy);
                    enemyPane.getChildren().add(enemy);
                    enemyCount++;
                } else {

                    nextLevel();

                }
            }
            if (level == 2) {
                if (enemyCount < enemyLimit) {
                    Random random = new Random();
                    double randomX;  // Zufällige x-Koordinate innerhalb des Bereichs [-380, 380]

                    double minDistance = 10.0;

                    do {
                        // Wählt eine zufällige x-Koordinate, solange der Abstand zu anderen Feinden nicht sicher ist
                        randomX = random.nextDouble() * (350 * 2) - 0;
                    } while (!isDistanceSafe(randomX, startY, minDistance));

                    Enemy enemy = new Enemy(randomX, startY); // Erstellt einen neuen Feind und fügt ihn zum Spiel hinzu
                    addEnemy(enemy);
                    enemyPane.getChildren().add(enemy);
                    enemyCount++;
                } else {

                    nextLevel();

                }
            }
            if (level == 3) {
                if (enemyCount < enemyLimit / 2) {
                    Random random = new Random();
                    double randomX;  // Zufällige x-Koordinate innerhalb des Bereichs [-380, 380]

                    double minDistance = 10.0;

                    do {
                        // Wählt eine zufällige x-Koordinate, solange der Abstand zu anderen Feinden nicht sicher ist
                        randomX = random.nextDouble() * (350 * 2) - 0;
                    } while (!isDistanceSafe(randomX, startY, minDistance));

                    Enemy enemy = new Enemy(randomX, startY); // Erstellt einen neuen Feind und fügt ihn zum Spiel hinzu
                    addEnemy(enemy);
                    enemyPane.getChildren().add(enemy);
                    enemyCount++;
                } else {

                    nextLevel();

                }
            }
        }
    }


    public static int getEnemyCount() {
        return enemyCount;
    }

    public void addEnemy(Enemy enemy) {
        enemiesMap.put(enemy.getEnemyId(), enemy);
    }

    /**
     * Methode um Gegner schneller zu machen, wenn der Level Counter durch 3 teilbar ist. Wave Counter wird nach dem dritten Level erhöht.
     */
    private void nextLevel() {
        // Stop spawning enemies during the level transition
        spawnTimeline.pause(); // Pausiere die Zeitachse für das Spawnen von Gegnern während des Levelübergangs

        levelTransition.play(); // Starte die Animation für den Levelübergang

        // Überprüfe, ob das aktuelle Level durch 3 teilbar ist
        if (level % 3 == 0) {
            waveCounter++; // Erhöhe den Wellenzähler, wenn das Level durch 3 teilbar ist
            enemySpeedMultiplier += 2.8; // Erhöhe den Multiplikator für die Geschwindigkeit der Gegner
        }
    }


    /**
     * Startet eine Timeline, die in regelmäßigen Abständen einen einzelnen Feind spawnt.
     * Regelt auch das Pausieren des Feindespawnings während des Levelübergangs.
     */
    private void spawnEnemiesAtInterval() {
        double startY = -800;
        spawnTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> spawnSingleEnemy(startY))
        );
        spawnTimeline.setCycleCount(Timeline.INDEFINITE);
        spawnTimeline.play();

        // Pausiert das Feindespawning während des Levelübergangs
    }

    /**
     * Bewegt die Feinde nach unten mit zufälliger Geschwindigkeit.
     */
    private void moveEnemies(ActionEvent event) {
        Random random = new Random();
        health = 3;

        for (var node : enemyPane.getChildren()) {
            if (node instanceof Enemy) {
                Enemy enemy = (Enemy) node;
                double speed = random.nextDouble() * 1 + 0.5;
                speed *= enemySpeedMultiplier;
                if (level == 3) {
                    speed = speed * 0.5;
                    enemy.move(speed);
                } else enemy.move(speed);


                if (enemy.getTranslateY() > playerY - 470 && health >= 1) {
                    switch (health) {
                        case 3:
                            heart1.setOpacity(0);
                            break;
                        case 2:
                            heart2.setOpacity(0);
                            break;
                        case 1:
                            heart3.setOpacity(0);
                            try {
                                gameOver((Stage) enemyPane.getScene().getWindow());
                                enemiesTimeline.stop();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                    }
                    health--; // Gesundheitsabnahme innerhalb der if-Anweisung verschieben
                }
            }
        }
    }

    public void gameOver(Stage stage) throws IOException {
        // Alle Timelines und logischen Parameter werden gestoppt
        timeline.stop();
        projectileTimeline.stop();
        spawnTimeline.stop();
        enemyCount = 0;

        // Laden des Game Over Screens
        FXMLLoader gameOverLoader = new FXMLLoader(getClass().getResource("GameOver.fxml"));
        GameOverController gameOverController = new GameOverController();
        gameOverLoader.setController(gameOverController);

        Scene gameOverScene = new Scene(gameOverLoader.load(), 800, 748, Color.WHITE);

        gameOverScene.setOnMouseClicked((MouseEvent event) -> {
            try {
                loadGameScreen(stage);  // Neustart wenn click
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        stage.setScene(gameOverScene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Behandelt Tastatureingaben, insbesondere die Bewegung des Spielers nach links, rechts und das Pausieren des Spiels.
     */
    @FXML
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case A:
                if (!gamePaused) {
                    moveLeft();
                    break;
                }
            case D:
                if (!gamePaused) {

                    moveRight();
                    break;
                }
            case S:
                if (!gamePaused) {
                    timeline.pause();
                }
        }
    }

    /**
     * Bewegt den Spieler nach links.
     */
    private void moveLeft() {
        timeline.stop(); // Stoppt die vorherige Bewegung
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.millis(16), e -> {
                    playerX -= 5; // Passe die Geschwindigkeit bei Bedarf an
                    player.setTranslateX(playerX);

                })
        );
        timeline.play();
    }

    /**
     * Bewegt den Spieler nach rechts.
     */
    private void moveRight() {
        timeline.stop(); // Stoppt die vorherige Bewegung
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.millis(16), e -> {
                    playerX += 5; // Passe die Geschwindigkeit bei Bedarf an
                    player.setTranslateX(playerX);

                })
        );
        timeline.play();
    }

    /**
     * Lässt den Spieler schießen, indem ein neues Projektil erstellt wird.
     */
    public void shoot() {
        if (!gamePaused) {
            Projectile projectile = new Projectile(player.getTranslateX() + 377.5, player.getTranslateY() - 770);
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


    /**
     * Überprüft, ob der Abstand zu vorhandenen Feinden sicher ist.
     *
     * @param x           Die x-Koordinate des zu überprüfenden Punktes.
     * @param y           Die y-Koordinate des zu überprüfenden Punktes.
     * @param minDistance Der minimale sichere Abstand.
     * @return True, wenn der Abstand sicher ist; False, wenn nicht.
     */
    private boolean isDistanceSafe(double x, double y, double minDistance) {
        for (var entry : enemiesMap.entrySet()) {
            Enemy existingEnemy = entry.getValue();
            double distance = calculateDistance(x, y, existingEnemy.getTranslateX(), existingEnemy.getTranslateY());

            if (distance < minDistance) {
                return false; // Der Abstand ist nicht sicher
            }
        }

        return true; // Der Abstand ist sicher
    }

    /**
     * Berechnet den Abstand zwischen zwei Punkten im 2D-Raum.
     *
     * @param x1 Die x-Koordinate des ersten Punktes.
     * @param y1 Die y-Koordinate des ersten Punktes.
     * @param x2 Die x-Koordinate des zweiten Punktes.
     * @param y2 Die y-Koordinate des zweiten Punktes.
     * @return Der berechnete Abstand zwischen den beiden Punkten.
     */
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Methode um berschiedenen Gegner unterschiedliche Punkte zu geben.
     * @param points Verteilte Punkte
     */
    private void increaseScore(int points) {
        if (level == 1) {
            score += points;
        } else if (level == 2) {
            score += (points + 40);
        } else if (level == 3) {
            score += (points + 90);
        }

        update();
    }

    /**
     * Führt Aktualisierungen des Spielzustands durch.
     */
    @Override
    public void update() {
        // Platz für Aktualisierungen des Spielzustands
        Platform.runLater(() -> scoreLabel.setText("Score: " + score));
        waveCounterLabel.setText("Wave: " + waveCounter);
    }
}