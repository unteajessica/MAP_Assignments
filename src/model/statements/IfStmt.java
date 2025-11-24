package model.statements;

import exceptions.TypeNotFound;
import model.PrgState;
import model.adt.*;
import model.expressions.*;
import model.values.*;
import model.types.*;
import exceptions.MyException;

public class IfStmt implements IStmt {
    private final Exp exp;
    private final IStmt thenS;
    private final IStmt elseS;

    public IfStmt(Exp e, IStmt t, IStmt el) {
        exp = e;
        thenS = t;
        elseS = el;
    }

    public String toString() {
        return "(IF(" + exp.toString() + ") THEN (" + thenS.toString() + ") ELSE (" + elseS.toString() + "))";
    }

    public PrgState execute(PrgState state) throws MyException {
        Value val = exp.eval(state.getSymTable(), state.getHeap());
        if (!val.getType().equals(new BoolType())) {
            throw new TypeNotFound();
        }
        else {
            BoolValue b = (BoolValue)val;
            if (b.getVal()) {
                state.getStack().push(thenS);
            }
            else {
                state.getStack().push(elseS);
            }
        }
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }

}
