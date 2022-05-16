package planner.exception;

public class LeonAccessException extends RuntimeException {
    public LeonAccessException(String message) {
        super(message);
    }

    public LeonAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
