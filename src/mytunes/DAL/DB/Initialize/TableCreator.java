package mytunes.DAL.DB.Initialize;

import mytunes.BLL.util.ConfigSystem;
import mytunes.DAL.DB.Connect.DatabaseConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class TableCreator {
    private DatabaseConnector databaseConnector;

    List<String> genresAsList = Arrays.asList("Rock", "Hip-Hop", "Pop", "Jazz", "Country", "Elektronisk", "R&B", "Klassisk", "Reggae", "Heavy Metal");

    public TableCreator() throws IOException {
        databaseConnector = new DatabaseConnector();
    }

    public boolean dropTables() throws SQLException {
        try (Connection conn = databaseConnector.getConnection()) {
            dropAllTables(conn);
            return true;
        }
    }

    public boolean initalize() throws Exception {
        try (Connection conn = databaseConnector.getConnection()) {
            // Begin a transaction
            conn.setAutoCommit(false);

            try {
                useDatabase(conn);

                createArtists(conn);

                createGenre(conn);
                insertDataIntoGenre(conn);

                createSongs(conn);

                createAlbums(conn);
                createAlbumSongs(conn);

                createPlaylists(conn);
                createPlaylistSongs(conn);

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

    private void dropAllTables(Connection conn) throws SQLException {
        String sql = "DROP TABLE IF EXISTS playlists_songs, playlists, Albums_songs, Albums, Songs, Genres, artists";
        try (PreparedStatement createStmt = conn.prepareStatement(sql)) {
            createStmt.executeUpdate();
        }
    }

    private void useDatabase(Connection conn) throws SQLException, IOException {
        String sql = "use " + ConfigSystem.getDatabaseName();
        try (PreparedStatement createStmt = conn.prepareStatement(sql)) {
            createStmt.executeUpdate();
        }
    }

    private void createPlaylistSongs(Connection conn) throws Exception {
        String sql = "create table playlists_songs\n" +
                "(\n" +
                "    playlist_id int not null\n" +
                "        constraint playlists_songs_Playlists_id_fk\n" +
                "            references Playlists,\n" +
                "    song_id     int\n" +
                "        constraint playlists_songs_Songs_id_fk\n" +
                "            references Songs,\n" +
                "    added       datetime\n" +
                ")";

        try (PreparedStatement createStmt = conn.prepareStatement(sql)) {
            createStmt.executeUpdate();
        }
    }

    private void createPlaylists(Connection conn) throws Exception {
        String sql = "create table dbo.Playlists\n" +
                "(\n" +
                "    id         int identity\n" +
                "        constraint Playlists_pk\n" +
                "            primary key,\n" +
                "    name       varchar(255) not null,\n" +
                "    order_id   int,\n" +
                "    PictureURL varchar(255)\n" +
                ")";
        try (PreparedStatement createStmt = conn.prepareStatement(sql)) {
            createStmt.executeUpdate();
        }
    }

    private void createArtists(Connection conn) throws SQLException {
        String sql = "create table dbo.artists\n" +
                "(\n" +
                "    id         int identity\n" +
                "        constraint artists_pk\n" +
                "            primary key,\n" +
                "    artist_id  varchar(255),\n" +
                "    name       varchar(255),\n" +
                "    alias      varchar(255),\n" +
                "    pictureURL varchar(255)\n" +
                ")";
        try (PreparedStatement createStmt = conn.prepareStatement(sql)) {
            createStmt.executeUpdate();
        }
    }

    private void createAlbumSongs(Connection conn) throws SQLException {
        String sql = "create table dbo.Albums_songs\n" +
                "(\n" +
                "    album_id int\n" +
                "        constraint Albums_songs_Albums_id_fk\n" +
                "            references Albums,\n" +
                "    song_id  int\n" +
                "        constraint Albums_songs_Songs_Id_fk\n" +
                "            references Songs,\n" +
                "    position int\n" +
                ")";
        try (PreparedStatement createStmt = conn.prepareStatement(sql)) {
            createStmt.executeUpdate();
        }
    }

    private void createAlbums(Connection conn) throws SQLException {
        String sql = "create table dbo.Albums\n" +
                "(\n" +
                "    id            int identity\n" +
                "        constraint Albums_pk\n" +
                "            primary key,\n" +
                "    name          varchar(255),\n" +
                "    released      varchar(255),\n" +
                "    type          varchar(50),\n" +
                "    artist_id     int\n" +
                "        constraint Albums_artists_id_fk\n" +
                "            references artists,\n" +
                "    MusicBrainzID varchar(255),\n" +
                "    pictureURL    varchar(255)\n" +
                ")";
        try (PreparedStatement createStmt = conn.prepareStatement(sql)) {
            createStmt.executeUpdate();
        }
    }

    private void createGenre(Connection conn) throws SQLException {
        String sql = "create table dbo.Genres\n" +
                "(\n" +
                "    name varchar(50) not null\n" +
                "        constraint Genre_pk\n" +
                "            primary key\n" +
                ")";
        try (PreparedStatement createStmt = conn.prepareStatement(sql)) {
            createStmt.executeUpdate();
        }
    }

    private void insertDataIntoGenre(Connection conn) throws SQLException {
        String insertDataSQL = "INSERT INTO dbo.Genres (name) VALUES (?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertDataSQL)) {
            for (String s : genresAsList) {
                insertStmt.setString(1, s);
                insertStmt.executeUpdate();
            }
        }
    }

    private void createSongs(Connection conn) throws Exception {
        String sql = "create table dbo.Songs\n" +
                "(\n" +
                "    Id         int identity\n" +
                "        constraint Songs_pk\n" +
                "            primary key,\n" +
                "    Title      varchar(255),\n" +
                "    Artist     int\n" +
                "        constraint Songs_artists_id_fk\n" +
                "            references artists,\n" +
                "    Filepath   varchar(255),\n" +
                "    SongID     varchar(255),\n" +
                "    PictureURL varchar(255),\n" +
                "    Genre      varchar(50)\n" +
                "        constraint Songs_Genres_name_fk\n" +
                "            references Genres\n" +
                ")";
        try (PreparedStatement createStmt = conn.prepareStatement(sql)) {
            createStmt.executeUpdate();
        }
    }
}
