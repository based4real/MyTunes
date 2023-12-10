package mytunes.GUI.Model;

import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.BLL.ArtistManager;

import java.io.IOException;
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

    public List<Artist> getAllArtists() throws Exception {
        return artistManager.getAllArtists();
    }
}
