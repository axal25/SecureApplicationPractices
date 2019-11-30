package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("PostgreSqlCourseDao")
public class SecurePostgreSqlCourseDao implements CourseDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SecurePostgreSqlCourseDao(@Qualifier("limitedSafeUserJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertCourse(UUID id, Course course) {
        final String sqlQuery = "INSERT INTO safe.courses (id, name) VALUES (?, ?)";
        return jdbcTemplate.update(
                sqlQuery,
                course.getId(),
                course.getName()
        );
    }

    @Override
    public List<Course> selectAllCourses() {
        final String sqlQuery = "SELECT id, name FROM safe.courses;";
        return jdbcTemplate.query(
            sqlQuery,
            (resultSet, i) -> {
                final UUID id = UUID.fromString(resultSet.getString("id"));
                final String name = resultSet.getString("name");
                return new Course( id, name );
            }
        );
    }

    @Override
    public Optional<Course> selectCourse(UUID id) {
        final String sqlQuery = "SELECT id, name FROM safe.courses WHERE id = ?;";
        Course course = null;
        try {
            course = jdbcTemplate.queryForObject(
                    sqlQuery,
                    new Object[]{id},
                    (resultSet, i) -> {
                        final UUID resultId = UUID.fromString(resultSet.getString("id"));
                        final String resultName = resultSet.getString("name");
                        return new Course(resultId, resultName);
                    }
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable( course );
    }

    @Override
    public String selectCourse(String stringId) {
        final String sqlQuery = "SELECT id, name FROM safe.courses WHERE id = (CAST(? AS uuid));";
        String course = null;
        try {
            course = jdbcTemplate.queryForObject(
                    sqlQuery,
                    new Object[]{stringId},
                    (resultSet, i) -> {
                        final UUID resultId = UUID.fromString(resultSet.getString("id"));
                        final String resultName = resultSet.getString("name");
                        return new Course(resultId, resultName).toString();
                    }
            );
        } catch(Exception e) {
            course = e.toString();
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public int deleteCourse(UUID id) {
        final String sqlQuery = "DELETE FROM safe.courses WHERE id = ?;";
        return jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public int updateCourse(UUID id, Course course) {
        final String sqlQuery = "UPDATE safe.courses SET name = ? WHERE id = ?;";
        return jdbcTemplate.update(sqlQuery, course.getName(), id);
    }
}
