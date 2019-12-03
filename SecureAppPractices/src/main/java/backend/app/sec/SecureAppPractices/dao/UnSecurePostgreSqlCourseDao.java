package backend.app.sec.SecureAppPractices.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("unSecurePostgreSqlCourseDao")
public class UnSecurePostgreSqlCourseDao extends CourseDao {

    private final JdbcTemplate jdbcTemplate;
    public static final String schema = CourseDao.unSafeSchemaName;
    public static final String table = CourseDao.unSafeTableName;

    @Autowired
    public UnSecurePostgreSqlCourseDao(@Qualifier("limitedUnSafeUserJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        super.setSchema(UnSecurePostgreSqlCourseDao.schema);
        super.setTable(UnSecurePostgreSqlCourseDao.table);
    }
}
