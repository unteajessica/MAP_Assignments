package model;

import model.adt.*;
import model.statements.*;
import model.values.*;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String, Value> symTable;
    private MyIList<Value> out;
    private final IStmt originalProgram;
    private final MyIFileTable fileTable;
    private MyIHeap heap;

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symtbl, MyIList<Value> ot,
                    IStmt prg, MyIFileTable ft, MyIHeap h) {
        this.exeStack = stk;
        this.symTable = symtbl;
        this.out = ot;
        this.originalProgram = prg.deepCopy();
        this.fileTable = ft;
        this.heap = h;
        exeStack.push(prg);

    }

    public MyIStack<IStmt> getStack() { return exeStack; }
    public MyIDictionary<String, Value> getSymTable() { return symTable; }
    public MyIList<Value> getOut() { return out; }
    public MyIFileTable getFileTable() { return fileTable; }
    public IStmt getOriginalProgram() { return originalProgram; }
    public MyIHeap getHeap() { return heap; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExeStack: ").append(exeStack.toString()).append("\n");
        sb.append("SymTable: ").append(symTable.toString()).append("\n");
        sb.append("Out: ").append(out.toString()).append("\n");
        sb.append("FileTable: ").append(fileTable.toString()).append("\n");
        sb.append("Heap: ").append(heap.toString()).append("\n");
        return sb.toString();
    }

    public MyIStack<IStmt> setStack(MyIStack<IStmt> stk) { return exeStack = stk; }
    public MyIDictionary<String, Value> setSymTable(MyIDictionary<String, Value> symtbl) { return symTable = symtbl; }
    public MyIList<Value> setOut(MyIList<Value> ot) { return out = ot; }
}
