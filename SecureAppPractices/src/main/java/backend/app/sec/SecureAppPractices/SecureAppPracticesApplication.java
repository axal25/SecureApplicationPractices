package backend.app.sec.SecureAppPractices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecureAppPracticesApplication {

	public static void main(String[] args) {
		System.out.println("Finished fully tutorial: https://www.youtube.com/watch?v=vtPkZShrvXQ&t=5580 \n");
		System.out.println("Finished tutorial for Docker/PostgreSQL at 1:08:40: https://www.youtube.com/watch?v=aHbE3pTyG-Q&t=1193 \n");
		System.out.println("To run .jar file: \n" +
				"1. right-click \"target\" folder \n" +
				"2. open in terminal \n" +
				"~/IdeaProjects/SecureAppPractices/target$ \t" +
				"java -jar SecureAppPractices-0.0.1-SNAPSHOT.jar \n");
		SpringApplication.run(SecureAppPracticesApplication.class, args);
	}

}
