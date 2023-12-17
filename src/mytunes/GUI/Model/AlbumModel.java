package mytunes.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BLL.AlbumManager;
import mytunes.BE.REST.Release;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class AlbumModel {

    private static AlbumModel single_instance = null;
    private AlbumManager albumManager;

    private AlbumModel() throws IOException {
        albumManager = new AlbumManager();
    }

    public static synchronized AlbumModel getInstance() throws Exception {
        if (single_instance == null)
            single_instance = new AlbumModel();

        return single_instance;
    }

    public boolean createAlbum(List<Release> albums, Song song, Artist artist) throws Exception {
        return albumManager.createAlbum(albums, song, artist);
    }

    public List<Song> getAlbumSongs(Album album) throws Exception {
        return albumManager.getAlbumSongs(album);
    }

    public ObservableList<Song> getObservableSongs(Album album) throws Exception {
        ObservableList<Song> SongsToBeViewed = FXCollections.observableArrayList();

        if (album == null || getAlbumSongs(album) == null)
            return SongsToBeViewed;

        SongsToBeViewed.addAll(getAlbumSongs(album));
        SongsToBeViewed.sort(Comparator.comparing(Song::getOrderID));

        return SongsToBeViewed;
    }

    public List<Album> getAllAlbums() throws Exception {
        return albumManager.getAllAlbums();
    }

    public String getAllPlayTime(Album album) throws Exception {
        return albumManager.getAllplayTime(album);
    }

    public Album getAlbumFromSong(Song song) {
        return albumManager.getAlbumFromSong(song);
    }

    public void initalize() throws Exception {
        List<Album> allAlbums = getAllAlbums();
        for (Album a : allAlbums)
            getObservableSongs(a);

    }
}
