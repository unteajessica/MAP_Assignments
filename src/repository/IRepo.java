package repository;

import model.PrgState;
import exceptions.MyException;

import java.util.List;

public interface IRepo {
    void addPrg(PrgState prg);
    void logPrgStateExec(PrgState prgState) throws MyException;
    List<PrgState> getProgramsList();
    void setProgramsList(List<PrgState> newProgramsList);
}
