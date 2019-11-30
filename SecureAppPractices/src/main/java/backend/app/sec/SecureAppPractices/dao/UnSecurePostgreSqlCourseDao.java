package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("UnSecurePostgreSqlCourseDao")
public class UnSecurePostgreSqlCourseDao implements CourseDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UnSecurePostgreSqlCourseDao(@Qualifier("postgresUserJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertCourse(UUID id, Course course) {
        final String sqlQuery = "INSERT INTO unsafe.courses (id, name) VALUES (CAST(? AS VARCHAR ), ?)";
        return jdbcTemplate.update(
                sqlQuery,
                course.getId(),
                course.getName()
        );
    }

    @Override
    public List<Course> selectAllCourses() {
        final String sqlQuery = "SELECT id, name FROM unsafe.courses;";
        return jdbcTemplate.query(
                sqlQuery,
                (resultSet, i) -> {
                    final UUID id = UUID.fromString(resultSet.getString("id"));
                    final String name = resultSet.getString("name");
                    return new Course(id, name);
                }
        );
    }

    @Override
    public Optional<Course> selectCourse(UUID id) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(course);
    }

//    @Override
//    public String selectCourse(String id) {
//        final String sqlQuery = "SELECT id, name FROM unsafe.courses WHERE id = ?;";
//        String response = null;
//        try {
//            response = jdbcTemplate.queryForObject(
//                    sqlQuery,
//                    new Object[]{id},
//                    (resultSet, i) -> {
//                        final String resultId = resultSet.getString("id");
//                        final String resultName = resultSet.getString("name");
//                        return new String("{ \"id\": \"" + resultId + "\", \"name\": \"" + resultName + "\" }");
//                    }
//            );
//        } catch (Exception e) {
//            response = e.toString();
//            e.printStackTrace();
//        }
//        return response;
//    }

    @Override
    public String selectCourse(String id) {
        final String sqlQuery = "SELECT id, name FROM unsafe.courses WHERE id = " + id + ";";
        String response = null;
        try {
            response = jdbcTemplate.queryForObject(
                    sqlQuery,
                    (resultSet, i) -> {
                        final String resultId = resultSet.getString("id");
                        final String resultName = resultSet.getString("name");
                        return new String("{ \"id\": \"" + resultId + "\", \"name\": \"" + resultName + "\" }");
                    }
            );
        } catch (Exception e) {
            response = e.toString();
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public int deleteCourse(UUID id) {
        final String sqlQuery = "DELETE FROM unsafe.courses WHERE id = ?;";
        return jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public int updateCourse(UUID id, Course course) {
        final String sqlQuery = "UPDATE unsafe.courses SET name = ? WHERE id = ?;";
        return jdbcTemplate.update(sqlQuery, course.getName(), id);
    }

    @Override
    public String runExecuteGetsNoResultsFromDatabase(String query) {
        try {
            query = query.replaceFirst("^\"", "");
            query = query.replaceFirst("\"$", "");
            return "Query has been run on schema 'unsafe': \n" + query;
        } catch (Exception e) {
            e.printStackTrace();
            return "Query run has FAILED on schema 'unsafe': \n" + query;
        }
    }

    @Override
    public String runQueryGetsResultsFromDatabase(String query) {
        query = query.replaceFirst("^\"", "");
        query = query.replaceFirst("\"$", "");
        StringBuilder response = null;
        try {
            List<String> listOfJsonObjects = jdbcTemplate.query(
                    query,
                    (resultSet, i) -> {
                        StringBuilder jsonObject = new StringBuilder().append("{");
                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        for(i=1; i<=resultSetMetaData.getColumnCount(); i++) {
                            if(i!=1) jsonObject.append(", ");
                            jsonObject.append(resultSetMetaData.getColumnName(i)).append(": ").append(resultSet.getString(i));
                        }
                        jsonObject.append("}");
                        String correctJson = jsonObject.toString().replaceAll(
                                "(?<=\\{|, ?)([a-zA-Z]+?): ?(?![ \\{\\[])(.+?)(?=,|})", "\"$1\": \"$2\"");
                        return correctJson;
                    }
            );
            for (String jsonObject : listOfJsonObjects ) {
                if(response == null) response = new StringBuilder().append("[");
                else response.append(", ");
                response.append(jsonObject);
            }
            response.append("]");
        } catch (Exception e1) {
            try {
                return runExecuteGetsNoResultsFromDatabase( query );
            } catch(Exception e2) {
                response = new StringBuilder().append(e1.toString()).append("\n").append(e2.toString());
                e1.printStackTrace();
            }
        }
        return response.toString();
    }
}
