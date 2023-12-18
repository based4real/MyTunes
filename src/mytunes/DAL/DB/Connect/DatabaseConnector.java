package mytunes.DAL.DB.Connect;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import mytunes.BLL.util.ConfigSystem;

import java.io.IOException;
import java.sql.Connection;

public class DatabaseConnector {
    private SQLServerDataSource dataSource;

    public DatabaseConnector() throws IOException {
        dataSource = new SQLServerDataSource();
        dataSource.setServerName(ConfigSystem.getDatabaseServer());
        dataSource.setDatabaseName(ConfigSystem.getDatabaseName());
        dataSource.setUser(ConfigSystem.getDatabaseUser());
        dataSource.setPassword(ConfigSystem.getDatabasePassword());
        dataSource.setPortNumber(ConfigSystem.getDatabasePort());
        dataSource.setTrustServerCertificate(ConfigSystem.getDatabaseTrustedCert());
    }
    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }
}
