package backend.app.sec.SecureAppPractices.configuration.datasource.properties;

public class BadDatabaseLocation extends Exception {
    static final String throwableMsg = "You need to set CustomDataSourceProperties this.databaseLocation to \"localhost\" or \"gcp\". this.databaseLocation: ";
    String callingClassName;
    String callingFunctionName;
    String databaseLocation;

    BadDatabaseLocation(String callingClassName, String callingFunctionName, String databaseLocation) {
        super(callingClassName + " >>> " + callingFunctionName + " >>> " + BadDatabaseLocation.class.getName() + ": " + "\n\r",
                new Throwable(throwableMsg)
        );
        this.callingClassName = callingClassName;
        this.callingFunctionName = callingFunctionName;
        this.databaseLocation = databaseLocation;
    }
}
