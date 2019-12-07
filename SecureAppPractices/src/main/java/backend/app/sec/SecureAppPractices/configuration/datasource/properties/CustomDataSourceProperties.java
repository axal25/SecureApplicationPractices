package backend.app.sec.SecureAppPractices.configuration.datasource.properties;

import backend.app.sec.SecureAppPractices.configuration.datasource.database.GcpToGcpInfo;
import backend.app.sec.SecureAppPractices.configuration.datasource.database.GcpToGcpPatterns;
import backend.app.sec.SecureAppPractices.configuration.datasource.database.LocalhostInfo;
import backend.app.sec.SecureAppPractices.configuration.datasource.database.LocalhostToGcpInfo;
import backend.app.sec.SecureAppPractices.configuration.datasource.users.GCP_PostgresUserInfo;
import backend.app.sec.SecureAppPractices.configuration.datasource.users.LimitedSafeUserInfo;
import backend.app.sec.SecureAppPractices.configuration.datasource.users.LimitedUnSafeUserInfo;
import backend.app.sec.SecureAppPractices.configuration.datasource.users.Localhost_PostgresUserInfo;
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

    // No public getters (or setters)
    private static final boolean isDebugging = false;
    private static final boolean isLesserDebugging = isDebugging || false;
    private static final String databaseLocation = CustomDataSourcePatterns.DatabaseLocation.localhost;
