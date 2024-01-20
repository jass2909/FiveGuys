package at.ac.fhcampuswien.fiveguysproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class PauseScreenController {

    @FXML
    public Button resumeGameButton;

    @FXML
    private Button optionsButton;
    @FXML
    private Button backToMenu;
    @FXML
    private Pane starPane;
    private MapController mapController;

    @FXML
    public void initialize() {
        mapController = new MapController(starPane);  // Initialize the MapController
    }

    @FXML
    public void resumeGame(ActionEvent event) {
        // Code to start the game (transition to the game screen)
        // You can use the code below as a reference:
        // SpaceInvadersApplication.launch(SpaceInvadersApplication.class);
        // You might need to adjust this depending on your game setup.
        System.out.println("Play Game button clicked");
    }

    @FXML
    private void options(ActionEvent event) {
        // Code to open the options window
        // You can implement this based on your preferences
    }

    @FXML
    private void quit(ActionEvent event) {
        // Code to quit the game
        System.exit(0);
    }

    public Button getResumeGameButton() {
        return resumeGameButton;
    }
}
