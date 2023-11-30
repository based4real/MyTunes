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

                        allSongsInPlaylist.add(new Song(songId, title, artist ,album, filePath));
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

    public void addSongToPlaylist(Playlist playlist, Song song) throws Exception {
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
