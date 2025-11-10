package exceptions;

public class FileException extends RuntimeException {
    public FileException() {
        super("File exception occurred.");
    }
}
