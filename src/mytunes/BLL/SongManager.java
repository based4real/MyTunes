package mytunes.BLL;

import mytunes.BE.Artist;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BLL.util.SongSearcher;
import mytunes.DAL.DB.Objects.SongDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongManager {

    private SongSearcher songSearcher = new SongSearcher();
    private AlbumManager albumManager;
    private SongDAO songDAO;
    private List<Song> allSongs = new ArrayList<>();

    private boolean shouldUpdate;

    public SongManager() throws IOException {
        songDAO = new SongDAO();
        albumManager = new AlbumManager();
    }

    public List<Song> getAllSongs() throws Exception {
        if (allSongs.isEmpty() || shouldUpdate) {
            allSongs = songDAO.getAllSongs();
            shouldUpdate = false;
        }
        
        return allSongs;
    }

    public Song createNewSong(Song newSong) throws Exception {
        shouldUpdate = true;
        return songDAO.createSong(newSong);
    }

    public void updateSong(Song selectedSong) throws Exception{
        songDAO.updateSong(selectedSong);
    }

    public boolean deleteSong(Song selectedSong) throws Exception {
        shouldUpdate = true;
        return songDAO.deleteSong(selectedSong);
    }

    public Artist getArtistFromSong(Song song) throws Exception {
        return songDAO.getArtistFromSong(song);
    }

    public void updateOrderID(Playlist playlist, Song draggedSong, Song droppedSong) throws Exception {
        songDAO.updateOrderID(playlist, draggedSong, droppedSong);
    }

    public List<Song> searchSong(String searchWord) throws Exception {
        List<Song> allSongs = getAllSongs();
        List<Song> searchResult = songSearcher.searchSong(allSongs,searchWord);
        return searchResult;
    }
}
