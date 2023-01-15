package ua.org.training.library.exceptions;

public class ControllerException extends RuntimeException {
    private final String message;

    public ControllerException(String message) {
        this.message = message;
    }

    public ControllerException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ControllerException{" +
                "message='" + message + '\'' +
                '}';
    }
}
