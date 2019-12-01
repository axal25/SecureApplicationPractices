package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.configuration.ServerPortCustomizer;
import backend.app.sec.SecureAppPractices.model.Course;
import backend.app.sec.SecureAppPractices.service.CourseService;
import backend.app.sec.SecureAppPractices.service.CourseServiceTest;
import com.google.gson.Gson;
import lombok.Getter;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port="+ ServerPortCustomizer.defaultPort)
public abstract class CourseControllerTest {

    @Getter
    private final String courseName = "Test Course inserted from " + this.getClass().getSimpleName() + " #";

    private final static int expectedPort = ServerPortCustomizer.defaultPort;
    private final String apiPrefix;
    private final String selectAllCoursesEndpoint;
    private final String insertCourseEndpoint;
    private final String selectCourseAsStringEndpoint;
    private final String selectCourseAsUUIDEndpoint;
    private final String deleteCourseEndpoint;
    private final String updateCourseEndpoint;

    private final TestRestTemplate restTemplate;
    private final CourseService courseService;
    private final CourseController courseController;

    @LocalServerPort
    private int autoInjectedPort;
    private String stringApiAddress;
    private URL url;

    public CourseControllerTest(
            CourseController courseController,
            CourseService courseService,
            TestRestTemplate restTemplate,
            String apiPrefix) throws MalformedURLException {
        this.courseController = courseController;
        this.courseService = courseService;
        this.restTemplate = restTemplate;
        this.apiPrefix = apiPrefix;
        this.selectAllCoursesEndpoint = this.apiPrefix;
        this.insertCourseEndpoint = this.apiPrefix;
        this.selectCourseAsStringEndpoint = this.apiPrefix + CourseController.selectCourseAsStringMapping;
        this.selectCourseAsUUIDEndpoint = this.apiPrefix + CourseController.selectCourseAsUUIDMapping;
        this.deleteCourseEndpoint = this.apiPrefix + CourseController.deleteCourseMapping;
        this.updateCourseEndpoint = this.apiPrefix + CourseController.updateCourseCourseMapping;
        initPort("CourseControllerTest()");
    }

    private void initPort(String callingFunction) throws MalformedURLException {
        if(this.autoInjectedPort != CourseControllerTest.expectedPort) {
            System.out.println("{\"location\": \"" + callingFunction + "\"} port != " + expectedPort + ", port == " + autoInjectedPort);
            this.autoInjectedPort = CourseControllerTest.expectedPort;
        }
        this.stringApiAddress = "http://localhost:" + this.autoInjectedPort + "/secureApi/courses";
        this.url = new URL(this.stringApiAddress);
    }

    @Test
    @Order(11)
    @DisplayName("insertCourse() endpoint: .../apiPrefix")
    public void insertCourse() {
        Course newCourse = new Course(UUID.randomUUID(), courseName+1);
        ResponseEntity<Object> responseEntityInteger = restTemplate.postForEntity(stringApiAddress, newCourse, null);
        assertEquals(null, responseEntityInteger.getBody());
        List<Course> listOfCourses = this.courseService.selectAllCourses();
        boolean wasFound = false;
        Course foundCourse = null;
        for (Course course: listOfCourses) {
            if( course.getName().compareTo(newCourse.getName())==0) {
                wasFound = true;
                foundCourse = course;
                break;
            }
        }
        assertEquals(newCourse.getName(), foundCourse.getName());
        assertNotEquals(newCourse.getId(), foundCourse.getId());
        assertTrue(wasFound);
    }

    @Test
    @Order(12)
    @DisplayName("selectAllCourses() endpoint: .../apiPrefix")
    public void selectAllCourses() {
        ResponseEntity<List<Course>> responseEntity = restTemplate.exchange(stringApiAddress, HttpMethod.GET, null, new ParameterizedTypeReference<List<Course>>(){});
        List<Course> listOfCourses = courseController.selectAllCourses();
        assertNotEquals(listOfCourses, responseEntity.getBody());
        assertEquals(
                CourseServiceTest.getCourseListAsJsonString(listOfCourses),
                CourseServiceTest.getCourseListAsJsonString(responseEntity.getBody())
        );
    }

    @Test
    @Order(13)
    @DisplayName("selectCourseString() endpoint: .../apiPrefix" + CourseController.selectCourseAsStringMapping)
    public void selectCourseString() {
        selectCourse( CourseController.selectCourseAsStringMapping );
    }

    @Test
    @Order(14)
    @DisplayName("selectCourseUUID() endpoint: .../apiPrefix" + CourseController.selectCourseAsUUIDMapping)
    public void selectCourseUUID() {
        selectCourse( CourseController.selectCourseAsUUIDMapping );
    }

