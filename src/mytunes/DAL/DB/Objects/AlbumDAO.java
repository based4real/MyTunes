package mytunes.DAL.DB.Objects;

import mytunes.BE.Album;
import mytunes.BE.Artist;
import mytunes.BE.Song;
import mytunes.BLL.util.CacheSystem;
import mytunes.BLL.util.ConfigSystem;
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
        String sql = "SELECT albums.*, artists.name as artistName\n" +
                "FROM dbo.Albums\n" +
                "JOIN artists ON Albums.artist_id = artists.id\n" +
                "WHERE albums.MusicBrainzID = ?";

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
                String artistName = rs.getString("artistName");

                return new Album(id, name, released, type, artist_id, pictureURL, artistName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private boolean doesSongExistInAlbum(Connection conn, Album album, Song song) {
        String sql = "SELECT * FROM Albums_songs WHERE album_id = ? AND song_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, album.getID());
            stmt.setInt(2, song.getId());

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

        String albumPicture = albumLink == null ? ConfigSystem.getAlbumDefault() : albumLink;

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

            return new Album(id, album.getTitle(), album.getDate(), type, artist.getPrimaryID(), storedPath, artist.getName());
        }
    }

    private void addSongToAlbum(Connection conn, Release release, Album album, Song song) throws SQLException {
        String sql = "INSERT INTO dbo.Albums_songs (album_id,song_id,position) VALUES (?,?,?);";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, album.getID());
            stmt.setInt(2, song.getId());
            stmt.setInt(3, release.getSongPos());

            stmt.executeUpdate();
            album.addToAlbumSongs(song);
        }
    }

    public List<Song> getAlbumSongs(Album album) throws Exception {
        ArrayList<Song> allSongsInAlbum = new ArrayList<>();

        String sql = "SELECT songs.*, artists.name as artistName, Albums_songs.position as order_id, Albums.pictureURL as albumsPicture, Albums.name as albumsName\n" +
                "FROM songs\n" +
                "JOIN Albums_songs ON songs.id = Albums_songs.song_id\n" +
                "JOIN Albums ON Albums_songs.album_id = Albums.id\n" +
                "JOIN artists ON songs.Artist = artists.id\n" +
                "WHERE Albums.id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, album.getID());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int songId = rs.getInt("Id");
                String title = rs.getString("Title");
                String artist = rs.getString("artistName");
                int artistID = rs.getInt("Artist");
                String genre = rs.getString("Genre");
                String filePath = rs.getString("Filepath");
                String musicBrainzID = rs.getString("SongID");
                String pictureURL = rs.getString("albumsPicture");
                String albumName = rs.getString("albumsName");
                int position = rs.getInt("order_id");

                allSongsInAlbum.add(new Song(musicBrainzID, songId, title, artist, genre, filePath, pictureURL, albumName, artistID, position));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get playlists from database", ex);
        }
        return allSongsInAlbum;
    }

    public Album getAlbumFromSong(Song song) {
        String sql = "SELECT Albums.*, artists.name as artistName FROM Albums\n" +
                "JOIN Albums_songs ON Albums.id = Albums_songs.album_id\n" +
                "JOIN artists on Albums.artist_id = Albums.artist_id\n" +
                "WHERE Albums_songs.song_id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, song.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String released = rs.getString("released");
                String type = rs.getString("type");
                int artistId = rs.getInt("artist_id");
                String pictureURL = rs.getString("pictureURL");
                String artistName = rs.getString("artistName");

                return new Album(id,name,released,type,artistId, pictureURL, artistName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean createAlbum(Release album, Song song, Artist artist) throws Exception {
        try (Connection conn = databaseConnector.getConnection()) {
            // Begin a transaction
            conn.setAutoCommit(false);

            try {
                Album createdAlbum = checkExists(conn, album);

                if (createdAlbum == null)
                    createdAlbum = createAlbum(conn, album, artist);

                if (!doesSongExistInAlbum(conn, createdAlbum, song))
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
            String sql = "SELECT albums.*, artists.name as artistName\n" +
                    "FROM dbo.Albums\n" +
                    "JOIN artists ON Albums.artist_id = artists.id";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String released = rs.getString("released");
                String type = rs.getString("type");
                int artistId = rs.getInt("artist_id");
                String pictureURL = rs.getString("pictureURL");
                String artistName = rs.getString("artistName");


                Album album = new Album(id,name,released,type,artistId, pictureURL, artistName);
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
