package controller;

import model.PrgState;
import exceptions.MyException;
import model.values.RefValue;
import model.values.Value;
import repository.IRepo;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller implements IController {

    private final IRepo repo;
    private boolean displayFlag;
    private ExecutorService executor;   // java thread pool

    public Controller(IRepo repo) {
        this.repo = repo;
        this.displayFlag = true;
    }

    @Override
    public void setDisplayFlag(boolean b) { displayFlag = b; }

    @Override
    public boolean getDisplayFlag() { return displayFlag; }

    private void runGarbageCollector(List<PrgState> prgList) {
        // collect all addresses from all symbol tables
        List<Integer> symTableAddr = getAddrFromAllSymTables(prgList);

        // get the shared heap
        Map<Integer, Value> heap = prgList.getFirst().getHeap().getHeap();

        // compute the new heap after garbage collection
        Map<Integer, Value> newHeap = safeGarbageCollector(symTableAddr, heap);

        // update the heap in all PrgStates
        prgList.forEach(prg -> prg.getHeap().setHeap(newHeap));
    }

    @Override
    public void allSteps() {
        executor = Executors.newFixedThreadPool(2);

        // remove completed programs
        List<PrgState> prgList = removeCompletedPrg(repo.getProgramsList());
        while (!prgList.isEmpty()) {
            runGarbageCollector(prgList);

            try {
                oneStepForAllPrg(prgList);
            } catch (MyException | IOException | InterruptedException e) {
                System.out.println("Exception occurred: " + e.getMessage());
            }
            // remove completed programs
            prgList = removeCompletedPrg(repo.getProgramsList());
        }

        executor.shutdownNow();
        // update the repository state
        repo.setProgramsList(prgList);
    }

    private static List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        // find all heap addresses that are still referenced in the symbol table
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddress())
                .collect(Collectors.toList());
    }

    private static List<Integer> getAddrFromAllSymTables(List<PrgState> prgList) {
        return prgList.stream()
                // for each PrgState, get the values from its symbol table
                .flatMap(prg ->
                        getAddrFromSymTable(prg.getSymTable().getValues()).stream()
                )
                .collect(Collectors.toList());
    }

    private static Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddrs,
                                                           Map<Integer, Value> heap) {
        // list of heap addresses that are directly referenced in the symbol table
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

        // filter the heap and return a new heap that contains only entries whose addresses are in reachable
        return heap.entrySet().stream()
                .filter(entry -> reachable.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    private void oneStepForAllPrg(List<PrgState> prgList)
            throws MyException, IOException, InterruptedException {
        // before the execution, print the PrgState List into the log file
        prgList.forEach(repo::logPrgStateExec);

        // prepare the list of callables
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                .toList();

        // start the execution of the callables
        // it returns the list of new created PrgStates (from fork statements)
        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        // add the new created PrgStates to the existing list of threads (PrgStates)
        prgList.addAll(newPrgList);

        // after the execution, print the PrgState List into the log file
        prgList.forEach(repo::logPrgStateExec);

        // save the current programs in the repository
        repo.setProgramsList(prgList);
    }

}
