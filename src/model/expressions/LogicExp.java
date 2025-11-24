package model.expressions;

import exceptions.OperandNotBoolean;
import exceptions.OperandNotInteger;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.*;
import model.values.Value;
import model.values.BoolValue;
import exceptions.MyException;

public class LogicExp implements Exp {
    Exp e1;
    Exp e2;
    int op; // 1-and, 2-or

    public LogicExp(Exp e1, Exp e2, int o) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = o;
    }

    public String toString() {
        char opChar;
        if (op == 1) opChar = '&';
        else opChar = '|';
        return "(" + e1.toString() + opChar + e2.toString() + ")";
    }

    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) throws MyException {
        Value v1, v2;
        v1 = e1.eval(tbl, heap);
        if (v1.getType().equals(new BoolType())) {
            v2 = e2.eval(tbl, heap);
            if (v2.getType().equals(new BoolType())) {
                BoolValue b1 = (BoolValue)v1;
                BoolValue b2 = (BoolValue)v2;
                boolean n1, n2;
                n1 = b1.getVal();
                n2 = b2.getVal();
                if (op == 1) return new BoolValue(n1 && n2);
                else if (op == 2) return new BoolValue(n1 || n2);
            } else {
                throw new OperandNotBoolean();
            }
        } else {
            throw new OperandNotInteger();
        }
        return null; // Unreachable, but required for compilation
    }

    @Override
    public Exp deepCopy() {
        return new LogicExp(e1.deepCopy(), e2.deepCopy(), op);
    }
}
