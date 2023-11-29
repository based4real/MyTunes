package sample.BLL;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import sample.BE.Song;

import java.io.IOException;
import java.util.List;

public class MediaPlayerHandler {

    private MediaPlayer lastSong, currentSong;
    private SongManager songManager;

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
            MediaPlayer.Status status = song.getStatus();
            if (status == MediaPlayer.Status.PLAYING)
                song.pause();
            else if (status == MediaPlayer.Status.PAUSED)
                song.play();

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
    }

    public boolean isPlaying() {
        return (currentSong.getStatus() == MediaPlayer.Status.PLAYING);
    }

    public void restartSong() {
        if (isPlaying())
            currentSong.seek(new Duration(0));
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
