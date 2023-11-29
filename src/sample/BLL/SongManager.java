package sample.BLL;

import sample.BE.Song;
import sample.BLL.util.SongFilter;
import sample.DAL.SongDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongManager {

    private SongFilter songFilter = new SongFilter();
    private SongDAO songDAO;
    private List<Song> allSongs = new ArrayList<>();


    public SongManager() throws IOException {
        songDAO = new SongDAO();
    }

    public List<Song> getAllSongs() throws Exception {
        if (allSongs.isEmpty())
            allSongs = songDAO.getAllSongs();

        return allSongs;
    }

    public List<Song> filterSongs(String query) throws Exception {
        List<Song> filterResult = songFilter.filter(allSongs, query);
        return filterResult;
    }

    public Song createNewSong(Song newSong) throws Exception {
        return songDAO.createSong(newSong);
    }

    public void updateSong(Song selectedSong) throws Exception{
        songDAO.updateSong(selectedSong);
    }

    public void deleteSong(Song selectedSong) throws Exception {
        songDAO.deleteSong(selectedSong);
    }
}
