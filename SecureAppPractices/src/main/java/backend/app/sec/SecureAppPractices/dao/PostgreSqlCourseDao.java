package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("PostgreSqlCourseDao")
public class PostgreSqlCourseDao implements CourseDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgreSqlCourseDao(JdbcTemplate jdbcTemplate) {
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
    public int deleteCourse(UUID id) {
        final String sqlQuery = "DELETE FROM safe.courses WHERE id = ?;";
        return jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public int updateCourse(UUID id, Course course) {
        final String sqlQuery = "UPDATE safe.courses SET name = ? WHERE id = ?;";
        return jdbcTemplate.update(sqlQuery, course.getName(), id);
    }

    @Override
    public String runQuery2(String query) {
        query = query.replaceFirst("^\"", "");
        query = query.replaceFirst("\"$", "");
        String response = null;
        try {
            response = jdbcTemplate.queryForObject(
                    query,
                    new Object[]{},
                    (resultSet, i) -> {
                        ResultSetMetaData rsmd = resultSet.getMetaData();
                        System.out.println("querying SELECT * FROM XXX");
                        int columnsNumber = rsmd.getColumnCount();
                        String value = "[";
                        do {
                            value += "{";
                            for (int i1 = 1; i1 <= columnsNumber; i1++) {
                                if (i1 > 1) {
                                    value += ",  ";
                                    System.out.print(",  ");
                                }
                                String columnValue = resultSet.getString(i1);
                                System.out.print(columnValue + " " + rsmd.getColumnName(i1));
                                value += rsmd.getColumnName(i1) + "=" + columnValue;
                            }
                            System.out.println("");
                            value += "}";
                        } while (resultSet.next());
                        value += "]";
                        return value;
//                        final String resultId = resultSet.getString("id");
//                        final String resultName = resultSet.getString("name");
//                        return new String("{ \"id\": \"" + resultId + "\", \"name\": \"" + resultName + "\" }");
                    }
            );
        } catch (Exception e) {
            response = e.toString();
            e.printStackTrace();
        }
        return response;
    }
}
