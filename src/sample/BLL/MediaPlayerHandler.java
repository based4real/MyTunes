package sample.BLL;

import javafx.scene.media.MediaPlayer;
import sample.BE.Song;

import java.io.IOException;
import java.util.List;

public class MediaPlayerHandler {

    private MediaPlayer lastSong, currentSong;
    private SongManager songManager;
    private boolean isPlaying = false;

    public MediaPlayerHandler() throws IOException {
        this.songManager = new SongManager();
    }

    private List<Song> getAllSongs() throws Exception {
        return songManager.getAllSongs();
    }

    private void stopPlayingSong(MediaPlayer song) {

        if (song.getStatus() == MediaPlayer.Status.PLAYING)
            song.stop();

        if (lastSong != null && lastSong.getStatus() == MediaPlayer.Status.PLAYING)
            lastSong.stop();
        
        currentSong = song;
    }

    private boolean shouldPause(MediaPlayer song) {
        if (song == currentSong) {
            if (isPlaying)
                song.pause();
            else
                song.play();


            isPlaying = !isPlaying;
            return true;
        }
        return false;
    }

    public void playSong(MediaPlayer song) {
        if (shouldPause(song))
            return;

        stopPlayingSong(song);
        currentSong.play();

        lastSong = song;
        isPlaying = true;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public MediaPlayer getCurrentSong() {
        return currentSong;
    }

    //refactor
    public String getCurrentTime() {
        double seconds = currentSong.getCurrentTime().toSeconds();

        double secs = seconds % 60;
        double minutes = (seconds / 60) % 60;

        return String.format("%d:%02d", (int)minutes, (int)secs);
    }

    public String getTimeFromDouble(double val) {
        //input millisekunder
        double seconds = val;

        double secs = seconds % 60;
        double minutes = (seconds / 60) % 60;

        return String.format("%d:%02d", (int)minutes, (int)secs);
    }
}