//    private static final String databaseLocation = CustomDataSourcePatterns.DatabaseLocation.gcp;

    private boolean getIsDebugging() { return isDebugging; }
    private String getDatabaseLocation() { return databaseLocation; }

    // No public setters or getters
    private String appDeploymentLocation = null;

    // No public setters or getters
    private void setAppDeploymentLocation(String appDeploymentLocation) { this.appDeploymentLocation = appDeploymentLocation; }
    private String getAppDeploymentLocation() { return appDeploymentLocation; }

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

    public CustomDataSourceProperties(Environment env, String userType) throws Exception {
        final String functionName = "CustomDataSourceProperties(Environment env, String userType)";
        if(env == null) throw new Exception(className + " >> " + functionName + ": env == " + env);
        this.setAppDeploymentLocation(env);
        this.setProperties(userType, this.appDeploymentLocation);
        if( isLesserDebugging ) printSetProperties();
    }

    private void setProperties(String userType, String appDeploymentLocation) throws Exception {
        final String functionName = "setProperties(String userType, String appDeploymentLocation)";
        if( isAppDeploymentLocationLocalhost(appDeploymentLocation) ) {
            setPropertiesFor_localhost_AppDeploymentLocation(userType);
        }
        else {
            if( isAppDeploymentLocationGCP(appDeploymentLocation) ) {
                setPropertiesFor_gcp_AppDeploymentLocation(userType);
            }
            else throw new Exception(className + " >> " + functionName + ": Bad appDeploymentLocation parameter: " + CustomDataSourceProperties.databaseLocation);
        }
    }

    private void setPropertiesFor_localhost_AppDeploymentLocation(String userType) throws Exception {
        final String functionName = "setPropertiesFor_localhost_AppDeploymentLocation(String userType)";
        if( isDatabaseLocationGCP() ) setPropertiesFor_localhost_AppDeploymentLocation_gcp_DatabaseLocation(userType);
        else if( isDatabaseLocationLocalhost() ) setPropertiesFor_localhost_AppDeploymentLocation_localhost_DatabaseLocation(userType);
        else throw new BadDatabaseLocation(className, functionName, CustomDataSourceProperties.databaseLocation);
    }

    private void setPropertiesFor_localhost_AppDeploymentLocation_gcp_DatabaseLocation(String userType) throws Exception {
        setJdbcUrl(LocalhostToGcpInfo.jdbcUrl);
        set_Username_Password(userType);
    }

    private void setPropertiesFor_localhost_AppDeploymentLocation_localhost_DatabaseLocation(String userType) throws Exception {
        setJdbcUrl(LocalhostInfo.jdbcUrl);
        set_Username_Password(userType);
    }

    private void setPropertiesFor_gcp_AppDeploymentLocation(String userType) throws Exception {
        final String functionName = "setPropertiesFor_gcp_AppDeploymentLocation(String userType)";
        if( isDatabaseLocationGCP() ) setPropertiesFor_gcp_AppDeploymentLocation_gcp_DatabaseLocation(userType);
        else if( isDatabaseLocationLocalhost() ) {
                throw new Exception("No configuration for this.appDeploymentLocation: \"" + this.appDeploymentLocation +
                        "\" and CustomDataSourceProperties.databaseLocation: \"" + CustomDataSourceProperties.databaseLocation + "\"");
        }
        else throw new BadDatabaseLocation(className, functionName, CustomDataSourceProperties.databaseLocation);
    }

    private void setPropertiesFor_gcp_AppDeploymentLocation_gcp_DatabaseLocation(String userType) throws Exception {
        final String functionName = "setPropertiesFor_gcp_AppDeploymentLocation_gcp_DatabaseLocation(String userType)";
        System.out.println("{ this.appDeploymentLocation: " + this.appDeploymentLocation + ", this.databaseLocation: " + this.databaseLocation + " }");
        setJdbcUrl(GcpToGcpInfo.jdbcUrl);
        setSocketFactory(GcpToGcpInfo.socketFactory);
        setCloudSqlInstance(GcpToGcpInfo.instanceConnectionName);
        setUseSSL(GcpToGcpInfo.useSSL);
        set_Username_Password(userType);
    }

    private void setPostgres_Username_Password(String databaseLocation) throws BadDatabaseLocation {
        final String functionName = "setPostgres_Username_Password(String databaseLocation)";
        if( isDatabaseLocationGCP() ) set_gcp_Postgres_Username_Password();
        else if( isDatabaseLocationLocalhost() ) set_localhost_Postgres_Username_Password();
        else throw new BadDatabaseLocation(className, functionName, CustomDataSourceProperties.databaseLocation);
    }

    private void set_Username_Password(String userType) throws Exception {
        if( isUserTypePostgres(userType) ) setPostgres_Username_Password(CustomDataSourceProperties.databaseLocation);
        else if( isUserTypeLimitedSafe(userType) ) setLimitedSafeUser_Username_Password();
        else if( isUserTypeLimitedUnSafe(userType) ) setLimitedUnSafeUser_Username_Password();
        else throw new Exception("Bad userType parameter: " + userType);
    }

    private void set_localhost_Postgres_Username_Password() {
        setUsername(Localhost_PostgresUserInfo.username);
        setPassword(Localhost_PostgresUserInfo.password);
    }

    private void set_gcp_Postgres_Username_Password() {
        setUsername(GCP_PostgresUserInfo.username);
        setPassword(GCP_PostgresUserInfo.password);
    }

    private void setLimitedSafeUser_Username_Password() {
        setUsername(LimitedSafeUserInfo.username);
        setPassword(LimitedSafeUserInfo.password);
    }

    private void setLimitedUnSafeUser_Username_Password() {
        setUsername(LimitedUnSafeUserInfo.username);
        setPassword(LimitedUnSafeUserInfo.password);
    }

    private boolean isUserTypePostgres(String userType) {
        return userType.compareTo(CustomDataSourcePatterns.UserType.postgres) == 0;
    }

    private boolean isUserTypeLimitedSafe(String userType) {
        return userType.compareTo(CustomDataSourcePatterns.UserType.limitedSafe) == 0;
    }

    private boolean isUserTypeLimitedUnSafe(String userType) {
        return userType.compareTo(CustomDataSourcePatterns.UserType.limitedUnSafe) == 0;
    }

    private boolean isAppDeploymentLocationLocalhost(String appDeploymentLocation) {
        return appDeploymentLocation.compareTo(CustomDataSourcePatterns.AppDeploymentLocation.localhost) == 0;
    }

    private boolean isAppDeploymentLocationGCP(String appDeploymentLocation) {
        return appDeploymentLocation.compareTo(CustomDataSourcePatterns.AppDeploymentLocation.gcp) == 0;
    }

    private boolean isDatabaseLocationLocalhost() {
        return CustomDataSourceProperties.databaseLocation.compareTo(CustomDataSourcePatterns.DatabaseLocation.localhost) == 0;
    }

    private boolean isDatabaseLocationGCP() {
        return CustomDataSourceProperties.databaseLocation.compareTo(CustomDataSourcePatterns.DatabaseLocation.gcp) == 0;
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

        boolean GAE_ENV_IsCurrentSameAsGcpPattern = getFlag( gcpCurrent_GAE_ENV, GcpToGcpPatterns.gcpPattern_GAE_ENV );
        boolean GAE_RUNTIME_IsCurrentSameAsGcpPattern = getFlag( gcpCurrent_GAE_RUNTIME, GcpToGcpPatterns.gcpPattern_GAE_RUNTIME );
        boolean GAE_SERVICE_IsCurrentSameAsGcpPattern = getFlag( gcpCurrent_GAE_SERIVCE, GcpToGcpPatterns.gcpPattern_GAE_SERIVCE );

        setAppDeploymentLocation(
                GAE_ENV_IsCurrentSameAsGcpPattern,
                GAE_RUNTIME_IsCurrentSameAsGcpPattern,
                GAE_SERVICE_IsCurrentSameAsGcpPattern
        );

        debugFeed(
                env,
                GAE_ENV_IsCurrentSameAsGcpPattern,
                GAE_RUNTIME_IsCurrentSameAsGcpPattern,
                GAE_SERVICE_IsCurrentSameAsGcpPattern,
                gcpCurrent_GAE_ENV,
                gcpCurrent_GAE_RUNTIME,
                gcpCurrent_GAE_SERIVCE
        );
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
                System.out.println("CustomDataSourceProperties.gcpUserPattern = \t" + GcpToGcpPatterns.gcpPattern_GAE_ENV);
                System.out.println("vs.");
                System.out.println("gcpCurrent_GAE_ENV = \t\t\t\t\t\t\t" + gcpCurrent_GAE_ENV);
            }
            if (!GAE_RUNTIME_IsCurrentSameAsGcpPattern) {
                System.out.println("\nGAE_RUNTIME_IsCurrentSameAsGcpPattern = " + GAE_RUNTIME_IsCurrentSameAsGcpPattern);
                System.out.println("CustomDataSourceProperties.gcpGoPathPattern = \t" + GcpToGcpPatterns.gcpPattern_GAE_RUNTIME);
                System.out.println("vs.");
                System.out.println("gcpCurrent_GAE_RUNTIME = \t\t\t\t\t\t" + gcpCurrent_GAE_RUNTIME);
            }
            if (!GAE_SERVICE_IsCurrentSameAsGcpPattern) {
                System.out.println("\nGAE_SERVICE_IsCurrentSameAsGcpPattern = " + GAE_SERVICE_IsCurrentSameAsGcpPattern);
                System.out.println("CustomDataSourceProperties.gcpHomePattern = \t" + GcpToGcpPatterns.gcpPattern_GAE_SERIVCE);
                System.out.println("vs.");
                System.out.println("gcpCurrent_GAE_SERIVCE = \t\t\t\t\t\t" + gcpCurrent_GAE_SERIVCE);
            }
            if( isAppDeploymentLocation(GAE_ENV_IsCurrentSameAsGcpPattern, GAE_RUNTIME_IsCurrentSameAsGcpPattern, GAE_SERVICE_IsCurrentSameAsGcpPattern) ) {
                this.appDeploymentLocation = "gcp";
                System.out.println("\nAll flags are TRUE. Application location = " + this.appDeploymentLocation + ". this.appDeploymentLocation = " + this.appDeploymentLocation);
            }
            else {
                this.appDeploymentLocation = "localhost";
                System.out.println("\nAt least one of the flags is FALSE. Application location = " + this.appDeploymentLocation + ". this.appDeploymentLocation = " + this.appDeploymentLocation);
            }
        }
    }

    private void setAppDeploymentLocation(boolean GAE_ENV_IsCurrentSameAsGcpPattern, boolean GAE_RUNTIME_IsCurrentSameAsGcpPattern, boolean GAE_SERVICE_IsCurrentSameAsGcpPattern) {
        if(isAppDeploymentLocation(GAE_ENV_IsCurrentSameAsGcpPattern, GAE_RUNTIME_IsCurrentSameAsGcpPattern, GAE_SERVICE_IsCurrentSameAsGcpPattern))
            this.appDeploymentLocation = "gcp";
        else this.appDeploymentLocation = "localhost";
    }

    private boolean isAppDeploymentLocation(boolean GAE_ENV_IsCurrentSameAsGcpPattern, boolean GAE_RUNTIME_IsCurrentSameAsGcpPattern, boolean GAE_SERVICE_IsCurrentSameAsGcpPattern) {
        return GAE_ENV_IsCurrentSameAsGcpPattern && GAE_RUNTIME_IsCurrentSameAsGcpPattern && GAE_SERVICE_IsCurrentSameAsGcpPattern;
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

    private void printSetProperties() {
        System.out.println("CustomDataSourceProperties: { \n" +
                "\tthis.appDeploymentLocation: " + this.appDeploymentLocation + ", \n" +
                "\tthis.databaseLocation: " + this.databaseLocation + ", \n" +
                "\tthis.username: " + this.username + ", \n" +
                "\tthis.password: " + this.password + ", \n" +
                "\tthis.jdbcUrl: " + this.jdbcUrl + ", \n" +
                "\tthis.socketFactory: " + this.socketFactory + ", \n" +
                "\tthis.cloudSqlInstance: " + this.cloudSqlInstance + ", \n" +
                "\tthis.useSSL: " + this.useSSL + ", \n" +
                "}");
    }
}
