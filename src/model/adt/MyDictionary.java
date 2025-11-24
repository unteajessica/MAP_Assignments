package model.adt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K,V> implements MyIDictionary<K,V> {
    private final Map<K,V> map = new HashMap<>();
    public void put(K key, V val) { map.put(key, val); }
    public V lookup(K key) { return map.get(key); }
    public boolean isDefined(K key) { return map.containsKey(key); }
    public void update(K key, V value) { map.put(key, value); }
    public String toString() { return map.toString(); }
    public Collection<V> getValues() { return map.values(); }
}
