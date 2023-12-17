package mytunes.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BLL.SongManager;

import java.util.Comparator;
import java.util.List;

public class SongModel {
    private ObservableList<Song> songsToBeViewed;

    private SongManager songManager;

    private static SongModel single_instance = null;

    private SongModel() throws Exception {
        songManager = new SongManager();
        songsToBeViewed = FXCollections.observableArrayList();
        songsToBeViewed.addAll(songManager.getAllSongs());
    }

    public static synchronized SongModel getInstance() throws Exception {
        if (single_instance == null)
            single_instance = new SongModel();

        return single_instance;
    }


    public Artist getArtistFromSong(Song song) throws Exception {
        return songManager.getArtistFromSong(song);
    }

    public ObservableList<Song> getObservableSongs() throws Exception {
        ObservableList<Song> SongsToBeViewed = FXCollections.observableArrayList();

        if (songManager.getAllSongs() == null)
            return SongsToBeViewed;

        SongsToBeViewed.addAll(songManager.getAllSongs());
        SongsToBeViewed.sort(Comparator.comparing(Song::getOrderID));

        return SongsToBeViewed;
    }

    public Song createNewSong(Song newSong) throws Exception {
        Song song = songManager.createNewSong(newSong);
        songsToBeViewed.add(song);
        return song;
    }

    public void updateSong(Song selectedSong) throws Exception {
        songManager.updateSong(selectedSong);
    }

    public boolean deleteSong(Song selectedSong) throws Exception {
        boolean deleted = songManager.deleteSong(selectedSong);
        if (deleted)
            songsToBeViewed.remove(selectedSong);

        return deleted;
    }

    public void updateOrderID(Playlist playlist, Song draggedSong, Song droppedSong) throws Exception {
        songManager.updateOrderID(playlist, draggedSong, droppedSong);
    }

    public void searchSong(String searchWord) throws Exception {
        List<Song> searchResults = songManager.searchSong(searchWord);
        songsToBeViewed.clear();
        songsToBeViewed.addAll(searchResults);
    }
}