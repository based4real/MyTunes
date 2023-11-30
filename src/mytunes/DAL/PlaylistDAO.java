package mytunes.DAL;

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

    public static void main(String[] args) throws Exception {
        PlaylistDAO playlistDAO = new PlaylistDAO();
    }
}
