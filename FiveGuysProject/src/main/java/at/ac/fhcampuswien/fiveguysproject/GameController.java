package at.ac.fhcampuswien.fiveguysproject;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public interface GameController extends EventHandler<KeyEvent> {
    // Add any common methods for game controllers here
    void update();
}
