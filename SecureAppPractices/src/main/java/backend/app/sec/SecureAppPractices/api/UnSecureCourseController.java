package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("UnSecureApi/courses")
@RestController
public class UnSecureCourseController extends CourseController {

    private final CourseService courseService;

    @Autowired
    public UnSecureCourseController(@Qualifier("unSecureCourseService") CourseService courseService) {
        super(courseService);
        this.courseService = courseService; }
}
