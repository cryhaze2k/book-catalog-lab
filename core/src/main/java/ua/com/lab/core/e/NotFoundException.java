package ua.com.lab.core.e;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}