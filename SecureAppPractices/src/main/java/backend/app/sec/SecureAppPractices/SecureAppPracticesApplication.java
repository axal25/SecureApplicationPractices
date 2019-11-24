package backend.app.sec.SecureAppPractices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecureAppPracticesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureAppPracticesApplication.class, args);
		System.out.println("Heroku: https://secure-app-practices.herokuapp.com/ | https://git.heroku.com/secure-app-practices.git");
	}

}
