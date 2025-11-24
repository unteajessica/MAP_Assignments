package model.adt;

import java.util.Collection;

public interface MyIDictionary<K,V> {
    void put(K key, V val); // add or update
    V lookup(K key); // get value by key
    boolean isDefined(K key); // check existence of key
    void update(K key, V value); // change value by key
    String toString();
    Collection<V> getValues();
}
