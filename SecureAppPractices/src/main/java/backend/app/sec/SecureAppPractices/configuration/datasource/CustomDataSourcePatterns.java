package backend.app.sec.SecureAppPractices.configuration.datasource;

public class CustomDataSourcePatterns {
    public static class AppDeploymentLocation {
        public static String localhost = "localhost";
        public static String gcp = "gcp";
    }
    public static class DatabaseLocation {
        public static String localhost = "localhost";
        public static String gcp = "gcp";
    }
    public static class UserType {
        public static String postgres = "localhost";
        public static String limitedSafe = "limitedsafe";
        public static String limitedUnSafe = "limitedunsafe";
    }
}
