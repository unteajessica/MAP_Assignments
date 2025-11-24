package controller;
import model.PrgState;
import exceptions.MyException;
import model.adt.MyIStack;
import model.statements.IStmt;
import model.values.RefValue;
import model.values.Value;
import repository.IRepo;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

            if (displayFlag) System.out.println(prg.toString());

            // garbage collector
            List<Integer> symTableAddrs = getAddrFromSymTable(prg.getSymTable().getValues());

            prg.getHeap().setHeap(
                    safeGarbageCollector(symTableAddrs, prg.getHeap().getHeap())
            );

            repo.logPrgStateExec();

        }

        if (!displayFlag) System.out.println(prg.toString());
    }

    public static List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .collect(Collectors.toList());
    }

    public static Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddrs,
                                                           Map<Integer, Value> heap) {

        List<Integer> reachable = new ArrayList<>(symTableAddrs);

        boolean changed;

        do {
            changed = false;

            List<Integer> newAddrs = heap.entrySet().stream()
                    .filter(entry -> reachable.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .filter(v -> v instanceof RefValue)
                    .map(v -> ((RefValue) v).getAddress())
                    .filter(addr -> !reachable.contains(addr))
                    .toList();

            if (!newAddrs.isEmpty()) {
                reachable.addAll(newAddrs);
                changed = true;
            }

        } while (changed);

        // keep only reachable addresses
        return heap.entrySet().stream()
                .filter(entry -> reachable.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
