package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
@DependsOn("secureCourseController")
public class SecureCourseControllerTest extends CourseControllerTest {
    final static String apiPrefix = "/secureApi/courses";
    private final CourseController courseController;
    private final CourseService courseService;
    private final TestRestTemplate restTemplate;

    @Autowired
    public SecureCourseControllerTest(
            @Qualifier("secureCourseController") CourseController courseController,
            @Qualifier("secureCourseService") CourseService courseService,
            TestRestTemplate restTemplate
    ) throws MalformedURLException {
        super(courseController, courseService, restTemplate, SecureCourseControllerTest.apiPrefix);
        this.courseController = courseController;
        this.courseService = courseService;
        this.restTemplate = restTemplate;
    }
}
