package at.ac.fhcampuswien.fiveguysproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class GameOverController {

    @FXML
    public MouseEvent event;
    public ImageView GameOver;

    @FXML
    private Pane starPane;

    @FXML
    public void initialize() {
        MapController mapController = new MapController(starPane);  // Initialize the MapController
    }

    @FXML
    public void handleMouseClicked(MouseEvent event) {
        // Handle mouse click event (e.g., transition to the start screen)
        System.out.println("Mouse clicked on the intermediate scene!");
    }
}