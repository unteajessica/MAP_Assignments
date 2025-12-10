package model.statements;

import exceptions.*;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.expressions.Exp;
import model.types.RefType;
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

        // 1. var must exist
        if (!symTable.isDefined(varName)) {
            throw new VariableNotDefined();
        }

        Value varValue = symTable.lookup(varName);

        // 2. var must be a RefValue
        if (!(varValue instanceof RefValue)) {
            throw new ValueTypeError();
        }

        RefValue refValue = (RefValue) varValue;
        int address = refValue.getAddress();

        // 3. address must exist in heap
        if (!heap.containsKey(address)) {
            throw new AddressNotDefined();
        }

        // 4. evaluate expression
        Value evaluated = exp.eval(symTable, heap);

        // 5. types must match
        Type locationType = refValue.getLocationType();
        if (!evaluated.getType().equals(locationType)) {
            throw new TypeMismatch();
        }

        // 6. update heap
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

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type varType = typeEnv.lookup(varName);
        Type expType = exp.typeCheck(typeEnv);

        if (varType.equals(new RefType(expType))) {
            return typeEnv;
        } else {
            throw new TypeMismatch();
        }
    }
}
