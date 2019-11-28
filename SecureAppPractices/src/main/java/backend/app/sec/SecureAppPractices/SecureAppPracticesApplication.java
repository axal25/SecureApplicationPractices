package backend.app.sec.SecureAppPractices;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.Collections;

@SpringBootApplication
public class SecureAppPracticesApplication {

	public static void main(String[] args) {
		initApplication(args);
	}

	private static void initApplication(String[] args) {
		SpringApplication secureAppPractices = new SpringApplication(SecureAppPracticesApplication.class);
		secureAppPractices.run(args);
	}
}
