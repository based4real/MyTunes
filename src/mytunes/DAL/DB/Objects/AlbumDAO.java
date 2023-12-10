package mytunes.DAL.DB.Objects;

import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.DB.Connect.DatabaseConnector;
import mytunes.BE.REST.Release;

import java.io.IOException;
import java.sql.*;

public class AlbumDAO {

    private DatabaseConnector databaseConnector;

    public AlbumDAO() throws IOException {
        databaseConnector = new DatabaseConnector();
    }

    private Album checkExists(Connection conn, Release album) {
        String sql = "SELECT * FROM Albums WHERE MusicBrainzID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, album.getReleaseId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String released = rs.getString("released");
                String type = rs.getString("type");
                int artist_id = rs.getInt("artist_id");
                String musicBrainzID = rs.getString("MusicBrainzID");

                return new Album(id, album.getTitle(), album.getDate(), type, artist_id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private boolean doesSongExist(Connection conn, Release album) {
        String sql = "SELECT * FROM Albums_songs WHERE position = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, album.getSongPos());

            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Album createAlbum(Connection conn, Release album, Artist artist) throws SQLException {
        String sql = "INSERT INTO dbo.Albums (name,released,type,artist_id,MusicBrainzID) VALUES (?,?,?,?,?);";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            String type = album.getIsSingle() ? "Single" : "Album";

            stmt.setString(1, album.getTitle());
            stmt.setString(2, album.getDate());
            stmt.setString(3, type);
            stmt.setInt(4, artist.getPrimaryID());
            stmt.setString(5, album.getReleaseId());

            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next())
                id = rs.getInt(1);

            return new Album(id, album.getTitle(), album.getDate(), type, artist.getPrimaryID());
        }
    }

    private void addSongToAlbum(Connection conn, Release release, Album album, Song song) throws SQLException {
        String sql = "INSERT INTO dbo.Albums_songs (album_id,song_id,position) VALUES (?,?,?);";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, album.getID());
            stmt.setInt(2, song.getId());
            stmt.setInt(3, release.getSongPos());
            stmt.executeUpdate();
        }
    }

    public boolean createAlbum(Release album, Song song, Artist artist) throws SQLException {
        try (Connection conn = databaseConnector.getConnection()) {
            // Begin a transaction
            conn.setAutoCommit(false);

            try {
                Album createdAlbum = checkExists(conn, album);

                if (createdAlbum == null)
                    createdAlbum = createAlbum(conn, album, artist);

                if (!doesSongExist(conn, album))
                    addSongToAlbum(conn, album, createdAlbum, song);

                conn.commit();
            } catch (Exception ex) {
                // Rollback the transaction in case of an exception
                conn.rollback();
                throw ex;
            }
        }
        return false;
    }
}
