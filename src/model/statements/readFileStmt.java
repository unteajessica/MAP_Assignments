package model.statements;
import exceptions.FileException;
import exceptions.MyException;
import model.PrgState;
import model.adt.MyIDictionary;
import model.expressions.Exp;
import model.types.IntType;
import model.types.StringType;
import model.types.Type;
import model.values.IntValue;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class readFileStmt implements IStmt {
    private final Exp exp;
    private final String var_name;

    public readFileStmt(Exp e, String var_name) {
        this.exp = e;
        this.var_name = var_name;
    }

    @Override
    public String toString() {
        return "readFile(" + exp.toString() + ", " + var_name + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();

        if (!symTable.isDefined(var_name)) {
            throw new FileException();
        }

        if (!symTable.lookup(var_name).getType().equals(new IntType())) {
            throw new FileException();
        }

        Value fileNameVal = exp.eval(symTable);
        if (!fileNameVal.getType().equals(new StringType())) {
            throw new FileException();
        }

        String fileName = ((StringValue) fileNameVal).getVal();

        if (!state.getFileTable().isDefined(fileName)) {
            throw new FileException();
        }

        // read line from file
        BufferedReader fileDescriptor = state.getFileTable().lookup(fileName);
        if (fileDescriptor == null) {
            throw new FileException();
        }

        int value;
        String line;
        try {
            line = fileDescriptor.readLine();
            if (line == null || line.trim().isEmpty()) {
                value = 0;
            }
            else {
                value = Integer.parseInt(line);
            }
        } catch (IOException e) {
            throw new FileException();
        }

        symTable.update(var_name, new IntValue(value));

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new readFileStmt(exp, var_name);
    }
}
