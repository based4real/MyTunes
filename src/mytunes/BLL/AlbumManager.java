package mytunes.BLL;

import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.BE.REST.Release;
import mytunes.DAL.DB.Objects.AlbumDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AlbumManager {

    private AlbumDAO albumDAO;
    public AlbumManager() throws IOException {
        this.albumDAO = new AlbumDAO();
    }
    public boolean createAlbum(List<Release> albums, Song song, Artist artist) throws SQLException {
        for (Release r : albums)
            albumDAO.createAlbum(r, song, artist);

        return false;
    }
}
