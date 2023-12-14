package mytunes.DAL.DB.Objects;

import mytunes.BE.Genre;
import mytunes.DAL.DB.Connect.DatabaseConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {
    private DatabaseConnector databaseConnector;

    public GenreDAO() throws IOException {
        databaseConnector = new DatabaseConnector();
    }

    public List<Genre> getAllGenres() throws Exception {
        ArrayList<Genre> allGenres = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql = "SELECT * FROM Genres";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("name");

                Genre genre = new Genre(name);
                allGenres.add(genre);
            }
            return allGenres;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get genres from database", ex);
        }
    }
}
