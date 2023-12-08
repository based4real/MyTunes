package mytunes.BLL;

import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.DB.PlaylistDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparing;

public class PlaylistManager {

    private PlaylistDAO playlistDAO;
    private List<Playlist> allPlaylists = new ArrayList<>();
    private List<Song> playlistSongs = new ArrayList<>();

    public PlaylistManager() throws IOException {
        playlistDAO = new PlaylistDAO();
    }

    public Playlist createPlaylist(Playlist playlist) throws Exception {
        return playlistDAO.createPlaylist(playlist);
    }

    public List<Song> getPlaylistSongs(Playlist playlist) throws Exception {
        if (playlist.getPlaylistSongs().isEmpty())
            playlist.setPlaylistSongList(playlistDAO.getSongs(playlist));

        return playlist.getPlaylistSongs();
    }

    public boolean addSongToPlaylist(Playlist playlist, Song song) throws Exception {
        if (playlistDAO.addSongToPlaylist(playlist, song))
            return playlist.addToPlaylist(song);

        return false;
    }

    public List<Playlist> getAllPlaylists() throws Exception {
        if (allPlaylists.isEmpty()) {
            allPlaylists = playlistDAO.getallPlaylists();
            Collections.sort(allPlaylists, comparing(Playlist::getOrderID));
        }

        return allPlaylists;
    }

    public boolean isSongInPlaylist(Song s, Playlist playlist) throws Exception {
        //Contains er lidt dum metode da objekter ændrer adresser.
        //Da id i DB er primær nøgle, burde dette ikke være et problem.
        for (Song song : getPlaylistSongs(playlist))
            if (song.getId() == s.getId())
                return true;

        return false;
    }

    public int getNextOrderID() throws Exception {
        return playlistDAO.getNextOrderID();
    }

    public boolean updateOrder(Playlist playlistNew, Playlist playlistOld) throws Exception {
        return playlistDAO.updateOrder(playlistNew, playlistOld);
    }
}
