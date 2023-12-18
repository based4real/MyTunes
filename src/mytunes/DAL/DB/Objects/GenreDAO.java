package mytunes.DAL.DB.Objects;

import mytunes.BE.Genre;
import mytunes.BE.Song;
import mytunes.DAL.DB.Connect.DatabaseConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {
    private DatabaseConnector databaseConnector;

    public GenreDAO() throws IOException {
        databaseConnector = new DatabaseConnector();
    }

    public List<Genre> getAllGenres() throws Exception {
        ArrayList<Genre> allGenres = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM Genres";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("name");

                Genre genre = new Genre(name);
                allGenres.add(genre);
            }
            return allGenres;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get genres from database", ex);
        }
    }

    public List<Song> getAllSongsFromGenre(Genre genre) throws SQLException {
        List<Song> allSongs = new ArrayList<>();

        String sql = "SELECT\n" +
                "songID,\n" +
                "songTitle,\n" +
                "filePath,\n" +
                "songMBID,\n" +
                "songGenre,\n" +
                "artistID,\n" +
                "artistName,\n" +
                "artistAlias,\n" +
                "pictureURL,\n" +
                "albumName\n" +
                "FROM (\n" +
                "SELECT\n" +
                "    Songs.Id as songID,\n" +
                "    Songs.Title as songTitle,\n" +
                "    Songs.Filepath as filePath,\n" +
                "    Songs.Genre as songGenre,\n" +
                "    Songs.SongID as songMBID,\n" +
                "    artists.id as artistID,\n" +
                "    artists.name as artistName,\n" +
                "    artists.alias as artistAlias,\n" +
                "    Songs.PictureURL as pictureURL,\n" +
                "    Albums.name as albumName,\n" +
                "    ROW_NUMBER() OVER (PARTITION BY Songs.songID ORDER BY Songs.songID) as row_num\n" +
                "FROM Songs\n" +
                "JOIN artists ON artists.id = Songs.Artist\n" +
                "JOIN Albums_songs ON Albums_songs.song_id = Songs.Id\n" +
                "JOIN Albums ON Albums_songs.album_id = Albums.id\n" +
                ") AS subquery\n" +
                "WHERE row_num = 1 AND songGenre = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, genre.getName());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("songID");
                String title = rs.getString("songTitle");
                String artist = rs.getString("artistName");
                int artistID = rs.getInt("artistID");
                String genreDb = rs.getString("songGenre");
                String filePath = rs.getString("filePath");
                String musicBrainzID = rs.getString("songMBID");
                String pictureURL = rs.getString("pictureURL");
                String albumName = rs.getString("albumName");

                allSongs.add(new Song(musicBrainzID, id, title, artist, genreDb, filePath, pictureURL, albumName, artistID));
            }
        }
        return allSongs;
    }
}
