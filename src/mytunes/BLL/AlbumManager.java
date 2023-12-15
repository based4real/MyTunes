package mytunes.BLL;

import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.BE.REST.Release;
import mytunes.DAL.DB.Objects.AlbumDAO;
import mytunes.DAL.REST.CoverArt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumManager {

    private AlbumDAO albumDAO;

    private List<Album> allAlbums = new ArrayList<>();
    private MediaPlayerHandler mediaPlayerHandler;

    public AlbumManager() throws IOException {
        this.albumDAO = new AlbumDAO();
        this.mediaPlayerHandler = new MediaPlayerHandler();
    }
    public boolean createAlbum(List<Release> albums, Song song, Artist artist) throws Exception {
        for (Release r : albums)
            albumDAO.createAlbum(r, song, artist);

        return false;
    }

    public List<Song> getAlbumSongs(Album album) throws Exception {
        if (album.getAlbumSongs().isEmpty())
            album.setAlbumSongs(albumDAO.getAlbumSongs(album));

        return album.getAlbumSongs();
    }

    public List<Album> getAllAlbums() throws Exception{
        if (allAlbums.isEmpty())
            allAlbums.addAll(albumDAO.getAllAlbums());

        return allAlbums;
    }

    public String getAllplayTime(Album album) throws Exception {
        List<Song> playlistSongs = getAlbumSongs(album);
        double totalTime = 0;

        for (Song s : playlistSongs)
            totalTime = totalTime + s.getDoubleTime();

        return mediaPlayerHandler.getRewrittenTimeFromDouble(totalTime);

    }
}
