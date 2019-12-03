package backend.app.sec.SecureAppPractices;

import backend.app.sec.SecureAppPractices.api.SecureCourseControllerTest;
import backend.app.sec.SecureAppPractices.api.UnSecureCourseControllerTest;
import backend.app.sec.SecureAppPractices.service.SecureCourseServiceTest;
import backend.app.sec.SecureAppPractices.service.UnSecureCourseServiceTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=8081")
class SecureAppPracticesApplicationTests {

	private final SecureCourseServiceTest secureCourseServiceTest;
	private final UnSecureCourseServiceTest unSecureCourseServiceTest;
	private final SecureCourseControllerTest secureCourseControllerTest;
	private final UnSecureCourseControllerTest unSecureCourseControllerTest;
	@Autowired
	SecureAppPracticesApplicationTests(
			SecureCourseServiceTest secureCourseServiceTest,
			UnSecureCourseServiceTest unSecureCourseServiceTest,
			SecureCourseControllerTest secureCourseControllerTest,
			UnSecureCourseControllerTest unSecureCourseControllerTest
	) {
		this.secureCourseServiceTest = secureCourseServiceTest;
		this.unSecureCourseServiceTest = unSecureCourseServiceTest;
		this.secureCourseControllerTest = secureCourseControllerTest;
		this.unSecureCourseControllerTest = unSecureCourseControllerTest;
	}

	@Test
	@Order(1)
	@DisplayName("SecureCourseServiceTest - databaseRepeatableInitiation")
	void secureCourseServiceTest_databaseRepeatableInitiation() {
		this.secureCourseServiceTest.databaseRepeatableInitiation();
	}

	@Test
	@Order(2)
	@DisplayName("SecureCourseServiceTest - selectCourseByStringIdAndByUuidId")
	void secureCourseServiceTest_selectCourseByStringIdAndByUuidId() throws Exception { this.secureCourseServiceTest.selectCourseByStringIdAndByUuidId(); }

	@Test
	@Order(3)
	@DisplayName("SecureCourseServiceTest - selectCourseByStringIdAndByUuidIdException")
	void secureCourseServiceTest_selectCourseByStringIdAndByUuidIdException() throws Exception { this.secureCourseServiceTest.selectCourseByStringIdAndByUuidIdException(); }

	@Test
	@Order(4)
	@DisplayName("SecureCourseServiceTest - insertCourse")
	void secureCourseServiceTest_insertCourse() throws Exception { this.secureCourseServiceTest.insertCourse(); }

	@Test
	@Order(5)
	@DisplayName("SecureCourseServiceTest - deleteCourse")
	void secureCourseServiceTest_deleteCourse() throws Exception { this.secureCourseServiceTest.deleteCourse(); }

	@Test
	@Order(6)
	@DisplayName("SecureCourseServiceTest - updateCourse")
	void secureCourseServiceTest_updateCourse() throws Exception { this.secureCourseServiceTest.updateCourse(); }

	@Test
	@Order(7)
	@DisplayName("SecureCourseServiceTest - runExecuteGetsNoResultsFromDatabase")
	void secureCourseServiceTest_runExecuteGetsNoResultsFromDatabase() throws Exception { this.secureCourseServiceTest.runExecuteGetsNoResultsFromDatabase(); }

	@Test
	@Order(8)
	@DisplayName("SecureCourseServiceTest - runQueryGetsResultsFromDatabase")
	void secureCourseServiceTest_runQueryGetsResultsFromDatabase() throws Exception { this.secureCourseServiceTest.runQueryGetsResultsFromDatabase(); }

	@Test
	@Order(11)
	@DisplayName("UnSecureCourseServiceTest - databaseRepeatableInitiation")
	public void unSecureCourseServiceTest_databaseRepeatableInitiation() { this.unSecureCourseServiceTest.databaseRepeatableInitiation(); }

	@Test
	@Order(12)
	@DisplayName("UnSecureCourseServiceTest - selectCourseByStringIdAndByUuidId")
	public void unSecureCourseServiceTest_selectCourseByStringIdAndByUuidId() throws Exception { this.unSecureCourseServiceTest.selectCourseByStringIdAndByUuidId(); }

	@Test
	@Order(13)
	@DisplayName("UnSecureCourseServiceTest - selectCourseByStringIdAndByUuidIdException")
	public void unSecureCourseServiceTest_selectCourseByStringIdAndByUuidIdException() throws Exception { this.unSecureCourseServiceTest.selectCourseByStringIdAndByUuidIdException(); }

