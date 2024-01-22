package at.ac.fhcampuswien.fiveguysproject;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Klasse ist zuständig für das Einfügen und Abspielen der Musik im Programm.
 */
public class SoundController {
    private static MediaPlayer backgroundMusicPlayer;

    public SoundController(String s) {
        initializeBackgroundMusic(s);
    }

    private void initializeBackgroundMusic(String backgroundMusicPath) {
        Media media = new Media(getClass().getResource(backgroundMusicPath).toExternalForm());
        backgroundMusicPlayer = new MediaPlayer(media);
        backgroundMusicPlayer.setOnEndOfMedia(() -> backgroundMusicPlayer.seek(Duration.ZERO)); // Nach Ablauf wird MP3 Datei wieder auf 0 sek zurückgesetzt.
    }

    public void playBackgroundMusic() {
        backgroundMusicPlayer.play();
    }

    public void stopBackgroundMusic() {
        backgroundMusicPlayer.stop();
    }

    public void setBackgroundMusic(String s) {
        stopBackgroundMusic();
        initializeBackgroundMusic(s);
    }

    public void setBackgroundMusicVolume(double volume) {
        backgroundMusicPlayer.setVolume(volume);
    }
}
