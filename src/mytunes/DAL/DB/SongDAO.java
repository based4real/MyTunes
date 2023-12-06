package mytunes.DAL.DB;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import mytunes.BE.Song;
import mytunes.BLL.util.CacheSystem;
import mytunes.DAL.DB.DatabaseConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    private DatabaseConnector databaseConnector;

    public SongDAO() throws IOException{
        databaseConnector = new DatabaseConnector();
    }

    public List<Song> getAllSongs() throws Exception {
        ArrayList<Song> allSongs = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
            Statement stmt = conn.createStatement())
        {
            String sql = "SELECT\n" +
                    "  Songs.Id as songID,\n" +
                    "  Songs.Title as songTitle,\n" +
                    "  Songs.Filepath as filePath,\n" +
                    "  Songs.songID as songID,\n" +
                    "  Songs.Album as songAlbum,\n" +
                    "  artists.id as artistID,\n" +
                    "  artists.name as artistName,\n" +
                    "  artists.alias as artistAlias,\n" +
                    "  Songs.PictureURL as pictureURL\n" +
                    "FROM Songs\n" +
                    "JOIN artists ON artists.id=Songs.Artist;\n";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("songID");
                String title = rs.getString("songTitle");
                String artist = rs.getString("artistName");
                String album = rs.getString("songAlbum");
                String filePath = rs.getString("filePath");
                String musicBrainzID = rs.getString("SongID");
                String pictureURL = rs.getString("pictureURL");

                Song song = new Song(musicBrainzID, id, title, artist ,album, filePath, pictureURL);
                allSongs.add(song);
            }
            return allSongs;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get songs from database", ex);
        }
    }

    public Song createSong(Song song) throws Exception {
        // SQL command
        String sql = "INSERT INTO dbo.Songs (Title, Artist,Album,Filepath, SongID, PictureURL) VALUES (?,?,?,?,?,?);";

        CacheSystem cacheSystem = new CacheSystem();
        String storedPath = cacheSystem.storeImage(song.getPictureURL());

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setInt(2, song.getArtistID());
            stmt.setString(3, song.getAlbum());
            stmt.setString(4, song.getFilePath());
            stmt.setString(5, song.getMusicBrainzID());
            stmt.setString(6, storedPath);

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create song object and send up the layers
            Song createdSong = new Song(song.getMusicBrainzID(), id, song.getTitle(), song.getArtistName(), song.getAlbum(), song.getFilePath(), storedPath);
            return createdSong;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create song", ex);
        }
    }

    public void updateSong(Song song) throws Exception {
        // SQL command
        String sql = "UPDATE dbo.Songs SET Title = ?, Artist = ?, Album = ?, Filepath = ? WHERE Id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setInt(2, song.getArtistID());
            stmt.setString(3, song.getAlbum());
            stmt.setString(4, song.getFilePath());
            stmt.setInt(5, song.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update song", ex);
        }
    }
    public void deleteSong(Song song) throws Exception {
        // SQL command
        String sql = "DELETE FROM dbo.Songs WHERE ID = (?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, song.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not delete song", ex);
        }
    }
}
