package model.statements;

import exceptions.FileException;
import exceptions.MyException;
import exceptions.TypeMismatch;
import exceptions.ValueTypeError;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIFileTable;
import model.expressions.Exp;
import model.types.StringType;
import model.types.Type;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class closeRFileStmt implements IStmt {

    private final Exp exp;

    public closeRFileStmt(Exp e) {
        this.exp = e;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        Value val = exp.eval(symTable, state.getHeap());

        // expression must evaluate to a string (filename)
        if (!val.getType().equals(new StringType())) {
            throw new ValueTypeError();
        }

        MyIFileTable fileTable = state.getFileTable();
        String fileName = ((StringValue) val).getVal();

        // file must be already opened
        if (!fileTable.isDefined(fileName)) {
            throw new FileException();
        }

        BufferedReader fileDescriptor = fileTable.lookup(fileName);
        try {
            fileDescriptor.close();
        } catch (IOException e) {
            throw new FileException();
        }

        // remove the file from the file table
        fileTable.remove(fileName);

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new closeRFileStmt(exp);
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeExp = exp.typeCheck(typeEnv);
        if (typeExp.equals(new StringType())) {
            return typeEnv;
        } else {
            throw new TypeMismatch();
        }
    }
}
