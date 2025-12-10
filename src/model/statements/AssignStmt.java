package model.statements;

import exceptions.TypeMismatch;
import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expressions.Exp;
import model.types.Type;
import model.values.Value;

public class AssignStmt implements IStmt {

    private final String id;
    private final Exp exp;

    public AssignStmt(String i, Exp e) {
        id = i;
        exp = e;
    }

    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }

    @Override
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
        // if not defined, do nothing
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(id, exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeVar = typeEnv.lookup(id);
        Type typeExp = exp.typeCheck(typeEnv);
        if (typeVar.equals(typeExp)) {
            return typeEnv;
        }
        else {
            throw new TypeMismatch();
        }
    }
}
