package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.configuration.datasource.properties.CustomDataSourceProperties;
import backend.app.sec.SecureAppPractices.model.Course;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Setter
@Getter
public abstract class CourseDao {

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
        final String uuidInjection;
        if(isSchemaAndTableWhereIdIsVarChar()) uuidInjection = "CAST(? AS VARCHAR )";
        else uuidInjection = "?";
        final String sqlQuery = "INSERT INTO " + schema + "." + table + " (id, name) VALUES (" + uuidInjection + ", ?)";
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

    abstract public Optional<Course> selectCourse(UUID id);

    abstract public int deleteCourse(UUID id);

    abstract public int updateCourse(UUID id, Course course);

    public String selectCourse(String stringId) {
        return CourseDao.defaultMessage;
    }

    public String runExecuteGetsNoResultsFromDatabase(String query) {
        return CourseDao.defaultMessage;
    }

    public String runQueryGetsResultsFromDatabase(String query) {
        return CourseDao.defaultMessage;
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
}
