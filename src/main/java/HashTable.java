package main.java;

import java.math.BigInteger;
import java.util.*;
import static java.util.stream.Collectors.toCollection;

public class HashTable<K, V> implements Map<K, V> {
    private int size;
    private int capacity;
    private int currentlyOccupied;
    private HashEntry[] pair;
    private HashEntry<K, V> Tombstone = new HashEntry<>(null, null);
    private int sizeDefault = 29;

    @Override
    public int size() {
        return size;
    }
    @Override
    public void clear() {
        initialize(sizeDefault);
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    @Override
    public boolean containsKey(Object key) {
        return keySet().contains(key);
    }
    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }
    @Override
    public Set<K> keySet() {
        return entrySet().
                stream().map(Entry::getKey).collect(toCollection(LinkedHashSet::new));
    }
    @Override
    public Collection<V> values() {
        return entrySet().
                stream().map(Entry::getValue).collect(toCollection(ArrayList::new));
    }

    public HashTable() {
        initialize(sizeDefault);
    }

    private void initialize(int capacity) {
        this.capacity = capacity;
        pair = new HashEntry[this.capacity];
        this.size = 0;
        this.currentlyOccupied = 0;
    }//NOTE: Setting up a blank template

    private int hash1(K key) {
        return Math.abs(key.hashCode() % capacity);
    }
    private int hash2(K key) {
        return Math.abs(5 - key.hashCode() % 5);
    }
    private int prob(K key, int x) {
        return x * hash2(key);
    }

    private int search(K key) {
        int value = 1;
        int index = hash1(key);
        while ((pair[index] != null) && (!Objects.equals(pair[index].getKey(), key))) {
            index = (hash1(key) + prob(key, value)) % capacity;
            value += 1;
        }
        return index;
    }

    @Override
    public V get(Object key) {
        int index = search((K) key);
        return pair[index] != null ? (V) pair[index].getValue() : null;
    }

    private void resize() {
        Map<K, V> map = new HashMap<>();
        for (Entry<K, V> entryKeyValue : entrySet()) {
            if (map.put(entryKeyValue.getKey(), entryKeyValue.getValue()) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }
        int RESIZE_FACTOR = 2;
        int biggerCapacity = BigInteger.valueOf(map.size() * RESIZE_FACTOR).nextProbablePrime().intValue();
        initialize(biggerCapacity);
        putAll(map);
    }//NOTE: In case the number of elements is too much

    @Override
    public V put(K key, V value) {
        int index = search(key);
        float loadFactor = 0.8f;
        if(currentlyOccupied > capacity * loadFactor) {
            resize();
        }
        V old = null;
        if (pair[index] != null) {
            old = (V) pair[index].getValue();
        } else {
            currentlyOccupied++;
            size++;
        }
        pair[index] = new HashEntry<>(key, value);
        return old;
    }

    @Override
    public V remove(Object key) {
        V oldValue;
        int index = search((K) key);
        if (pair[index] == null) {
            return null;
        } else {
            oldValue = (V) pair[index].getValue();
            pair[index] = Tombstone; //NOTE: Marks that this index might have contained something previously
            size--;
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new LinkedHashSet<>(size);
        for (HashEntry entry : pair) {
            if (!(entry == null || Objects.equals(entry, Tombstone))) {
                set.add(entry);
            }
        }
        return set;
    }
}