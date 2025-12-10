package model.expressions;

import exceptions.MyException;
import exceptions.OperandNotInteger;
import exceptions.UnknownOperator;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.BoolType;
import model.types.IntType;
import model.types.Type;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.Value;

public class RelationalExp implements Exp {

    private final Exp exp1;
    private final Exp exp2;
    private final String operator;  // "<", "<=", ">", ">=", "==", "!="

    public RelationalExp(Exp e1, Exp e2, String op) {
        exp1 = e1;
        exp2 = e2;
        operator = op;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> tbl, MyIHeap heap) throws MyException {
        Value v1 = exp1.eval(tbl, heap);
        Value v2 = exp2.eval(tbl, heap);

        if (!v1.getType().equals(new IntType())) {
            throw new OperandNotInteger();
        }
        if (!v2.getType().equals(new IntType())) {
            throw new OperandNotInteger();
        }

        int n1 = ((IntValue) v1).getVal();
        int n2 = ((IntValue) v2).getVal();

        return switch (operator) {
            case "<"  -> new BoolValue(n1 < n2);
            case "<=" -> new BoolValue(n1 <= n2);
            case "==" -> new BoolValue(n1 == n2);
            case "!=" -> new BoolValue(n1 != n2);
            case ">"  -> new BoolValue(n1 > n2);
            case ">=" -> new BoolValue(n1 >= n2);
            default   -> throw new UnknownOperator();
        };
    }

    @Override
    public Exp deepCopy() {
        return new RelationalExp(exp1.deepCopy(), exp2.deepCopy(), operator);
    }

    @Override
    public String toString() {
        return exp1.toString() + " " + operator + " " + exp2.toString();
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type t1, t2;
        t1 = exp1.typeCheck(typeEnv);
        t2 = exp2.typeCheck(typeEnv);
        if (t1.equals(new IntType())) {
            if (t2.equals(new IntType())) {
                return new BoolType();
            } else {
                throw new OperandNotInteger();
            }
        } else {
            throw new OperandNotInteger();
        }
    }
}
