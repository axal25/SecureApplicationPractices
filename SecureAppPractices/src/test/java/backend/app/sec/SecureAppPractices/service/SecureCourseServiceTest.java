package backend.app.sec.SecureAppPractices.service;

import backend.app.sec.SecureAppPractices.model.Course;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecureCourseServiceTest {
    @Autowired
    SecureCourseService secureCourseService;

    @Autowired
    Flyway flyway;

    final int courseAmountFrom_R__RepeatableMigration_CourseTable_sql = 8;
    final int courseAmountFrom_SecureCourseService = 100;
    final int courseAmountFrom_SecureCourseService_checkIfTwoIdenticalIdCanExist = 1;
    int courseAmount = courseAmountFrom_SecureCourseService +
            courseAmountFrom_R__RepeatableMigration_CourseTable_sql +
            courseAmountFrom_SecureCourseService_checkIfTwoIdenticalIdCanExist;
    final String courseName = "Test Course inserted from SecureCourseServiceTest #";
    int courseNameCounter = 1;

    @DisplayName("selectCourse by String id & UUID id")
    @Test
    @Order(1)
    void selectCourseByStringIdAndByUuidId() {
        List<Course> listOfCourses = secureCourseService.selectAllCourses();
        assertNotNull(listOfCourses);
        assertFalse(listOfCourses.isEmpty());
        assertTrue(listOfCourses.size() >= 1);
        Course exampleCourse = listOfCourses.get(0);
        UUID courseUuidId = exampleCourse.getId();
        String courseStringId = courseUuidId.toString();
        Course selectedCourseByUuidId = secureCourseService.selectCourse( courseUuidId ).orElse(null);
        String selectedCourseByStringId = secureCourseService.selectCourse( courseStringId );
        assertNotNull(selectedCourseByUuidId);
        assertNotNull(selectedCourseByStringId);
        assertFalse(selectedCourseByStringId.isEmpty());
        assertEquals(exampleCourse.toString(), selectedCourseByUuidId.toString());
        assertEquals(exampleCourse.toString(), selectedCourseByStringId);
    }

    @Order(2)
    @DisplayName("selectAllCourses & database repeatable initiation (Flyway .clean and .migration)")
    @Test
    void databaseRepeatableInitiation() {
        int courseCounter = 0;
        int courseCounterFrom_R__RepeatableMigration_CourseTable_sql = 0;
        int courseCounterFrom_SecureCourseService = 0;
        List<Course> listOfCourses = secureCourseService.selectAllCourses();
        assertNotNull( listOfCourses );
        assertFalse(listOfCourses.isEmpty());
        assertTrue(listOfCourses.size() == courseAmount);
        Pattern courseNamePatterFrom_R__RepeatableMigration_CourseTable_sql = Pattern.compile("^Predefined Example Course from /resources/db/migration/\\*\\.sql file #1\\.[0-9]+$");
        Pattern courseNamePatterFrom_SecureCourseService = Pattern.compile("^Predefined Example Course from CourseService class insertMockUpData\\(\\) method #[0-9]+$");
        for(Course course : listOfCourses) {
            assertNotNull(course);
            assertNotNull(course.getName());
            assertFalse(course.getName().isEmpty());
            assertNotNull(course.getId());

            Matcher matcher_R__RepeatableMigration_CourseTable_sql = courseNamePatterFrom_R__RepeatableMigration_CourseTable_sql.matcher( course.getName() );
            Matcher matcher_R__SecureCourseService = courseNamePatterFrom_SecureCourseService.matcher( course.getName() );
            if( matcher_R__RepeatableMigration_CourseTable_sql.matches() ) courseCounterFrom_R__RepeatableMigration_CourseTable_sql++;
            if( matcher_R__SecureCourseService.matches() ) courseCounterFrom_SecureCourseService++;
            courseCounter++;
        }
        assertEquals(courseAmount, courseCounter);
        assertEquals(courseAmountFrom_R__RepeatableMigration_CourseTable_sql, courseCounterFrom_R__RepeatableMigration_CourseTable_sql);
        assertEquals(courseAmountFrom_SecureCourseService, courseCounterFrom_SecureCourseService);
    }

    @DisplayName("insertCourse")
    @Test
    @Order(3)
    void insertCourse() {
        Course course1 = new Course(UUID.randomUUID(), courseName + courseNameCounter);
        assertEquals(1, secureCourseService.insertCourse( course1 ));
        courseAmount++;
        Course course2 = new Course(course1.getId(), courseName + courseNameCounter);
        assertEquals(1, secureCourseService.insertCourse( course2 ));
        courseAmount++;
        List<Course> listOfCourses = secureCourseService.selectAllCourses();
        assertNotNull(listOfCourses);
        assertFalse(listOfCourses.isEmpty());
        assertTrue(listOfCourses.size() >= 2);
        assertEquals(courseAmount, listOfCourses.size());
        Course selectedCourse1 = listOfCourses.get(listOfCourses.size()-2);
        assertNotNull(selectedCourse1);
        assertEquals(course1.getName(), selectedCourse1.getName());
        Course selectedCourse2 = listOfCourses.get(listOfCourses.size()-1);
        assertNotNull(selectedCourse2);
        assertEquals(course2.getName(), selectedCourse2.getName());
    }

    @DisplayName("deleteCourse & selectCourse fail")
    @Test
    @Order(4)
    void deleteCourse() {
        final String courseName = "Test Course inserted from SecureCourseServiceTest #";
        List<Course> listOfCourses = secureCourseService.selectAllCourses();
        courseNameCounter = listOfCourses.size() + 1 - courseAmount;
        courseAmount = listOfCourses.size();
        assertNotNull(listOfCourses);
        assertFalse(listOfCourses.isEmpty());
        assertTrue(listOfCourses.size() >= 1);
        Course course = listOfCourses.get(listOfCourses.size() - 1);
        boolean removeBoolResult = listOfCourses.remove(course);
        int removeIntResult = removeBoolResult ? 1 : 0;
        assertEquals(removeIntResult, secureCourseService.deleteCourse(course.getId()));
        listOfCourses = secureCourseService.selectAllCourses();
        courseAmount--;
        assertNotNull(listOfCourses);
        assertFalse(listOfCourses.contains(course));
        assertTrue(listOfCourses.size()>=1);
        assertEquals(courseAmount, listOfCourses.size());

        UUID courseUuidId = course.getId();
        String courseStringId = courseUuidId.toString();
        Course selectedCourseByUuidId = secureCourseService.selectCourse(courseUuidId).orElse(null);
        System.out.println("There is supposed to be an e.printStackTrace here \\/\\/\\/");
        String selectedCourseByStringId = secureCourseService.selectCourse(courseStringId);
        System.out.println("There is supposed to be an e.printStackTrace here /\\/\\/\\");
        assertNull(selectedCourseByUuidId);
        assertNotNull(selectedCourseByStringId);
        assertFalse(selectedCourseByStringId.isEmpty());
        assertNotEquals(course.toString(), selectedCourseByStringId);
        Pattern exceptionMessagePattern = Pattern.compile("org.springframework.dao.EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0.*");
        Matcher exceptionMessageMatcher = exceptionMessagePattern.matcher( selectedCourseByStringId );
        assertTrue(exceptionMessageMatcher.matches());
    }

    @DisplayName("updateCourse")
    @Test
    @Order(5)
    void updateCourse() {
        List<Course> listOfCourses = secureCourseService.selectAllCourses();
        courseNameCounter = listOfCourses.size() + 1 - courseAmount;
        courseAmount = listOfCourses.size();
        Course course = listOfCourses.get(listOfCourses.size()-1);
        Course modifiedCourse = new Course(course.getId(), courseName + courseNameCounter);
        courseNameCounter++;
        assertEquals(course.getId(), modifiedCourse.getId());
        assertNotEquals(course.getName(), modifiedCourse.getName());
        assertEquals(1, secureCourseService.updateCourse(modifiedCourse.getId(), modifiedCourse));
        Course selectedCourse = secureCourseService.selectCourse(modifiedCourse.getId()).orElse(null);
        assertEquals(course.getId(), selectedCourse.getId());
        assertEquals(modifiedCourse.getId(), selectedCourse.getId());
        assertNotEquals(course.getName(), selectedCourse.getName());
        assertEquals(modifiedCourse.getName(), selectedCourse.getName());

        listOfCourses = secureCourseService.selectAllCourses();
        boolean containsId = false;
        UUID generatedUUID = null;
        do {
            generatedUUID = UUID.randomUUID();
            for(Course courseFromList : listOfCourses) {
                if(courseFromList.getId().compareTo(generatedUUID)==0) containsId = true;
            }
        } while( containsId = false );
        Course nonExistingCourse = new Course(generatedUUID, courseName + courseNameCounter);
        assertEquals(0, secureCourseService.updateCourse(nonExistingCourse.getId(), nonExistingCourse));
    }
}
