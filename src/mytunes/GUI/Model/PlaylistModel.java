package mytunes.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BLL.PlaylistManager;

import java.io.IOException;
import java.util.List;

public class PlaylistModel {
    private ObservableList<Playlist> playlistToBeViewed;
    private ObservableList<Song> SongsToBeViewed;

    private PlaylistManager playlistManager;
    private static PlaylistModel single_instance = null;


    private PlaylistModel() throws Exception {
        playlistManager = new PlaylistManager();
        playlistToBeViewed = FXCollections.observableArrayList();
        playlistToBeViewed.addAll(getPlaylists());
    }

    public static synchronized PlaylistModel getInstance() throws Exception {
        if (single_instance == null)
            single_instance = new PlaylistModel();

        return single_instance;
    }

    public ObservableList<Song> getObservableSongs(Playlist playlist) throws Exception {
        SongsToBeViewed = FXCollections.observableArrayList();

        if (playlist == null || getSongs(playlist) == null)
            return SongsToBeViewed;

        SongsToBeViewed.addAll(getSongs(playlist));

        return SongsToBeViewed;
    }

    public ObservableList<Playlist> getObservablePlaylists() {
        return playlistToBeViewed;
    }

    public List<Playlist> getPlaylists() throws Exception {
        return playlistManager.getAllPlaylists();
    }

    public boolean updateOrder(Playlist playlistNew, Playlist playlistOld) throws Exception {
        return playlistManager.updateOrder(playlistNew, playlistOld);
    }

    public List<Song> getSongs(Playlist playlist) throws Exception {
        return playlistManager.getPlaylistSongs(playlist);
    }

    public boolean addSongToPlaylist(Playlist playlist, Song song) throws Exception {
        return playlistManager.addSongToPlaylist(playlist, song);
    }

    public Playlist createPlaylist(String name) throws Exception {
        return playlistManager.createPlaylist(new Playlist(name));
    }

    public Playlist editPlaylist(Playlist playlist, String newName) throws Exception {
        return playlistManager.editPlaylist(playlist, newName);
    }

    public boolean isSongInPlaylist(Song s, Playlist playlist) throws Exception {
        return playlistManager.isSongInPlaylist(s, playlist);
    }

    public void deletePlaylist(Playlist playlist) throws Exception {
        playlistManager.deletePlaylist(playlist);
    }

    public boolean removeSongFromPlaylist(Playlist playlist, Song song) throws Exception {
        return playlistManager.removeSongFromPlaylist(playlist, song);
    }

}
