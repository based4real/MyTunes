package mytunes.BLL.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigSystem {

    private static final String PROP_FILE = "config/config.settings";

    private static Properties getConfigProperties() throws IOException {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream((PROP_FILE)));
        return databaseProperties;
    }

    public static String getAlbumDefault() throws IOException {
        return getConfigProperties().getProperty("album.default_img");
    }

    public static String getArtistDefault() throws IOException {
        return getConfigProperties().getProperty("artist.default_img");
    }

    public static String getSongDefault() throws IOException {
        return getConfigProperties().getProperty("song.default_img");
    }

    public static String getPlaylistDefault() throws IOException {
        return getConfigProperties().getProperty("playlist.default_img");
    }

    public static String getDatabaseServer() throws IOException {
        return getConfigProperties().getProperty("db.server");
    }

    public static String getDatabaseName() throws IOException {
        return getConfigProperties().getProperty("db.database");
    }

    public static String getDatabaseUser() throws IOException {
        return getConfigProperties().getProperty("db.user");
    }

    public static String getDatabasePassword() throws IOException {
        return getConfigProperties().getProperty("db.password");
    }

    public static int getDatabasePort() throws IOException {
        String portStr = getConfigProperties().getProperty("db.port");
        int portInt;
        try {
            portInt = Integer.parseInt(portStr);
        } catch (Exception e) {
            portInt = 1433;
            System.out.println("Port cannot be converted to int, using default: 1433");
        }

        return portInt;
    }

    public static boolean getDatabaseTrustedCert() throws IOException {
        String trustedStr = getConfigProperties().getProperty("db.trusted_cert");
        boolean trustedBoolean;
        try {
            trustedBoolean = Boolean.parseBoolean(trustedStr);
        } catch (NumberFormatException e) {
            trustedBoolean = true;
            System.out.println("Cert cannot be converted to boolean, using default: true");
        }

        return trustedBoolean;
    }

}
