package view;

import controller.Controller;
import exceptions.MyException;
import model.PrgState;
import model.adt.*;
import model.expressions.*;
import model.statements.*;
import model.types.*;
import repository.IRepo;
import repository.Repo;

public class View {

    private static PrgState prg(IStmt stmt) {
        return new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                stmt,
                new MyFileTable(),
                new MyHeap()
        );
    }

    public static void main() {
        TextMenu menu = new TextMenu();

        // --- Example 1 setup
        IStmt ex1 = Examples.example1();
        try {
            MyIDictionary<String, Type> typeEnv1 = new MyDictionary<>();
            ex1.typeCheck(typeEnv1);

            PrgState prg1 = prg(ex1);
            IRepo repo1 = new Repo(prg1, "log1.txt");   // <- PDF style
            Controller ctr1 = new Controller(repo1);

            Command c1 = new RunExample("1", ex1.toString(), ctr1);
            menu.addCommand(c1);
        } catch (MyException e) {
            System.out.println("Error in example 1: " + e.getMessage());
        }

        // --- Example 2 setup
        IStmt ex2 = Examples.example2();
        try {
            MyIDictionary<String, Type> typeEnv2 = new MyDictionary<>();
            ex2.typeCheck(typeEnv2);

            PrgState prg2 = prg(ex2);
            IRepo repo2 = new Repo(prg2, "log2.txt");
            Controller ctr2 = new Controller(repo2);

            Command c2 = new RunExample("2", ex2.toString(), ctr2);
            menu.addCommand(c2);
        } catch (MyException e) {
            System.out.println("Error in example 2: " + e.getMessage());
        }

        // --- Example 3 setup
        IStmt ex3 = Examples.example3();
        try {
            MyIDictionary<String, Type> typeEnv3 = new MyDictionary<>();
            ex3.typeCheck(typeEnv3);

            PrgState prg3 = prg(ex3);
            IRepo repo3 = new Repo(prg3, "log3.txt");
            Controller ctr3 = new Controller(repo3);

            Command c3 = new RunExample("3", ex3.toString(), ctr3);
            menu.addCommand(c3);
        } catch (MyException e) {
            System.out.println("Error in example 3: " + e.getMessage());
        }

        // --- Test Program setup
        IStmt testProg = Examples.testProgram();
        try {
            MyIDictionary<String, Type> typeEnv4 = new MyDictionary<>();
            testProg.typeCheck(typeEnv4);

            PrgState prgTest = prg(testProg);
            IRepo repoTest = new Repo(prgTest, "logTestProgram.txt");
            Controller ctrTest = new Controller(repoTest);

            Command c4 = new RunExample("4", testProg.toString(), ctrTest);
            menu.addCommand(c4);
        } catch (MyException e) {
            System.out.println("Error in example 4: " + e.getMessage());
        }

        // --- Test Heap setup
        IStmt heapProg = Examples.testHeap();
        try {
            MyIDictionary<String, Type> typeEnv5 = new MyDictionary<>();
            heapProg.typeCheck(typeEnv5);

            PrgState prgHeap = prg(heapProg);
            IRepo repoHeap = new Repo(prgHeap, "logTestHeap.txt");
            Controller ctrHeap = new Controller(repoHeap);

            Command c5 = new RunExample("5", heapProg.toString(), ctrHeap);
            menu.addCommand(c5);
        } catch (MyException e) {
            System.out.println("Error in example 5: " + e.getMessage());
        }

        // --- Test While setup
        IStmt whileProg = Examples.testWhile();
        try {
            MyIDictionary<String, Type> typeEnv6 = new MyDictionary<>();
            whileProg.typeCheck(typeEnv6);

            PrgState prgWhile = prg(whileProg);
            IRepo repoWhile = new Repo(prgWhile, "logTestWhile.txt");
            Controller ctrWhile = new Controller(repoWhile);

            Command c6 = new RunExample("6", whileProg.toString(), ctrWhile);
            menu.addCommand(c6);
        } catch (MyException e) {
            System.out.println("Error in example 6: " + e.getMessage());
        }

        // --- Test Garbage Collector
        IStmt garbageCollectorProg = Examples.testGarbageCollector();
        try {
            MyIDictionary<String, Type> typeEnv7 = new MyDictionary<>();
            garbageCollectorProg.typeCheck(typeEnv7);

            PrgState prgGarbageCollector = prg(garbageCollectorProg);
            IRepo repoGarbageCollector = new Repo(prgGarbageCollector, "logGarbageCollector.txt");
            Controller ctrGarbageCollector = new Controller(repoGarbageCollector);

            Command c7 = new RunExample("7", garbageCollectorProg.toString(), ctrGarbageCollector);
            menu.addCommand(c7);
        } catch (MyException e) {
            System.out.println("Error in example 7: " + e.getMessage());
        }

        // --- Test Fork setup
        IStmt forkProg = Examples.testFork();
        try {
            MyIDictionary<String, Type> typeEnv8 = new MyDictionary<>();
            forkProg.typeCheck(typeEnv8);

            PrgState prgFork = prg(forkProg);
            IRepo repoFork = new Repo(prgFork, "logTestFork.txt");
            Controller ctrFork = new Controller(repoFork);

            Command c8 = new RunExample("8", forkProg.toString(), ctrFork);
            menu.addCommand(c8);
        } catch (MyException e) {
            System.out.println("Error in example 8: " + e.getMessage());
        }

        menu.show();
    }

}
