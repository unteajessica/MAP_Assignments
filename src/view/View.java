package view;

import java.util.Arrays;
import controller.Controller;
import model.PrgState;
import model.adt.*;
import model.expressions.*;
import model.statements.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.RefType;
import model.types.StringType;
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

    IStmt testProgram() {
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

    IStmt testHeap() {
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

    IStmt testWhile() {
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

    public static void main(String[] args) {
        // --- Example 1 setup
        IStmt ex1 = example1();
        PrgState prg1 = prg(ex1);
        IRepo repo1 = new Repo(prg1, "log1.txt");   // <- PDF style
        Controller ctr1 = new Controller(repo1);

        // --- Example 2 setup
        IStmt ex2 = example2();
        PrgState prg2 = prg(ex2);
        IRepo repo2 = new Repo(prg2, "log2.txt");
        Controller ctr2 = new Controller(repo2);

        // --- Example 3 setup
        IStmt ex3 = example3();
        PrgState prg3 = prg(ex3);
        IRepo repo3 = new Repo(prg3, "log3.txt");
        Controller ctr3 = new Controller(repo3);

        // --- Test Program setup
        IStmt testProg = new View().testProgram();
        PrgState prgTest = prg(testProg);
        IRepo repoTest = new Repo(prgTest, "logTestProgram.txt");
        Controller ctrTest = new Controller(repoTest);

        // --- Test Heap setup
        IStmt heapProg = new View().testHeap();
        PrgState prgHeap = prg(heapProg);
        IRepo repoHeap = new Repo(prgHeap, "logTestHeap.txt");
        Controller ctrHeap = new Controller(repoHeap);

        // --- Test While setup
        IStmt whileProg = new View().testWhile();
        PrgState prgWhile = prg(whileProg);
        IRepo repoWhile = new Repo(prgWhile, "logTestWhile.txt");
        Controller ctrWhile = new Controller(repoWhile);

        // --- Text menu (PDF)
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        // If your Controller method is allSteps() (plural), make RunExample call that.
        menu.addCommand(new RunExample("1", ex1.toString(), ctr1));
        menu.addCommand(new RunExample("2", ex2.toString(), ctr2));
        menu.addCommand(new RunExample("3", ex3.toString(), ctr3));
        menu.addCommand(new SetDisplayCommand("4", "Change display flag", Arrays.asList(ctr1, ctr2, ctr3)));
        menu.addCommand(new RunExample("5", testProg.toString(), ctrTest));
        menu.addCommand(new RunExample("6", heapProg.toString(), ctrHeap));
        menu.addCommand(new RunExample("7", whileProg.toString(), ctrWhile));

        menu.show();
    }

}
