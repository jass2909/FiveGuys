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

/**
 * Die SpaceInvadersController-Klasse steuert das Hauptspielgeschehen des Space Invaders-Spiels.
 * Sie implementiert das GameController-Interface und behandelt Tastatureingaben sowie die Spiellogik.
 */
public class SpaceInvadersController implements GameController {

    public ImageView player;
    private Map<Integer, Enemy> enemiesMap = new HashMap<>(); // Feinde werden in einer Map gespeichert

    public void addEnemy(Enemy enemy) {
        enemiesMap.put(enemy.getEnemyId(), enemy);
    }

    @FXML
    private Button startButton;
    public Button pauseButton;
    @FXML
    private Pane starPane;// Hintergrund-Sterne
    private MapController mapController;
    @FXML
    public Pane enemyPane; // Panes für Feinde
    public Pane projectilePane;// Panes für Projektile
    private Timeline projectileTimeline; // Timelines für Projektile
    private Timeline timeline;// Timelines für Spielereignisse
    @FXML
    private ImageView pauseButtonImage;
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

    /**
     * Initialisierungsmethode für den Controller. Bilder für play- und pause-Button geladen.
     * Controller initialisiert.
     */
    @FXML
    public void initialize() {
        startButton.setStyle("-fx-background-color: lightblue;");

        loadImages();
        mapController = new MapController(starPane);
        initializeTimeline();
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
        pauseButtonImage.setOnMouseClicked(event -> pauseGame());
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

    /**
     * Pausierung des Spiels. Timelines bleiben stehen, Spieler und Gegner können nicht bewegt werden.
     */
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

    /**
     *Fortsetzung des Spiels.
     */
    private void resumeGame() {
        pauseButtonImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/pause.png"))));
        timeline.play();
        projectileTimeline.play();

    }

    /**
     * Aktualisiere die Spiellogik
     */
    private void update(ActionEvent actionEvent) {
        // Logik für das Aktualisieren des Spielzustands
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
            if (projectile.checkCollision(enemy, projectilePane)) {     // Behandelt die Kollision und fügt den Feind zur Entfernungsliste hinzu
                enemy.handleCollision(projectile, enemyPane, projectilePane);

                enemiesToRemove.add(enemy);
                break;
            }
        }
        enemyPane.getChildren().removeAll(enemiesToRemove);     // Entfernt die getroffenen Feinde aus den Panes
        enemiesMap.values().removeAll(enemiesToRemove);         // Entfernt die getroffenen Feinde aus der Map
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
                if (enemyCount < enemyLimit) {
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
                    System.out.println(enemyCount);
                } else {
                    enemyCount = 0; // Erreicht das Limit, erhöhe das Level
                    level = 2;
                }
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
                System.out.println(enemyCount);
            }
        }
    }

    /**
     * Startet eine Timeline, die in regelmäßigen Abständen einen einzelnen Feind spawnt.
     * Regelt auch das Pausieren des Feindespawnings während des Levelübergangs.
     */
    private void spawnEnemiesAtInterval() {
        double startY = -700;
        Timeline spawnTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> spawnSingleEnemy(startY))
        );
        spawnTimeline.setCycleCount(Timeline.INDEFINITE);
        spawnTimeline.play();

        // Pausiert das Feindespawning während des Levelübergangs
        spawnTimeline.setOnFinished(event -> {
            if (level == 2) {
                // Pause enemy spawning for 5 seconds
                pauseEnemySpawning();
            }
        });
    }

    /**
     * Pausiert das Feindespawning für einen bestimmten Zeitraum und setzt es danach fort.
     */
    private void pauseEnemySpawning() {
        Timeline pauseTimeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> {
                    // Resume enemy spawning after the pause
                    spawnEnemiesAtInterval();
                })
        );
        pauseTimeline.play();
    }

    /**
     * Verringert die Anzahl der verbleibenden Feinde und gibt eine Nachricht aus, wenn alle Feinde besiegt sind.
     */
    public static void decreaseEnemyCount() {
        if (enemyCount == 1) {
            System.out.println("All enemies dead!!");
        }
    }
    /**
     * Bewegt die Feinde nach unten mit zufälliger Geschwindigkeit.
     */
    private void moveEnemies(ActionEvent event) {
        Random random = new Random();

        for (var node : enemyPane.getChildren()) {
            if (node instanceof Enemy) {
                Enemy enemy = (Enemy) node;
                double speed = random.nextDouble() * 1 + 0.5;
                enemy.move(speed);

                // Setzt den Feind wieder an den Anfang, wenn er das Spielfeld nach unten verlässt
                if (enemy.getTranslateY() > enemyPane.getPrefHeight()) {
                    enemy.setTranslateY(0);
                }
            }
        }
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
                    playerX -= 3.5; // Passe die Geschwindigkeit bei Bedarf an
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
     * Führt Aktualisierungen des Spielzustands durch.
     */
    @Override
    public void update() {
        // Platz für Aktualisierungen des Spielzustands
    }
}

