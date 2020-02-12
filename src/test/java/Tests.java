package test.java;

import main.java.HashTable;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;


public class Tests {

    private void putTemplate(HashTable table){
        HashTable<String, String> hashTable = new HashTable<>();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 200000; i++) {
            String key = String.format("key %d", i);
            String value = String.format("value %d", i);
            hashTable.put(key, value);
            map.put(key, value);
        }
    }//NOTE: FUNCTION FOR INCLUDING HIGH QUANTITIES OF ELEMENTS

    @Test
    public void checkInput() {
        HashTable<String, String> hashTable = new HashTable<>();
        hashTable.put("key", "value");
        String value = hashTable.get("key");
        assertEquals(1, hashTable.size());
        assertEquals("value", value);
        assertEquals(1, hashTable.size());
    }

    @Test
    public void remove() {
        HashTable<String, String> hashTable = new HashTable<>();
        hashTable.put("key", "value");
        String value = hashTable.remove("key");
        assertEquals("value", value);
        value = hashTable.get("key");
        assertNull(value);
        assertTrue(hashTable.isEmpty());
    }

    @Test
    public void addHighQuantity() {
        HashTable<String, String> hashTable = new HashTable<>();
        Map<String, String> map = new HashMap<>();
        putTemplate(hashTable);
        assertEquals(map, hashTable);
    }

    @Test
    public void removeHighQuantity() {
        HashTable<String, String> hashTable = new HashTable<>();
        Map<String, String> map = new HashMap<>();
        putTemplate(hashTable);
        for (int i = 0; i < 200000; i++) {
            String key = String.format("key %d", i);
            hashTable.remove(key);
            map.remove(key);
        }
        assertEquals(map, hashTable);
    }

    @Test
    public void checkCollisions() {
        HashTable<String, String> hashTable = new HashTable<>();
        hashTable.put("key33", "value33");
        hashTable.put("key333", "value333");
        assertEquals(2, hashTable.size());
        assertEquals("value33", hashTable.get("key33"));
        assertEquals("value333", hashTable.get("key333"));
    }


    @Test
    public void returnNull() {
        HashTable<String, String> hashTable = new HashTable<>();
        String oldValue = hashTable.put("key", "value1");
        assertNull(oldValue);
        assertEquals(1, hashTable.size());
    }

    @Test
    public void returnOld() {
        HashTable<String, String> hashTable = new HashTable<>();
        hashTable.put("key", "value1");
        String old = hashTable.put("key", "value2");
        assertEquals("value1", old);
        assertEquals(1, hashTable.size());
    }

}
