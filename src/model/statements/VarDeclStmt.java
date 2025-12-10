package model.statements;

import exceptions.MyException;
import exceptions.VariableAlreadyDefined;
import model.PrgState;
import model.adt.MyIDictionary;
import model.types.Type;
import model.values.Value;

public class VarDeclStmt implements IStmt {

    private final String name;
    private final Type type;

    public VarDeclStmt(String n, Type t) {
        name = n;
        type = t;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();

        if (symTable.isDefined(name)) {
            throw new VariableAlreadyDefined(name);
        }

        symTable.put(name, type.defaultValue());
        return null;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(name, type);
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.put(name, type);
        return typeEnv;
    }
}