	@Test
	@Order(14)
	@DisplayName("UnSecureCourseServiceTest - insertCourse")
	public void unSecureCourseServiceTest_insertCourse() throws Exception { this.unSecureCourseServiceTest.insertCourse(); }

	@Test
	@Order(15)
	@DisplayName("UnSecureCourseServiceTest - deleteCourse")
	public void unSecureCourseServiceTest_deleteCourse() throws Exception { this.unSecureCourseServiceTest.deleteCourse(); }

	@Test
	@Order(16)
	@DisplayName("UnSecureCourseServiceTest - updateCourse")
	public void unSecureCourseServiceTest_updateCourse() throws Exception { this.unSecureCourseServiceTest.updateCourse(); }

	@Test
	@Order(17)
	@DisplayName("UnSecureCourseServiceTest - runExecuteGetsNoResultsFromDatabase")
	void unSecureCourseServiceTest_runExecuteGetsNoResultsFromDatabase() throws Exception {
		this.unSecureCourseServiceTest.runExecuteGetsNoResultsFromDatabase();
	}

	@Test
	@Order(18)
	@DisplayName("UnSecureCourseServiceTest - runQueryGetsResultsFromDatabase")
	void unSecureCourseServiceTest_runQueryGetsResultsFromDatabase() throws Exception { this.unSecureCourseServiceTest.runQueryGetsResultsFromDatabase(); }

	@Test
	@Order(21)
	@DisplayName("SecureCourseControllerTest - insertCourse")
	public void secureCourseControllerTest_insertCourse() { this.secureCourseControllerTest.insertCourse(); }

	@Test
	@Order(22)
	@DisplayName("SecureCourseControllerTest - selectAllCourses")
	public void secureCourseControllerTest_selectAllCourses() { this.secureCourseControllerTest.selectAllCourses(); }

	@Test
	@Order(23)
	@DisplayName("SecureCourseControllerTest - selectCourseString")
	public void secureCourseControllerTest_selectCourseString() throws Exception { this.secureCourseControllerTest.selectCourseString(); }

	@Test
	@Order(24)
	@DisplayName("SecureCourseControllerTest - selectCourseUUID")
	public void secureCourseControllerTest_selectCourseUUID() throws Exception { this.secureCourseControllerTest.selectCourseUUID(); }

	@Test
	@Order(25)
	@DisplayName("SecureCourseControllerTest - deleteCourse")
	public void secureCourseControllerTest_deleteCourse() { this.secureCourseControllerTest.deleteCourse(); }

	@Test
	@Order(26)
	@DisplayName("SecureCourseControllerTest - updateCourse")
	public void secureCourseControllerTest_updateCourse() throws Exception { this.secureCourseControllerTest.updateCourse(); }

	@Test
	@Order(31)
	@DisplayName("UnSecureCourseControllerTest - insertCourse")
	public void unSecureCourseControllerTest_insertCourse() {
		this.unSecureCourseControllerTest.insertCourse();
	}

	@Test
	@Order(32)
	@DisplayName("UnSecureCourseControllerTest - selectAllCourses")
	public void unSecureCourseControllerTest_selectAllCourses() {
		this.unSecureCourseControllerTest.selectAllCourses();
	}

	@Test
	@Order(33)
	@DisplayName("UnSecureCourseControllerTest - selectCourseString")
	public void unSecureCourseControllerTest_selectCourseString() throws Exception {
		this.unSecureCourseControllerTest.selectCourseString();
	}

	@Test
	@Order(34)
	@DisplayName("UnSecureCourseControllerTest - selectCourseUUID")
	public void unSecureCourseControllerTest_selectCourseUUID() throws Exception {
		this.unSecureCourseControllerTest.selectCourseUUID();
	}

	@Test
	@Order(35)
	@DisplayName("UnSecureCourseControllerTest - deleteCourse")
	public void unSecureCourseControllerTest_deleteCourse() {
		this.unSecureCourseControllerTest.deleteCourse();
	}

	@Test
	@Order(36)
	@DisplayName("UnSecureCourseControllerTest - updateCourse")
	public void unSecureCourseControllerTest_updateCourse() throws Exception {
		this.unSecureCourseControllerTest.updateCourse();
	}
}
