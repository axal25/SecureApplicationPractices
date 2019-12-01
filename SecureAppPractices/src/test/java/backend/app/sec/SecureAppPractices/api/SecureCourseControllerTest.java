package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.service.CourseService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=8081")
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
    ) throws MalformedURLException, URISyntaxException {
        super(courseController, courseService, restTemplate, SecureCourseControllerTest.apiPrefix);
        this.courseController = courseController;
        this.courseService = courseService;
        this.restTemplate = restTemplate;
    }
}
