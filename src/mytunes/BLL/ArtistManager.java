package mytunes.BLL;

import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.DAL.DB.Objects.ArtistDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArtistManager {

    private ArtistDAO artistDAO;
    private List<Artist> allArtists = new ArrayList<>();

    public ArtistManager() throws IOException {
        artistDAO = new ArtistDAO();
    }


    public Artist createArtist(Artist artist) throws Exception {
        Artist existing = artistDAO.doesArtistExist(artist);
        if (existing != null)
            return existing;

        return artistDAO.createArtist(artist);
    }

    public List<Song> getArtistSongs(Artist artist) throws Exception {
        return artistDAO.getArtistSongs(artist);
    }

    public List<Artist> getAllArtists() throws Exception {
        if (allArtists.isEmpty())
            allArtists = artistDAO.getAllArtists();

        return allArtists;
    }
}
