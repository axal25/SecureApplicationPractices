package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.model.Course;
import backend.app.sec.SecureAppPractices.service.SecureCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping("secureApi/courses")
@RestController
public class SecureCourseController implements CourseController {

    private final SecureCourseService secureCourseService;

    @Autowired
    public SecureCourseController(SecureCourseService secureCourseService) { this.secureCourseService = secureCourseService; }

    @Override
    @PostMapping
    public void insertCourse(@RequestBody @Valid @NotNull  Course course) { secureCourseService.insertCourse( course ); }

    @Override
    @GetMapping
    public List<Course> selectAllCourses() { return secureCourseService.selectAllCourses(); }

    @Override
    @GetMapping(path = CourseController.selectCourseAsUUIDMapping)
    public Course selectCourse(@PathVariable("id") UUID id) { return secureCourseService.selectCourse( id ).orElse( null ); }

    @Override
    @GetMapping(path = CourseController.selectCourseAsStringMapping)
    public String selectCourse(String stringId) { return secureCourseService.selectCourse( stringId ); }

    @Override
    @DeleteMapping(path = CourseController.deleteCourseMapping)
    public void deleteCourse(@PathVariable("id") UUID id) { secureCourseService.deleteCourse( id ); }

    @Override
    @PutMapping(path = CourseController.updateCourseCourseMapping)
    public int updateCourse(@PathVariable("id") UUID id, @RequestBody @Valid @NotNull Course course) { return secureCourseService.updateCourse( id, course ); }

    @Override
    @GetMapping(path = CourseController.runExecuteGetsNoResultsFromDatabaseMapping)
    public String runExecuteGetsNoResultsFromDatabase(@QueryParam("query") String query) { return secureCourseService.runExecuteGetsNoResultsFromDatabase( query ); }

    @Override
    @GetMapping(path = CourseController.runQueryGetsResultsFromDatabaseMapping)
    public String runQueryGetsResultsFromDatabase(@QueryParam("query") String query) { return secureCourseService.runQueryGetsResultsFromDatabase( query ); }
}
