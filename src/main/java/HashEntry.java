package main.java;

import java.util.*;

public class HashEntry<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    HashEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

    public K getKey() {
        return key;
    }
    public V getValue() {
        return value;
    }
}
