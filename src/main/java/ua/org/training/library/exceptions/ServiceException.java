package ua.org.training.library.exceptions;

public class ServiceException extends Exception {
    private final String message;

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "message='" + message + '\'' +
                '}';
    }
}
