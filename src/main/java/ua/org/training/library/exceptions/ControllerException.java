package ua.org.training.library.exceptions;

public class ControllerException extends RuntimeException {
    String message;

    public ControllerException(String message) {
        this.message = message;
    }

    public ControllerException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
