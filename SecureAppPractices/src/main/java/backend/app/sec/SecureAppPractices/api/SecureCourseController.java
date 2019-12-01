package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("secureApi/courses")
@RestController
public class SecureCourseController extends CourseController {

    private final CourseService courseService;

    @Autowired
    public SecureCourseController(@Qualifier("secureCourseService") CourseService courseService) {
        super(courseService);
        this.courseService = courseService; }
}
