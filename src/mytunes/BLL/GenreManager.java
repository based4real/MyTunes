package mytunes.BLL;

import mytunes.BE.Genre;
import mytunes.BE.Song;
import mytunes.DAL.DB.Objects.GenreDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreManager {
    private GenreDAO genreDAO;
    private List<String> allGenreNames = new ArrayList<>();

    private MediaPlayerHandler mediaPlayerHandler;

    public GenreManager() throws IOException {
        genreDAO = new GenreDAO();
        mediaPlayerHandler = new MediaPlayerHandler();
    }

    public List<String> getAllGenreNames() throws Exception {
        if (allGenreNames.isEmpty()){
            for (Genre g: genreDAO.getAllGenres()) {
                allGenreNames.add(g.getName());
            }
        }
        return allGenreNames;
    }

    public List<Song> getGenreSongs(Genre genre) throws SQLException {
        if (genre.getGenreSongs().isEmpty())
            genre.setGenreSongs(genreDAO.getAllSongsFromGenre(genre));

        return genre.getGenreSongs();
    }

    public String getAllPlayTime(Genre genre) throws Exception {
        List<Song> genreSongs = getGenreSongs(genre);
        double totalTime = 0;

        for (Song s : genreSongs)
            totalTime = totalTime + s.getDoubleTime();

        return mediaPlayerHandler.getRewrittenTimeFromDouble(totalTime);
    }

}
