package model.expressions;

import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.values.Value;
import exceptions.MyException;

public class VarExp implements Exp {
    private final String id;

    public VarExp(String i) { id = i; }

    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) throws MyException {
        return tbl.lookup(id);
    }

    public String toString() { return id; }

    @Override
    public Exp deepCopy() {
        return new VarExp(id);
    }
}
