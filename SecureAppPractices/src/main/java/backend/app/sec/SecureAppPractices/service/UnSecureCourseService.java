package backend.app.sec.SecureAppPractices.service;

import backend.app.sec.SecureAppPractices.dao.CourseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("unSecureCourseService")
public class UnSecureCourseService extends CourseService {

    private final CourseDao courseDao;

    @Autowired
    public UnSecureCourseService(@Qualifier("unSecurePostgreSqlCourseDao") CourseDao courseDao) throws Exception {
        super(courseDao);
        this.courseDao = courseDao;
    }
}
