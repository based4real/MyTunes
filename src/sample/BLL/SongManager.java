package sample.BLL;

import sample.BE.Song;
import sample.DAL.SongDAO;

import java.io.IOException;
import java.util.List;

public class SongManager {

    private SongDAO songDAO;

    public SongManager() throws IOException {
        songDAO = new SongDAO();
    }

    public List<Song> getAllSongs() throws Exception {
        return songDAO.getAllSongs();
    }

    public Song createNewSong(Song newSong) throws Exception {
        return songDAO.createMovie(newSong);
    }

    public void updateSong(Song selectedSong) throws Exception{
        songDAO.updateMovie(selectedSong);
    }

    public void deleteSong(Song selectedSong) throws Exception {
        songDAO.deleteMovie(selectedSong);
    }
}
