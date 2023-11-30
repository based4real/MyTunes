package mytunes.DAL;

import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.DB.DatabaseConnector;

import java.io.IOException;
import java.sql.*;

public class PlaylistDAO {

    private DatabaseConnector databaseConnector;

    public PlaylistDAO() throws IOException {
        databaseConnector = new DatabaseConnector();
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
}
