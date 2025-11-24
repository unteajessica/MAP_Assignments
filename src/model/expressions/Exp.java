package model.expressions;
import exceptions.MyException;
import model.adt.MyIHeap;
import model.values.Value;
import model.adt.MyIDictionary;

public interface Exp {
    Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) throws MyException;
    Exp deepCopy();
}
