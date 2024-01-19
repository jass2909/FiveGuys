package at.ac.fhcampuswien.fiveguysproject;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/*
 * Das GameController-Interface dient als gemeinsame Schnittstelle für alle Spiel-Controller-Klassen.
 * Es erweitert das EventHandler-Interface, um auf Tastatureingaben zu reagieren, und enthält eine
 * Methode zum Aktualisieren des Spielzustands.
 */
public interface GameController extends EventHandler<KeyEvent> {

    /*
     * Aktualisiert den Spielzustand. Diese Methode sollte von den konkreten
     * Implementierungen der Spiel-Controller-Klassen implementiert werden.
     */
    void update();
}