    @Test
    @Order(15)
    @DisplayName("deleteCourse() endpoint: .../apiPrefix" + CourseController.deleteCourseMapping)
    public void deleteCourse() {
        List<Course> listOfCourses = courseService.selectAllCourses();
        Course course = listOfCourses.get(listOfCourses.size()-1);
        String endpointSuffix = CourseController.deleteCourseMapping.replaceFirst("\\{.*\\}", course.getId().toString());
        restTemplate.delete(url + endpointSuffix, String.class);
        listOfCourses = courseService.selectAllCourses();
        assertFalse(listOfCourses.contains(course));
    }

    @Test
    @Order(16)
    @DisplayName("updateCourse() endpoint .../apiPrefix" + CourseController.updateCourseCourseMapping)
    public void updateCourse() {
        List<Course> listOfCourses = courseService.selectAllCourses();
        Course course = listOfCourses.get(listOfCourses.size()-1);
        Course modifiedCourse = new Course(course.getId(), courseName+2);
        String endpointSuffix = CourseController.updateCourseCourseMapping.replaceFirst("\\{.*\\}", modifiedCourse.getId().toString());
        HttpEntity<Course> responseEntityModifiedCourse = new HttpEntity<Course>(modifiedCourse);
        ResponseEntity<Integer> responseEntityInt = restTemplate.exchange(stringApiAddress + endpointSuffix, HttpMethod.PUT, responseEntityModifiedCourse, Integer.class);
        Course selectedCourse = courseService.selectCourse(course.getId()).orElse(null);
        assertNotNull(selectedCourse);
        assertEquals(selectedCourse.getId(), course.getId());
        assertNotEquals(selectedCourse.getName(), course.getName());
        assertEquals(modifiedCourse.toString(), selectedCourse.toString());
    }

    private void selectCourse(String courseControllerSelectCourseAs_UUID_or_String_Mapping) {
        List<Course> listOfCourses = this.courseService.selectAllCourses();
        assertNotNull(listOfCourses);
        assertFalse(listOfCourses.isEmpty());
        Course course = listOfCourses.get(listOfCourses.size()-1);
        assertNotNull(course);
        assertNotNull(course.getId());
        assertNotNull(course.getId().toString());
        System.out.println("course.getId().toString() = " + course.getId().toString());
        listOfCourses = this.courseService.selectAllCourses();
        assertEquals(course.toString(), listOfCourses.get(listOfCourses.size()-1).toString());
        String courseFromSecureCourseService = courseService.selectCourse(course.getId().toString());
        assertEquals(courseFromSecureCourseService, course.toString());
        String endpointSuffix = courseControllerSelectCourseAs_UUID_or_String_Mapping.replaceFirst("\\{.*\\}", course.getId().toString());
        String responseStringCourse = restTemplate.getForObject(url + endpointSuffix, String.class);
        System.out.println("responseStringCourse: " + responseStringCourse);
        Course responseCourse = new Gson().fromJson(responseStringCourse, Course.class);
        assertNotNull(responseCourse);
        assertEquals(course.toString(), responseCourse.toString());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("{ \n")
                .append("\t\"courseName: \"").append(courseName).append("\", \n")
                .append("\t\"apiPrefix: \"").append(apiPrefix).append("\", \n")
                .append("\t\"selectAllCoursesEndpoint: \"").append(selectAllCoursesEndpoint).append("\", \n")
                .append("\t\"insertCourseEndpoint: \"").append(insertCourseEndpoint).append("\", \n")
                .append("\t\"selectCourseAsStringEndpoint: \"").append(selectCourseAsStringEndpoint).append("\", \n")
                .append("\t\"selectCourseAsUUIDEndpoint: \"").append(selectCourseAsUUIDEndpoint).append("\", \n")
                .append("\t\"deleteCourseEndpoint: \"").append(deleteCourseEndpoint).append("\", \n")
                .append("\t\"updateCourseEndpoint: \"").append(updateCourseEndpoint).append("\", \n")
                .append("\t\"restTemplate: \"").append(restTemplate).append("\", \n")
                .append("\t\"courseService: \"").append(courseService).append("\", \n")
                .append("\t\"courseController: \"").append(courseController).append("\", \n")
                .append("\t\"expectedPort: \"").append(expectedPort).append("\", \n")
                .append("\t\"autoInjectedPort: \"").append(autoInjectedPort).append("\", \n")
                .append("\t\"stringApiAddress: \"").append(stringApiAddress).append("\", \n")
                .append("\t\"url: \"").append(url).append("\", \n")
                .append("\t\"courseName: \"").append(courseName).append("\", \n")
                .append("}");
        return stringBuilder.toString();
    }
}
