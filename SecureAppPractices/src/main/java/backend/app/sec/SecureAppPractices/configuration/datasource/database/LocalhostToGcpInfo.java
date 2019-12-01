package backend.app.sec.SecureAppPractices.configuration.datasource.database;

public class LocalhostToGcpInfo {
    public static final String hostAddress = "34.76.176.166";
    public static final String databaseName = "postgres";
    public static final String jdbcUrl = "jdbc:postgresql://" + hostAddress + "/" + databaseName + "?useSSL=false";
}
