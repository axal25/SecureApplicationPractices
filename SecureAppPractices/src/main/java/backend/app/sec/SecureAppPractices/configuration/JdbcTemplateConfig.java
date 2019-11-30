package backend.app.sec.SecureAppPractices.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {
    @Bean("postgresUserJdbcTemplate")
    public JdbcTemplate postgresUserJdbcTemplate(@Qualifier("postgresUserHikariDataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource( dataSource );
        return jdbcTemplate;
    }
    @Bean("limitedSafeUserJdbcTemplate")
    public JdbcTemplate limitedUserJdbcTemplate(@Qualifier("limitedSafeUserHikariDataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource( dataSource );
        return jdbcTemplate;
    }
    @Bean("limitedUnSafeUserJdbcTemplate")
    public JdbcTemplate limitedUnUserJdbcTemplate(@Qualifier("limitedUnSafeUserHikariDataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource( dataSource );
        return jdbcTemplate;
    }
}
