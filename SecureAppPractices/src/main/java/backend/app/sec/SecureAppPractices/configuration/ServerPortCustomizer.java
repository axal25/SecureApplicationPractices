package backend.app.sec.SecureAppPractices.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ServerPortCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    private static final int defaultPort = 8080;
    @Getter
    private final Environment env;
    @Getter
    private final int applicationListeningPort;

    @Autowired
    ServerPortCustomizer(Environment env) {
        this.env = env;
        this.applicationListeningPort = determineApplicationListeningPort();
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(this.applicationListeningPort);
    }

    private int determineApplicationListeningPort() {
        int applicationListeningPort;
        try {
            applicationListeningPort = getApplicationListeningPortFromEnvironment();
        }
        catch(Exception e) {
            applicationListeningPort = ServerPortCustomizer.defaultPort;
        }
        return applicationListeningPort;
    }

    public int getApplicationListeningPortFromEnvironment() {
        if( env == null ) System.err.println("env = " + this.env);
        String applicationListeningPort = null;
        if( env != null ) applicationListeningPort = this.env.getProperty("PORT");
        else System.err.println("applicationListeningPort = " + applicationListeningPort);
        if( applicationListeningPort == null ) {
            applicationListeningPort = String.valueOf(defaultPort);
            System.err.println("applicationListeningPort = " + applicationListeningPort);
        }
        int intApplicationListeningPort = Integer.parseInt( applicationListeningPort );
        return intApplicationListeningPort;
    }
}
