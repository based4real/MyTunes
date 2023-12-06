package mytunes.GUI.Model;

import mytunes.BE.Artist;
import mytunes.BLL.ArtistManager;

import java.io.IOException;

public class ArtistModel {

    private ArtistManager artistManager;

    public ArtistModel() throws IOException {
        artistManager = new ArtistManager();
    }

    public Artist createArtist(Artist artist) throws Exception {
        return artistManager.createArtist(artist);
    }
}
