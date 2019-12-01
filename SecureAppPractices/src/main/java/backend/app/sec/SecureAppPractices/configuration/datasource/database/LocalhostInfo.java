package backend.app.sec.SecureAppPractices.configuration.datasource.database;

public class LocalhostInfo {
    public static final String hostAddress = "localhost";
    public static final String databaseName = "postgres";
    public static final String jdbcUrl = "jdbc:postgresql://" + hostAddress + ":5432/" + databaseName;
}
