package model.statements;

import exceptions.*;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.expressions.Exp;
import model.types.Type;
import model.values.RefValue;
import model.values.Value;

public class WriteHeapStmt implements IStmt {
    private final String varName;
    private final Exp exp;

    public WriteHeapStmt(String varName, Exp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();

        // var must exist
        if (!symTable.isDefined(varName)) {
            throw new VariableNotDefined();
        }

        Value varValue = symTable.lookup(varName);

        // var must be a RefValue
        if (!(varValue instanceof RefValue)) {
            throw new ValueTypeError();
        }

        RefValue refValue = (RefValue) varValue;
        int address = refValue.getAddress();

        // address must exist in heap
        if (!heap.containsKey(address)) {
            throw new AddressNotDefined();
        }

        // evaluate expression
        Value evaluated = exp.eval(symTable, heap);

        // types must match
        Type locationType = refValue.getLocationType();
        if (!evaluated.getType().equals(locationType)) {
            throw new TypeMismatch();
        }

        // update heap
        heap.update(address, evaluated);

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WriteHeapStmt(varName, exp.deepCopy());
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + exp.toString() + ")";
    }
}
