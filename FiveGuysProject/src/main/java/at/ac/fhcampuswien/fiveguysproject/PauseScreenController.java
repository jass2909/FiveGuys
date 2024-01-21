package at.ac.fhcampuswien.fiveguysproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PauseScreenController extends SpaceInvadersController {

    @FXML
    private Pane starPane;
    private MapController mapController;
    @FXML
    private Button resumeButton;

    @FXML
    public void initialize() {
        mapController = new MapController(starPane);
    }

    @FXML
    private void resumeGame(ActionEvent event) {
        System.out.println("Resume Game button clicked");
        timeline.play();
        projectileTimeline.play();
    }

    public Button getResumeButton() {
        return resumeButton;
    }

    @FXML
    private void options(ActionEvent event) {
        // Code to open the options window
        // You can implement this based on your preferences
    }

    @FXML
    private void quitButton(ActionEvent event) {
        System.out.println("Back to Menu button clicked");
        System.exit(0);
    }
}
