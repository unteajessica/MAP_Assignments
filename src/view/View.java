package view;

import java.util.Arrays;
import controller.Controller;
import exceptions.MyException;
import model.PrgState;
import model.adt.*;
import model.expressions.*;
import model.statements.*;
import model.types.*;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;
import repository.IRepo;
import repository.Repo;
import model.statements.openRFileStmt;
import model.statements.readFileStmt;
import model.statements.closeRFileStmt;

public class View {
    // example 1:
    // int v; v=2; Print(v)
    private static IStmt example1() {
        return new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );
    }

    // example 2:
    // int a; int b; a=2+3*5; b=a+1; Print(b)
    private static IStmt example2() {
        return new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithExp(1,new ValueExp(new IntValue(2)),new
                                ArithExp(3,new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b",new ArithExp(1,new VarExp("a"), new ValueExp(new
                                        IntValue(1)))), new PrintStmt(new VarExp("b"))))));
    }

    // example 3:
    // bool a; int v; a=true; if (a) then v=2 else v=3; Print(v)
    private static IStmt example3() {
        return new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new
                                        IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new
                                        VarExp("v"))))));
    }

    private static IStmt testProgram() {
        return
                new CompStmt(
                        new VarDeclStmt("varf", new StringType()), // string varf
                        new CompStmt(
                                new AssignStmt("varf", new ValueExp(new StringValue("test.in"))), //
                                new CompStmt(
                                        new openRFileStmt(new VarExp("varf")), // openRFile(varf)
                                        new CompStmt(
                                                new VarDeclStmt("var", new IntType()), // int var
                                                new CompStmt(
                                                        new readFileStmt(new VarExp("varf"), "var"), // readFile(varf, var)
                                                        new CompStmt(
                                                                new PrintStmt(new VarExp("var")), // print(var)
                                                                new CompStmt(
                                                                        new readFileStmt(new VarExp("varf"), "var"),
                                                                        new CompStmt(// readFile(varf, var)
                                                                            new PrintStmt(new VarExp("var")),
                                                                                new closeRFileStmt(new VarExp("varf"))// print(var)
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    private static IStmt testHeap() {
        return new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),                  // Ref int v
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),             // new(v,20)
                        new CompStmt(
                                new PrintStmt(new ReadHeapExp(new VarExp("v"))),          // print(rH(v))
                                new CompStmt(
                                        new WriteHeapStmt("v", new ValueExp(new IntValue(30))), // wH(v,30)
                                        new PrintStmt(
                                                new ArithExp(
                                                        1,
                                                        new ReadHeapExp(new VarExp("v")),             // rH(v)
                                                        new ValueExp(new IntValue(5))                 // +5
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static IStmt testWhile() {
        return new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(
                                new WhileStmt(
                                        new RelationalExp(new VarExp("v"), new ValueExp(new IntValue(0)), ">"),
                                        new CompStmt(
                                                new PrintStmt(new VarExp("v")),
                                                new AssignStmt("v",
                                                        new ArithExp(2, new VarExp("v"), new ValueExp(new IntValue(1))))
                                        )
                                ),
                                new PrintStmt(new VarExp("v"))
                        )
                )
        );

    }

    private static IStmt testGarbageCollector() {
        return new CompStmt(
                // Ref int v;
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        // new(v, 20);
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                // Ref Ref int a;
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        // new(a, v);
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                // new(v, 30);
                                                new NewStmt("v", new ValueExp(new IntValue(30))),
                                                // print(rH(rH(a)));
                                                new PrintStmt(
                                                        new ReadHeapExp(        // rH( … )
                                                                new ReadHeapExp( // rH(a)
                                                                        new VarExp("a")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    // fork example
    // int v; Ref int a; v=10; new(a,22);
    // fork( wH(a,30); v=32; print(v); print(rH(a)) );
    // print(v); print(rH(a))
    private static IStmt testFork() {
        return new CompStmt(
                // int v
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        // Ref int a
                        new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(
                                // v = 10
                                new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(
                                        // new(a,22)
                                        new NewStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(
                                                // fork(wH(a,30); v=32; print(v); print(rH(a)))
                                                new ForkStmt(
                                                        new CompStmt(
                                                                new WriteHeapStmt("a", new ValueExp(new IntValue(30))),
                                                                new CompStmt(
                                                                        new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                                        new CompStmt(
                                                                                new PrintStmt(new VarExp("v")),
                                                                                new PrintStmt(
                                                                                        new ReadHeapExp(
                                                                                                new VarExp("a")
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                ),
                                                // print(v); print(rH(a))
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("v")),
                                                        new PrintStmt(
                                                                new ReadHeapExp(
                                                                        new VarExp("a")
                                                                )
                                                        )
                                                )
                                        )

                                )
                        )
                )
        );
    }

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
        IStmt ex1 = example1();
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
        IStmt ex2 = example2();
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
        IStmt ex3 = example3();
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
        IStmt testProg = testProgram();
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
        IStmt heapProg = testHeap();
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
        IStmt whileProg = testWhile();
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
        IStmt garbageCollectorProg = testGarbageCollector();
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
        IStmt forkProg = testFork();
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
