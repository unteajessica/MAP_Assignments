package model.adt;

import java.io.BufferedReader;

public interface MyIFileTable {
    void put(String filename, BufferedReader fileDescriptor);
    BufferedReader lookup(String filename);
    boolean isDefined(String filename);
    void remove(String filename);
    String toString();
}
