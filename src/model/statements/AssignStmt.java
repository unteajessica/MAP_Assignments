package model.statements;

import exceptions.TypeMismatch;
import model.PrgState;
import model.adt.*;
import model.expressions.*;
import model.values.*;
import model.types.*;
import exceptions.MyException;

public class AssignStmt implements IStmt {
    private final String id;
    private final Exp exp;

    public AssignStmt(String i, Exp e) {
        id = i;
        exp = e;
    }

    public String toString() {
        return id + "=" + exp.toString();
    }

    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();

        if (symTable.isDefined(id)) {
            Value val = exp.eval(symTable, state.getHeap());
            if (!val.getType().equals(symTable.lookup(id).getType())) {
                throw new TypeMismatch();
            }
            else {
                symTable.update(id, val);
            }
        }

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(id, exp.deepCopy());
    }
}
