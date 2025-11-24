package exceptions;

public class VariableNotDefined extends MyException {
    public VariableNotDefined() {
        super("Variable not defined in the symbol table.");
    }
}
