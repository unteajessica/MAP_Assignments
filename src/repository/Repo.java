package repository;
import exceptions.MyException;
import model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Repo implements IRepo {
    public final List<PrgState> programsList;
    private final String logFilePath;

    public Repo(PrgState prg, String logFilePath) {
        this.programsList = new ArrayList<>();
        this.logFilePath = logFilePath;
        this.programsList.add(prg);
    }

    public PrgState getCurrentPrg() {
        return programsList.getFirst();
    }

    @Override
    public void addPrg(PrgState prg) {
        programsList.add(prg);
    }

    @Override
    public void logPrgStateExec() throws MyException, IOException {
        PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        PrgState currentPrg = getCurrentPrg();
        /**
        logFile.println("Original Program:");
        logFile.println(currentPrg.getOriginalProgram().toString());
        logFile.println("-----------------------------------------------");
         **/

        logFile.println(currentPrg.toString());
        logFile.println("===============================================");

        logFile.close();
    }
}
