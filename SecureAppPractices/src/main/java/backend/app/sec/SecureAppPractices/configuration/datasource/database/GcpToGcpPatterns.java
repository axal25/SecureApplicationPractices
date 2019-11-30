package backend.app.sec.SecureAppPractices.configuration.datasource.database;

public class GcpToGcpPatterns {
    public static final String gcpPattern_GAE_ENV = "standard";
    public static final String gcpPattern_GAE_SERIVCE = "default";
    public static final String gcpPattern_GAE_RUNTIME = "java11";

    /**
     { "GAE_ENV": "standard" },                                                                 // 5/5
     { "GAE_SERVICE": "default" },                                                              // 5/5
     { "GAE_RUNTIME": "java11" },                                                               // 5/5
     { "GAE_APPLICATION": "e~braided-tracker-259922" },                                         // 4/5
     { "GOOGLE_CLOUD_PROJECT": "braided-tracker-259922" },                                      // 4/5
     { "GAE_VERSION": "secure-app-practices-version-from-pom-xml" }, // dependant on to pom.xml -> not constant
     // use to be:
     { "GAE_VERSION": "springboot-helloworld" },                                                // ?/?
     { "GAE_DEPLOYMENT_ID": "422777054906127631" }, // NOT CONSTANT                             // ?/?
     { "PORT": "8081" }, // NOT CONSTANT                                                        // ?/?
     **/
}
