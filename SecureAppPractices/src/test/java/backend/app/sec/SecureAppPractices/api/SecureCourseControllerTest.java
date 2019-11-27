package backend.app.sec.SecureAppPractices.api;

import backend.app.sec.SecureAppPractices.model.Course;
import backend.app.sec.SecureAppPractices.service.SecureCourseService;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
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
@TestPropertySource(properties = "server.port=8081")
public class SecureCourseControllerTest {
    final String courseName = "Test Course inserted from SecureCourseControllerTest #";

    final static int expectedPort = 8081;
    final static String selectAllCoursesEndpoint = "/secureApi/courses";
    final static String insertCourseEndpoint = "/secureApi/courses";
    final static String selectCourseStringEndpoint = selectAllCoursesEndpoint + CourseController.selectCourseAsStringMapping;
    final static String selectCourseUUIDEndpoint = selectAllCoursesEndpoint + CourseController.selectCourseAsUUIDMapping;
    final static String deleteCourseEndpoint = selectAllCoursesEndpoint + CourseController.deleteCourseMapping;
    final static String updateCourseEndpoint = selectAllCoursesEndpoint + CourseController.updateCourseCourseMapping;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SecureCourseService secureCourseService;

    @LocalServerPort
    private int port;
    private String stringApiAddress;
    private URL url;

    public SecureCourseControllerTest() throws MalformedURLException, URISyntaxException {
        initPort("SecureCourseControllerTest()");
    }

    @Test
    @Order(11)
    @DisplayName("insertCourse() endpoint: " + insertCourseEndpoint)
    public void insertCourse() throws MalformedURLException, URISyntaxException {
        initPort("insertCourse()");
        Course newCourse = new Course(UUID.randomUUID(), courseName+1);
        ResponseEntity<Object> responseEntityInteger = restTemplate.postForEntity(stringApiAddress, newCourse, null);
        assertEquals(null, responseEntityInteger.getBody());
        List<Course> listOfCourses = this.secureCourseService.selectAllCourses();
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
    @DisplayName("selectAllCourses() endpoint: " + selectAllCoursesEndpoint)
    public void selectAllCourses() throws MalformedURLException, URISyntaxException {
        initPort("selectAllCourses()");
        ResponseEntity<List<Course>> responseEntity = restTemplate.exchange(stringApiAddress, HttpMethod.GET, null, new ParameterizedTypeReference<List<Course>>(){});
        List<Course> listOfCourses = this.secureCourseService.selectAllCourses();
        assertNotEquals(listOfCourses, responseEntity.getBody());
        assertEquals(getCourseListAsJsonString(listOfCourses), getCourseListAsJsonString(responseEntity.getBody()));
    }

    @Test
    @Order(13)
    @DisplayName("selectCourseString() endpoint: " + selectCourseStringEndpoint)
    public void selectCourseString() throws MalformedURLException, URISyntaxException {
        selectCourse( CourseController.selectCourseAsStringMapping );
    }

    @Test
    @Order(14)
    @DisplayName("selectCourseUUID() endpoint: " + selectCourseUUIDEndpoint)
    public void selectCourseUUID() throws MalformedURLException, URISyntaxException {
        selectCourse( CourseController.selectCourseAsUUIDMapping );
    }

    @Test
    @Order(15)
    @DisplayName("deleteCourse() endpoint: " + deleteCourseEndpoint)
    public void deleteCourse() throws MalformedURLException, URISyntaxException {
        initPort("deleteCourse()");
        List<Course> listOfCourses = secureCourseService.selectAllCourses();
        Course course = listOfCourses.get(listOfCourses.size()-1);
        String endpointSuffix = CourseController.deleteCourseMapping.replaceFirst("\\{.*\\}", course.getId().toString());
        restTemplate.delete(url + endpointSuffix, String.class);
        listOfCourses = secureCourseService.selectAllCourses();
        assertFalse(listOfCourses.contains(course));
    }

    @Test
    @Order(16)
    @DisplayName("updateCourse() endpoint: " + updateCourseEndpoint)
    public void updateCourse() throws MalformedURLException, URISyntaxException {
        initPort("updateCourse()");
        List<Course> listOfCourses = secureCourseService.selectAllCourses();
        Course course = listOfCourses.get(listOfCourses.size()-1);
        Course modifiedCourse = new Course(course.getId(), courseName+2);
        String endpointSuffix = CourseController.updateCourseCourseMapping.replaceFirst("\\{.*\\}", modifiedCourse.getId().toString());
        HttpEntity<Course> responseEntityModifiedCourse = new HttpEntity<Course>(modifiedCourse);
        ResponseEntity<Integer> responseEntityInt = restTemplate.exchange(stringApiAddress + endpointSuffix, HttpMethod.PUT, responseEntityModifiedCourse, Integer.class);
        Course selectedCourse = secureCourseService.selectCourse(course.getId()).orElse(null);
        assertNotNull(selectedCourse);
        assertEquals(selectedCourse.getId(), course.getId());
        assertNotEquals(selectedCourse.getName(), course.getName());
        assertEquals(modifiedCourse.toString(), selectedCourse.toString());
    }

    private void initPort(String callingFunction) throws URISyntaxException, MalformedURLException {
        // @TestPropertySource(properties = "server.port=8080")
        if(this.port!=8080) {
            System.out.println("{\"location\": \"" + callingFunction + "\"} port != " + expectedPort + ", port == " + port);
            if(callingFunction.compareTo("SecureCourseControllerTest()")!=0) {
                System.out.println("port = " + expectedPort);
                this.port = expectedPort;
                assertNotEquals(0, port);
                assertEquals(expectedPort, port);
            }
        }
        stringApiAddress = "http://localhost:" + port + "/secureApi/courses";
        url = new URL(stringApiAddress);
    }

    private String getCourseListAsJsonString(List<Course> listOfCourses) {
        Gson gson = new Gson();
        return gson.toJson( listOfCourses );
    }

    private void selectCourse(String courseControllerSelectCourseAsXXXXMapping) throws MalformedURLException, URISyntaxException {
        initPort("selectCourse() - " + courseControllerSelectCourseAsXXXXMapping);
        List<Course> listOfCourses = this.secureCourseService.selectAllCourses();
        assertNotNull(listOfCourses);
        assertFalse(listOfCourses.isEmpty());
        Course course = listOfCourses.get(listOfCourses.size()-1);
        String endpointSuffix = courseControllerSelectCourseAsXXXXMapping.replaceFirst("\\{.*\\}", course.getId().toString());
        Course responseCourse = restTemplate.getForObject(url + endpointSuffix, Course.class);
        assertNotNull(responseCourse);
        assertEquals(course.toString(), responseCourse.toString());
    }
}
