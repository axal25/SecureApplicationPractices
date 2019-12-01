package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.model.Course;
import backend.app.sec.SecureAppPractices.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

public abstract class CourseController {
    public static final String selectCourseAsUUIDMapping = "/asUUID/{id}";
    public static final String selectCourseAsStringMapping = "/asString/{id}";
    public static final String deleteCourseMapping = "/{id}";
    public static final String updateCourseCourseMapping = "/{id}";
    public static final String runExecuteGetsNoResultsFromDatabaseMapping = "/runExecuteGetsNoResultsFromDatabase";
    public static final String runQueryGetsResultsFromDatabaseMapping = "/runQueryGetsResultsFromDatabase";

    private final CourseService courseService;

    protected CourseController(CourseService courseService) { this.courseService = courseService; }

    @PostMapping
    public void insertCourse(@RequestBody @Valid @NotNull Course course) throws Exception { courseService.insertCourse( course ); }

    @GetMapping
    public List<Course> selectAllCourses() { return courseService.selectAllCourses(); }

    @GetMapping(path = CourseController.selectCourseAsUUIDMapping)
    public Course selectCourse(@PathVariable("id") UUID id) { return courseService.selectCourse( id ).orElse( null ); }

    @GetMapping(path = CourseController.selectCourseAsStringMapping)
    public String selectCourse(@PathVariable("id") String stringId) { return courseService.selectCourse( stringId ); }

    @DeleteMapping(path = CourseController.deleteCourseMapping)
    public void deleteCourse(@PathVariable("id") UUID id) { courseService.deleteCourse( id ); }

    @PutMapping(path = CourseController.updateCourseCourseMapping)
    public int updateCourse(@PathVariable("id") UUID id, @RequestBody @Valid @NotNull Course course) { return courseService.updateCourse( id, course ); }

    @GetMapping(path = CourseController.runExecuteGetsNoResultsFromDatabaseMapping)
    public String runExecuteGetsNoResultsFromDatabase(@QueryParam("query") String query) { return courseService.runExecuteGetsNoResultsFromDatabase( query ); }

    @GetMapping(path = CourseController.runQueryGetsResultsFromDatabaseMapping)
    public String runQueryGetsResultsFromDatabase(@QueryParam("query") String query) { return courseService.runQueryGetsResultsFromDatabase( query ); }
}
