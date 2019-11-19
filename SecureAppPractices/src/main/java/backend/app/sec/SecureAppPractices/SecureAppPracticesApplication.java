package backend.app.sec.SecureAppPractices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecureAppPracticesApplication {

	public static void main(String[] args) {
		System.out.println("To run .jar file: \n" +
				"1. right-click \"target\" folder \n" +
				"2. open in terminal \n" +
				"cd ~/IdeaProjects/SecureAppPractices/target$ \t" +
				"3. java -jar SecureAppPractices-0.0.1-SNAPSHOT.jar \n");
		SpringApplication.run(SecureAppPracticesApplication.class, args);
	}

}
