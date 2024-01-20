package at.ac.fhcampuswien.fiveguysproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class IntermediateSceneController {

    @FXML
    public MouseEvent event;
    public ImageView StartScreen;

    @FXML
    private Pane starPane;

    private MapController mapController;

    @FXML
    public void initialize() {
        mapController = new MapController(starPane);  // Initialize the MapController
    }

    @FXML
    public void handleMouseClicked(MouseEvent event) {
        // Handle mouse click event (e.g., transition to the start screen)
        System.out.println("Mouse clicked on the intermediate scene!");
    }
}