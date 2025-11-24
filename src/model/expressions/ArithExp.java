package model.expressions;

import exceptions.DivisionByZero;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.IntType;
import model.values.IntValue;
import model.values.Value;
import exceptions.MyException;
import exceptions.OperandNotInteger;

public class ArithExp implements Exp {
    Exp left;
    Exp right;
    int op; // 1-plus, 2-minus, 3-mult, 4-div

    public ArithExp(int o, Exp l, Exp r) {
        left = l;
        right = r;
        op = o;
    }

    public String toString() {
        char opChar;
        if (op == 1) opChar = '+';
        else if (op == 2) opChar = '-';
        else if (op == 3) opChar = '*';
        else opChar = '/';
        return "(" + left.toString() + opChar + right.toString() + ")";
    }

    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) throws MyException {
        Value v1 = left.eval(tbl, heap);
        Value v2;
        if (v1.getType().equals(new IntType())) {
            v2 = right.eval(tbl, heap);
            if (v2.getType().equals(new IntType())) {
                IntValue i1 = (IntValue)v1;
                IntValue i2 = (IntValue)v2;
                int n1, n2;
                n1 = i1.getVal();
                n2 = i2.getVal();
                if (op == 1) return new IntValue(n1 + n2);
                if (op == 2) return new IntValue(n1 - n2);
                if (op == 3) return new IntValue(n1 * n2);
                if (op == 4) {
                    if (n2 == 0) throw new DivisionByZero();
                    else return new IntValue(n1 / n2);
                }
            }
            else
                throw new OperandNotInteger();
        }
        else
            throw new OperandNotInteger();

        return null;
    }

    @Override
    public Exp deepCopy() {
        return new ArithExp(op, left.deepCopy(), right.deepCopy());
    }
}
