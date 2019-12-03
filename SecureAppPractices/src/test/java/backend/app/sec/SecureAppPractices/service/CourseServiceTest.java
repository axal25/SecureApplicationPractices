package backend.app.sec.SecureAppPractices.service;

import backend.app.sec.SecureAppPractices.dao.CourseDao;
import backend.app.sec.SecureAppPractices.dao.SecurePostgreSqlCourseDao;
import backend.app.sec.SecureAppPractices.dao.UnSecurePostgreSqlCourseDao;
import backend.app.sec.SecureAppPractices.model.Course;
import com.google.gson.Gson;
import lombok.Getter;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public abstract class CourseServiceTest {

    private final ApplicationContext applicationContext;
    private final Flyway flyway;
    private final CourseService courseService;

    @Getter
    private final int courseAmountFrom_CourseService;
    private static final String preDefNamePatternFromMigration = "^Predefined Example Course from /resources/db/migration/\\*\\.sql file #[0-9]\\.[0-9]+$";
    private static final String emptyResultDataAccessExceptionPattern = "^org.springframework.dao.EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0.*$";
    private static final String dataIntegrityViolationExceptionPattern = "^org.springframework.dao.DataIntegrityViolationException: StatementCallback; SQL \\[.*\\]; ERROR: invalid input syntax for type uuid: \".*\"([ \\r\\n\\t]*+(.))*$";
    private static final String noResultReturnedDataIntegrityViolationExceptionPattern = "^org.springframework.dao.DataIntegrityViolationException: StatementCallback; SQL \\[.*\\]; No results were returned by the query.; nested exception is org.postgresql.util.PSQLException: No results were returned by the query.$";
    private static final String badSqlGrammarExceptionPattern = "^org.springframework.jdbc.BadSqlGrammarException: StatementCallback; bad SQL grammar \\[.*\\]; nested exception is org.postgresql.util.PSQLException: ERROR: syntax error at or near \".*\"([ \\r\\n\\t]*+(.))*$";
    private static final String mustBeOwnerOfTableBadSqlGrammarExceptionPattern = "^org.springframework.jdbc.BadSqlGrammarException: StatementCallback; bad SQL grammar \\[.*\\]; nested exception is org.postgresql.util.PSQLException: ERROR: must be owner of table courses$";
    @Getter
    private final String courseName = "Test Course inserted from " + this.getClass().getSimpleName() + " #";
    private final int courseAmountFromMigration = 8; // Comes strictly from inserts to given table in R__RepeatableMigration_CourseTable.sql
    private final int courseAmountFrom_CourseService_checkIfTwoIdenticalIdCanExist;
    private final int startingCourseAmount;
    private int currentCourseAmount;
    private int courseNameCounter = 1;

    public CourseServiceTest(ApplicationContext applicationContext, Flyway flyway, CourseService courseService) throws Exception {
        this.applicationContext = applicationContext;
        this.flyway = flyway;
        this.courseService = courseService;
        this.currentCourseAmount = this.courseService.selectAllCourses().size();
        if(this.courseService.getCheckIfTwoIdenticalIdCanExistAmount() == -1) {
            this.courseService.insertMockUpData();
            this.courseService.checkIfTwoIdenticalIdCanExist();
        }
        this.courseAmountFrom_CourseService_checkIfTwoIdenticalIdCanExist = this.courseService.getCheckIfTwoIdenticalIdCanExistAmount();
        this.courseAmountFrom_CourseService = CourseService.getInsertMockUpDataAmount() + this.courseAmountFrom_CourseService_checkIfTwoIdenticalIdCanExist;
        this.startingCourseAmount = this.courseAmountFrom_CourseService +
                this.courseAmountFromMigration;
    }

    public void databaseRepeatableInitiation() {
        int courseCounter = 0;
        int courseCounterFrom_R__RepeatableMigration_CourseTable_sql = 0;
        int courseCounterFrom_CourseService = 0;
        List<Course> listOfCourses = courseService.selectAllCourses();
        assertNotNull( listOfCourses );
        assertFalse(listOfCourses.isEmpty());
        assertEquals(startingCourseAmount, listOfCourses.size());
        Pattern courseNamePatterFrom_R__RepeatableMigration_CourseTable_sql = Pattern.compile(preDefNamePatternFromMigration);
        Pattern courseNamePatterFrom_CourseService = Pattern
                .compile(
                        new StringBuilder()
                                .append("^")
                                .append(
                                        courseService.getInsertMockUpDataPreDefName()
                                                .replaceFirst("\\(","\\\\\\(")
                                                .replaceFirst("\\)","\\\\\\)")
                                ).append("[0-9]+$")
                                .toString()
                );
        for(Course course : listOfCourses) {
            assertNotNull(course);
            assertNotNull(course.getName());
            assertFalse(course.getName().isEmpty());
            assertNotNull(course.getId());

            Matcher matcher_R__RepeatableMigration_CourseTable_sql = courseNamePatterFrom_R__RepeatableMigration_CourseTable_sql.matcher( course.getName() );
            Matcher matcher_R__SecureCourseService = courseNamePatterFrom_CourseService.matcher( course.getName() );
            if( matcher_R__RepeatableMigration_CourseTable_sql.matches() ) courseCounterFrom_R__RepeatableMigration_CourseTable_sql++;
            if( matcher_R__SecureCourseService.matches() ) courseCounterFrom_CourseService++;
            courseCounter++;
        }
        System.out.println("startingCourseAmount: " + startingCourseAmount + " vs. courseCounter: " + courseCounter);
        System.out.println("courseAmountFromMigration: " + courseAmountFromMigration + " vs. courseCounterFrom_R__RepeatableMigration_CourseTable_sql: " + courseCounterFrom_R__RepeatableMigration_CourseTable_sql);
        System.out.println("courseAmountFrom_CourseService: " + courseAmountFrom_CourseService + " vs. courseCounterFrom_CourseService: " + courseCounterFrom_CourseService);
        assertEquals(startingCourseAmount, courseCounter);
        assertEquals(courseAmountFromMigration, courseCounterFrom_R__RepeatableMigration_CourseTable_sql);
        assertEquals(courseAmountFrom_CourseService, courseCounterFrom_CourseService);
    }

    public void selectCourseByStringIdAndByUuidId() throws Exception {
        List<Course> listOfCourses = courseService.selectAllCourses();
        assertNotNull(listOfCourses);
        assertFalse(listOfCourses.isEmpty());
        assertTrue(listOfCourses.size() >= 1);
        Course exampleCourse = listOfCourses.get(0);
        UUID courseUuidId = exampleCourse.getId();
        String courseStringId = courseUuidId.toString();
        Course selectedCourseByUuidId = courseService.selectCourse( courseUuidId ).orElse(null);
        String selectedCourseByStringId = courseService.selectCourse( courseStringId );
        assertNotNull(selectedCourseByUuidId);
        assertNotNull(selectedCourseByStringId);
        assertFalse(selectedCourseByStringId.isEmpty());
        assertEquals(exampleCourse.toString(), selectedCourseByUuidId.toString());
        assertEquals(exampleCourse.toString(), selectedCourseByStringId);
    }

    public void selectCourseByStringIdAndByUuidIdException() throws Exception {
        List<Course> listOfCourses = courseService.selectAllCourses();
        assertNotNull(listOfCourses);
        assertFalse(listOfCourses.isEmpty());
        assertTrue(listOfCourses.size() >= 1);
        Course notExistingCourse = null;
        for(int i=0; i<listOfCourses.size(); i++) {
            if( notExistingCourse == null || notExistingCourse.getId() == listOfCourses.get(i).getId() ) {
                notExistingCourse = new Course( UUID.randomUUID(), "NotExistingCourse");
                i = -1;
            }
        }
        assertNotNull(notExistingCourse);
        UUID courseUuidId = notExistingCourse.getId();
        String courseStringId = courseUuidId.toString();


        FunctionalInterface selectedCourseByUuidId = (() -> { courseService.selectCourse( courseUuidId ); });
        assertThrows(EmptyResultDataAccessException.class, selectedCourseByUuidId::executeThrowsException);
        EmptyResultDataAccessException e1 = null;
        try { selectedCourseByUuidId.executeThrowsException(); } catch(EmptyResultDataAccessException eTmp1) { e1 = eTmp1; }
        Pattern exceptionMessagePattern = Pattern.compile(emptyResultDataAccessExceptionPattern);
        Matcher exceptionMessageMatcher = exceptionMessagePattern.matcher( e1.toString() );
        assertTrue(exceptionMessageMatcher.matches());

        FunctionalInterface selectedCourseByStringId = (() -> { courseService.selectCourse( courseStringId ); });
        assertThrows(EmptyResultDataAccessException.class, selectedCourseByStringId::executeThrowsException);
        EmptyResultDataAccessException e2 = null;
        try { selectedCourseByStringId.executeThrowsException(); } catch(EmptyResultDataAccessException eTmp2) { e2 = eTmp2; }
        exceptionMessageMatcher = exceptionMessagePattern.matcher( e2.toString() );
        assertTrue(exceptionMessageMatcher.matches());

        FunctionalInterface selectedCourseByStringNOTid = (() -> { courseService.selectCourse( "Completely Not Id" ); });
        if( this.courseService instanceof SecureCourseService) {
            assertThrows(DataIntegrityViolationException.class, selectedCourseByStringNOTid::executeThrowsException);
            DataIntegrityViolationException e3 = null;
            try { selectedCourseByStringNOTid.executeThrowsException(); } catch(DataIntegrityViolationException eTmp3) { e3 = eTmp3; }
            exceptionMessagePattern = Pattern.compile(dataIntegrityViolationExceptionPattern);
            exceptionMessageMatcher = exceptionMessagePattern.matcher( e3.toString() );
            assertTrue(exceptionMessageMatcher.matches());
        }
        else if( this.courseService instanceof  UnSecureCourseService) {
            assertThrows(EmptyResultDataAccessException.class, selectedCourseByStringNOTid::executeThrowsException);
            EmptyResultDataAccessException e3 = null;
            try { selectedCourseByStringNOTid.executeThrowsException(); } catch(EmptyResultDataAccessException eTmp3) { e3 = eTmp3; }
            exceptionMessagePattern = Pattern.compile(emptyResultDataAccessExceptionPattern);
            exceptionMessageMatcher = exceptionMessagePattern.matcher( e3.toString() );
            assertTrue(exceptionMessageMatcher.matches());
        }
    }

    public void insertCourse() throws Exception {
        List<Course> listOfCourses = courseService.selectAllCourses();
        assertNotNull(listOfCourses);
        Course course1 = new Course(UUID.randomUUID(), courseName + courseNameCounter);
        assertEquals(1, courseService.insertCourse( course1 ));
        currentCourseAmount++;
        Course course2 = new Course(course1.getId(), courseName + courseNameCounter);
        assertEquals(1, courseService.insertCourse( course2 ));
        currentCourseAmount++;
        listOfCourses = courseService.selectAllCourses();
        assertNotNull(listOfCourses);
        assertFalse(listOfCourses.isEmpty());
        assertEquals(currentCourseAmount, listOfCourses.size());
        assertEquals(2, currentCourseAmount - startingCourseAmount);
        assertTrue(currentCourseAmount >= 2);
        Course selectedCourse1 = listOfCourses.get(currentCourseAmount-2);
        assertNotNull(selectedCourse1);
        assertEquals(course1.getName(), selectedCourse1.getName());
        Course selectedCourse2 = listOfCourses.get(currentCourseAmount-1);
        assertNotNull(selectedCourse2);
        assertEquals(course2.getName(), selectedCourse2.getName());
    }

    public void deleteCourse() throws Exception {
        final String courseName = "Test Course inserted from SecureCourseServiceTest #";
        List<Course> listOfCourses = courseService.selectAllCourses();
        assertNotNull(listOfCourses);
        assertFalse(listOfCourses.isEmpty());
        assertTrue(listOfCourses.size() >= 1);
        courseNameCounter = listOfCourses.size() + 1 - startingCourseAmount;
        assertEquals(currentCourseAmount, listOfCourses.size());
        Course course = listOfCourses.get(currentCourseAmount-1);
        boolean removeBoolResult = listOfCourses.remove(course);
        int removeIntResult = removeBoolResult ? 1 : 0;
        assertEquals(removeIntResult, courseService.deleteCourse(course.getId()));

        removeBoolResult = listOfCourses.remove(course);
        removeIntResult = removeBoolResult ? 1 : 0;
        assertEquals(removeIntResult, courseService.deleteCourse(course.getId()));
        assertEquals(0, courseService.deleteCourse(course.getId()));

        currentCourseAmount--;
        assertEquals(
                SecureCourseServiceTest.getCourseListAsJsonString(courseService.selectAllCourses()),
                SecureCourseServiceTest.getCourseListAsJsonString(listOfCourses)
        );
        assertEquals(currentCourseAmount, listOfCourses.size());

        UUID courseUuidId = course.getId();
        String courseStringId = courseUuidId.toString();
        FunctionalInterface selectCourseAsUUID = (() -> { courseService.selectCourse(courseUuidId); });
        FunctionalInterface selectCourseAsString = (() -> { courseService.selectCourse(courseStringId); });
        assertThrows(EmptyResultDataAccessException.class, selectCourseAsUUID::executeThrowsException);
        assertThrows(EmptyResultDataAccessException.class, selectCourseAsString::executeThrowsException);

        EmptyResultDataAccessException e1 = null;
        EmptyResultDataAccessException e2 = null;
        try{ selectCourseAsUUID.executeThrowsException(); } catch(EmptyResultDataAccessException eTmp1) { e1 = eTmp1; }
        try { selectCourseAsString.executeThrowsException(); } catch(EmptyResultDataAccessException eTmp2) { e2 = eTmp2; }
        Pattern exceptionMessagePattern = Pattern.compile(emptyResultDataAccessExceptionPattern);
        Matcher exceptionMessageMatcher = exceptionMessagePattern.matcher( e1.toString() );
        assertTrue(exceptionMessageMatcher.matches());
        exceptionMessageMatcher = exceptionMessagePattern.matcher( e2.toString() );
        assertTrue(exceptionMessageMatcher.matches());
    }

    public void updateCourse() throws Exception {
        List<Course> listOfCourses = courseService.selectAllCourses();
        courseNameCounter = listOfCourses.size() + 1 - startingCourseAmount;
        assertEquals(currentCourseAmount, listOfCourses.size());
        Course course = listOfCourses.get(currentCourseAmount-1);
        Course modifiedCourse = new Course(course.getId(), courseName + courseNameCounter);
        courseNameCounter++;
        assertEquals(course.getId(), modifiedCourse.getId());
        assertNotEquals(course.getName(), modifiedCourse.getName());
        assertEquals(1, courseService.updateCourse(modifiedCourse.getId(), modifiedCourse));
        Course selectedCourse = courseService.selectCourse(modifiedCourse.getId()).orElse(null);
        assertEquals(course.getId(), selectedCourse.getId());
        assertEquals(modifiedCourse.getId(), selectedCourse.getId());
        assertNotEquals(course.getName(), selectedCourse.getName());
        assertEquals(modifiedCourse.getName(), selectedCourse.getName());

        listOfCourses = courseService.selectAllCourses();
        boolean containsId = false;
        UUID generatedUUID = null;
        do {
            generatedUUID = UUID.randomUUID();
            for(Course courseFromList : listOfCourses) {
                if(courseFromList.getId().compareTo(generatedUUID)==0) containsId = true;
            }
        } while( containsId = false );
        Course nonExistingCourse = new Course(generatedUUID, courseName + courseNameCounter);
        assertEquals(0, courseService.updateCourse(nonExistingCourse.getId(), nonExistingCourse));
    }

    public void runExecuteGetsNoResultsFromDatabase() throws Exception {
        final String schema = getSchemaName();
        final String table = getTableName();
        assertNotNull(schema);
        assertNotNull(table);
        final String selectQuery1 = new StringBuilder().append("SELECT * FROM ")
                .append(schema).append(".").append(table).append(";").toString();
        final String selectQuery2 = new StringBuilder().append("\"").append(selectQuery1).append("\"").toString();
        courseService.runExecuteGetsNoResultsFromDatabase(selectQuery1);
        courseService.runExecuteGetsNoResultsFromDatabase(selectQuery2);

        final String jibberishQuery = "jibberish";
        FunctionalInterface jibberish = () -> { courseService.runQueryGetsResultsFromDatabase(jibberishQuery); };
        assertThrows(BadSqlGrammarException.class, jibberish::executeThrowsException);
        BadSqlGrammarException e1 = null;
        try{ jibberish.executeThrowsException(); } catch(BadSqlGrammarException eTmp1) { e1 = eTmp1; }
        Pattern exceptionMessagePattern = Pattern.compile(badSqlGrammarExceptionPattern);
        Matcher exceptionMessageMatcher = exceptionMessagePattern.matcher( e1.toString() );
        assertTrue(exceptionMessageMatcher.matches());

        List<Course> listOfCourses1 = courseService.selectAllCourses();
        final String newCourseName = new StringBuilder().append(courseName).append(courseNameCounter).toString();
        StringBuilder queryBuilder3 = new StringBuilder().append("INSERT INTO ").append(schema).append(".").append(table)
                .append(" (id, name) VALUES (");
        if( isSchemaAndTableWhereIdIsVarChar(schema, table) ) queryBuilder3.append("CAST(uuid_generate_v4() AS VARCHAR)");
        else queryBuilder3.append("uuid_generate_v4()");
        final String query3 = queryBuilder3.append(",'").append(newCourseName).append("');").toString();
        courseService.runExecuteGetsNoResultsFromDatabase(query3);
        List<Course> listOfCourses2 = courseService.selectAllCourses();
        assertNotNull(listOfCourses2);
        assertFalse(listOfCourses2.isEmpty());
        Course lastCourse = listOfCourses2.get(listOfCourses2.size()-1);
        assertNotNull(lastCourse);
        assertFalse(listOfCourses1.contains(lastCourse));
        assertEquals(newCourseName, lastCourse.getName());
        Course selectedCourse = courseService.selectCourse(lastCourse.getId()).orElse(null);
        assertNotNull(selectedCourse);
        assertEquals(lastCourse.toString(), selectedCourse.toString());

        final String dropQuery = new StringBuilder().append("DROP TABLE IF EXISTS ").append(schema).append(".").append(table).append(";").toString();
        FunctionalInterface noPrivilegesForSecure = () -> { courseService.runExecuteGetsNoResultsFromDatabase(dropQuery); };
        if( !isSchemaAndTableWhereIdIsVarChar(schema, table) ) {
            assertThrows(BadSqlGrammarException.class, noPrivilegesForSecure::executeThrowsException);
            BadSqlGrammarException e3 = null;
            try { noPrivilegesForSecure.executeThrowsException(); } catch(BadSqlGrammarException tmpE3) { e3 = tmpE3; }
            exceptionMessagePattern = Pattern.compile(mustBeOwnerOfTableBadSqlGrammarExceptionPattern);
            exceptionMessageMatcher = exceptionMessagePattern.matcher( e3.toString() );
            assertTrue(exceptionMessageMatcher.matches());
        }
        else {
            noPrivilegesForSecure.executeThrowsException();
            cleanDatabaseAndRestartUserDataSources();
        }
    }

    public void runQueryGetsResultsFromDatabase() throws Exception {
        final String schema = getSchemaName();
        final String table = getTableName();
        assertNotNull(schema);
        assertNotNull(table);
        final String selectQuery1 = new StringBuilder().append("SELECT * FROM ")
                .append(schema).append(".").append(table).append(";").toString();
        final String selectQuery2 = new StringBuilder().append("\"").append(selectQuery1).append("\"").toString();
        String resp1 = courseService.runQueryGetsResultsFromDatabase(selectQuery1);
        String resp2 = courseService.runQueryGetsResultsFromDatabase(selectQuery2);
        List<Course> listOfCourses1 = courseService.selectAllCourses();
        String comp1 = getCourseListAsJsonString(listOfCourses1);
        assertEquals(comp1, resp1);
        assertEquals(comp1, resp2);

        final String jibberishQuery = "jibberish";
        FunctionalInterface jibberish = () -> { courseService.runQueryGetsResultsFromDatabase(jibberishQuery); };
        assertThrows(BadSqlGrammarException.class, jibberish::executeThrowsException);
        BadSqlGrammarException e1 = null;
        try{ jibberish.executeThrowsException(); } catch(BadSqlGrammarException eTmp1) { e1 = eTmp1; }
        Pattern exceptionMessagePattern = Pattern.compile(badSqlGrammarExceptionPattern);
        Matcher exceptionMessageMatcher = exceptionMessagePattern.matcher( e1.toString() );
        assertTrue(exceptionMessageMatcher.matches());

        final String newCourseName = new StringBuilder().append(courseName).append(courseNameCounter).toString();
        StringBuilder queryBuilder3 = new StringBuilder().append("INSERT INTO ").append(schema).append(".").append(table)
                .append(" (id, name) VALUES (");
        if( isSchemaAndTableWhereIdIsVarChar(schema, table) ) queryBuilder3.append("CAST(uuid_generate_v4() AS VARCHAR)");
        else queryBuilder3.append("uuid_generate_v4()");
        final String query3 = queryBuilder3.append(",'").append(newCourseName).append("');").toString();
        FunctionalInterface noResultReturnException = () -> { courseService.runQueryGetsResultsFromDatabase(query3); };
        assertThrows(DataIntegrityViolationException.class, noResultReturnException::executeThrowsException);
        List<Course> listOfCourses2 = courseService.selectAllCourses();
        assertNotNull(listOfCourses2);
        assertFalse(listOfCourses2.isEmpty());
        Course lastCourse = listOfCourses2.get(listOfCourses2.size()-1);
        assertNotNull(lastCourse);
        assertFalse(listOfCourses1.contains(lastCourse));
        assertEquals(newCourseName, lastCourse.getName());
        Course selectedCourse = courseService.selectCourse(lastCourse.getId()).orElse(null);
        assertNotNull(selectedCourse);
        assertEquals(lastCourse.toString(), selectedCourse.toString());

        DataIntegrityViolationException e2 = null;
        try { noResultReturnException.executeThrowsException(); } catch(DataIntegrityViolationException tmpE2) { e2 = tmpE2; };
        assertNotNull(e2);
        exceptionMessagePattern = Pattern.compile(noResultReturnedDataIntegrityViolationExceptionPattern);
        exceptionMessageMatcher = exceptionMessagePattern.matcher( e2.toString() );
        assertTrue(exceptionMessageMatcher.matches());

        final String dropQuery = new StringBuilder().append("DROP TABLE IF EXISTS ").append(schema).append(".").append(table).append(";").toString();
        FunctionalInterface noPrivilegesForSecure = () -> { courseService.runQueryGetsResultsFromDatabase(dropQuery); };
        if( !isSchemaAndTableWhereIdIsVarChar(schema, table) ) {
            assertThrows(BadSqlGrammarException.class, noPrivilegesForSecure::executeThrowsException);
            BadSqlGrammarException e3 = null;
            try { noPrivilegesForSecure.executeThrowsException(); } catch(BadSqlGrammarException tmpE3) { e3 = tmpE3; }
            assertNotNull(e3);
            exceptionMessagePattern = Pattern.compile(mustBeOwnerOfTableBadSqlGrammarExceptionPattern);
            exceptionMessageMatcher = exceptionMessagePattern.matcher( e3.toString() );
            assertTrue(exceptionMessageMatcher.matches());
        }
        else {
            assertThrows(DataIntegrityViolationException.class, noPrivilegesForSecure::executeThrowsException);
            cleanDatabaseAndRestartUserDataSources();
            CourseService courseService = (CourseService) this.applicationContext.getBean("unSecureCourseService");
            noPrivilegesForSecure = () -> { courseService.runQueryGetsResultsFromDatabase(dropQuery); };
            DataIntegrityViolationException e3 = null;
            try { noPrivilegesForSecure.executeThrowsException(); } catch(DataIntegrityViolationException tmpE3) { e3 = tmpE3; };
            assertNotNull(e3);
            exceptionMessagePattern = Pattern.compile(noResultReturnedDataIntegrityViolationExceptionPattern);
            exceptionMessageMatcher = exceptionMessagePattern.matcher( e3.toString() );
            assertTrue(exceptionMessageMatcher.matches());
            cleanDatabaseAndRestartUserDataSources();
        }
    }

    public static String getCourseListAsJsonString(List<Course> listOfCourses) {
        Gson gson = new Gson();
        return gson.toJson( listOfCourses );
    }

    public String getSchemaName() throws Exception {
        if(this instanceof SecureCourseServiceTest) return SecurePostgreSqlCourseDao.schema;
        else if(this instanceof UnSecureCourseServiceTest) return UnSecurePostgreSqlCourseDao.schema;
        else throw new Exception("this is not instanceof SecureCourseServiceTest or UnSecureCourseServiceTest");
    }

    public String getTableName() throws Exception {
        if (this instanceof SecureCourseServiceTest) return SecurePostgreSqlCourseDao.table;
        else if (this instanceof UnSecureCourseServiceTest) return UnSecurePostgreSqlCourseDao.table;
        else throw new Exception("this is not instanceof SecureCourseServiceTest or UnSecureCourseServiceTest");
    }

    private boolean isSchemaAndTableWhereIdIsVarChar(String schema, String table) throws Exception {
        final String functionName = "";
        if( CourseDao.unSafeSchemaName.compareTo(schema) == 0 && CourseDao.unSafeTableName.compareTo(table) == 0 ) return true;
        else if( CourseDao.safeSchemaName.compareTo(schema) == 0 && CourseDao.safeTableName.compareTo(table) == 0 ) return false;
        else throw new Exception(
                    new StringBuilder()
                            .append(this.getClass().getSimpleName()).append(" >> ")
                            .append(functionName).append(": Bad schema parameter: ")
                            .append(schema).append(" or bad table parameter: ")
                            .append(table).toString()
            );
    }

    private void cleanDatabaseAndRestartUserDataSources() throws Exception {
        System.out.println("cleanDatabaseAndRestartUserDataSources() - flyway.clean()");
        flyway.clean();
        System.out.println("cleanDatabaseAndRestartUserDataSources() - flyway.migrate()");
        flyway.migrate();
        String[] beansNames = {
                "secureCourseController", "unSecureCourseController",
                "secureCourseService", "unSecureCourseService",
                "securePostgreSqlCourseDao", "unSecurePostgreSqlCourseDao",
                "limitedSafeUserJdbcTemplate", "limitedUnSafeUserJdbcTemplate",
                "limitedSafeUserHikariDataSource", "limitedUnSafeUserHikariDataSource"
        };
        restartBean(beansNames);
//        SecureCourseService secureCourseService = (SecureCourseService) this.applicationContext.getBean("secureCourseService");
//        secureCourseService.insertMockUpData();
//        secureCourseService.checkIfTwoIdenticalIdCanExist();
//        UnSecureCourseService unSecureCourseService = (UnSecureCourseService) this.applicationContext.getBean("unSecureCourseService");
//        unSecureCourseService.insertMockUpData();
//        unSecureCourseService.checkIfTwoIdenticalIdCanExist();
    }

    private void restartBean(String[] beanNames) {
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) this.applicationContext;
        BeanDefinition[] beanDefinitions = new BeanDefinition[beanNames.length];
        for(int i=0; i<beanNames.length; i++) {
            beanDefinitions[i] = genericApplicationContext.getBeanDefinition(beanNames[i]);
            System.out.println(i + ". cleanDatabaseAndRestartUserDataSources() - Remove User Bean (" + beanNames[i] + ")");
        }
        for(int i=beanNames.length-1; i>=0; i--) {
            genericApplicationContext.removeBeanDefinition(beanNames[i]);
            System.out.println(i + ". cleanDatabaseAndRestartUserDataSources() - Register User Bean (" + beanNames[i] + ")");
            genericApplicationContext.registerBeanDefinition(beanNames[i], beanDefinitions[i]);
            System.out.println(i + ". cleanDatabaseAndRestartUserDataSources() - Restarted User Bean (" + beanNames[i] + ")");
        }
    }
}
