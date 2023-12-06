package mytunes.DAL.DB;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import mytunes.BE.Artist;
import mytunes.BE.Playlist;
import mytunes.BE.Song;

import java.io.IOException;
import java.sql.*;

public class ArtistDAO {

    private DatabaseConnector databaseConnector;

    public ArtistDAO() throws IOException {
        databaseConnector = new DatabaseConnector();
    }

    public Artist doesArtistExist(Artist artist) throws Exception {
        String sql = "SELECT * FROM dbo.artists WHERE artist_id = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, artist.getArtistID());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("artist_id") != null) {
                    int id = rs.getInt("id");
                    String artistID = rs.getString("artist_id");
                    String name = rs.getString("name");
                    String alias = rs.getString("alias");
                    return new Artist(id, artistID, name, alias);
                }
            }
        }
        return null;
    }

    public Artist createArtist(Artist artist) throws Exception {
        // SQL command
        String sql = "INSERT INTO dbo.artists (artist_id, name, alias) VALUES (?,?,?);";

        //
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            // Bind parameters
            stmt.setString(1, artist.getArtistID());
            stmt.setString(2, artist.getName());
            stmt.setString(3, artist.getAlias());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            if (rs.next()) {
                id = rs.getInt(1);
            }

            // Create song object and send up the layers
            Artist createdArtist = new Artist(id, artist.getArtistID(), artist.getName(), artist.getAlias());

            return createdArtist;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not create song", ex);
        }
    }

}
