package model.expressions;

import exceptions.AddressNotDefined;
import exceptions.MyException;
import exceptions.ValueTypeError;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.RefType;
import model.types.Type;
import model.values.RefValue;
import model.values.Value;

public class ReadHeapExp implements Exp {

    private final Exp exp;

    public ReadHeapExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) throws MyException {
        // 1. evaluate inner expression
        Value val = exp.eval(tbl, heap);

        // 2. must be a reference
        if (!(val instanceof RefValue)) {
            throw new ValueTypeError();
        }

        RefValue refVal = (RefValue) val;
        int address = refVal.getAddress();

        // 3. address must exist in heap
        if (!heap.containsKey(address)) {
            throw new AddressNotDefined();
        }

        // 4. return stored value from heap
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

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t = exp.typeCheck(typeEnv);
        if (t instanceof RefType) {
            RefType refType = (RefType) t;
            return refType.getInner();
        } else {
            throw new ValueTypeError();
        }
    }
}
