package model.statements;

import exceptions.MyException;
import exceptions.TypeMismatch;
import exceptions.ValueTypeError;
import exceptions.VariableNotDefined;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.expressions.Exp;
import model.types.RefType;
import model.values.RefValue;
import model.values.Value;

public class NewStmt implements IStmt {
    private final String varName;
    private final Exp exp;

    public NewStmt(String varName, Exp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) {
        // Implementation goes here
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();

        // 1. var must exist in SymTable
        if (!symTable.isDefined(varName))
            throw new VariableNotDefined();

        Value varValue = symTable.lookup(varName);

        // 2. var must have RefType
        if (!(varValue.getType() instanceof RefType))
            throw new ValueTypeError();

        RefType refType = (RefType) varValue.getType();

        // 3. Evaluate expression
        Value evaluated = exp.eval(symTable, heap);

        // 4. evaluated type must match inner type of RefType
        if (!evaluated.getType().equals(refType.getInner()))
            throw new TypeMismatch();

        // 5. Allocate new address on heap
        int newAddress = heap.allocateAddress(evaluated);

        // 6. Update var in SymTable
        symTable.update(varName, new RefValue(newAddress, refType.getInner()));

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(varName, exp.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + exp.toString() + ")";
    }
}
