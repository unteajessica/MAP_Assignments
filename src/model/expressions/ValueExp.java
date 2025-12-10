package model.expressions;

import exceptions.MyException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.Type;
import model.values.Value;

public class ValueExp implements Exp {

    private final Value val;

    public ValueExp(Value v) {
        val = v;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) throws MyException {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public Exp deepCopy() {
        return new ValueExp(val);
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return val.getType();
    }
}
