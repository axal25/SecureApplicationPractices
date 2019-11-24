package backend.app.sec.SecureAppPractices.datasource;

import lombok.Getter;
import org.springframework.core.env.Environment;

public class CustomDataSourceProperties {

    private final boolean isDebugging = true;

    private final String localhostDatabaseUrl = "localhost";
    private final String localhostDatabaseName = "postgres";
    private final String localhostJdbcUrl = "jdbc:postgresql://" + localhostDatabaseUrl + ":5432/" + localhostDatabaseName;
    private final String localhostUsername = "postgres";
    private final String localhostPassword = "password";

    private final String gcpIpAddress = "34.76.176.166";
    private final String gcpInstanceConnectionName = "braided-tracker-259922:europe-west1:gcp-remote-postgres";
    private final String gcpDatabaseName = "";
    private final String gcpJdbcUrl = "jdbc:postgresql://" + gcpIpAddress + "/" + gcpDatabaseName + "?useSSL=false";
    private final String gcpUsername = "postgres";
    private final String gcpPassword = "ueaE3nlsxwBPobznEPNklHLz0cJ51Bc36tFzzswEkEEhlIHLI1yahmHBCsAzCvdp";

    private final String gcpShellUsername = "emevig";
    private final String gcpUserPattern = this.gcpShellUsername;
    private final String gcpGoPathPattern = "/home/" + gcpShellUsername + "/gopath:/google/gopath";
    private final String gcpPwdPattern = "/home/" + gcpShellUsername;
    private final String gcpHomePattern = "/home/" + gcpShellUsername;

    private final String overriddenTarget = "localhost";
//    private final String overriddenTarget = "gcp";
//    private final String overriddenTarget = null;

    @Getter
    private final String jdbcUrl;
    @Getter
    private final String username;
    @Getter
    private final String password;
    @Getter
    private final String driverClassName;

    public CustomDataSourceProperties(Environment env) {
        this.driverClassName = "org.postgresql.Driver";

        String gcpUser = null;
        String gcpGoPath = null;
        String gcpPwd = null;
        String gcpHome = null;

        if( env != null ) {
            gcpUser = env.getProperty("USER");
            gcpGoPath = env.getProperty("GOPATH");
            gcpPwd = env.getProperty("PWD");
            gcpHome = env.getProperty("HOME");
        }

        boolean userCheckFlag = true;
        boolean goPathCheckFlag = true;
        boolean pwdCheckFlag = true;
        boolean homeCheckFlag = true;

        if(gcpUser != null && gcpUser.compareTo(this.gcpUserPattern) == 0) userCheckFlag = false;
        if(gcpGoPath != null && gcpGoPath.compareTo(this.gcpGoPathPattern) == 0) goPathCheckFlag = false;
        if(gcpPwd != null && gcpPwd.compareTo(this.gcpPwdPattern) == 0) pwdCheckFlag = false;
        if(gcpHome != null && gcpHome.compareTo(this.gcpHomePattern) == 0) homeCheckFlag = false;

        debugFeed(env, userCheckFlag, goPathCheckFlag, pwdCheckFlag, homeCheckFlag,
                gcpUser, gcpGoPath, gcpPwd, gcpHome);

        if( this.overriddenTarget != null ) {
            if(this.overriddenTarget.compareTo("gcp") == 0) {
                userCheckFlag = false;
                goPathCheckFlag = false;
                pwdCheckFlag = false;
                homeCheckFlag = false;
            }
            if(this.overriddenTarget.compareTo("localhost") == 0) {
                userCheckFlag = true;
                goPathCheckFlag = true;
                pwdCheckFlag = true;
                homeCheckFlag = true;
            }
        }
        if( userCheckFlag ||
                goPathCheckFlag ||
                pwdCheckFlag ||
                homeCheckFlag ) {
            if(this.overriddenTarget == null) System.out.println("One of the flags raised. Database from localhost.");
            else System.out.println("Overridden target to LOCALHOST. this.overriddenTarget = " + this.overriddenTarget);
            this.jdbcUrl = this.localhostJdbcUrl;
            this.username = this.localhostUsername;
            this.password = this.localhostPassword;
        }
        else {
            if(this.overriddenTarget == null) System.out.println("Not one of the flags raised. Database from heroku.");
            else System.out.println("Overridden target to LOCALHOST. this.overriddenTarget = " + this.overriddenTarget);
            this.jdbcUrl = this.gcpJdbcUrl;
            this.username = this.gcpUsername;
            this.password = this.gcpPassword;
        }
    }

    private void debugFeed(Environment env, boolean userCheckFlag, boolean goPathCheckFlag, boolean pwdCheckFlag, boolean homeCheckFlag,
                          String gcpUser, String gcpGoPath, String gcpPwd, String gcpHome) {
        if( isDebugging ) {
            System.out.println("\nEnvironment env = " + env);
            if (userCheckFlag) {
                System.out.println("\nthis.gcpUserPattern = \t" + this.gcpUserPattern);
                System.out.println("vs.");
                System.out.println("gcpUser = \t\t" + gcpUser);
            }
            if (goPathCheckFlag) {
                System.out.println("\nthis.gcpGoPathPattern = \t" + this.gcpGoPathPattern);
                System.out.println("vs.");
                System.out.println("gcpGoPath = \t\t" + gcpGoPath);
            }
            if (pwdCheckFlag) {
                System.out.println("\nthis.gcpPwdPattern = \t" + this.gcpPwdPattern);
                System.out.println("vs.");
                System.out.println("gcpPwd = \t\t" + gcpPwd);
            }
            if (homeCheckFlag) {
                System.out.println("\nthis.gcpHomePattern = \t" + this.gcpHomePattern);
                System.out.println("vs.");
                System.out.println("gcpHome = \t\t" + gcpHome);
            }
        }
    }
}
