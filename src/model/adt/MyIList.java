package model.adt;

import java.util.List;

public interface MyIList<T> {
    void add(T elem);
    T get(int i);
    int size();
    String toString();
    List<T> getList();
}
