package mytunes.BLL;

import mytunes.BE.Genre;
import mytunes.DAL.DB.Objects.GenreDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenreManager {
    private GenreDAO genreDAO;
    private List<String> allGenreNames = new ArrayList<>();

    public GenreManager() throws IOException {
        genreDAO = new GenreDAO();
    }

    public List<String> getAllGenreNames() throws Exception {
        if (allGenreNames.isEmpty()){
            for (Genre g: genreDAO.getAllGenres()) {
                allGenreNames.add(g.getName());
            }
        }
        return allGenreNames;
    }
}
