package model.adt;

import model.values.Value;
import java.util.Map;

public interface MyIHeap {
    int allocateAddress(Value value);
    void put(Integer key, Value value);
    Value get(Integer key);
    boolean containsKey(Integer key);
    void update(Integer key, Value value);
    Map<Integer, Value> getHeap();
    void setHeap(Map<Integer, Value> newHeap);
}
