package mytunes.BLL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.IOException;

public class MediaPlayerHandler {

    private MediaPlayer lastSong, currentSong;

    public MediaPlayerHandler() throws IOException {

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

    private void songEnd(MediaPlayer song) {
        Duration currentTime = song.getCurrentTime();
        Duration totalDuration = song.getTotalDuration();

        if (Math.abs(currentTime.toMillis() - totalDuration.toMillis()) < 1.0)
            song.seek(Duration.seconds(0));
    }

    public void playSong(MediaPlayer song) {
        songEnd(song);

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

    public String getTimeFromDouble(double val) {
        //input millisekunder
        double seconds = val;

        double secs = seconds % 60;
        double minutes = (seconds / 60) % 60;

        return String.format("%d:%02d", (int)minutes, (int)secs);
    }

    public String getRewrittenTimeFromDouble(double val) {
        long seconds = (long) val;

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        StringBuilder result = new StringBuilder();

        if (hours > 0) {
            result.append(hours).append(" t.");
            result.append(" ");
        }

        if (minutes > 0) {
            result.append(minutes).append(" min.");
            result.append(" ");
        }

        result.append(remainingSeconds).append(" sek.");
        return result.toString();
    }
}
