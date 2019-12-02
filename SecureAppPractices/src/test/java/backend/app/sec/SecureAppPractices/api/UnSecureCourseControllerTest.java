package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
@DependsOn("unSecureCourseController")
public class UnSecureCourseControllerTest extends CourseControllerTest {
    final static String apiPrefix = "/unSecureApi/courses";
    private final CourseController courseController;
    private final CourseService courseService;
    private final TestRestTemplate restTemplate;

    @Autowired
    public UnSecureCourseControllerTest(
            @Qualifier("unSecureCourseController") CourseController courseController,
            @Qualifier("unSecureCourseService") CourseService courseService,
            TestRestTemplate restTemplate
    ) throws MalformedURLException {
        super(courseController, courseService, restTemplate, UnSecureCourseControllerTest.apiPrefix);
        this.courseController = courseController;
        this.courseService = courseService;
        this.restTemplate = restTemplate;
    }
}
