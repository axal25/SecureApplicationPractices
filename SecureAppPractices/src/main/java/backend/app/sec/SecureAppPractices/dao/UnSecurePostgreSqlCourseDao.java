package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository("UnSecurePostgreSqlCourseDao")
public class UnSecurePostgreSqlCourseDao implements CourseDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UnSecurePostgreSqlCourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertCourse(UUID id, Course course) {
        return 0;
    }

    @Override
    public List<Course> selectAllCourses() {
        final String sqlQuery = "SELECT id, name FROM unsafe.courses;";
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
    public Optional<Course> selectCourse(UUID id){
        final String sqlQuery = "SELECT id, name FROM unsafe.courses WHERE id = (CAST( ? AS VARCHAR ));";
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
    public String selectCourse(String id) {
        final String sqlQuery = "SELECT id, name FROM unsafe.courses WHERE id = ?;";
        String response = null;
        try {
            response = jdbcTemplate.queryForObject(
                    sqlQuery,
                    new Object[]{id},
                    (resultSet, i) -> {
                        final String resultId = resultSet.getString("id");
                        final String resultName = resultSet.getString("name");
                        return new String("{ \"id\": \"" + resultId + "\", \"name\": \"" + resultName + "\" }");
                    }
            );
        } catch(Exception e) {
            response = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public int deleteCourse(UUID id) {
        return 0;
    }

    @Override
    public int updateCourse(UUID id, Course course) {
        return 0;
    }

    @Override
    public String runQuery(String query) {
        try {
            query = query.replaceFirst("^\"", "");
            query = query.replaceFirst("\"$", "");
            jdbcTemplate.execute( query );
            return "Query has been run on schema 'unsafe': \n" + query;
        } catch(Exception e) {
            e.printStackTrace();
            return "Query run has FAILED on schema 'unsafe': \n" + query;
        }
    }
}