package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("PostgreSqlCourseDao")
public class PostgreSqlCourseDao implements CourseDao {

    @Override
    public int insertCourse(UUID id, Course course) {
        return 0;
    }

    @Override
    public List<Course> selectAllCourses() {
        return List.of(new Course(UUID.randomUUID(), "First Person from PostgreSqlCourseDao") );
    }

    @Override
    public Optional<Course> selectCourse(UUID id) {
        return Optional.empty();
    }

    @Override
    public int deleteCourse(UUID id) {
        return 0;
    }

    @Override
    public int updateCourse(UUID id, Course course) {
        return 0;
    }
}
