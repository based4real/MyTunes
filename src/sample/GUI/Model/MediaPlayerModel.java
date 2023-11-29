package sample.GUI.Model;

import javafx.scene.media.MediaPlayer;
import sample.BLL.MediaPlayerHandler;

import java.io.IOException;

public class MediaPlayerModel {

    private MediaPlayerHandler mediaPlayerHandler;

    public MediaPlayerModel() throws IOException {
        mediaPlayerHandler = new MediaPlayerHandler();
    }

    public void playSong(MediaPlayer song) {
        mediaPlayerHandler.playSong(song);
    }

    public MediaPlayer getPlayingSong() {
        return mediaPlayerHandler.getCurrentSong();
    }

    public String getCurrentTime() {
        return mediaPlayerHandler.getCurrentTime();
    }

    public String getTimeFromDouble(double time) {
        return mediaPlayerHandler.getTimeFromDouble(time);
    }

    public boolean isPlaying() {
        return mediaPlayerHandler.isPlaying();
    }
}
