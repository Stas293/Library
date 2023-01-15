package ua.org.training.library.exceptions;

public class LoadFieldsException extends RuntimeException {
    private final String message;

    public LoadFieldsException(String message) {
        this.message = message;
    }

    public LoadFieldsException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "LoadFieldsException{" +
                "message='" + message + '\'' +
                '}';
    }
}
