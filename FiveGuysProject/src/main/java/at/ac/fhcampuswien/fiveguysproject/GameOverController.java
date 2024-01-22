package at.ac.fhcampuswien.fiveguysproject;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class GameOverController {

    @FXML
    private Pane starPane;

    @FXML
    public void initialize() {
        MapController mapController = new MapController(starPane);  // Initialize the MapController
    }
}