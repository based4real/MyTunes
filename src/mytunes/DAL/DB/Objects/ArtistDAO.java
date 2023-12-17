package mytunes.DAL.DB.Objects;

import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.BLL.util.CacheSystem;
import mytunes.BLL.util.ConfigSystem;
import mytunes.DAL.DB.Connect.DatabaseConnector;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ArtistDAO {

    private DatabaseConnector databaseConnector;

    public ArtistDAO() throws IOException {
        databaseConnector = new DatabaseConnector();
    }

    public Artist doesArtistExist(Artist artist) throws Exception {
        String sql = "SELECT * FROM dbo.artists WHERE artist_id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, artist.getArtistID());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("artist_id") != null) {
                    int id = rs.getInt("id");
                    String artistID = rs.getString("artist_id");
                    String name = rs.getString("name");
                    String alias = rs.getString("alias");
                    String pictureURL = rs.getString("pictureURL");

                    return new Artist(id, artistID, name, alias, pictureURL);
                }
            }
        }
        return null;
    }

    public Artist createArtist(Artist artist) throws Exception {
        // SQL command
        String sql = "INSERT INTO dbo.artists (artist_id, name, alias,pictureURL) VALUES (?,?,?,?);";

        //
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            // Bind parameters
            stmt.setString(1, artist.getArtistID());
            stmt.setString(2, artist.getName());
            stmt.setString(3, artist.getAlias());

            CacheSystem cacheSystem = new CacheSystem();
            String storedPath = cacheSystem.storeImage(ConfigSystem.getArtistDefault());

            stmt.setString(4, storedPath);

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create song object and send up the layers
            Artist createdArtist = new Artist(id, artist.getArtistID(), artist.getName(), artist.getAlias(), storedPath);

            return createdArtist;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create song", ex);
        }
    }

    public List<Artist> getAllArtists() throws Exception {
        ArrayList<Artist> allArtists = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM dbo.artists";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String musicBrainzID = rs.getString("artist_id");
                String name = rs.getString("name");
                String alias = rs.getString("alias");
                String pictureURL = rs.getString("pictureURL");

                Artist artist = new Artist(id, musicBrainzID, name, alias, pictureURL);
                allArtists.add(artist);
            }
            return allArtists;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database", ex);
        }
    }

    public List<Song> getArtistSongs(Artist artist) throws Exception {
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
                "    SELECT\n" +
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
                "WHERE row_num = 1 AND artistID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            List<Song> songsByArtist = new ArrayList<>();

            stmt.setInt(1, artist.getPrimaryID());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("songID");
                String title = rs.getString("songTitle");
                String artistName = rs.getString("artistName");
                int artistID = rs.getInt("artistID");
                String genre = rs.getString("songGenre");
                String filePath = rs.getString("filePath");
                String musicBrainzID = rs.getString("songMBID");
                String pictureURL = rs.getString("pictureURL");
                String album = rs.getString("albumName");

                songsByArtist.add(new Song(musicBrainzID, id, title, artistName, genre, filePath, pictureURL, album, artistID));
            }
            return songsByArtist;
        }
    }

    public List<Album> getArtistAlbums(Artist artist) throws Exception {
        String sql = "SELECT Albums.*, artists.name AS artistName\n" +
                "FROM Albums\n" +
                "JOIN artists ON Albums.artist_id = artists.id\n" +
                "WHERE artists.id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            List<Album> albumsByArtist = new ArrayList<>();
            stmt.setInt(1, artist.getPrimaryID());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String released = rs.getString("released");
                String type = rs.getString("type");
                int artist_id = rs.getInt("artist_id");
                String musicBrainzID = rs.getString("MusicBrainzID");
                String pictureURL = rs.getString("pictureURL");
                String artistName = rs.getString("artistName");

                albumsByArtist.add(new Album(id, name, released, type, artist_id, pictureURL, artistName));
            }
            return albumsByArtist;
        }
    }
}
