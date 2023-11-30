package mytunes.BLL;

import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.PlaylistDAO;
import mytunes.DAL.SongDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {

    private PlaylistDAO playlistDAO;
    private List<Playlist> allPlaylists = new ArrayList<>();


    public PlaylistManager() throws IOException {
        playlistDAO = new PlaylistDAO();
    }

    public Playlist createPlaylist(Playlist playlist) throws Exception {
        return playlistDAO.createPlaylist(playlist);
    }

    public List<Playlist> getAllPlaylists() throws Exception {
        if (allPlaylists.isEmpty())
            allPlaylists = playlistDAO.getallPlaylists();

        return allPlaylists;
    }

    public int getNextOrderID() throws Exception {
        return playlistDAO.getNextOrderID();
    }
}
