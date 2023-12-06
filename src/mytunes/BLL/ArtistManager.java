package mytunes.BLL;

import mytunes.BE.Artist;
import mytunes.DAL.DB.ArtistDAO;

import java.io.IOException;

public class ArtistManager {

    private ArtistDAO artistDAO;

    public ArtistManager() throws IOException {
        artistDAO = new ArtistDAO();
    }


    public Artist createArtist(Artist artist) throws Exception {
        Artist existing = artistDAO.doesArtistExist(artist);
        if (existing != null)
            return existing;

        return artistDAO.createArtist(artist);
    }
}
