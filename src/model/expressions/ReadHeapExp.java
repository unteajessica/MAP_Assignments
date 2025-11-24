package model.expressions;

import exceptions.AddressNotDefined;
import exceptions.MyException;
import exceptions.ValueTypeError;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.values.RefValue;
import model.values.Value;

public class ReadHeapExp implements Exp {
    private final Exp exp;

    public ReadHeapExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) throws MyException {
        // eval exp
        Value val = exp.eval(tbl, heap);
        // must be a RefValue
        if (!(val instanceof RefValue)) {
            throw new ValueTypeError();
        }

        RefValue refVal = (RefValue) val;
        int address = refVal.getAddress();

        // check if address is defined in heap
        if (!heap.containsKey(address)) {
            throw new AddressNotDefined();
        }
        return heap.get(address);
    }

    @Override
    public Exp deepCopy() {
        return new ReadHeapExp(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "rH(" + exp.toString() + ")";
    }
}
