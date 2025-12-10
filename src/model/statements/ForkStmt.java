package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.*;
import model.types.Type;
import model.values.Value;

public class ForkStmt implements IStmt {

    private final IStmt stmt; // body of the fork stmt

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        // new stack containing only stmt
        MyIStack<IStmt> newStack = new MyStack<>();
        newStack.push(stmt);

        // clone symbol table (separate copy for the new thread)
        MyIDictionary<String, Value> newSymTable = state.getSymTable().deepCopy();

        // shared structures: heap, file table, output list
        MyIList<Value> out = state.getOut();
        MyIFileTable fileTable = state.getFileTable();
        MyIHeap heap = state.getHeap();

        // create and return new program state
        return new PrgState(newStack, newSymTable, out, fileTable, heap);
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(stmt.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + stmt.toString() + ")";
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        stmt.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
