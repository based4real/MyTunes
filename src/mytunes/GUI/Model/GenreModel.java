package mytunes.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Album;
import mytunes.BE.Genre;
import mytunes.BE.Song;
import mytunes.BLL.GenreManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class GenreModel {

    private GenreManager genreManager;
    private static GenreModel single_instance = null;

    private GenreModel() throws IOException {
        genreManager = new GenreManager();
    }

    public static synchronized GenreModel getInstance() throws Exception {
        if (single_instance == null)
            single_instance = new GenreModel();

        return single_instance;
    }

    public List<String> getAllGenreNames() throws Exception {
        return genreManager.getAllGenreNames();
    }

    public List<Song> getGenreSongs(Genre genre) throws SQLException {
        return genreManager.getGenreSongs(genre);
    }

    public ObservableList<Song> getObservableSongs(Genre genre) throws Exception {
        ObservableList<Song> SongsToBeViewed = FXCollections.observableArrayList();

        if (genre == null || getGenreSongs(genre) == null)
            return SongsToBeViewed;

        SongsToBeViewed.addAll(getGenreSongs(genre));
        SongsToBeViewed.sort(Comparator.comparing(Song::getOrderID));

        return SongsToBeViewed;
    }
}
