package at.ac.fhcampuswien.fiveguysproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class StartScreenController {

    @FXML
    public Button playGameButton;
    public VBox StartMenu;
    public VBox OptionsMenu;
    public Label volumeLabel;
    public Slider volumeSlider;
    public Button backButton;

    @FXML
    private Button optionsButton;
    @FXML
    private Button quitButton;

    @FXML
    private Pane starPane;

    private MapController mapController;
    private SoundController soundController;

    @FXML
    public void initialize() {
        mapController = new MapController(starPane);  // Initialize the MapController

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Adjust the music volume based on the slider value (newValue)
            double volume = newValue.doubleValue();
            // You can use the 'volume' value to set the volume for your music player
            // For example, if you are using a MediaPlayer, you can use mediaPlayer.setVolume(volume);
            // Replace the following line with your actual code to change the music volume
            soundController.setBackgroundMusicVolume(volume);

        });

    }
    public void setSoundController(SoundController soundController) {
        this.soundController = soundController;
    }

    @FXML
    public void playGame(ActionEvent event) {

        System.out.println("Play Game button clicked");
    }

    @FXML
    private void options(ActionEvent event) {
        StartMenu.setDisable(true);
        StartMenu.setVisible(false);
        OptionsMenu.setDisable(false);
        OptionsMenu.setVisible(true);
    }

    @FXML
    private void quit(ActionEvent event) {
        // Code to quit the game
        System.exit(0);
    }

    public void playGame() {

        System.out.println("Starting the game!");
    }

    public Button getPlayGameButton() {
        return playGameButton;
    }

    public void backToStart(ActionEvent event) {
        StartMenu.setDisable(false);
        StartMenu.setVisible(true);
        OptionsMenu.setDisable(true);
        OptionsMenu.setVisible(false);
    }
}