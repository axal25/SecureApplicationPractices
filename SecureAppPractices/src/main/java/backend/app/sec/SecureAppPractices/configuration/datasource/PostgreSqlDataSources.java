package backend.app.sec.SecureAppPractices.configuration.datasource;

import backend.app.sec.SecureAppPractices.configuration.datasource.properties.CustomDataSourcePatterns;
import backend.app.sec.SecureAppPractices.configuration.datasource.properties.CustomDataSourceProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

@Configuration
public class PostgreSqlDataSources {
    public static final String className = PostgreSqlDataSources.class.getSimpleName();
    public static final boolean isDebugging = false;
    private final Environment env;
    private final CustomDataSourceProperties postgresUserCustomDataSourceProperties;
    private final CustomDataSourceProperties limitedSafeUserCustomDataSourceProperties;
    private final CustomDataSourceProperties limitedUnSafeUserCustomDataSourceProperties;

    @Autowired
    PostgreSqlDataSources(Environment env ) {
        this.env = env;
        if( isDebugging ) System.out.println("this.postgresUserCustomDataSourceProperties");
        this.postgresUserCustomDataSourceProperties = tryInitCustomDataSourceProperties(CustomDataSourcePatterns.UserType.postgres);
        if( isDebugging ) System.out.println("this.limitedSafeUserCustomDataSourceProperties");
        this.limitedSafeUserCustomDataSourceProperties = tryInitCustomDataSourceProperties(CustomDataSourcePatterns.UserType.limitedSafe);
        if( isDebugging ) System.out.println("this.limitedUnSafeUserCustomDataSourceProperties");
        this.limitedUnSafeUserCustomDataSourceProperties = tryInitCustomDataSourceProperties(CustomDataSourcePatterns.UserType.limitedUnSafe);
    }

    private CustomDataSourceProperties tryInitCustomDataSourceProperties(String userType) {
        try {
            return new CustomDataSourceProperties( env, userType );
        } catch (Exception e) {
            System.err.println("\n\n\n[ERROR] Exception inside CustomDataSourceProperties customDataSourceProperties = new CustomDataSourceProperties( env )\n\n\n");
            System.err.println("\n\n\n[ERROR] " + e.toString() + "\n\n\n");
            e.printStackTrace();
            return null;
        }
    }

    @Bean("postgresUserHikariDataSource")
    public HikariDataSource postgresUserHikariDataSource() throws Exception {
        return hikariDataSource(this.postgresUserCustomDataSourceProperties, 1, "postgresUserCustomDataSourceProperties()");
    }

    @Bean("limitedSafeUserHikariDataSource")
    @DependsOn("flyway")
    public HikariDataSource limitedSafeUserHikariDataSource() throws Exception {
        return hikariDataSource(this.limitedSafeUserCustomDataSourceProperties, 25, "limitedSafeUserCustomDataSourceProperties()");
    }

    @Bean("limitedUnSafeUserHikariDataSource")
    @DependsOn("flyway")
    public HikariDataSource limitedUnSafeUserHikariDataSource() throws Exception {
        return hikariDataSource(this.limitedUnSafeUserCustomDataSourceProperties, 25, "limitedUnSafeUserHikariDataSource()");
    }

    private HikariDataSource hikariDataSource(
            CustomDataSourceProperties customDataSourceProperties,
            int maximumPoolSize,
            String callingFunctionName
    ) throws Exception {
        final String functionName = "hikariDataSource(CustomDataSourceProperties customDataSourceProperties, int maximumPoolSize, String callingFunctionName)";
        HikariDataSource hikariDataSource = getHikariDataSourceWithSetBasicProperties(customDataSourceProperties, maximumPoolSize);
        hikariDataSource = setAdditionalPropertiesIfNotNull(
                hikariDataSource,
                customDataSourceProperties,
                new StringBuilder()
                .append(callingFunctionName)
                .append(" >> ")
                .append(functionName)
                .toString()
        );
        System.out.println("PostgresSqlDataSource >> HikariDataSource " + callingFunctionName);
        return hikariDataSource;
    }

    private HikariDataSource getHikariDataSourceWithSetBasicProperties(CustomDataSourceProperties customDataSourceProperties, int maximumPoolSize) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl( customDataSourceProperties.getJdbcUrl() );
        hikariDataSource.setUsername( customDataSourceProperties.getUsername() );
        hikariDataSource.setPassword( customDataSourceProperties.getPassword() );
        hikariDataSource.setDriverClassName( customDataSourceProperties.getDriverClassName() );
        hikariDataSource.setMaximumPoolSize(maximumPoolSize);
        return hikariDataSource;
    }

    private HikariDataSource setAdditionalPropertiesIfNotNull(
            HikariDataSource hikariDataSource,
            CustomDataSourceProperties customDataSourceProperties,
            String callingFunctionName
    ) throws Exception {
        final String functionName = "setAdditionalPropertiesIfNotNull(HikariDataSource hikariDataSource, CustomDataSourceProperties customDataSourceProperties, String callingFunctionName)";
        if( hikariDataSource == null ) throw new Exception( new StringBuilder()
                .append(className).append(" >> ")
                .append(callingFunctionName).append(" >> ")
                .append(functionName).append(" >> ")
                .append("hikariDataSource == ").append(hikariDataSource.toString()).toString()
        );
        if( customDataSourceProperties.getSocketFactory() != null &&
                customDataSourceProperties.getCloudSqlInstance() != null &&
                customDataSourceProperties.getUseSSL() != null
        ) {
            hikariDataSource.addDataSourceProperty("socketFactory", customDataSourceProperties.getSocketFactory());
            hikariDataSource.addDataSourceProperty("cloudSqlInstance", customDataSourceProperties.getCloudSqlInstance());
            hikariDataSource.addDataSourceProperty("useSSL", customDataSourceProperties.getUseSSL());
        }
        return hikariDataSource;
    }
}
