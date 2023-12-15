package mytunes.BLL;

import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.DB.Objects.PlaylistDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparing;

public class PlaylistManager {

    private PlaylistDAO playlistDAO;
    private List<Playlist> allPlaylists = new ArrayList<>();

    private MediaPlayerHandler mediaPlayerHandler;

    public PlaylistManager() throws IOException {
        playlistDAO = new PlaylistDAO();
        mediaPlayerHandler = new MediaPlayerHandler();
    }

    public Playlist createPlaylist(Playlist playlist) throws Exception {
        Playlist created = playlistDAO.createPlaylist(playlist);
        if (created == null)
            return new Playlist("err");

        allPlaylists.add(created);
        return created;
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
        for (Song song : getPlaylistSongs(playlist)) {
            System.out.println(song.getTitle());
            if (song.getId() == s.getId()) {
                return true;
            }
        }
        return false;
    }

    public int getNextOrderID() throws Exception {
        return playlistDAO.getNextOrderID();
    }

    public boolean updateOrder(Playlist playlistNew, Playlist playlistOld) throws Exception {
        return playlistDAO.updateOrder(playlistNew, playlistOld);
    }

    public Playlist editPlaylist(Playlist playlist, String newName) throws Exception {
        return playlistDAO.editPlaylist(playlist, newName);
    }

    public void deletePlaylist(Playlist playlist) throws Exception {
        boolean deleted = playlistDAO.deletePlaylist(playlist);
        if (deleted)
            allPlaylists.remove(playlist);
    }

    public boolean removeSongFromPlaylist(Playlist playlist, Song song) throws Exception {
        boolean removed = playlistDAO.removeSongFromPlaylist(playlist, song);
        if (removed)
            playlist.removeSongFromPlaylist(song);

        return removed;
    }

    public String getAllplayTime(Playlist playlist) throws Exception {
        List<Song> playlistSongs = getPlaylistSongs(playlist);
        double totalTime = 0;

        for (Song s : playlistSongs)
            totalTime = totalTime + s.getDoubleTime();

        return mediaPlayerHandler.getRewrittenTimeFromDouble(totalTime);
    }
}
