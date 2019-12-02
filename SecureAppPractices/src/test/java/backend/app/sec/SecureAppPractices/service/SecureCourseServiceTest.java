package backend.app.sec.SecureAppPractices.service;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("secureCourseService")
public class SecureCourseServiceTest extends CourseServiceTest {
    private final CourseService courseService;
    private final Flyway flyway;

    @Autowired
    public SecureCourseServiceTest(@Qualifier("secureCourseService") CourseService courseService, Flyway flyway) {
        super(courseService, flyway);
        this.courseService = courseService;
        this.flyway = flyway;
    }
}
