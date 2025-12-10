package exceptions;

public class ConditionNotBoolean extends MyException {
    public ConditionNotBoolean() {
        super("The condition does not have type Bool.");
    }
}
