package view;

import model.expressions.*;
import model.statements.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.RefType;
import model.types.StringType;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;

import java.util.ArrayList;
import java.util.List;

public final class Examples {
    private Examples() { }

    public static List<IStmt> getAllExamples() {
        List<IStmt> examples = new ArrayList<>();
        examples.add(example1());
        examples.add(example2());
        examples.add(example3());
        examples.add(testProgram());
        examples.add(testHeap());
        examples.add(testWhile());
        examples.add(testGarbageCollector());
        examples.add(testFork());
        return examples;
    }

    // example 1:
    // int v; v=2; Print(v)
    public static IStmt example1() {
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
    public static IStmt example2() {
        return new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(
                        new VarDeclStmt("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a",
                                        new ArithExp(1,
                                                new ValueExp(new IntValue(2)),
                                                new ArithExp(3,
                                                        new ValueExp(new IntValue(3)),
                                                        new ValueExp(new IntValue(5))))
                                ),
                                new CompStmt(
                                        new AssignStmt("b",
                                                new ArithExp(1,
                                                        new VarExp("a"),
                                                        new ValueExp(new IntValue(1)))
                                        ),
                                        new PrintStmt(new VarExp("b"))
                                )
                        )
                )
        );
    }

    // example 3:
    // bool a; int v; a=true; if (a) then v=2 else v=3; Print(v)
    public static IStmt example3() {
        return new CompStmt(
                new VarDeclStmt("a", new BoolType()),
                new CompStmt(
                        new VarDeclStmt("v", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("a"),
                                                new AssignStmt("v", new ValueExp(new IntValue(2))),
                                                new AssignStmt("v", new ValueExp(new IntValue(3)))
                                        ),
                                        new PrintStmt(new VarExp("v"))
                                )
                        )
                )
        );
    }

    public static IStmt testProgram() {
        return new CompStmt(
                new VarDeclStmt("varf", new StringType()),
                new CompStmt(
                        new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new CompStmt(
                                new openRFileStmt(new VarExp("varf")),
                                new CompStmt(
                                        new VarDeclStmt("var", new IntType()),
                                        new CompStmt(
                                                new readFileStmt(new VarExp("varf"), "var"),
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("var")),
                                                        new CompStmt(
                                                                new readFileStmt(new VarExp("varf"), "var"),
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("var")),
                                                                        new closeRFileStmt(new VarExp("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public static IStmt testHeap() {
        return new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                new CompStmt(
                                        new WriteHeapStmt("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(
                                                new ArithExp(
                                                        1,
                                                        new ReadHeapExp(new VarExp("v")),
                                                        new ValueExp(new IntValue(5))
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public static IStmt testWhile() {
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

    public static IStmt testGarbageCollector() {
        return new CompStmt(
                new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(
                        new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        new NewStmt("a", new VarExp("v")),
                                        new CompStmt(
                                                new NewStmt("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(
                                                        new ReadHeapExp(
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

    public static IStmt testFork() {
        return new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(
                                new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(
                                        new NewStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(
                                                new ForkStmt(
                                                        new CompStmt(
                                                                new WriteHeapStmt("a", new ValueExp(new IntValue(30))),
                                                                new CompStmt(
                                                                        new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                                        new CompStmt(
                                                                                new PrintStmt(new VarExp("v")),
                                                                                new PrintStmt(
                                                                                        new ReadHeapExp(new VarExp("a"))
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("v")),
                                                        new PrintStmt(
                                                                new ReadHeapExp(new VarExp("a"))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }
}
