package backend.app.sec.SecureAppPractices.datasource;

import lombok.Getter;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
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

    /*
    private static final String gcpShellUsername = null; // "emevig";
    private static final String gcpUserPattern = CustomDataSourceProperties.gcpShellUsername;
    private static final String gcpGoPathPattern = null; // "/home/" + gcpShellUsername + "/gopath:/google/gopath";
    private static final String gcpHomePattern = "/root";// "/home/" + gcpShellUsername;
     */

    private static final String gcpPattern_GAE_ENV = "standard";
    private static final String gcpPattern_GAE_SERIVCE = "default";
    private static final String gcpPattern_GAE_RUNTIME = "java11";
    /**
     { "GAE_ENV": "standard" },                                                                 // 5/5
     { "GAE_SERVICE": "default" },                                                              // 5/5
     { "GAE_RUNTIME": "java11" },                                                               // 5/5
     { "GAE_APPLICATION": "e~braided-tracker-259922" },                                         // 4/5
     { "GOOGLE_CLOUD_PROJECT": "braided-tracker-259922" },                                      // 4/5
     { "GAE_VERSION": "secure-app-practices-version-from-pom-xml" }, // dependant on to pom.xml -> not constant
     // use to be:
     { "GAE_VERSION": "springboot-helloworld" },                                                // ?/?
     { "GAE_DEPLOYMENT_ID": "422777054906127631" }, // NOT CONSTANT                             // ?/?
     { "PORT": "8081" }, // NOT CONSTANT                                                        // ?/?
     **/


    private static final String databaseLocation = "localhost";
//    private static final String databaseLocation = "gcp";
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
        String gcpCurrent_GAE_ENV = null;
        String gcpCurrent_GAE_RUNTIME = null;
        String gcpCurrent_GAE_SERIVCE = null;

        if( env != null ) {
            gcpCurrent_GAE_ENV = env.getProperty("GAE_ENV");
            gcpCurrent_GAE_RUNTIME = env.getProperty("GAE_RUNTIME");
            gcpCurrent_GAE_SERIVCE = env.getProperty("GAE_SERVICE");
        }

        boolean GAE_ENV_IsCurrentSameAsGcpPattern = getFlag( gcpCurrent_GAE_ENV, CustomDataSourceProperties.gcpPattern_GAE_ENV );
        boolean GAE_RUNTIME_IsCurrentSameAsGcpPattern = getFlag( gcpCurrent_GAE_RUNTIME, CustomDataSourceProperties.gcpPattern_GAE_RUNTIME );
        boolean GAE_SERVICE_IsCurrentSameAsGcpPattern = getFlag( gcpCurrent_GAE_SERIVCE, CustomDataSourceProperties.gcpPattern_GAE_SERIVCE );

        debugFeed(env, GAE_ENV_IsCurrentSameAsGcpPattern, GAE_RUNTIME_IsCurrentSameAsGcpPattern, GAE_SERVICE_IsCurrentSameAsGcpPattern,
                gcpCurrent_GAE_ENV, gcpCurrent_GAE_RUNTIME, gcpCurrent_GAE_SERIVCE);
    }

    private boolean getFlag(String gcpProperty, String gcpPattern) {
        boolean flag = true;

        if(gcpProperty != null && gcpPattern != null && gcpProperty.compareTo(gcpPattern) != 0) flag = false;
        if((gcpProperty == null || gcpPattern == null) && (gcpProperty != gcpPattern)) flag = false;

        return flag;
    }

    private void debugFeed(Environment env,
                           boolean GAE_ENV_IsCurrentSameAsGcpPattern,
                           boolean GAE_RUNTIME_IsCurrentSameAsGcpPattern,
                           boolean GAE_SERVICE_IsCurrentSameAsGcpPattern,
                          String gcpCurrent_GAE_ENV, String gcpCurrent_GAE_RUNTIME, String gcpCurrent_GAE_SERIVCE) {
        if( isDebugging ) {
            System.out.println("\nEnvironment env = " + env);
            printAllEnvironmentProperties(env);
            if (!GAE_ENV_IsCurrentSameAsGcpPattern) {
                System.out.println("\nGAE_ENV_IsCurrentSameAsGcpPattern = " + GAE_ENV_IsCurrentSameAsGcpPattern);
                System.out.println("CustomDataSourceProperties.gcpUserPattern = \t" + CustomDataSourceProperties.gcpPattern_GAE_ENV);
                System.out.println("vs.");
                System.out.println("gcpCurrent_GAE_ENV = \t\t\t\t\t\t\t" + gcpCurrent_GAE_ENV);
            }
            if (!GAE_RUNTIME_IsCurrentSameAsGcpPattern) {
                System.out.println("\nGAE_RUNTIME_IsCurrentSameAsGcpPattern = " + GAE_RUNTIME_IsCurrentSameAsGcpPattern);
                System.out.println("CustomDataSourceProperties.gcpGoPathPattern = \t" + CustomDataSourceProperties.gcpPattern_GAE_RUNTIME);
                System.out.println("vs.");
                System.out.println("gcpCurrent_GAE_RUNTIME = \t\t\t\t\t\t" + gcpCurrent_GAE_RUNTIME);
            }
            if (!GAE_SERVICE_IsCurrentSameAsGcpPattern) {
                System.out.println("\nGAE_SERVICE_IsCurrentSameAsGcpPattern = " + GAE_SERVICE_IsCurrentSameAsGcpPattern);
                System.out.println("CustomDataSourceProperties.gcpHomePattern = \t" + CustomDataSourceProperties.gcpPattern_GAE_SERIVCE);
                System.out.println("vs.");
                System.out.println("gcpCurrent_GAE_SERIVCE = \t\t\t\t\t\t" + gcpCurrent_GAE_SERIVCE);
            }
            if( GAE_ENV_IsCurrentSameAsGcpPattern && GAE_RUNTIME_IsCurrentSameAsGcpPattern && GAE_SERVICE_IsCurrentSameAsGcpPattern ) {
                this.appDeploymentLocation = "gcp";
                System.out.println("\nAll flags are TRUE. Application location = " + this.appDeploymentLocation + ". this.appDeploymentLocation = " + this.appDeploymentLocation);
            }
            else {
                this.appDeploymentLocation = "localhost";
                System.out.println("\nAt least one of the flags is FALSE. Application location = " + this.appDeploymentLocation + ". this.appDeploymentLocation = " + this.appDeploymentLocation);
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
