package backend.app.sec.SecureAppPractices.service;

import backend.app.sec.SecureAppPractices.model.Course;
import com.google.gson.Gson;
import lombok.Getter;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public abstract class CourseServiceTest {

    private final CourseService courseService;
    private final Flyway flyway;

    @Getter
    private static final int courseAmountFrom_CourseService = CourseService.getInsertMockUpDataAmount();
    private static final String preDefNamePatternFromMigration = "^Predefined Example Course from /resources/db/migration/\\*\\.sql file #[0-9]\\.[0-9]+$";
    private static final String emptyResultDataAccessExceptionPattern = "^org.springframework.dao.EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0.*$";
    private static final String dataIntegrityViolationExceptionPattern = "^org.springframework.dao.DataIntegrityViolationException: StatementCallback; SQL \\[.*\\]; ERROR: invalid input syntax for type uuid: \".*\"([ \\r\\n\\t]*+(.))*$";
    @Getter
    private final String courseName = "Test Course inserted from " + this.getClass().getSimpleName() + " #";
    private final int courseAmountFromMigration = 8; // Comes strictly from inserts to given table in R__RepeatableMigration_CourseTable.sql
    private final int courseAmountFrom_CourseService_checkIfTwoIdenticalIdCanExist;
    private final int startingCourseAmount;
    private int currentCourseAmount;
    private int courseNameCounter = 1;

    public CourseServiceTest(CourseService courseService, Flyway flyway) {
        this.courseService = courseService;
        this.flyway = flyway;
        this.currentCourseAmount = this.courseService.selectAllCourses().size();
        this.courseAmountFrom_CourseService_checkIfTwoIdenticalIdCanExist = this.courseService.getCheckIfTwoIdenticalIdCanExistAmount();
        this.startingCourseAmount = courseAmountFrom_CourseService +
                courseAmountFromMigration +
                courseAmountFrom_CourseService_checkIfTwoIdenticalIdCanExist;
    }

    @Order(1)
    @DisplayName("selectAllCourses & database repeatable initiation (Flyway .clean and .migration)")
    @Test
    void databaseRepeatableInitiation() {
        int courseCounter = 0;
        int courseCounterFrom_R__RepeatableMigration_CourseTable_sql = 0;
        int courseCounterFrom_SecureCourseService = 0;
        List<Course> listOfCourses = courseService.selectAllCourses();
        assertNotNull( listOfCourses );
        assertFalse(listOfCourses.isEmpty());
        assertTrue(listOfCourses.size() == startingCourseAmount);
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
            if( matcher_R__SecureCourseService.matches() ) courseCounterFrom_SecureCourseService++;
            courseCounter++;
        }
        assertEquals(startingCourseAmount, courseCounter);
        assertEquals(courseAmountFromMigration, courseCounterFrom_R__RepeatableMigration_CourseTable_sql);
        assertEquals(courseAmountFrom_CourseService, courseCounterFrom_SecureCourseService);
    }

    @DisplayName("selectCourse by String id & UUID id")
    @Test
    @Order(2)
    void selectCourseByStringIdAndByUuidId() {
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

    @DisplayName("selectCourse by String id & UUID id Exception")
    @Test
    @Order(3)
    void selectCourseByStringIdAndByUuidIdException() {
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


        Thread selectedCourseByUuidId = new Thread(() -> { courseService.selectCourse( courseUuidId ).orElse(null); });
        assertThrows(EmptyResultDataAccessException.class, selectedCourseByUuidId::run);
        EmptyResultDataAccessException e1 = null;
        try { selectedCourseByUuidId.run(); } catch(EmptyResultDataAccessException eTmp1) { e1 = eTmp1; }
        Pattern exceptionMessagePattern = Pattern.compile(emptyResultDataAccessExceptionPattern);
        Matcher exceptionMessageMatcher = exceptionMessagePattern.matcher( e1.toString() );
        assertTrue(exceptionMessageMatcher.matches());

        Thread selectedCourseByStringId = new Thread(() -> { courseService.selectCourse( courseStringId ); });
        assertThrows(EmptyResultDataAccessException.class, selectedCourseByStringId::run);
        EmptyResultDataAccessException e2 = null;
        try { selectedCourseByStringId.run(); } catch(EmptyResultDataAccessException eTmp2) { e2 = eTmp2; }
        exceptionMessageMatcher = exceptionMessagePattern.matcher( e2.toString() );
        assertTrue(exceptionMessageMatcher.matches());

        Thread selectedCourseByStringNOTid = new Thread(() -> { courseService.selectCourse( "Completely Not Id" ); });
        if( this.courseService instanceof SecureCourseService) {
            assertThrows(DataIntegrityViolationException.class, selectedCourseByStringNOTid::run);
            DataIntegrityViolationException e3 = null;
            try { selectedCourseByStringNOTid.run(); } catch(DataIntegrityViolationException eTmp3) { e3 = eTmp3; }
            exceptionMessagePattern = Pattern.compile(dataIntegrityViolationExceptionPattern);
            exceptionMessageMatcher = exceptionMessagePattern.matcher( e3.toString() );
            assertTrue(exceptionMessageMatcher.matches());
        }
        else if( this.courseService instanceof  UnSecureCourseService) {
            assertThrows(EmptyResultDataAccessException.class, selectedCourseByStringNOTid::run);
            EmptyResultDataAccessException e3 = null;
            try { selectedCourseByStringNOTid.run(); } catch(EmptyResultDataAccessException eTmp3) { e3 = eTmp3; }
            exceptionMessagePattern = Pattern.compile(emptyResultDataAccessExceptionPattern);
            exceptionMessageMatcher = exceptionMessagePattern.matcher( e3.toString() );
            assertTrue(exceptionMessageMatcher.matches());
        }
    }

    @DisplayName("insertCourse")
    @Test
    @Order(4)
    void insertCourse() throws Exception {
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

    @DisplayName("deleteCourse & selectCourse (asUUID, asString) Exception")
    @Test
    @Order(5)
    void deleteCourse() {
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
        Thread selectCourseAsUUID = new Thread(() -> { courseService.selectCourse(courseUuidId).orElse(null); });
        Thread selectCourseAsString = new Thread(() -> { courseService.selectCourse(courseStringId); });
        assertThrows(EmptyResultDataAccessException.class, selectCourseAsUUID::run);
        assertThrows(EmptyResultDataAccessException.class, selectCourseAsString::run);

        EmptyResultDataAccessException e1 = null;
        EmptyResultDataAccessException e2 = null;
        try{ selectCourseAsUUID.run(); } catch(EmptyResultDataAccessException eTmp1) { e1 = eTmp1; }
        try { selectCourseAsString.run(); } catch(EmptyResultDataAccessException eTmp2) { e2 = eTmp2; }
        Pattern exceptionMessagePattern = Pattern.compile("^org.springframework.dao.EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0.*$");
        Matcher exceptionMessageMatcher = exceptionMessagePattern.matcher( e1.toString() );
        assertTrue(exceptionMessageMatcher.matches());
        exceptionMessageMatcher = exceptionMessagePattern.matcher( e2.toString() );
        assertTrue(exceptionMessageMatcher.matches());
    }

    @DisplayName("updateCourse")
    @Test
    @Order(6)
    void updateCourse() {
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

    public static String getCourseListAsJsonString(List<Course> listOfCourses) {
        Gson gson = new Gson();
        return gson.toJson( listOfCourses );
    }
}
