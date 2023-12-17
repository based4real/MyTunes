package mytunes.GUI.Model;

import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import mytunes.BE.Song;
import mytunes.BLL.MediaPlayerHandler;
import mytunes.GUI.Controller.Containers.MediaPlayerContainer;

import java.io.IOException;

public class MediaPlayerModel {

    private MediaPlayerHandler mediaPlayerHandler;
    private Song song;

    private static MediaPlayerModel single_instance = null;

    private TableView<Song> playlistSongs;
    private MediaPlayerContainer mediaPlayerContainer;

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

    public void wasClickedTable(TableView<Song> tbl) {
        tbl.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2 ) {
                Song selectedSong = tbl.getSelectionModel().getSelectedItem();
                playSelectedSong(selectedSong);
            }
        });
    }

    // This might have to be re-done but in order for our
    // UI to update, we have to call the controller.
    // Call right now ? > Model > Controller > Model
    public void playSelectedSong(Song song) {
        mediaPlayerContainer.playSelectedSong(song);
        this.setSelectedSong(song);
    }

    public void setMediaPlayerContainer(MediaPlayerContainer container) {
        this.mediaPlayerContainer = container;
    }

    public MediaPlayer getPlayingSong() {
        return mediaPlayerHandler.getCurrentSong();
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
