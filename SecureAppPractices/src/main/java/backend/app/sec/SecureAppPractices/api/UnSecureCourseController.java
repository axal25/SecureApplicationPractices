package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.dao.CourseDao;
import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping("UnSecureApi/courses")
@RestController
public class UnSecureCourseController implements CourseController {
    private final CourseDao courseDao;

    @Autowired
    public UnSecureCourseController(@Qualifier("UnSecurePostgreSqlCourseDao") CourseDao courseDao) { this.courseDao = courseDao; }

    @Override
    @PostMapping
    public void insertCourse(@Valid @NotNull Course course) { courseDao.insertCourse( course ); }

    @Override
    @GetMapping
    public List<Course> selectAllCourses() { return courseDao.selectAllCourses(); }

    @Override
    @GetMapping(path = CourseController.selectCourseAsCourseMapping)
    public Course selectCourse(@PathVariable("id") UUID id) { return courseDao.selectCourse( id ).orElse( null ); }

    @Override
    @GetMapping(path = CourseController.selectCourseAsStringMapping)
    public String selectCourse(@PathVariable("id") String stringId) { return courseDao.selectCourse( stringId ); }

    @Override
    @DeleteMapping(path = CourseController.deleteCourseMapping)
    public void deleteCourse(UUID id) { courseDao.deleteCourse( id ); }

    @Override
    @PutMapping(path = CourseController.updateCourseCourseMapping)
    public int updateCourse(UUID id, @Valid @NotNull Course course) { return courseDao.updateCourse( id, course ); }

    @Override
    @GetMapping(path = CourseController.runExecuteGetsNoResultsFromDatabaseMapping)
    public String runExecuteGetsNoResultsFromDatabase(@QueryParam("query") String query) { return courseDao.runExecuteGetsNoResultsFromDatabase( query ); }

    @Override
    @GetMapping(path = CourseController.runQueryGetsResultsFromDatabaseMapping)
    public String runQueryGetsResultsFromDatabase(@QueryParam("query") String query) { return courseDao.runQueryGetsResultsFromDatabase( query ); }
}
