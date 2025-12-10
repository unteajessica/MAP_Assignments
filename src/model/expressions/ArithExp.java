package model.expressions;

import exceptions.DivisionByZero;
import exceptions.MyException;
import exceptions.OperandNotInteger;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.IntType;
import model.types.Type;
import model.values.IntValue;
import model.values.Value;

public class ArithExp implements Exp {

    private final Exp left;
    private final Exp right;
    private final int op; // 1-plus, 2-minus, 3-multiplication, 4-div

    public ArithExp(int o, Exp l, Exp r) {
        left = l;
        right = r;
        op = o;
    }

    @Override
    public String toString() {
        char opChar;
        if (op == 1) opChar = '+';
        else if (op == 2) opChar = '-';
        else if (op == 3) opChar = '*';
        else opChar = '/';
        return "(" + left.toString() + opChar + right.toString() + ")";
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) throws MyException {
        Value v1 = left.eval(tbl, heap);

        if (v1.getType().equals(new IntType())) {
            Value v2 = right.eval(tbl, heap);
            if (v2.getType().equals(new IntType())) {
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                int n1 = i1.getVal();
                int n2 = i2.getVal();

                if (op == 1) return new IntValue(n1 + n2);
                if (op == 2) return new IntValue(n1 - n2);
                if (op == 3) return new IntValue(n1 * n2);
                if (op == 4) {
                    if (n2 == 0) {
                        throw new DivisionByZero();
                    } else {
                        return new IntValue(n1 / n2);
                    }
                }
            } else {
                throw new OperandNotInteger();
            }
        } else {
            throw new OperandNotInteger();
        }

        return null; // formally needed; unreachable if logic is correct
    }

    @Override
    public Exp deepCopy() {
        return new ArithExp(op, left.deepCopy(), right.deepCopy());
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1, t2;
        t1 = left.typeCheck(typeEnv);
        t2 = right.typeCheck(typeEnv);
        if (t1.equals(new IntType())) {
            if (t2.equals(new IntType())) {
                return new IntType();
            } else {
                throw new OperandNotInteger();
            }
        } else {
            throw new OperandNotInteger();
        }
    }
}
