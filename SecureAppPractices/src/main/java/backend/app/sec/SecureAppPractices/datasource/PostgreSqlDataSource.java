package backend.app.sec.SecureAppPractices.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class PostgreSqlDataSource {

    private final Environment env;
    private final CustomDataSourceProperties customDataSourceProperties;

    @Autowired
    PostgreSqlDataSource( Environment env ) {
        this.env = env;
        customDataSourceProperties  = new CustomDataSourceProperties( env );
    }

    @Primary
    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure().dataSource( hikariDataSource() )
                .schemas( "safe" )
                .schemas( "demos" )
                .schemas( "public" )
                .mixed( true )
                .baselineOnMigrate( true )
                .table( "flyway_history" )
                .cleanOnValidationError( true )
//                .initSql( "ALTER ROLE " + customDataSourceProperties.getUsername() + " SUPERUSER;" )
                .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    public HikariDataSource hikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl( customDataSourceProperties.getJdbcUrl() );
        hikariDataSource.setUsername( customDataSourceProperties.getUsername() );
        hikariDataSource.setPassword( customDataSourceProperties.getPassword() );
        hikariDataSource.setDriverClassName( customDataSourceProperties.getDriverClassName() );
        hikariDataSource.setSchema( "safe" );
        hikariDataSource.setMaximumPoolSize(30);

        return hikariDataSource;
    }

//    @Bean
//    @ConfigurationProperties("app.datasource")
//    public HikariDataSource hikariDataSource() {
//        return DataSourceBuilder
//                .create()
//                .type(HikariDataSource.class)
//                .build();
//    }
}
