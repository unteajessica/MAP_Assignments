package model.expressions;

import exceptions.MyException;
import exceptions.OperandNotBoolean;
import exceptions.OperandNotInteger;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.BoolType;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

public class LogicExp implements Exp {

    private final Exp e1;
    private final Exp e2;
    private final int op; // 1-and, 2-or

    public LogicExp(Exp e1, Exp e2, int o) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = o;
    }

    @Override
    public String toString() {
        char opChar = (op == 1) ? '&' : '|';
        return "(" + e1.toString() + opChar + e2.toString() + ")";
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) throws MyException {
        Value v1 = e1.eval(tbl, heap);

        if (v1.getType().equals(new BoolType())) {
            Value v2 = e2.eval(tbl, heap);
            if (v2.getType().equals(new BoolType())) {
                BoolValue b1 = (BoolValue) v1;
                BoolValue b2 = (BoolValue) v2;
                boolean n1 = b1.getVal();
                boolean n2 = b2.getVal();

                if (op == 1) {
                    return new BoolValue(n1 && n2);
                } else if (op == 2) {
                    return new BoolValue(n1 || n2);
                }
            } else {
                throw new OperandNotBoolean();
            }
        } else {
            // (likely meant OperandNotBoolean, but we keep your original logic)
            throw new OperandNotInteger();
        }

        return null;
    }

    @Override
    public Exp deepCopy() {
        return new LogicExp(e1.deepCopy(), e2.deepCopy(), op);
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1, t2;
        t1 = e1.typeCheck(typeEnv);
        t2 = e2.typeCheck(typeEnv);
        if (t1.equals(new BoolType())) {
            if (t2.equals(new BoolType())) {
                return new BoolType();
            } else {
                throw new OperandNotBoolean();
            }
        } else {
            throw new OperandNotBoolean();
        }
    }
}
