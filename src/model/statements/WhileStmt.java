package model.statements;

import exceptions.ConditionNotBoolean;
import exceptions.MyException;
import exceptions.TypeMismatch;
import model.PrgState;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.adt.MyIStack;
import model.expressions.Exp;
import model.types.BoolType;
import model.values.BoolValue;
import model.values.Value;
import model.types.Type;

public class WhileStmt implements IStmt {

    private final Exp condition;
    private final IStmt body;

    public WhileStmt(Exp condition, IStmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();

        Value condValue = condition.eval(symTable, heap);

        if (!(condValue instanceof BoolValue)) {
            throw new TypeMismatch();
        }

        BoolValue boolCond = (BoolValue) condValue;

        if (boolCond.getVal()) {
            MyIStack<IStmt> stack = state.getStack();
            // re-push while and body:
            stack.push(this);   // loop again after body
            stack.push(body);   // execute body now
        }
        // if false → do nothing, loop ends

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(condition.deepCopy(), body.deepCopy());
    }

    @Override
    public String toString() {
        return "while(" + condition.toString() + ") { " + body.toString() + " }";
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeCond = condition.typeCheck(typeEnv);
        if (typeCond.equals(new BoolType())) {
            body.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else {
            throw new ConditionNotBoolean();
        }
    }
}
