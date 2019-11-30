package backend.app.sec.SecureAppPractices.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FlywayConfig {

    public static final boolean isDebugging = false;

    @Primary
    @Bean("flyway")
    @Autowired
    public Flyway flyway(@Qualifier("postgresUserHikariDataSource") HikariDataSource hikariDataSource) {
        if( isDebugging ) System.out.println("PostgresSqlDataSource >> Flyway flyway() \\/\\/\\/");
        Flyway flyway = Flyway.configure()
                .dataSource( hikariDataSource )
                .schemas( "safe" )
                .schemas( "unsafe" )
                .schemas( "demos" )
                .schemas( "public" )
                .mixed( true )
                .baselineOnMigrate( true )
                .table( "flyway_history" )
                .cleanOnValidationError( true )
                .cleanDisabled( false )
//                .initSql( "" )
                .load();
        if( isDebugging ) System.out.println("PostgresSqlDataSource >> Flyway flyway() - load() /\\/\\/\\");
        flyway.clean();
        if( isDebugging ) System.out.println("PostgresSqlDataSource >> Flyway flyway() - clean() /\\/\\/\\");
        flyway.migrate();
        if( isDebugging ) System.out.println("PostgresSqlDataSource >> Flyway flyway() - migrate() /\\/\\/\\");
        return flyway;
    }
}
