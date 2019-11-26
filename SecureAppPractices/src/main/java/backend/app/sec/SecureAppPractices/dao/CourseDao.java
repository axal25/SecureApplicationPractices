package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.model.Course;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseDao {
    public static final String defaultMessage = "This method is unsecure and can't be used from this api. You can use this from UnSecureController at: .../UnSecureApi/courses";
    int insertCourse(UUID id, Course course);

    default int insertCourse(Course course) {
        UUID id = UUID.randomUUID();
        return insertCourse(id, new Course(id, course.getName()));
    }

    List<Course> selectAllCourses();

    Optional<Course> selectCourse(UUID id);

    int deleteCourse(UUID id);

    int updateCourse(UUID id, Course course);

    default String selectCourse(String stringId) {
        return CourseDao.defaultMessage;
    }

    default String runExecuteGetsNoResultsFromDatabase(String query) {
        return CourseDao.defaultMessage;
    }

    default String runQueryGetsResultsFromDatabase(String query) {
        return CourseDao.defaultMessage;
    }
}
