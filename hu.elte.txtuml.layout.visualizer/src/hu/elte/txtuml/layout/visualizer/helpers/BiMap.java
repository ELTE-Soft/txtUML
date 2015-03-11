package hu.elte.txtuml.layout.visualizer.helpers;

import java.util.HashMap;

public class BiMap<Key, Value>
{
	private HashMap<Key, Value> _oneway;
	private HashMap<Value, Key> _otherway;
	
	public BiMap()
	{
		_oneway = new HashMap<Key, Value>();
		_otherway = new HashMap<Value, Key>();
	}
	
	public boolean containsKey(Key k)
	{
		if (_oneway.containsKey(k))
			return true;
		return false;
	}
	
	public boolean containsValue(Value v)
	{
		if (_otherway.containsKey(v))
			return true;
		return false;
	}
	
	public boolean put(Key k, Value v)
	{
		return add(k, v);
	}
	
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
	
	public Value get(Key k)
	{
		return getValue(k);
	}
	
	public Value getValue(Key k)
	{
		return _oneway.get(k);
	}
	
	public Key getKey(Value v)
	{
		return _otherway.get(v);
	}
	
	public void changeValue(Key k, Value v)
	{
		Value old = _oneway.get(k);
		_oneway.put(k, v);
		_otherway.remove(old);
		_otherway.put(v, k);
	}
	
	public void changeKey(Value v, Key k)
	{
		Key old = _otherway.get(v);
		_otherway.put(v, k);
		_oneway.remove(old);
		_oneway.put(k, v);
	}
	
}
