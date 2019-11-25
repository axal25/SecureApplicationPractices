package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.dao.CourseDao;
import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping("UnSecureApi/courses")
@RestController
public class UnSecureController {
    private final CourseDao courseDao;

    @Autowired
    public UnSecureController(@Qualifier("UnSecurePostgreSqlCourseDao") CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @GetMapping(path = "/UUID/{id}")
    public Course selectSecureCourse(@PathVariable("id") UUID id) { return courseDao.selectCourse( id ).orElse( null ); }

    @GetMapping(path = "/String/{id}")
    public String selectUnSecureCourse(@PathVariable("id") String stringId) { return courseDao.selectCourse( stringId ); }

    @GetMapping
    public List<Course> selectAllCourses() {
        return courseDao.selectAllCourses();
    }

    @GetMapping(path = "/query")
    public String runQuery(@QueryParam("query") String query) {
        return courseDao.runQuery( query );
    }
}
