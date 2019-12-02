package backend.app.sec.SecureAppPractices.service;

import backend.app.sec.SecureAppPractices.model.Course;

public interface FunctionalInterface {
    Course executeThrowsException() throws Exception;
}
