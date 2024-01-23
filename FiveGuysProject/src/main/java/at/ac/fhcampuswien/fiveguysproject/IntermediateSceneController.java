package at.ac.fhcampuswien.fiveguysproject;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class IntermediateSceneController {

    public ImageView StartScreen;
    @FXML
    private Pane starPane;
    private MapController mapController;

    @FXML
    public void initialize() {
        mapController = new MapController(starPane);  // Initialize the MapController
    }
}