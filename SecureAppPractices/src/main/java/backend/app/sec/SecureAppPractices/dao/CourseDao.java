package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.model.Course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseDao {

    int insertCourse(UUID id, Course course);

    default int insertCourse(Course course) {
        UUID id = UUID.randomUUID();
        return insertCourse(id, new Course(id, course.getName()));
    }

    List<Course> selectAllCourses();

    Optional<Course> selectCourse(UUID id);
    default String selectCourse(String id) {
        return "This method is unsecure and can't be used from this api. You can use this from UnSecureController at: .../UnSecureApi/courses";
    }

    int deleteCourse(UUID id);

    int updateCourse(UUID id, Course course);

    default String runQuery(String query) {
        return "This method is unsecure and can't be used from this api. You can use this from UnSecureController at: .../UnSecureApi/courses";
    }

    default String runQuery2(String query) {
        return "This method is unsecure and can't be used from this api. You can use this from UnSecureController at: .../UnSecureApi/courses";
    }
}
