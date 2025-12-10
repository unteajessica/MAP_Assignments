package model.adt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {

    // final -> the reference to the map cannot be changed
    private final Map<K, V> map = new HashMap<>();

    @Override
    public void put(K key, V val) {
        map.put(key, val);
    }

    @Override
    public V lookup(K key) {
        return map.get(key);
    }

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public void update(K key, V value) {
        map.put(key, value);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public Collection<V> getValues() {
        return map.values();
    }

    @Override
    public MyIDictionary<K, V> deepCopy() {
        MyIDictionary<K, V> newDict = new MyDictionary<>();
        for (K key : map.keySet()) {
            newDict.put(key, map.get(key));
        }
        return newDict;
    }
}
