package backend.app.sec.SecureAppPractices.configuration.datasource.database;

public class LocalhostInfo {
    public static final String databaseUrl = "localhost";
    public static final String databaseName = "postgres";
    public static final String jdbcUrl = "jdbc:postgresql://" + databaseUrl + ":5432/" + databaseName;
}
