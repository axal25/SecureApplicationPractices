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

    int deleteCourse(UUID id);

    int updateCourse(UUID id, Course course);
}
