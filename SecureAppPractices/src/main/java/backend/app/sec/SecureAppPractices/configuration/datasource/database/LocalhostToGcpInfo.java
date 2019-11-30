package backend.app.sec.SecureAppPractices.configuration.datasource.database;

public class LocalhostToGcpInfo {
    public static final String gcpIpAddress = "34.76.176.166";
    public static final String gcpDatabaseName = "postgres";
    public static final String jdbcUrl = "jdbc:postgresql://" + gcpIpAddress + "/" + gcpDatabaseName + "?useSSL=false";
}
