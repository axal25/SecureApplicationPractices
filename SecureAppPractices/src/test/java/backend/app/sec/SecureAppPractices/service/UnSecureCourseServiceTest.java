package backend.app.sec.SecureAppPractices.service;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnSecureCourseServiceTest extends CourseServiceTest {
    private final CourseService courseService;
    private final Flyway flyway;

    @Autowired
    public UnSecureCourseServiceTest(@Qualifier("unSecureCourseService") CourseService courseService, Flyway flyway) {
        super(courseService, flyway);
        this.courseService = courseService;
        this.flyway = flyway;
    }
}
