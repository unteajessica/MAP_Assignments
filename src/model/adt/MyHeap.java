package model.adt;

import model.values.Value;
import java.util.HashMap;
import java.util.Map;

public class MyHeap implements MyIHeap {

    private Map<Integer, Value> heap;
    private int freeLocation;

    public MyHeap() {
        this.heap = new HashMap<>();
        this.freeLocation = 1;
    }

    @Override
    public int allocateAddress(Value value) {
        int address = freeLocation;
        heap.put(address, value);
        freeLocation++;
        return address;
    }

    @Override
    public void put(Integer key, Value value) {
        heap.put(key, value);
    }

    @Override
    public Value get(Integer key) {
        return heap.get(key);
    }

    @Override
    public boolean containsKey(Integer key) {
        return heap.containsKey(key);
    }

    @Override
    public void update(Integer key, Value value) {
        heap.put(key, value);
    }

    @Override
    public Map<Integer, Value> getHeap() {
        return heap;
    }

    @Override
    public void setHeap(Map<Integer, Value> newHeap) {
        this.heap = newHeap;
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}
