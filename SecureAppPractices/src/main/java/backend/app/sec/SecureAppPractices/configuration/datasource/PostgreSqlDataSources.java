package backend.app.sec.SecureAppPractices.configuration.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

@Configuration
public class PostgreSqlDataSources {

    public static final boolean isDebugging = false;
    private final Environment env;
    private final CustomDataSourceProperties postgresUserCustomDataSourceProperties;
    private final CustomDataSourceProperties limitedSafeUserCustomDataSourceProperties;
    private final CustomDataSourceProperties limitedUnSafeUserCustomDataSourceProperties;
    private static final int maximumPoolSize = 5;

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
    public HikariDataSource postgresUserHikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl( postgresUserCustomDataSourceProperties.getJdbcUrl() );
        hikariDataSource.setUsername( postgresUserCustomDataSourceProperties.getUsername() );
        hikariDataSource.setPassword( postgresUserCustomDataSourceProperties.getPassword() );
        hikariDataSource.setDriverClassName( postgresUserCustomDataSourceProperties.getDriverClassName() );
        if( postgresUserCustomDataSourceProperties.getSocketFactory() != null &&
                postgresUserCustomDataSourceProperties.getCloudSqlInstance() != null &&
                postgresUserCustomDataSourceProperties.getUseSSL() != null ) {
            hikariDataSource.addDataSourceProperty("socketFactory", postgresUserCustomDataSourceProperties.getSocketFactory());
            hikariDataSource.addDataSourceProperty("cloudSqlInstance", postgresUserCustomDataSourceProperties.getCloudSqlInstance());
            hikariDataSource.addDataSourceProperty("useSSL", postgresUserCustomDataSourceProperties.getUseSSL());
        }
        hikariDataSource.setSchema( "safe" ); // POTRZEBNE?
        hikariDataSource.setMaximumPoolSize(PostgreSqlDataSources.maximumPoolSize);
        System.out.println("PostgresSqlDataSource >> HikariDataSource postgresUserHikariDataSource()");
        return hikariDataSource;
    }

    @Bean("limitedSafeUserHikariDataSource")
    @DependsOn("flyway")
    public HikariDataSource limitedSafeUserHikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl( limitedSafeUserCustomDataSourceProperties.getJdbcUrl() );
        hikariDataSource.setUsername( limitedSafeUserCustomDataSourceProperties.getUsername() );
        hikariDataSource.setPassword( limitedSafeUserCustomDataSourceProperties.getPassword() );
        hikariDataSource.setDriverClassName( limitedSafeUserCustomDataSourceProperties.getDriverClassName() );
        if( limitedSafeUserCustomDataSourceProperties.getSocketFactory() != null &&
                limitedSafeUserCustomDataSourceProperties.getCloudSqlInstance() != null &&
                limitedSafeUserCustomDataSourceProperties.getUseSSL() != null ) {
            hikariDataSource.addDataSourceProperty("socketFactory", limitedSafeUserCustomDataSourceProperties.getSocketFactory());
            hikariDataSource.addDataSourceProperty("cloudSqlInstance", limitedSafeUserCustomDataSourceProperties.getCloudSqlInstance());
            hikariDataSource.addDataSourceProperty("useSSL", limitedSafeUserCustomDataSourceProperties.getUseSSL());
        }
        hikariDataSource.setSchema( "safe" ); // POTRZEBNE?
        hikariDataSource.setMaximumPoolSize(PostgreSqlDataSources.maximumPoolSize);
        System.out.println("PostgresSqlDataSource >> HikariDataSource limitedSafeUserHikariDataSource()");
        return hikariDataSource;
    }

    @Bean("limitedUnSafeUserHikariDataSource")
    @DependsOn("flyway")
    public HikariDataSource limitedUnSafeUserHikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl( limitedUnSafeUserCustomDataSourceProperties.getJdbcUrl() );
        hikariDataSource.setUsername( limitedUnSafeUserCustomDataSourceProperties.getUsername() );
        hikariDataSource.setPassword( limitedUnSafeUserCustomDataSourceProperties.getPassword() );
        hikariDataSource.setDriverClassName( limitedUnSafeUserCustomDataSourceProperties.getDriverClassName() );
        if( limitedUnSafeUserCustomDataSourceProperties.getSocketFactory() != null &&
                limitedUnSafeUserCustomDataSourceProperties.getCloudSqlInstance() != null &&
                limitedUnSafeUserCustomDataSourceProperties.getUseSSL() != null ) {
            hikariDataSource.addDataSourceProperty("socketFactory", limitedUnSafeUserCustomDataSourceProperties.getSocketFactory());
            hikariDataSource.addDataSourceProperty("cloudSqlInstance", limitedUnSafeUserCustomDataSourceProperties.getCloudSqlInstance());
            hikariDataSource.addDataSourceProperty("useSSL", limitedUnSafeUserCustomDataSourceProperties.getUseSSL());
        }
        hikariDataSource.setSchema( "unsafe" ); // POTRZEBNE?
        hikariDataSource.setMaximumPoolSize(PostgreSqlDataSources.maximumPoolSize);
        System.out.println("PostgresSqlDataSource >> HikariDataSource limitedUnSafeUserHikariDataSource()");
        return hikariDataSource;
    }
}
