package hu.elte.txtuml.layout.visualizer.helpers;

import java.util.HashMap;

/**
 * Type for a bi-directional map. Only allows unique keys and unique values.
 * 
 * @author Balázs Gregorics
 *
 * @param <Key>
 *            Primal Key type. Secondary Value type.
 * @param <Value>
 *            Primal Value type. Secondary Key type.
 */
public class BiMap<Key, Value>
{
	// Variables
	
	private HashMap<Key, Value> _oneway;
	private HashMap<Value, Key> _otherway;
	
	// end Variables
	
	// Ctors
	
	/**
	 * Create Bimap.
	 */
	public BiMap()
	{
		_oneway = new HashMap<Key, Value>();
		_otherway = new HashMap<Value, Key>();
	}
	
	// end Ctors
	
	// Methods
	
	/**
	 * Method determining if Bimap contains a key.
	 * 
	 * @param k
	 *            Key to search for.
	 * @return Boolean of Bimap contains key.
	 */
	public boolean containsKey(Key k)
	{
		if (_oneway.containsKey(k))
			return true;
		return false;
	}
	
	/**
	 * Method determining if Bimap contains a value.
	 * 
	 * @param v
	 *            Value to search for.
	 * @return Boolean of Bimap contains value.
	 */
	public boolean containsValue(Value v)
	{
		if (_otherway.containsKey(v))
			return true;
		return false;
	}
	
	/**
	 * Put a Key-Value pair into the bi-directional map.
	 * 
	 * @param k
	 *            Key to put.
	 * @param v
	 *            Value mapped to key.
	 * @return Whether the method was successfull or not. False if key or value
	 *         already exists in the bimap.
	 */
	public boolean put(Key k, Value v)
	{
		return add(k, v);
	}
	
	/**
	 * Put a Key-Value pair into the bi-directional map.
	 * 
	 * @param k
	 *            Key to put.
	 * @param v
	 *            Value mapped to key.
	 * @return Whether the method was successfull or not. False if key or value
	 *         already exists in the bimap.
	 */
	public boolean add(Key k, Value v)
	{
		if (_oneway.containsKey(k) || _otherway.containsKey(v))
			return false;
		if (!_oneway.containsKey(k) && !_otherway.containsKey(v))
		{
			_oneway.put(k, v);
			_otherway.put(v, k);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove a key-value mapping identified by the primal key.
	 * 
	 * @param k
	 *            Key to remove.
	 * @return Success of the method. False if the key is not present in the
	 *         bimap.
	 */
	public boolean removeKey(Key k)
	{
		if (_oneway.containsKey(k))
		{
			Value temp = _oneway.get(k);
			_oneway.remove(k);
			_otherway.remove(temp);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove a key-value mapping identified by the primal value.
	 * 
	 * @param v
	 *            Value to remove.
	 * @return Success of the method. False if the value is not present in the
	 *         bimap.
	 */
	public boolean removeValue(Value v)
	{
		if (_otherway.containsKey(v))
		{
			Key temp = _otherway.get(v);
			_otherway.remove(v);
			_oneway.remove(temp);
			return true;
		}
		return false;
	}
	
	/**
	 * Get the mapped value for the key.
	 * 
	 * @param k
	 *            Key to search for.
	 * @return Value of the mapped key.
	 */
	public Value get(Key k)
	{
		return getValue(k);
	}
	
	/**
	 * Get the mapped value for the key.
	 * 
	 * @param k
	 *            Key to search for.
	 * @return Value of the mapped key.
	 */
	public Value getValue(Key k)
	{
		return _oneway.get(k);
	}
	
	/**
	 * Get the mapped key for the value.
	 * 
	 * @param v
	 *            Value to search for.
	 * @return Key of the mapped value.
	 */
	public Key getKey(Value v)
	{
		return _otherway.get(v);
	}
	
	/**
	 * Change the value mapped to a key.
	 * 
	 * @param k
	 *            Key to search for.
	 * @param v
	 *            Value to change.
	 */
	public void changeValue(Key k, Value v)
	{
		Value old = _oneway.get(k);
		_oneway.put(k, v);
		_otherway.remove(old);
		_otherway.put(v, k);
	}
	
	/**
	 * Change the key mapped to a value.
	 * 
	 * @param v
	 *            Value to search for.
	 * @param k
	 *            Key to change.
	 */
	public void changeKey(Value v, Key k)
	{
		Key old = _otherway.get(v);
		_otherway.put(v, k);
		_oneway.remove(old);
		_oneway.put(k, v);
	}
	
	// end Methods
	
}
