package ua.org.training.library.exceptions;

public class ConnectionDBException extends Exception {
    private final String message;

    public ConnectionDBException(String message) {
        this.message = message;
    }

    public ConnectionDBException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ConnectionDBException{" +
                "message='" + message + '\'' +
                '}';
    }
}
