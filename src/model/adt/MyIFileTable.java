package model.adt;

import java.io.BufferedReader;
import java.util.Map;

public interface MyIFileTable {
    void put(String filename, BufferedReader fileDescriptor);
    BufferedReader lookup(String filename);
    boolean isDefined(String filename);
    void remove(String filename);
    String toString();
    Map<String, BufferedReader> getContent();
}
