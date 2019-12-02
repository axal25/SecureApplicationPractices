package backend.app.sec.SecureAppPractices.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("securePostgreSqlCourseDao")
public class SecurePostgreSqlCourseDao extends CourseDao {

    private final JdbcTemplate jdbcTemplate;
    private final String schema = CourseDao.safeSchemaName;
    private final String table = CourseDao.safeTableName;

    @Autowired
    public SecurePostgreSqlCourseDao(@Qualifier("limitedSafeUserJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        super.setSchema(this.schema);
        super.setTable(this.table);
    }
}
