package mytunes.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Song;
import mytunes.BLL.SongManager;

import java.util.List;

public class SongModel {
    private ObservableList<Song> songsToBeViewed;

    private SongManager songManager;

    public SongModel() throws Exception {
        songManager = new SongManager();
        songsToBeViewed = FXCollections.observableArrayList();
        songsToBeViewed.addAll(songManager.getAllSongs());
    }

    public ObservableList<Song> getObservableSongs() {
        return songsToBeViewed;
    }

    public void createNewSong(Song newSong) throws Exception {
        Song song = songManager.createNewSong(newSong);
        songsToBeViewed.add(song);
    }

    public void updateSong(Song selectedSong) throws Exception {
        songManager.updateSong(selectedSong);
    }

    public void deleteSong(Song selectedSong) throws Exception {
        songManager.deleteSong(selectedSong);
        songsToBeViewed.remove(selectedSong);
    }

    /*
    public void filterSong(String query) throws Exception {
        List<Song> searchResults = songManager.filterSongs(query);
        songsToBeViewed.clear();
        songsToBeViewed.addAll(searchResults);
    }
    */
}