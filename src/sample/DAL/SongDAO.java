package sample.DAL;

import sample.BE.Song;

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
            String sql = "SELECT * FROM Songs;";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("Id");
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
                String album = rs.getString("Album");
                String filePath = rs.getString("Filepath");

                Song song = new Song(id, title, artist ,album, filePath);
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
        String sql = "INSERT INTO dbo.Songs (Title,Artist,Album,Filepath) VALUES (?,?,?,?);";

        //
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            // Bind parameters
            stmt.setString(1,song.getTitle());
            stmt.setString(2, song.getArtist());
            stmt.setString(3, song.getAlbum());
            stmt.setString(4, song.getFilePath());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create song object and send up the layers
            Song createdSong = new Song(id, song.getTitle(), song.getArtist(), song.getAlbum(), song.getFilePath());

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
                stmt.setString(2, song.getArtist());
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
