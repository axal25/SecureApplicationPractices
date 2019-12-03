package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.model.Course;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
public abstract class CourseDao {
    private static final boolean isDebugging = true;
    public static final String defaultMessage = "This method is unsecure and can't be used from this api. You can use this from UnSecureController at: .../UnSecureApi/courses";
    final private JdbcTemplate jdbcTemplate;
    private String schema = null;
    private String table = null;
    static final public String safeSchemaName = "safe";
    static final public String safeTableName = "courses";
    static final public String unSafeSchemaName = "unsafe";
    static final public String unSafeTableName = "courses";

    public CourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertCourse(Course course) throws Exception {
        UUID id = UUID.randomUUID();
        return insertCourse(id, new Course(id, course.getName()));
    }

    public int insertCourse(UUID id, Course course) throws Exception {
        final String sqlQuery = "INSERT INTO " + schema + "." + table + " (id, name) VALUES (" + getUUIDinjection() + ", ?)";
        return jdbcTemplate.update(
                sqlQuery,
                course.getId(),
                course.getName()
        );
    }

    public List<Course> selectAllCourses() {
        final String sqlQuery = "SELECT id, name FROM " + schema + "." + table + ";";
        return jdbcTemplate.query(
                sqlQuery,
                (resultSet, i) -> {
                    final UUID id = UUID.fromString(resultSet.getString("id"));
                    final String name = resultSet.getString("name");
                    return new Course( id, name );
                }
        );
    }

    public Optional<Course> selectCourse(UUID id) throws Exception {
        final String sqlQuery = "SELECT id, name FROM " + schema + "." + table + " WHERE id = " + getUUIDinjection() + ";";
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

    public String selectCourse(String stringId) throws Exception {
        final String sqlQuery = "SELECT id, name FROM " + schema + "." + table + " WHERE id = " + getStringinjection(stringId) + ";";
        return jdbcTemplate.queryForObject(
                sqlQuery,
                (resultSet, i) -> {
                    final UUID resultId = UUID.fromString(resultSet.getString("id"));
                    final String resultName = resultSet.getString("name");
                    return new Course(resultId, resultName).toString();
                }
        );
    }

    public int deleteCourse(UUID id) throws Exception {
        final String sqlQuery = "DELETE FROM " + schema + "." + table + " WHERE id = " + getUUIDinjection() + ";";
        return jdbcTemplate.update(sqlQuery, id);
    }

    public int updateCourse(UUID id, Course course) throws Exception {
        final String sqlQuery = "UPDATE " + schema + "." + table + " SET name = ? WHERE id = " + getUUIDinjection() + ";";
        return jdbcTemplate.update(sqlQuery, course.getName(), id);
    }

    public String runExecuteGetsNoResultsFromDatabase(String query) {
        query = removeDoubleQuotesIfPresent(query);
        jdbcTemplate.execute(query);
        return "Query has been run on schema 'unsafe': \n" + query;
    }

    public String runQueryGetsResultsFromDatabase(String query) {
        query = removeDoubleQuotesIfPresent(query);
        List<String> listOfJsonObjects = jdbcTemplate.query(
                query,
                (resultSet, i) -> {
                    StringBuilder jsonObject = new StringBuilder().append("{");
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    for(i=1; i<=resultSetMetaData.getColumnCount(); i++) {
                        if(i!=1) jsonObject.append(",");
                        jsonObject
                                .append("\"").append(resultSetMetaData.getColumnName(i)).append("\"")
                                .append(":")
                                .append("\"").append(resultSet.getString(i)).append("\"");
                    }
                    jsonObject.append("}");
                    return jsonObject.toString();
                }
        );
        StringBuilder response = new StringBuilder().append("[");
        for (String jsonObject : listOfJsonObjects ) {
            if(response.toString().compareTo("[") != 0) response.append(",");
            response.append(jsonObject);
        }
        response.append("]");

        return response.toString();
    }

    private String getUUIDinjection() throws Exception {
        if(isSchemaAndTableWhereIdIsVarChar()) return "CAST(? AS VARCHAR )";
        else return "?";
    }

    private String getStringinjection(String stringId) throws Exception {
        if(isSchemaAndTableWhereIdIsVarChar()) return "'" + stringId + "'";
        else return "(CAST( '" + stringId + "' AS UUID))";
    }

    private boolean isSchemaAndTableWhereIdIsVarChar() throws Exception {
        final String functionName = "";
        if( CourseDao.unSafeSchemaName.compareTo(this.schema) == 0 && CourseDao.unSafeTableName.compareTo(this.table) == 0 ) return true;
        else if( CourseDao.safeSchemaName.compareTo(this.schema) == 0 && CourseDao.safeTableName.compareTo(this.table) == 0 ) return false;
        else throw new Exception(
                    new StringBuilder()
                            .append(this.getClass().getSimpleName()).append(" >> ")
                            .append(functionName).append(": Bad schema parameter: ")
                            .append(this.schema).append(" or bad table parameter: ")
                            .append(this.table).toString()
            );
    }

    private String removeDoubleQuotesIfPresent(String query) {
        Pattern queryPattern = Pattern.compile("^\".*\"$");
        Matcher queryMatcher = queryPattern.matcher(query);
        if(isDebugging) System.out.println("entry query: >>" + query + "<<");
        if(queryMatcher.matches()) {
            query = query.replaceFirst("^\"", "");
            query = query.replaceFirst("\"$", "");
            if(isDebugging) System.out.println("stripped query: >>" + query + "<<");
        }
        return query;
    }
}
