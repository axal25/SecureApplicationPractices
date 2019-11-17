package backend.app.sec.SecureAppPractices.dao;

import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeCourseDao")
public class FakeCourseDao implements CourseDao {

    private static List<Course> db = null;

    public FakeCourseDao() {
        db = new ArrayList<Course>();
    }

    @Override
    public int insertCourse(UUID id, Course course) {
        db.add( new Course(id, course.getName()) );
        return 1;
    }

    @Override
    public List<Course> selectAllCourses() {
        return db;
    }

    @Override
    public Optional<Course> selectCourse(UUID id) {
        return db.stream()
                .filter(
                        course -> {
                            return course.getId().equals(id);
                        }
                ).findFirst();
    }

    @Override
    public int deleteCourse(UUID id) {
        Optional<Course> optCourse = selectCourse( id );
        if( optCourse.isEmpty() ) return 0;
        else {
            db.remove( optCourse.get() );
            return 1;
        }
    }

    @Override
    public int updateCourse(UUID id, Course course) {
        return selectCourse( id )
                .map( selectedByIdCourse -> {
                        int indexOfCourseToUpdate = db.indexOf( selectedByIdCourse );
                        if( indexOfCourseToUpdate >= 0 ) {
                            db.set(indexOfCourseToUpdate, new Course(id, course.getName()));
                            return 1;
                        }
                        else return 0;
                    }
                )
                .orElse(0);
    }
}
