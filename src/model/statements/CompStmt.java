package model.statements;

import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIStack;
import model.types.Type;

public class CompStmt implements IStmt {

    private final IStmt first;
    private final IStmt second;

    public CompStmt(IStmt s1, IStmt s2) {
        first = s1;
        second = s2;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ";" + second.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stack = state.getStack();
        // push second first, so that first is on the top of the stack
        stack.push(second);
        stack.push(first);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CompStmt(first.deepCopy(), second.deepCopy());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        MyIDictionary<String, Type> typeEnv1 = first.typeCheck(typeEnv);
        return second.typeCheck(typeEnv1);
    }
}
