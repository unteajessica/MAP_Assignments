package model.adt;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class MyFileTable implements MyIFileTable {

    private final Map<String, BufferedReader> fileTable = new HashMap<>();

    @Override
    public void put(String filename, BufferedReader fileDescriptor) {
        fileTable.put(filename, fileDescriptor);
    }

    @Override
    public BufferedReader lookup(String filename) {
        return fileTable.get(filename);
    }

    @Override
    public boolean isDefined(String filename) {
        return fileTable.containsKey(filename);
    }

    @Override
    public void remove(String filename) {
        fileTable.remove(filename);
    }

    // not in the interface, but useful internally / in controller
    public Map<String, BufferedReader> getContent() {
        return fileTable;
    }

    @Override
    public String toString() {
        if (fileTable.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        for (String filename : fileTable.keySet()) {
            sb.append(filename);
            // you could also append " -> " + descriptor if you want
            // sb.append(" -> ").append(fileTable.get(filename)).append("\n");
        }
        return sb.toString();
    }
}
