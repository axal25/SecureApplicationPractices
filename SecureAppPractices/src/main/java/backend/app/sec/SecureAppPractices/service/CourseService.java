package backend.app.sec.SecureAppPractices.service;

import backend.app.sec.SecureAppPractices.dao.CourseDao;
import backend.app.sec.SecureAppPractices.model.Course;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class CourseService {

    private final CourseDao courseDao;
    private final String preDefNamePrefix = "Predefined Example Course from " + this.getClass().getSimpleName() + " class";
    @Getter
    private final String insertMockUpDataPreDefName = this.preDefNamePrefix + " insertMockUpData() method #";
    @Getter
    private final String checkIfTwoIdenticalIdCanExistPreDefName = this.preDefNamePrefix + " insertMockUpData() method #";
    @Getter
    private static final int insertMockUpDataAmount = 100;
    @Getter
    private int checkIfTwoIdenticalIdCanExistAmount = -1;

    public CourseService(CourseDao courseDao) throws Exception {
        this.courseDao = courseDao;
        insertMockUpData();
        checkIfTwoIdenticalIdCanExist();
    }

    public int insertCourse(Course course) throws Exception {
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

    public void insertMockUpData() throws Exception {
        final UUID tmpId = new UUID(0, 0);
        for (int i = 0; i < 100; i++) {
            // this tmpId will not be the one of the Course inside of DAO collection
            // this tmpId will be changed to random one when put inside of DAO collection
            this.courseDao.insertCourse( new Course( tmpId, this.insertMockUpDataPreDefName + i ) );
        }
    }

    public boolean checkIfTwoIdenticalIdCanExist() {
        final String resultPrefix = this.getClass().getSimpleName() + " >> " + "checkIfTwoIdenticalIdCanExist(): Two Courses with same \"id\" ";
        final String resultSuffix = " cause exception.";
        try {
            final UUID permId = UUID.randomUUID();
            final String preDefName = checkIfTwoIdenticalIdCanExistPreDefName;
            this.courseDao.insertCourse(permId, new Course(permId, preDefName));
            this.courseDao.insertCourse(permId, new Course(permId, preDefName));
            checkIfTwoIdenticalIdCanExistAmount = 2;
            System.err.println(resultPrefix + "would NOT" + resultSuffix);
            return false;
        }
        catch(Exception e) {
            checkIfTwoIdenticalIdCanExistAmount = 1;
            System.err.println(resultPrefix + "WOULD" + resultSuffix);
            return true;
        }
    }
}
