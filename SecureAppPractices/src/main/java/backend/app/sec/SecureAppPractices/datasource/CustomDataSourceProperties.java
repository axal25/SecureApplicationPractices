package backend.app.sec.SecureAppPractices.datasource;

import lombok.Getter;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CustomDataSourceProperties {
    public static final String className = CustomDataSourceProperties.class.getSimpleName();

    private static final boolean isDebugging = true;

    private static final String localhostDatabaseUrl = "localhost";
    private static final String localhostDatabaseName = "postgres";
    private static final String localhostJdbcUrl = "jdbc:postgresql://" + localhostDatabaseUrl + ":5432/" + localhostDatabaseName;
    private static final String localhostUsername = "postgres";
    private static final String localhostPassword = "password";

    private static final String gcpIpAddress = "34.76.176.166";
    private static final String gcpDatabaseName = "postgres";
    private static final String localhostToGcpJdbcUrl = "jdbc:postgresql://" + gcpIpAddress + "/" + gcpDatabaseName + "?useSSL=false";
    private static final String gcpUsername = "postgres";
    private static final String gcpPassword = "jacekoles_lukaszstawowy_studioprojektowe_2019";

    private static final String gcpToGcpJdbcUrl = "jdbc:postgresql:///" + gcpDatabaseName + "?useSSL=false";
    private static final String gcpToGcpSocketFactory = "com.google.cloud.sql.postgres.SocketFactory";
    private static final String gcpToGcpInstanceConnectionName = "braided-tracker-259922:europe-west1:gcp-remote-postgres";
    private static final String gcpToGcpUseSSL = "false";

    private static final String gcpShellUsername = null; // "emevig";
    private static final String gcpUserPattern = CustomDataSourceProperties.gcpShellUsername;
    private static final String gcpGoPathPattern = null; // "/home/" + gcpShellUsername + "/gopath:/google/gopath";
    private static final String gcpHomePattern = "/root";// "/home/" + gcpShellUsername;

//    private static final String target = "localhost";
    private static final String databaseLocation = "gcp";
    private String appDeploymentLocation = null;

    @Getter
    private static final String driverClassName = "org.postgresql.Driver";
    // No public setters
    @Getter
    private String jdbcUrl;
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    private String socketFactory = null;
    @Getter
    private String cloudSqlInstance = null;
    @Getter
    private String useSSL = null;

    // No public setters
    private void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
    private void setUsername(String username) {
        this.username = username;
    }
    private void setPassword(String password) {
        this.password = password;
    }
    private void setSocketFactory(String socketFactory) {
        this.socketFactory = socketFactory;
    }
    private void setCloudSqlInstance(String cloudSqlInstance) {
        this.cloudSqlInstance = cloudSqlInstance;
    }
    private void setUseSSL(String useSSL) {
        this.useSSL = useSSL;
    }

    public CustomDataSourceProperties(Environment env) throws Exception {
        final String functionName = "CustomDataSourceProperties(Environment env)";
        this.setAppDeploymentLocation(env);
        this.setJdbcUrlAndUserNameAndPassword();
    }

    private void setJdbcUrlAndUserNameAndPassword() throws Exception {
        final String functionName = "CustomDataSourceProperties(Environment env)";
        if(this.appDeploymentLocation.compareTo("localhost") == 0) {
            if(CustomDataSourceProperties.databaseLocation.compareTo("gcp") == 0) {
                System.out.println("{ this.appDeploymentLocation: " + this.appDeploymentLocation + ", this.databaseLocation: " + this.databaseLocation + " }");
                setJdbcUrl(CustomDataSourceProperties.localhostToGcpJdbcUrl);
                setUsername(CustomDataSourceProperties.gcpUsername);
                setPassword(CustomDataSourceProperties.gcpPassword);
            }
            else {
                if(CustomDataSourceProperties.databaseLocation.compareTo("localhost") == 0) {
                    System.out.println("{ this.appDeploymentLocation: " + this.appDeploymentLocation + ", this.databaseLocation: " + this.databaseLocation + " }");
                    setJdbcUrl(CustomDataSourceProperties.localhostJdbcUrl);
                    setUsername(CustomDataSourceProperties.localhostUsername);
                    setPassword(CustomDataSourceProperties.localhostPassword);
                }
                else throw new BadDatabaseLocation(className, functionName, CustomDataSourceProperties.databaseLocation);
            }
        }
        else {
            if(this.appDeploymentLocation.compareTo("gcp") == 0) {
                if(CustomDataSourceProperties.databaseLocation.compareTo("gcp") == 0) {
                    System.out.println("{ this.appDeploymentLocation: " + this.appDeploymentLocation + ", this.databaseLocation: " + this.databaseLocation + " }");
                    setJdbcUrl(CustomDataSourceProperties.gcpToGcpJdbcUrl);
                    setUsername(CustomDataSourceProperties.gcpUsername);
                    setPassword(CustomDataSourceProperties.gcpPassword);
                    setSocketFactory(CustomDataSourceProperties.gcpToGcpSocketFactory);
                    setCloudSqlInstance(CustomDataSourceProperties.gcpToGcpInstanceConnectionName);
                    setUseSSL(CustomDataSourceProperties.gcpToGcpUseSSL);
                }
                else {
                    if(CustomDataSourceProperties.databaseLocation.compareTo("localhost") == 0) {
                        throw new Exception("No configuration for this.appDeploymentLocation: \"" + this.appDeploymentLocation +
                                "\" and CustomDataSourceProperties.databaseLocation: \"" + CustomDataSourceProperties.databaseLocation + "\"");
                    }
                    else throw new BadDatabaseLocation(className, functionName, CustomDataSourceProperties.databaseLocation);
                }
            }
            else throw new Exception();
        }
    }

    private void setAppDeploymentLocation(Environment env) {
        String gcpUser = null;
        String gcpGoPath = null;
        String gcpHome = null;

        if( env != null ) {
            gcpUser = env.getProperty("USER");
            gcpGoPath = env.getProperty("GOPATH");
            gcpHome = env.getProperty("HOME");
        }

        boolean userIsCurrentSameAsGcpPattern = getFlag( gcpUser, CustomDataSourceProperties.gcpUserPattern );
        boolean goPathIsCurrentSameAsGcpPattern = getFlag( gcpGoPath, CustomDataSourceProperties.gcpGoPathPattern );
        boolean homeIsCurrentSameAsGcpPattern = getFlag( gcpHome, CustomDataSourceProperties.gcpHomePattern );

        debugFeed(env, userIsCurrentSameAsGcpPattern, goPathIsCurrentSameAsGcpPattern, homeIsCurrentSameAsGcpPattern,
                gcpUser, gcpGoPath, gcpHome);
    }

    private boolean getFlag(String gcpProperty, String gcpPattern) {
        boolean flag = true;

        if(gcpProperty != null && gcpPattern != null && gcpProperty.compareTo(gcpPattern) != 0) flag = false;
        if((gcpProperty == null || gcpPattern == null) && (gcpProperty != gcpPattern)) flag = false;

        return flag;
    }

    private void debugFeed(Environment env,
                           boolean userIsCurrentSameAsGcpPattern,
                           boolean goPathIsCurrentSameAsGcpPattern,
                           boolean homeIsCurrentSameAsGcpPattern,
                          String gcpUser, String gcpGoPath, String gcpHome) {
        if( isDebugging ) {
            System.out.println("\nEnvironment env = " + env);
            printAllEnvironmentProperties(env);
            if (!userIsCurrentSameAsGcpPattern) {
                System.out.println("userIsCurrentSameAsGcpPattern = " + userIsCurrentSameAsGcpPattern);
                System.out.println("\nCustomDataSourceProperties.gcpUserPattern = \t" + CustomDataSourceProperties.gcpUserPattern);
                System.out.println("vs.");
                System.out.println("gcpUser = \t\t" + gcpUser);
            }
            if (!goPathIsCurrentSameAsGcpPattern) {
                System.out.println("goPathIsCurrentSameAsGcpPattern = " + goPathIsCurrentSameAsGcpPattern);
                System.out.println("\nCustomDataSourceProperties.gcpGoPathPattern = \t" + CustomDataSourceProperties.gcpGoPathPattern);
                System.out.println("vs.");
                System.out.println("gcpGoPath = \t\t" + gcpGoPath);
            }
            if (!homeIsCurrentSameAsGcpPattern) {
                System.out.println("homeIsCurrentSameAsGcpPattern = " + homeIsCurrentSameAsGcpPattern);
                System.out.println("\nCustomDataSourceProperties.gcpHomePattern = \t" + CustomDataSourceProperties.gcpHomePattern);
                System.out.println("vs.");
                System.out.println("gcpHome = \t\t" + gcpHome);
            }
            if( userIsCurrentSameAsGcpPattern && goPathIsCurrentSameAsGcpPattern && homeIsCurrentSameAsGcpPattern ) {
                this.appDeploymentLocation = "gcp";
                System.out.println("One of the flags raised. Application location = " + this.appDeploymentLocation + ". this.appDeploymentLocation = " + this.appDeploymentLocation);
            }
            else {
                this.appDeploymentLocation = "localhost";
                System.out.println("One of the flags raised. Application location = " + this.appDeploymentLocation + ". this.appDeploymentLocation = " + this.appDeploymentLocation);
            }
        }
    }

    private Properties getAllEnvironmentProperties(Environment env) {
        Properties props = new Properties();
        MutablePropertySources propsSources = ((AbstractEnvironment) env).getPropertySources();
        StreamSupport.stream(propsSources.spliterator(), false)
                .filter(propsSource -> propsSource instanceof EnumerablePropertySource)
                .map(propertySource -> ((EnumerablePropertySource) propertySource).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> props.setProperty(propName, env.getProperty(propName)));
        return props;
    }

    private void printAllEnvironmentProperties(Environment env) {
        Properties props = getAllEnvironmentProperties( env );
        System.out.println("{ Environment.Properties: [");
        Enumeration propsNames = props.propertyNames();
        while(propsNames.hasMoreElements()) {
            String propName = (String) propsNames.nextElement();
            String propValue = (String) props.get(propName);
            System.out.print("\t{ \"" + propName + "\": " + "\"" + propValue + "\" }");
            if(propsNames.hasMoreElements()) System.out.println(", ");
            else System.out.println();
        }
        System.out.println("] }");
    }
}
