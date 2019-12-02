package backend.app.sec.SecureAppPractices;

import backend.app.sec.SecureAppPractices.api.SecureCourseControllerTest;
import backend.app.sec.SecureAppPractices.api.UnSecureCourseControllerTest;
import backend.app.sec.SecureAppPractices.service.SecureCourseServiceTest;
import backend.app.sec.SecureAppPractices.service.UnSecureCourseServiceTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
	void secureCourseServiceTest_databaseRepeatableInitiation() { this.secureCourseServiceTest.databaseRepeatableInitiation(); }

	@Test
	@Order(2)
	void secureCourseServiceTest_selectCourseByStringIdAndByUuidId() throws Exception { this.secureCourseServiceTest.selectCourseByStringIdAndByUuidId(); }

	@Test
	@Order(3)
	void secureCourseServiceTest_selectCourseByStringIdAndByUuidIdException() throws Exception { this.secureCourseServiceTest.selectCourseByStringIdAndByUuidIdException(); }

	@Test
	@Order(4)
	void secureCourseServiceTest_insertCourse() throws Exception { this.secureCourseServiceTest.insertCourse(); }

	@Test
	@Order(5)
	void secureCourseServiceTest_deleteCourse() throws Exception { this.secureCourseServiceTest.deleteCourse(); }

	@Test
	@Order(6)
	void secureCourseServiceTest_updateCourse() throws Exception { this.secureCourseServiceTest.updateCourse(); }

	@Test
	@Order(11)
	public void unSecureCourseServiceTest_databaseRepeatableInitiation() { this.unSecureCourseServiceTest.databaseRepeatableInitiation(); }

	@Test
	@Order(12)
	public void unSecureCourseServiceTest_selectCourseByStringIdAndByUuidId() throws Exception { this.unSecureCourseServiceTest.selectCourseByStringIdAndByUuidId(); }

	@Test
	@Order(13)
	public void unSecureCourseServiceTest_selectCourseByStringIdAndByUuidIdException() throws Exception { this.unSecureCourseServiceTest.selectCourseByStringIdAndByUuidIdException(); }

	@Test
	@Order(14)
	public void unSecureCourseServiceTest_insertCourse() throws Exception { this.unSecureCourseServiceTest.insertCourse(); }

	@Test
	@Order(15)
	public void unSecureCourseServiceTest_deleteCourse() throws Exception { this.unSecureCourseServiceTest.deleteCourse(); }

	@Test
	@Order(16)
	public void unSecureCourseServiceTest_updateCourse() throws Exception { this.unSecureCourseServiceTest.updateCourse(); }

	@Test
	@Order(21)
	public void secureCourseControllerTest_insertCourse() { this.secureCourseControllerTest.insertCourse(); }

	@Test
	@Order(22)
	public void secureCourseControllerTest_selectAllCourses() { this.secureCourseControllerTest.selectAllCourses(); }

	@Test
	@Order(23)
	public void secureCourseControllerTest_selectCourseString() throws Exception { this.secureCourseControllerTest.selectCourseString(); }

	@Test
	@Order(24)
	public void secureCourseControllerTest_selectCourseUUID() throws Exception { this.secureCourseControllerTest.selectCourseUUID(); }

	@Test
	@Order(25)
	public void secureCourseControllerTest_deleteCourse() { this.secureCourseControllerTest.deleteCourse(); }

	@Test
	@Order(26)
	public void secureCourseControllerTest_updateCourse() throws Exception { this.secureCourseControllerTest.updateCourse(); }

	@Test
	@Order(31)
	public void unSecureCourseControllerTest_insertCourse() {
		this.unSecureCourseControllerTest.insertCourse();
	}

	@Test
	@Order(32)
	public void unSecureCourseControllerTest_selectAllCourses() {
		this.unSecureCourseControllerTest.selectAllCourses();
	}

	@Test
	@Order(33)
	public void unSecureCourseControllerTest_selectCourseString() throws Exception {
		this.unSecureCourseControllerTest.selectCourseString();
	}

	@Test
	@Order(34)
	public void unSecureCourseControllerTest_selectCourseUUID() throws Exception {
		this.unSecureCourseControllerTest.selectCourseUUID();
	}

	@Test
	@Order(35)
	public void unSecureCourseControllerTest_deleteCourse() {
		this.unSecureCourseControllerTest.deleteCourse();
	}

	@Test
	@Order(36)
	public void unSecureCourseControllerTest_updateCourse() throws Exception {
		this.unSecureCourseControllerTest.updateCourse();
	}
}
