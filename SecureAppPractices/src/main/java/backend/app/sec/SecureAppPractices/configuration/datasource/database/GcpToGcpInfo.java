package backend.app.sec.SecureAppPractices.configuration.datasource.database;

public class GcpToGcpInfo {
    public static final String jdbcUrl = "jdbc:postgresql:///" + LocalhostToGcpInfo.gcpDatabaseName + "?useSSL=false";
    public static final String socketFactory = "com.google.cloud.sql.postgres.SocketFactory";
    public static final String instanceConnectionName = "braided-tracker-259922:europe-west1:gcp-remote-postgres";
    public static final String useSSL = "false";
}
