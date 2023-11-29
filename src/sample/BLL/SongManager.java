package sample.BLL;

import sample.BE.Song;
import sample.DAL.SongDAO;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SongManager {

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
