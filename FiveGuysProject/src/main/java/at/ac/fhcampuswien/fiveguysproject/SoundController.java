package at.ac.fhcampuswien.fiveguysproject;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
public class SoundController {
    private static MediaPlayer backgroundMusicPlayer;

    public SoundController(String s) {
        initializeBackgroundMusic(s);
    }


    private void initializeBackgroundMusic(String backgroundMusicPath) {
        Media media = new Media(getClass().getResource(backgroundMusicPath).toExternalForm());
        backgroundMusicPlayer = new MediaPlayer(media);
        backgroundMusicPlayer.setOnEndOfMedia(() -> backgroundMusicPlayer.seek(Duration.ZERO));
    }
    private void initializeGameMusic(String s) {
        Media media = new Media(getClass().getResource(s).toExternalForm());
        backgroundMusicPlayer = new MediaPlayer(media);
        backgroundMusicPlayer.setOnEndOfMedia(() -> backgroundMusicPlayer.seek(Duration.ZERO));
    }
    public void playBackgroundMusic() {
        backgroundMusicPlayer.play();
    }

    public void stopBackgroundMusic() {
        backgroundMusicPlayer.stop();
    }
    public void setBackgroundMusic(String s) {
        stopBackgroundMusic(); // Stop the current background music
        initializeBackgroundMusic(s); // Initialize the new background music
    }


    public void setBackgroundMusicVolume(double volume) {
        backgroundMusicPlayer.setVolume(volume);
    }
}
