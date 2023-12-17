package mytunes.GUI.Model;

import mytunes.BLL.GenreManager;

import java.io.IOException;
import java.util.List;

public class GenreModel {

    private GenreManager genreManager;

    public GenreModel() throws IOException {
        genreManager = new GenreManager();
    }

    public List<String> getAllGenreNames() throws Exception {
        return genreManager.getAllGenreNames();
    }
}
