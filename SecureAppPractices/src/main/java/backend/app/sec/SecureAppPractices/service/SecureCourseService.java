package backend.app.sec.SecureAppPractices.service;

import backend.app.sec.SecureAppPractices.dao.CourseDao;
import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SecureCourseService {

    private final CourseDao courseDao;

    @Autowired
    public SecureCourseService(@Qualifier("PostgreSqlCourseDao"/*"fakeCourseDao"*/) CourseDao courseDao) {
        this.courseDao = courseDao;
        insertMockUpData();
        checkIfTwoIdenticalIdCanExist();
    }

    public int insertCourse(Course course) {
        return courseDao.insertCourse( course );
    }

    public List<Course> selectAllCourses() {
        return courseDao.selectAllCourses();
    }

    public Optional<Course> selectCourse(UUID id) { return courseDao.selectCourse( id ); }

    public String selectCourse(String stringId) { return courseDao.selectCourse( stringId ); }

    public int deleteCourse(UUID id) { return courseDao.deleteCourse( id ); }

    public int updateCourse(UUID id, Course course) { return courseDao.updateCourse( id, course ); }

    public String runExecuteGetsNoResultsFromDatabase(String query) { return courseDao.runExecuteGetsNoResultsFromDatabase(query); }

    public String runQueryGetsResultsFromDatabase(String query) { return courseDao.runQueryGetsResultsFromDatabase(query); }

    private void insertMockUpData() {
        final UUID tmpId = new UUID(0, 0);
        final String preDefName = "Predefined Example Course from CourseService class insertMockUpData() method #";
        for (int i = 0; i < 100; i++) {
            // this tmpId will not be the one of the Course inside of DAO collection
            // this tmpId will be changed to random one when put inside of DAO collection
            this.courseDao.insertCourse( new Course( tmpId, preDefName + i ) );
        }
    }

    private boolean checkIfTwoIdenticalIdCanExist() {
        final String resultPrefix = this.getClass().getName() + " >> " + "checkIfTwoIdenticalIdCanExist(): Two Courses with same \"id\" ";
        final String resultSuffix = " cause exception.";
        try {
            final UUID permId = UUID.randomUUID();
            final String preDefName = "Predefined Example Course from CourseService class checkIfTwoIdenticalIdCanExist() method";
            this.courseDao.insertCourse(permId, new Course(permId, preDefName));
            this.courseDao.insertCourse(permId, new Course(permId, preDefName));
            System.out.println(resultPrefix + "would NOT" + resultSuffix);
            return false;
        }
        catch(Exception e) {
            System.out.println(resultPrefix + "WOULD" + resultSuffix);
            return true;
        }
    }
}
