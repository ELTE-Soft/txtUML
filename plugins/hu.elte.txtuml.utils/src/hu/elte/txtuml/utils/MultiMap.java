package hu.elte.txtuml.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A collection that maps keys to values, similar to {@link java.util.Map Map}, but in which each key may be associated with <i>multiple</i> values.
 * You can imagine the contents of a MultiMap as a Map from keys to collections of values
 *
 * @param <K>
 * @param <V>
 * @author András Dobreff
 */
public final class MultiMap<K,V>
{
	private final HashMap<K, HashSet<V>> map = new HashMap<K, HashSet<V>>();
	
	/**
	 * Removes all of the mappings. The map will be empty after this call returns.
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * Returns true if this map contains a mapping for the specified key.
	 * @param key - The key whose presence in this map is to be tested
	 * @return Returns true if this map contains a mapping for the specified key.
	 */
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	/**
	 * Returns true if this map maps one or more keys to the specified value.
	 * More formally, returns true if and only if this map contains at least
	 * one mapping to a value v such that (value==null ? v==null : value.equals(v)).
	 * This operation will probably require time linear in the map size.
	 * @param value
	 * @return
	 */
	public boolean containsValue(V value) {
		for (HashSet<V> set : map.values()) {
			if (set.contains(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns all values associated with the key.
	 * @param key
	 * @return {@link HashSet} of values
	 */
	public HashSet<V> get(K key) {
		return map.get(key);
	}

	/**
	 * Returns true if this map contains no key-value mappings.
	 * @return true if this map contains no key-value mappings 
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * Returns a {@link Set} view of the keys contained in this map.
	 * The set is backed by the map, so changes to the map
	 * are reflected in the set, and vice-versa. If the map
	 * is modified while an iteration over the set is in progress
	 * (except through the iterator's own remove operation),
	 * the results of the iteration are undefined. The set
	 * supports element removal, which removes the corresponding
	 * mapping from the map, via the Iterator.remove, Set.remove,
	 * removeAll, retainAll, and clear operations.
	 * It does not support the add or addAll operations.
	 * @return a set view of the keys contained in this map 
	 */
	public Set<K> keySet() {
		return map.keySet();
	}

	/**
	 * Puts the value in the {@link HashSet} associated with the key
	 * @param key - key with which the specified value is to be associated
	 * @param value - value to be associated with the specified key
	 */
	public void put(K key, V value) {
		HashSet<V> set = map.get(key);
		if (set == null) {
			set = new HashSet<V>();
			map.put(key, set);
		}
		set.add(value);
	}

	/**
	 * Puts all elements of values into the map.
	 * @param key - key with which the specified value is to be associated
	 * @param values - value to be associated with the specified key
	 */
	public void putAll(K key, Collection<V> values) {
		HashSet<V> set = map.get(key);
		if (set == null) {
			set = new HashSet<V>();
			map.put(key, set);
		}
		values.forEach(set::add);
	}

	/**
	 * Removes the mapping for a key from this map if it is present.
	 * More formally, if this map contains a mapping from
	 * key k to value v such that (key==null ? k==null : key.equals(k)),
	 * that mapping is removed. Returns the value to which this map
	 * previously associated the key, or null if the map contained no
	 * mapping for the key.If this map permits null values, then a return
	 * value of null does not necessarily indicate that the map contained
	 * no mapping for the key; it's also possible that the map explicitly
	 * mapped the key to null.The map will not contain a mapping for the
	 * specified key once the call returns.
	 * @param key - key whose mapping is to be removed from the map
	 * @return the previous value associated with key, or null if there was
	 * no mapping for key. (A null return can also indicate that the map previously associated null with key.) 
	 */
	public HashSet<V> remove(K key){
		return map.remove(key);
	}
	
	/**
	 * Removes the value from all mappings of he map 
	 * @param value - The value to be removed
	 */
	public void removeValue(V value) {
		map.values().forEach(set -> {
			set.remove(value);
		});
	}

	/**
	 * Returns the number of key-value mappings in this map.
	 * @return the number of key-value mappings in this map.
	 */
	public int size() {
		return map.size();
	}

	/**
	 * Returns a Collection view of the values contained in this map.
	 * The collection is backed by the map, so changes to the map are
	 * reflected in the collection, and vice-versa. If the map is modified
	 * while an iteration over the collection is in progress
	 * (except through the iterator's own remove operation),
	 * the results of the iteration are undefined. The collection supports
	 * element removal, which removes the corresponding mapping from the map,
	 * via the Iterator.remove, Collection.remove, removeAll, retainAll
	 * and clear operations. It does not support the add or addAll operations.
	 * @return a collection view of the values contained in this map
	 */
	public Collection<HashSet<V>> values() {
		return map.values();
	}

}