package mytunes.DAL.DB;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.application.Application;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.DB.DatabaseConnector;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    private DatabaseConnector databaseConnector;

    public PlaylistDAO() throws IOException {
        databaseConnector = new DatabaseConnector();
    }

    public List<Playlist> getallPlaylists() throws Exception {
        ArrayList<Playlist> allPlaylists = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM Playlists;";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int order_id = rs.getInt("order_id");

                Playlist playlist = new Playlist(id, name, order_id);
                allPlaylists.add(playlist);
            }

            return allPlaylists;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database", ex);
        }
    }

    public boolean updateOrder(Playlist playlistNew, Playlist playlistOld) throws Exception {
        String sql = "UPDATE Playlists SET order_id = ? WHERE id = ?;";

        // Create connection here so we can check later
        // In the catch statements.

        Connection conn = null;
        try {
            conn = databaseConnector.getConnection();
            conn.setAutoCommit(false);  // Start a transaction

            int orderIDNew = playlistNew.getOrderID();
            int orderIDOld = playlistOld.getOrderID();

            // Update our old value with order id from new playlist.
            try (PreparedStatement stmtOld = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmtOld.setInt(1, playlistNew.getOrderID());
                stmtOld.setInt(2, playlistOld.getId());
                stmtOld.executeUpdate();
            }

            // Update our new value with order id from old playlist.
            try (PreparedStatement stmtNew = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmtNew.setInt(1, playlistOld.getOrderID());
                stmtNew.setInt(2, playlistNew.getId());
                stmtNew.executeUpdate();
            }

            conn.commit();  // If both updates succeed, commit the transaction

            // Update order_id values in array
            playlistNew.setOrderID(orderIDOld);
            playlistOld.setOrderID(orderIDNew);

            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();  // If an error occurs, rollback the transaction
                } catch (SQLException ex) {
                    ex.printStackTrace();  // Handle rollback exception
                }
            }
            e.printStackTrace();
            throw new Exception("Could not update playlist order in the database", e);
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getNextOrderID() throws Exception {
        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT order_id FROM Playlists;";
            ResultSet rs = stmt.executeQuery(sql);

            int newID = 0;
            while (rs.next()) {
                int id = rs.getInt("order_id");

                if (id > newID)
                    newID = id;
            }
            return newID + 1;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database", ex);
        }
    }

    public Playlist createPlaylist(Playlist playlist) throws Exception {
        String sql = "INSERT INTO dbo.Playlists (name,order_id) VALUES (?,?);";

        //
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            // Bind parameters
            stmt.setString(1, playlist.getName());
            stmt.setString(2, Integer.toString(playlist.getOrderID()));

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create song object and send up the layers
            Playlist createdPlaylist = new Playlist(id, playlist.getName(), playlist.getOrderID());

            return createdPlaylist;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create playlist", ex);
        }
    }

    public List<Song> getSongs(Playlist playlist) throws Exception {
        ArrayList<Song> allSongsInPlaylist = new ArrayList<>();

        String sql = "SELECT song_id FROM playlists_songs WHERE playlist_id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, playlist.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String sqlSong = "SELECT * FROM Songs WHERE Id = ?;";
                int id = rs.getInt("song_id");

                try (PreparedStatement stmt2 = conn.prepareStatement(sqlSong, Statement.RETURN_GENERATED_KEYS)) {
                    stmt2.setInt(1, id);
                    ResultSet rs2 = stmt2.executeQuery();

                    while (rs2.next()) {
                        int songId = rs2.getInt("Id");
                        String title = rs2.getString("Title");
                        String artist = rs2.getString("Artist");
                        String album = rs2.getString("Album");
                        String filePath = rs2.getString("Filepath");

                        Song song = new Song(songId, title, artist ,album, filePath);
                        allSongsInPlaylist.add(song);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw new Exception("Could not get playlists from database", ex);
                }
            }
            return allSongsInPlaylist;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database", ex);
        }
    }

    public boolean addSongToPlaylist(Playlist playlist, Song song) throws Exception {
        String sql = "INSERT INTO dbo.playlists_songs (playlist_id,song_id) VALUES (?,?);";

        //
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            // Bind parameters
            stmt.setInt(1, playlist.getId());
            stmt.setInt(2, song.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create playlist", ex);
        }
    }

}
