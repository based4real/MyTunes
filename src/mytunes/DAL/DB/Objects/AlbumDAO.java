package mytunes.DAL.DB.Objects;

import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.BLL.util.CacheSystem;
import mytunes.DAL.DB.Connect.DatabaseConnector;
import mytunes.BE.REST.Release;
import mytunes.DAL.REST.CoverArt;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                String pictureURL = rs.getString("pictureURL");

                return new Album(id, album.getTitle(), album.getDate(), type, artist_id, pictureURL);
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

    private Album createAlbum(Connection conn, Release album, Artist artist) throws Exception {
        String sql = "INSERT INTO dbo.Albums (name,released,type,artist_id,MusicBrainzID,pictureURL) VALUES (?,?,?,?,?,?);";


        CoverArt coverArt = new CoverArt(album.getReleaseId());
        String albumLink = coverArt.getFrontThumbnail();

        String albumPicture = albumLink == null ? "https://i.imgur.com/LnNRAzz.png" : albumLink;

        CacheSystem cacheSystem = new CacheSystem();
        String storedPath = cacheSystem.storeImage(albumPicture);

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            String type = album.getIsSingle() ? "Single" : "Album";

            stmt.setString(1, album.getTitle());
            stmt.setString(2, album.getDate());
            stmt.setString(3, type);
            stmt.setInt(4, artist.getPrimaryID());
            stmt.setString(5, album.getReleaseId());
            stmt.setString(6, storedPath);

            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next())
                id = rs.getInt(1);

            return new Album(id, album.getTitle(), album.getDate(), type, artist.getPrimaryID(), storedPath);
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

    public boolean createAlbum(Release album, Song song, Artist artist) throws Exception {
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

    public List<Album> getAllAlbums() throws Exception {
        ArrayList<Album> allAlbums = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM dbo.Albums";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String released = rs.getString("released");
                String type = rs.getString("type");
                int artistId = rs.getInt("artist_id");
                String pictureURL = rs.getString("pictureURL");


                Album album = new Album(id,name,released,type,artistId, pictureURL);
                allAlbums.add(album);
            }
            return allAlbums;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get albums from database", ex);
        }
    }
}
