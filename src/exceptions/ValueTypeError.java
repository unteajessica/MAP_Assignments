package exceptions;

public class ValueTypeError extends RuntimeException {
    public ValueTypeError() {
        super("Value type error!");
    }
}
