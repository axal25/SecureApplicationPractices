package backend.app.sec.SecureAppPractices.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("unSecurePostgreSqlCourseDao")
public class UnSecurePostgreSqlCourseDao extends CourseDao {

    private final JdbcTemplate jdbcTemplate;
    private final String schema = CourseDao.unSafeSchemaName;
    private final String table = CourseDao.unSafeTableName;

    @Autowired
    public UnSecurePostgreSqlCourseDao(@Qualifier("postgresUserJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        super.setSchema(this.schema);
        super.setTable(this.table);
    }
}
