package model.statements;

import exceptions.MyException;
import exceptions.TypeMismatch;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.expressions.Exp;
import model.values.BoolValue;
import model.values.Value;

public class WhileStmt implements IStmt {
    private final Exp condition;
    private final IStmt body;

    public WhileStmt(Exp condition, IStmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();

        Value condValue = condition.eval(symTable, heap);

        if (!(condValue instanceof BoolValue)) {
            throw new TypeMismatch();
        }

        BoolValue boolCond = (BoolValue) condValue;

        if (boolCond.getVal()) {
            state.getStack().push(this);
            state.getStack().push(body);
        }

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(condition.deepCopy(), body.deepCopy());
    }

    @Override
    public String toString() {
        return "while(" + condition.toString() + ") { " + body.toString() + " }";
    }
}
