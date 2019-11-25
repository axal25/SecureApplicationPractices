package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.model.Course;
import backend.app.sec.SecureAppPractices.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RequestMapping("api/courses")
@RestController
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public void insertCourse(@RequestBody @Valid @NotNull  Course course) {
        courseService.insertCourse( course );
    }

    @GetMapping
    public List<Course> selectAllCourses() {
        return courseService.selectAllCourses();
    }

    @GetMapping(path = "/{id}")
    public Course selectCourse(@PathVariable("id") UUID id) { return courseService.selectCourse( id ).orElse( null ); }

    @DeleteMapping(path = "/{id}")
    public void deleteCourse(@PathVariable("id") UUID id) { courseService.deleteCourse( id ); }

    @PutMapping(path = "/{id}")
    public int updateCourse(@PathVariable("id") UUID id, @RequestBody @Valid @NotNull Course course) { return courseService.updateCourse( id, course ); }
}
