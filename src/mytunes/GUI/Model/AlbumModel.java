package mytunes.GUI.Model;

import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.BLL.AlbumManager;
import mytunes.BE.REST.Release;

import java.io.IOException;
import java.sql.SQLException;
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

    public List<Album> getAllAlbums() throws Exception {
       return albumManager.getAllAlbums();
    }

}
