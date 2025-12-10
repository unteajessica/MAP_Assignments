package repository;

import exceptions.MyException;
import model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repo implements IRepo {

    private List<PrgState> programsList;
    private final String logFilePath;

    public Repo(PrgState prg, String logFilePath) {
        this.programsList = new ArrayList<>();
        this.logFilePath = logFilePath;
        this.programsList.add(prg);
    }

    @Override
    public void addPrg(PrgState prg) {
        programsList.add(prg);
    }

    @Override
    public void logPrgStateExec(PrgState prgState) throws MyException {
        try (PrintWriter logFile =
                     new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.println(prgState.toString());
            logFile.println("===============================================");
        } catch (IOException e) {
            throw new MyException("Could not write to log file: " + e.getMessage());
        }
    }

    @Override
    public List<PrgState> getProgramsList() {
        return programsList;
    }

    @Override
    public void setProgramsList(List<PrgState> newProgramsList) {
        this.programsList = newProgramsList;
    }
}
