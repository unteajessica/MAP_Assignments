package model.adt;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T> {

    private final List<T> list = new ArrayList<>();

    @Override
    public void add(T elem) {
        list.add(elem);
    }

    @Override
    public T get(int i) {
        return list.get(i);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        return list.toString();
    }

    public List<T> getList() {
        return list;
    }
}
