package backend.app.sec.SecureAppPractices.service;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("unSecureCourseService")
public class UnSecureCourseServiceTest extends CourseServiceTest {
    private final ApplicationContext applicationContext;
    private final Flyway flyway;
    private final CourseService courseService;

    @Autowired
    public UnSecureCourseServiceTest(ApplicationContext applicationContext, Flyway flyway, @Qualifier("unSecureCourseService") CourseService courseService) throws Exception {
        super(applicationContext, flyway, courseService);
        this.applicationContext = applicationContext;
        this.flyway = flyway;
        this.courseService = courseService;
    }
}