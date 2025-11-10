package controller;
import model.PrgState;
import exceptions.MyException;
import model.adt.MyIStack;
import model.statements.IStmt;
import repository.IRepo;

import java.io.IOException;
import java.util.EmptyStackException;

public class Controller implements IController {
    private final IRepo repo;
    private boolean displayFlag;

    public Controller(IRepo repo) {
        this.repo = repo;
        this.displayFlag = true;
    }

    public void setDisplayFlag(boolean b) { displayFlag = b; }
    public boolean getDisplayFlag() { return displayFlag; }

    @Override
    public PrgState oneStep(PrgState state) throws EmptyStackException {
        MyIStack<IStmt> stk = state.getStack();
        if (stk.isEmpty()) throw new EmptyStackException();
        IStmt crtStmt = stk.pop();
        return crtStmt.execute(state);
    }

    @Override
    public void allSteps() throws MyException, IOException {
        PrgState prg = repo.getCurrentPrg();
        repo.logPrgStateExec();
        if (displayFlag) System.out.println(prg.toString());
        while (!prg.getStack().isEmpty()) {
            oneStep(prg);
            repo.logPrgStateExec();

            if (displayFlag) System.out.println(prg.toString());
        }

        if (!displayFlag) System.out.println(prg.toString());
    }
}
