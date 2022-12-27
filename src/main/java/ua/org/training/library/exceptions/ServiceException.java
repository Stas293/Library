package ua.org.training.library.exceptions;

public class ServiceException extends Exception {
    private String message;

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "message='" + message + '\'' +
                '}';
    }
}
