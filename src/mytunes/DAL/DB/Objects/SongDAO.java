package mytunes.DAL.DB.Objects;

import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BLL.util.CacheSystem;
import mytunes.BLL.util.ConfigSystem;
import mytunes.DAL.DB.Connect.DatabaseConnector;
import mytunes.DAL.REST.CoverArt;
import org.json.Cookie;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
            // This query removes duplicates, so if two with the same
            // MusicBrainz ID is present in the DB, don't include them.

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
                    "WHERE row_num = 1";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("songID");
                String title = rs.getString("songTitle");
                String artist = rs.getString("artistName");
                int artistID = rs.getInt("artistID");
                String genre = rs.getString("songGenre");
                String filePath = rs.getString("filePath");
                String musicBrainzID = rs.getString("songMBID");
                String pictureURL = rs.getString("pictureURL");
                String album = rs.getString("albumName");

                Song song = new Song(musicBrainzID, id, title, artist, genre, filePath, pictureURL, album, artistID);
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

    private Song doesSongExist(Connection conn, Song song) {
        String sql = "SELECT\n" +
                "    Songs.Id as songID,\n" +
                "    Songs.Title as songTitle,\n" +
                "    Songs.Filepath as filePath,\n" +
                "    Songs.songID as songMBID,\n" +
                "    Songs.Genre as songGenre,\n" +
                "    artists.id as artistID,\n" +
                "    artists.name as artistName,\n" +
                "    artists.alias as artistAlias,\n" +
                "    Songs.PictureURL as pictureURL,\n" +
                "    Albums.name as albumName\n" +
                "FROM Songs\n" +
                "JOIN artists ON artists.id = Songs.Artist\n" +
                "JOIN Albums_songs ON Albums_songs.song_id = Songs.Id\n" +
                "JOIN Albums ON Albums_songs.album_id = Albums.id\n" +
                "WHERE SongID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, song.getMusicBrainzID());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("songID");
                String title = rs.getString("songTitle");
                String artist = rs.getString("artistName");
                int artistID = rs.getInt("artistID");
                String genre = rs.getString("songGenre");
                String filePath = rs.getString("filePath");
                String musicBrainzID = rs.getString("songMBID");
                String pictureURL = rs.getString("pictureURL");
                String albumName = rs.getString("albumName");

                return new Song(musicBrainzID, id, title, artist, genre, filePath, pictureURL, albumName, artistID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Song createSong(Song song) throws Exception {
        // SQL command
        String sql = "INSERT INTO dbo.Songs (Title, Artist, Genre, Filepath, SongID, PictureURL) VALUES (?,?,?,?,?,?);";

        CacheSystem cacheSystem = new CacheSystem();
        String storedPath = cacheSystem.storeImage(song.getPictureURL());

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            Song songExists = doesSongExist(conn, song);

            if (songExists != null)
                return songExists;

            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setInt(2, song.getArtistID());
            stmt.setString(3, song.getGenre());
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
            Song createdSong = new Song(song.getMusicBrainzID(), id, song.getTitle(), song.getArtistName(), song.getGenre(), song.getFilePath(), storedPath);
            return createdSong;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create song", ex);
        }
    }

    public Artist getArtistFromSong(Song song) throws Exception {
        String sql = "SELECT artists.*\n" +
                "FROM artists\n" +
                "JOIN Songs on artists.id = Songs.Artist\n" +
                "WHERE Songs.Artist = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, song.getArtistID());

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

    public void updateSong(Song song) throws Exception {
        // SQL command
        String sql = "UPDATE dbo.Songs SET Title = ?, Artist = ?, Filepath = ? WHERE Id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            // Bind parameters
            stmt.setString(1, song.getTitle());
            stmt.setInt(2, song.getArtistID());
            stmt.setString(3, song.getFilePath());
            stmt.setInt(4, song.getId());

            // Run the specified SQL statement
            stmt.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not update song", ex);
        }
    }

    private void deleteSongFromAlbums(Connection conn, Song song) throws SQLException {
        String deleteSql = "DELETE FROM dbo.Albums_songs WHERE song_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, song.getId());
            deleteStmt.executeUpdate();
        }
    }

    private void deleteSongFromPlaylists(Connection conn, Song song) throws SQLException {
        String deleteSql = "DELETE FROM dbo.playlists_songs WHERE song_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, song.getId());
            deleteStmt.executeUpdate();
        }
    }

    private void deleteSongInDb(Connection conn, Song song) throws SQLException {
        String deleteSql = "DELETE FROM dbo.Songs WHERE ID = (?);";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, song.getId());
            deleteStmt.executeUpdate();
        }
    }

    public boolean deleteSong(Song song) throws Exception {
        try (Connection conn = databaseConnector.getConnection()) {
            // Begin a transaction
            conn.setAutoCommit(false);

            try {
                System.out.println("d");
                // Delete songs in the playlist
                deleteSongFromPlaylists(conn, song);

                // Delete playlist
                deleteSongFromAlbums(conn, song);

                // Update the orderIDs
                deleteSongInDb(conn, song);

                // Commit the transaction
                conn.commit();
                return true;
            } catch (Exception ex) {
                // Rollback the transaction in case of an exception
                System.out.println(ex);
                conn.rollback();
                throw ex;
            }
        }
    }

    // Should maybe be in PlaylistDAO and not SongDAO
    public boolean updateOrderID(Playlist playlist, Song draggedSong, Song droppedSong) throws Exception {
        String sql = "UPDATE dbo.Playlists_songs SET order_id = ? WHERE song_id = ? AND playlist_id = ?;";

        // Create connection here, so we can check later
        // In the catch statements.
        Connection conn = null;
        try {
            conn = databaseConnector.getConnection();
            conn.setAutoCommit(false);  // Start a transaction

            int orderIDNew = draggedSong.getOrderID();
            int orderIDOld = droppedSong.getOrderID();

            // Update our old value with order id from new song.
            try (PreparedStatement stmtOld = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmtOld.setInt(1, draggedSong.getOrderID());
                stmtOld.setInt(2, droppedSong.getId());
                stmtOld.setInt(3, playlist.getId());

                stmtOld.executeUpdate();
            }

            // Update our new value with order id from old song.
            try (PreparedStatement stmtNew = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmtNew.setInt(1, droppedSong.getOrderID());
                stmtNew.setInt(2, draggedSong.getId());
                stmtNew.setInt(3, playlist.getId());
                stmtNew.executeUpdate();
            }

            conn.commit();  // If both updates succeed, commit the transaction

            draggedSong.setOrderID(orderIDOld);
            droppedSong.setOrderID(orderIDNew);

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
}
