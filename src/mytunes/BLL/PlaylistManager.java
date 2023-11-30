package mytunes.BLL;

import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.PlaylistDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        if (playlistDAO.addSongToPlaylist(playlist, song)) {
            playlist.addToPlaylist(song);
            return true;
        }
        return false;
    }

    public List<Playlist> getAllPlaylists() throws Exception {
        if (allPlaylists.isEmpty())
            allPlaylists = playlistDAO.getallPlaylists();

        return allPlaylists;
    }

    public boolean isSongInPlaylist(Song s, Playlist playlist) throws Exception {
        //Contains er lidt dum metode da objekter ændrer adresser.
        //Da id i DB er primær nøgle, burde dette ikke være et problem.
        for (Song song : getPlaylistSongs(playlist)) {
            System.out.println(song.getId() + " " + s.getId());
            if (song.getId() == s.getId())
                return true;
        }
        return false;
    }

    public int getNextOrderID() throws Exception {
        return playlistDAO.getNextOrderID();
    }

}
