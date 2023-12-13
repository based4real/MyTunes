package mytunes.GUI.Model;

import javafx.scene.control.TableView;
import javafx.scene.media.MediaPlayer;
import mytunes.BE.Song;
import mytunes.BLL.MediaPlayerHandler;

import java.io.IOException;

public class MediaPlayerModel {

    private MediaPlayerHandler mediaPlayerHandler;
    private Song song;

    private static MediaPlayerModel single_instance = null;

    private TableView<Song> playlistSongs;

    private MediaPlayerModel() throws IOException {
        mediaPlayerHandler = new MediaPlayerHandler();
    }

    public static synchronized MediaPlayerModel getInstance() throws Exception {
        if (single_instance == null)
            single_instance = new MediaPlayerModel();

        return single_instance;
    }

    public Song getSelectedSong() {
        return song;
    }

    public void setSelectedSong(Song selected) {
        song = selected;
    }

    public void playSong(MediaPlayer song) {
        mediaPlayerHandler.playSong(song);
    }

    public void setPlaylistSongs(TableView<Song> tbl) {
        this.playlistSongs = tbl;
    }

    public TableView<Song> getPlaylistSongs() {
        return playlistSongs;
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

    public void restartSong() {
        mediaPlayerHandler.restartSong();
    }
}
