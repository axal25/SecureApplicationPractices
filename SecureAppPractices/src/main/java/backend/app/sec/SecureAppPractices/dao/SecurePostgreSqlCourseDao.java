package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<Course> selectCourse(UUID id) {
        final String sqlQuery = "SELECT id, name FROM " + schema + "." + table + " WHERE id = ?;";
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                    sqlQuery,
                    new Object[]{id},
                    (resultSet, i) -> {
                        final UUID resultId = UUID.fromString(resultSet.getString("id"));
                        final String resultName = resultSet.getString("name");
                        return new Course(resultId, resultName);
                    }
            )
        );
    }

    @Override
    public String selectCourse(String stringId) {
        final String sqlQuery = "SELECT id, name FROM " + schema + "." + table + " WHERE id = (CAST( '" + stringId + "' AS UUID));";
        return jdbcTemplate.queryForObject(
                sqlQuery,
                (resultSet, i) -> {
                    final UUID resultId = UUID.fromString(resultSet.getString("id"));
                    final String resultName = resultSet.getString("name");
                    return new Course(resultId, resultName).toString();
                }
        );
    }

    @Override
    public int deleteCourse(UUID id) {
        final String sqlQuery = "DELETE FROM " + schema + "." + table + " WHERE id = ?;";
        return jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public int updateCourse(UUID id, Course course) {
        final String sqlQuery = "UPDATE " + schema + "." + table + " SET name = ? WHERE id = ?;";
        return jdbcTemplate.update(sqlQuery, course.getName(), id);
    }
}
