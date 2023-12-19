package mytunes.DAL.DB.Objects;

import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.DB.Connect.DatabaseConnector;

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
                String pictureURL = rs.getString("PictureURL");

                Playlist playlist = new Playlist(id, name, order_id, pictureURL);
                allPlaylists.add(playlist);
            }

            return allPlaylists;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database", ex);
        }
    }

    public boolean updateOrder(Playlist playlistNew, Playlist playlistOld) throws Exception {
        String sql = "UPDATE Playlists SET order_id = ? WHERE id = ?;";

        // Create connection here, so we can check later
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
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database", ex);
        }
    }

    public Playlist createPlaylist(Playlist playlist) throws Exception {
        String sql = "INSERT INTO dbo.Playlists (name,order_id,PictureURL) VALUES (?,?,?);";

        //
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Bind parameters
            stmt.setString(1, playlist.getName());
            stmt.setInt(2, playlist.getOrderID());
            stmt.setString(3, playlist.getPictureURL());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create song object and send up the layers
            Playlist createdPlaylist = new Playlist(id, playlist.getName(), playlist.getOrderID(), playlist.getPictureURL());

            return createdPlaylist;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create playlist", ex);
        }
    }

    public List<Song> getSongs(Playlist playlist) throws Exception {
        ArrayList<Song> allSongsInPlaylist = new ArrayList<>();

        String sql = "SELECT\n" +
                "    songs.id,\n" +
                "    MAX(songs.title) as title,\n" +
                "    MAX(songs.Filepath) as Filepath,\n" +
                "    MAX(songs.SongID) as SongID,\n" +
                "    MAX(songs.PictureURL) as PictureURL,\n" +
                "    MAX(songs.Genre) as Genre,\n" +
                "    MAX(artists.name) as artistName,\n" +
                "    MAX(artists.id) as artistID,\n" +
                "    MAX(playlists_songs.added) as timeAdded,\n" +
                "    MAX(AlbumsRanked.albumName) as albumName\n" +
                "FROM\n" +
                "    songs\n" +
                "JOIN playlists_songs ON songs.id = playlists_songs.song_id\n" +
                "JOIN playlists ON playlists_songs.playlist_id = playlists.id\n" +
                "JOIN artists ON songs.Artist = artists.id\n" +
                "JOIN (\n" +
                "    SELECT\n" +
                "        Albums_songs.song_id,\n" +
                "        Albums_songs.album_id,\n" +
                "        Albums.name as albumName,  -- Adjust the alias for the column\n" +
                "        ROW_NUMBER() OVER (PARTITION BY Albums_songs.song_id ORDER BY Albums_songs.album_id) as row_num\n" +
                "    FROM\n" +
                "        Albums_songs\n" +
                "    JOIN Albums ON Albums_songs.album_id = Albums.id\n" +
                ") AS AlbumsRanked ON songs.Id = AlbumsRanked.song_id AND AlbumsRanked.row_num = 1\n" +
                "WHERE\n" +
                "    playlists.id = ?\n" +
                "GROUP BY\n" +
                "    songs.id;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, playlist.getId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int songId = rs.getInt("Id");
                String title = rs.getString("Title");
                String artist = rs.getString("artistName");
                int artistID = rs.getInt("artistID");
                String genre = rs.getString("Genre");
                String filePath = rs.getString("Filepath");
                String musicBrainzID = rs.getString("SongID");
                String pictureURL = rs.getString("PictureURL");
                String albumName = rs.getString("albumName");
                Timestamp timeAdded = rs.getTimestamp("timeAdded");

                Song song = new Song(musicBrainzID, songId, title, artist, genre, filePath, pictureURL, albumName, artistID, timeAdded);
                allSongsInPlaylist.add(song);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database", ex);
        }
        return allSongsInPlaylist;
    }

    public boolean addSongToPlaylist(Playlist playlist, Song song) throws Exception {
        String sql = "INSERT INTO dbo.playlists_songs (playlist_id,song_id,added) VALUES (?,?,?);";

        //
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            // Bind parameters
            stmt.setInt(1, playlist.getId());
            stmt.setInt(2, song.getId());
            stmt.setTimestamp(3, currentTime);

            // Run the specified SQL statement
            stmt.executeUpdate();

            song.setDate(currentTime);
            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create playlist", ex);
        }
    }

    public Playlist editPlaylist(Playlist playlist, String newName) throws Exception {
        // SQL command
        String sql = "UPDATE dbo.Playlists SET name = ? WHERE [id] = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            // Bind parameters
            stmt.setString(1, newName);
            stmt.setInt(2, playlist.getId());

            playlist.setName(newName);

            // Run the specified SQL statement
            stmt.executeUpdate();

            return playlist;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update song", ex);
        }
    }

    private void updateOrderIDDelete(Connection conn, int deletedOrderId) throws SQLException {
        String updateSql = "UPDATE dbo.Playlists SET order_id = order_id - 1 WHERE order_id >= ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, deletedOrderId);
            updateStmt.executeUpdate();
        }
    }

    private void deleteplaylistByID(Connection conn, int playlistID) throws SQLException {
        String deleteSql = "DELETE FROM dbo.Playlists WHERE [id] = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, playlistID);
            deleteStmt.executeUpdate();
        }
    }

    private void deleteSongsInPlaylist(Connection conn, int playlistID) throws SQLException {
        String deleteSql = "DELETE FROM dbo.playlists_songs WHERE playlist_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, playlistID);
            deleteStmt.executeUpdate();
        }
    }

    public boolean deletePlaylist(Playlist playlist) throws Exception {
        try (Connection conn = databaseConnector.getConnection()) {
            // Begin a transaction
            conn.setAutoCommit(false);

            try {
                // Delete songs in the playlist
                deleteSongsInPlaylist(conn, playlist.getId());

                // Delete playlist
                deleteplaylistByID(conn, playlist.getId());

                // Update the orderIDs
                updateOrderIDDelete(conn, playlist.getOrderID());

                // Commit the transaction
                conn.commit();
                return true;
            } catch (Exception ex) {
                // Rollback the transaction in case of an exception
                conn.rollback();
                throw ex;
            }
        }
    }

    public boolean removeSongFromPlaylist(Playlist playlist, Song song) throws Exception {
        String sql = "DELETE FROM dbo.playlists_songs WHERE playlist_id = ? AND song_id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, playlist.getId());
            stmt.setInt(2, song.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not delete song", ex);
        }
    }
}
