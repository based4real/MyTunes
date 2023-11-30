package mytunes.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BLL.PlaylistManager;

import java.io.IOException;

public class PlaylistModel {
    private ObservableList<Playlist> playlistToBeViewed;

    private PlaylistManager playlistManager;

    public PlaylistModel() throws Exception {
        playlistManager = new PlaylistManager();
        playlistToBeViewed = FXCollections.observableArrayList();
        playlistToBeViewed.addAll(playlistManager.getAllPlaylists());
    }

    public ObservableList<Playlist> getObservablePlaylists() {
        return playlistToBeViewed;
    }

    public void createPlaylist(String name) throws Exception {
        playlistManager.createPlaylist(new Playlist(name));
    }
}
