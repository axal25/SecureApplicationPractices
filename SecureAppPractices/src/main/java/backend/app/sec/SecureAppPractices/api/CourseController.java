package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.model.Course;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

public interface CourseController {
    public static final String selectCourseAsCourseMapping = "/asUUID/{id}";
    public static final String selectCourseAsStringMapping = "/asString/{id}";
    public static final String deleteCourseMapping = "/{id}";
    public static final String updateCourseCourseMapping = "/{id}";
    public static final String runExecuteGetsNoResultsFromDatabaseMapping = "/runExecuteGetsNoResultsFromDatabase";
    public static final String runQueryGetsResultsFromDatabaseMapping = "/runQueryGetsResultsFromDatabase";

    @PostMapping
    public void insertCourse(@RequestBody @Valid @NotNull Course course);

    @GetMapping
    public List<Course> selectAllCourses();

    @GetMapping(path = CourseController.selectCourseAsCourseMapping)
    public Course selectCourse(@PathVariable("id") UUID id);

    @GetMapping(path = CourseController.selectCourseAsStringMapping)
    public String selectCourse(@PathVariable("id") String stringId);

    @DeleteMapping(path = CourseController.deleteCourseMapping)
    public void deleteCourse(@PathVariable("id") UUID id);

    @PutMapping(path = CourseController.updateCourseCourseMapping)
    public int updateCourse(@PathVariable("id") UUID id, @RequestBody @Valid @NotNull Course course);

    @GetMapping(path = CourseController.runExecuteGetsNoResultsFromDatabaseMapping)
    public String runExecuteGetsNoResultsFromDatabase(@QueryParam("query") String query);

    @GetMapping(path = CourseController.runQueryGetsResultsFromDatabaseMapping)
    public String runQueryGetsResultsFromDatabase(@QueryParam("query") String query);
}
