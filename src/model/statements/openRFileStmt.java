package model.statements;

import exceptions.FileException;
import exceptions.MyException;
import exceptions.TypeMismatch;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIFileTable;
import model.expressions.Exp;
import model.types.StringType;
import model.types.Type;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class openRFileStmt implements IStmt{

    private final Exp exp;

    public openRFileStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "openRFile(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIFileTable fileTable = state.getFileTable();

        Value val = exp.eval(symTable, state.getHeap());
        if (!val.getType().equals(new StringType())) {
            throw new FileException();
        }

        String fileName = ((StringValue) val).getVal();

        if (fileTable.isDefined(fileName)) {
            throw new FileException(); // file already opened
        }

        // open file
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            fileTable.put(fileName, br);
        } catch (IOException e) {
            throw new FileException();
        }

        System.out.println("Opened file: " + fileName);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new openRFileStmt(exp);
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeExp = exp.typeCheck(typeEnv);
        if (typeExp.equals(new  StringType())) {
            return typeEnv;
        } else {
            throw new TypeMismatch();
        }
    }
}
