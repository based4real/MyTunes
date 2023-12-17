package mytunes.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.BLL.ArtistManager;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ArtistModel {

    private ArtistManager artistManager;
    private static ArtistModel single_instance = null;

    private ArtistModel() throws IOException {
        artistManager = new ArtistManager();
    }

    public static synchronized ArtistModel getInstance() throws Exception {
        if (single_instance == null)
            single_instance = new ArtistModel();

        return single_instance;
    }

    public Artist createArtist(Artist artist) throws Exception {
        return artistManager.createArtist(artist);
    }

    public List<Song> getArtistSongs(Artist artist) throws Exception {
        return artistManager.getArtistSongs(artist);
    }

    public ObservableList<Song> getArtistObservableSongs(Artist artist) throws Exception {
        ObservableList<Song> SongsToBeViewed = FXCollections.observableArrayList();

        if (artist == null || getArtistSongs(artist) == null)
            return SongsToBeViewed;

        SongsToBeViewed.addAll(getArtistSongs(artist));
        SongsToBeViewed.sort(Comparator.comparing(Song::getOrderID));

        return SongsToBeViewed;
    }

    public List<Album> getArtistAlbums(Artist artist) throws Exception {
        return artistManager.getArtistAlbums(artist);
    }

    public List<Artist> getAllArtists() throws Exception {
        return artistManager.getAllArtists();
    }
}
