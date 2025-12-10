package model.statements;

import model.PrgState;
import exceptions.MyException;
import model.adt.MyIDictionary;
import model.types.Type;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;
    String toString();
    IStmt deepCopy();
    MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}
